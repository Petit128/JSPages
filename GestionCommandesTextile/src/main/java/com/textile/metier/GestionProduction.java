package com.textile.metier;

import com.textile.model.*;
import com.textile.dao.*;
import java.util.*;
import java.sql.SQLException;

public class GestionProduction {
    
    private CommandeDAO commandeDAO;
    private EtapeProductionDAO etapeDAO;
    private HistoriqueDAO historiqueDAO;
    
    private static final String[] ETAPES_ORDRE = {"COUPE", "COUTURE", "FINITION", "LIVRAISON"};
    private static final int[] DUREES_PAR_DEFAUT = {2, 3, 2, 1}; // jours par étape
    
    public GestionProduction() {
        commandeDAO = new CommandeDAO();
        etapeDAO = new EtapeProductionDAO();
        historiqueDAO = new HistoriqueDAO();
    }
    
 // Modifier la méthode creerCommande
    public void creerCommande(int utilisateurId, String client, String typeArticle, int quantite, 
                              String taille, String couleur, Date dateLivraisonPrevue) throws Exception {
        Commande commande = new Commande();
        commande.setUtilisateurId(utilisateurId);  // NOUVEAU : lier à l'utilisateur
        commande.setClient(client);
        commande.setTypeArticle(typeArticle);
        commande.setQuantite(quantite);
        commande.setTaille(taille);
        commande.setCouleur(couleur);
        commande.setDateCommande(new Date());
        commande.setDateLivraisonPrevue(dateLivraisonPrevue);
        commande.setStatut("EN_ATTENTE");
        
        commandeDAO.ajouterCommande(commande);
        
        // Créer les étapes de production
        Date dateCourante = new Date();
        for (int i = 0; i < ETAPES_ORDRE.length; i++) {
            EtapeProduction etape = new EtapeProduction();
            etape.setCommandeId(commande.getId());
            etape.setNomEtape(ETAPES_ORDRE[i]);
            etape.setStatut("EN_ATTENTE");
            etape.setDureePrevueJours(DUREES_PAR_DEFAUT[i]);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateCourante);
            for (int j = 0; j <= i; j++) {
                cal.add(Calendar.DAY_OF_MONTH, DUREES_PAR_DEFAUT[j]);
            }
            etape.setDateFinPrevue(cal.getTime());
            
            etapeDAO.ajouterEtape(etape);
        }
        
