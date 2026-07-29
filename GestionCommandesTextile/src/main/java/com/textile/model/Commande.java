package com.textile.model;

import java.util.Date;
import java.util.List;

public class Commande {
    private int id;
    private int utilisateurId;  // NOUVEAU : lien avec l'utilisateur
    private String client;
    private String typeArticle;
    private int quantite;
    private String taille;
    private String couleur;
    private Date dateCommande;
    private Date dateLivraisonPrevue;
    private String statut;
    private List<EtapeProduction> etapes;
    
    public Commande() {}
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    
    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }
    
    public String getTypeArticle() { return typeArticle; }
    public void setTypeArticle(String typeArticle) { this.typeArticle = typeArticle; }
    
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    
    public String getTaille() { return taille; }
    public void setTaille(String taille) { this.taille = taille; }
    
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    
    public Date getDateCommande() { return dateCommande; }
    public void setDateCommande(Date dateCommande) { this.dateCommande = dateCommande; }
    
    public Date getDateLivraisonPrevue() { return dateLivraisonPrevue; }
    public void setDateLivraisonPrevue(Date dateLivraisonPrevue) { this.dateLivraisonPrevue = dateLivraisonPrevue; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public List<EtapeProduction> getEtapes() { return etapes; }
    public void setEtapes(List<EtapeProduction> etapes) { this.etapes = etapes; }
}