        ajouterHistorique(commande.getId(), "CREATION_COMMANDE", 
                         "Commande créée pour le client " + client);
    }
    
    public boolean demarrerEtape(int commandeId, String nomEtape) throws Exception {
        // Vérifier si l'étape précédente est terminée
        EtapeProduction etapePrecedente = etapeDAO.getEtapePrecedente(commandeId, nomEtape);
        
        if (etapePrecedente != null && !"TERMINEE".equals(etapePrecedente.getStatut())) {
            throw new IllegalStateException(
                "Impossible de démarrer l'étape " + nomEtape + 
                ". L'étape précédente (" + getEtapePrecedenteNom(nomEtape) + 
                ") n'est pas encore terminée."
            );
        }
        
        // Récupérer l'étape à démarrer
        List<EtapeProduction> etapes = etapeDAO.getEtapesByCommandeId(commandeId);
        EtapeProduction etape = trouverEtape(etapes, nomEtape);
        
        if (etape == null) {
            throw new IllegalArgumentException("Étape non trouvée");
        }
        
        if (!"EN_ATTENTE".equals(etape.getStatut())) {
            throw new IllegalStateException("L'étape est déjà en cours ou terminée");
        }
        
        // Démarrer l'étape
        etapeDAO.demarrerEtape(etape.getId());
        
        // Mettre à jour le statut global de la commande
        commandeDAO.mettreAJourStatut(commandeId, "EN_PRODUCTION");
        
        // Ajouter historique
        ajouterHistorique(commandeId, "DEMARRAGE_ETAPE", 
                         "Démarrage de l'étape " + nomEtape);
        
        return true;
    }
    
    public void terminerEtape(int commandeId, String nomEtape, String commentaire) throws Exception {
        List<EtapeProduction> etapes = etapeDAO.getEtapesByCommandeId(commandeId);
        EtapeProduction etape = trouverEtape(etapes, nomEtape);
        
        if (etape == null) {
            throw new IllegalArgumentException("Étape non trouvée");
        }
        
        if (!"EN_COURS".equals(etape.getStatut())) {
            throw new IllegalStateException("L'étape n'est pas en cours");
        }
        
        // Terminer l'étape
        etapeDAO.terminerEtape(etape.getId(), commentaire);
        
        // Vérifier si toutes les étapes sont terminées
        boolean toutesTerminees = true;
        for (EtapeProduction e : etapes) {
            if (!"TERMINEE".equals(e.getStatut())) {
                toutesTerminees = false;
                break;
            }
        }
        
        if (toutesTerminees) {
            commandeDAO.mettreAJourStatut(commandeId, "TERMINE");
        }
        
        // Ajouter historique
        ajouterHistorique(commandeId, "TERMINAISON_ETAPE", 
                         "Terminaison de l'étape " + nomEtape + 
                         (commentaire != null ? " - Commentaire: " + commentaire : ""));
    }
    
    public Map<String, Object> calculerDelais(int commandeId) throws Exception {
        Map<String, Object> delais = new HashMap<>();
        
        Commande commande = commandeDAO.getCommandeById(commandeId);
        List<EtapeProduction> etapes = etapeDAO.getEtapesByCommandeId(commandeId);
        
        // Délai total prévu
        int dureeTotalePrevue = 0;
        for (EtapeProduction etape : etapes) {
            dureeTotalePrevue += etape.getDureePrevueJours();
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(commande.getDateCommande());
        cal.add(Calendar.DAY_OF_MONTH, dureeTotalePrevue);
        Date dateLivraisonEstimee = cal.getTime();
        
        // Vérifier les retards
        boolean enRetard = false;
        long retardJours = 0;
        
        if (dateLivraisonEstimee.after(commande.getDateLivraisonPrevue())) {
            enRetard = true;
            retardJours = (dateLivraisonEstimee.getTime() - commande.getDateLivraisonPrevue().getTime()) 
                         / (1000 * 60 * 60 * 24);
        }
        
        // Délais par étape
        Map<String, Date> datesFinPrevues = new HashMap<>();
        Map<String, Boolean> retardsEtapes = new HashMap<>();
        
        for (EtapeProduction etape : etapes) {
            datesFinPrevues.put(etape.getNomEtape(), etape.getDateFinPrevue());
            
            if (etape.getDateFin() != null && etape.getDateFin().after(etape.getDateFinPrevue())) {
                retardsEtapes.put(etape.getNomEtape(), true);
            } else {
                retardsEtapes.put(etape.getNomEtape(), false);
            }
        }
        
        delais.put("dureeTotalePrevue", dureeTotalePrevue);
        delais.put("dateLivraisonEstimee", dateLivraisonEstimee);
        delais.put("enRetard", enRetard);
        delais.put("retardJours", retardJours);
        delais.put("datesFinPrevues", datesFinPrevues);
        delais.put("retardsEtapes", retardsEtapes);
        
        return delais;
    }
    
    public List<EtapeProduction> getWorkflowCommande(int commandeId) throws Exception {
        return etapeDAO.getEtapesByCommandeId(commandeId);
    }
    
    public List<Commande> getCommandesByStatut(String statut) throws Exception {
        return commandeDAO.getCommandesByStatut(statut);
    }
    
    public Map<String, Object> getTableauDeBord() throws Exception {
        Map<String, Object> dashboard = new HashMap<>();
        
        List<Commande> toutesCommandes = commandeDAO.getAllCommandes();
        List<Commande> commandesEnProduction = commandeDAO.getCommandesByStatut("EN_PRODUCTION");
        List<Commande> commandesEnAttente = commandeDAO.getCommandesByStatut("EN_ATTENTE");
        List<Commande> commandesTerminees = commandeDAO.getCommandesByStatut("TERMINE");
        
        dashboard.put("totalCommandes", toutesCommandes.size());
        dashboard.put("commandesEnProduction", commandesEnProduction.size());
        dashboard.put("commandesEnAttente", commandesEnAttente.size());
        dashboard.put("commandesTerminees", commandesTerminees.size());
        
        // Calcul des commandes en retard
        int commandesEnRetard = 0;
        for (Commande cmd : toutesCommandes) {
            Map<String, Object> delais = calculerDelais(cmd.getId());
            if ((boolean) delais.get("enRetard")) {
                commandesEnRetard++;
            }
        }
        dashboard.put("commandesEnRetard", commandesEnRetard);
        
        return dashboard;
    }
    
    private EtapeProduction trouverEtape(List<EtapeProduction> etapes, String nomEtape) {
        for (EtapeProduction e : etapes) {
            if (e.getNomEtape().equals(nomEtape)) {
                return e;
            }
        }
        return null;
    }
    
    private String getEtapePrecedenteNom(String nomEtape) {
        for (int i = 0; i < ETAPES_ORDRE.length; i++) {
            if (ETAPES_ORDRE[i].equals(nomEtape) && i > 0) {
                return ETAPES_ORDRE[i - 1];
            }
        }
        return null;
    }
    
    private void ajouterHistorique(int commandeId, String action, String details) throws SQLException {
        HistoriqueAction histo = new HistoriqueAction();
        histo.setCommandeId(commandeId);
        histo.setAction(action);
        histo.setUtilisateur("SYSTEME");
        histo.setDetails(details);
        historiqueDAO.ajouterAction(histo);
    }
}