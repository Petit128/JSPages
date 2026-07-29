package com.taxis.controller.api;

import com.taxis.dao.ParametresDAO;
import com.taxis.model.Parametres;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/parametres")
public class ParametresAPIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ParametresDAO parametresDAO;
    private Gson gson;
    
    @Override
    public void init() {
        parametresDAO = new ParametresDAO();
        gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Parametres parametres = parametresDAO.getParametres();
            
            Map<String, Object> result = new HashMap<>();
            result.put("prixPriseEnCharge", parametres.getPrixPriseEnCharge());
            result.put("prixKmJour", parametres.getPrixKmJour());
            result.put("prixKmNuit", parametres.getPrixKmNuit());
            result.put("prixAttenteMin", parametres.getPrixAttenteMin());
            result.put("commissionEntreprise", parametres.getCommissionEntreprise());
            result.put("fraisBagages", parametres.getFraisBagages());
            result.put("fraisAnimaux", parametres.getFraisAnimaux());
            result.put("majorationNuit", parametres.getMajorationNuit());
            result.put("majorationWeekend", parametres.getMajorationWeekend());
            
            out.print(gson.toJson(result));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            out.print(gson.toJson(error));
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            Map<String, Object> data = gson.fromJson(sb.toString(), Map.class);
            
            Parametres parametres = new Parametres();
            
            // Conversion correcte des valeurs
            Object prixBaseObj = data.get("prixBase");
            parametres.setPrixPriseEnCharge(prixBaseObj instanceof Number ? ((Number) prixBaseObj).doubleValue() : Double.parseDouble((String) prixBaseObj));
            
            Object prixKmJourObj = data.get("prixKmJour");
            parametres.setPrixKmJour(prixKmJourObj instanceof Number ? ((Number) prixKmJourObj).doubleValue() : Double.parseDouble((String) prixKmJourObj));
            
            Object prixKmNuitObj = data.get("prixKmNuit");
            parametres.setPrixKmNuit(prixKmNuitObj instanceof Number ? ((Number) prixKmNuitObj).doubleValue() : Double.parseDouble((String) prixKmNuitObj));
            
            Object prixAttenteObj = data.get("prixAttente");
            parametres.setPrixAttenteMin(prixAttenteObj instanceof Number ? ((Number) prixAttenteObj).doubleValue() : Double.parseDouble((String) prixAttenteObj));
            
            Object commissionObj = data.get("commission");
            parametres.setCommissionEntreprise(commissionObj instanceof Number ? ((Number) commissionObj).doubleValue() : Double.parseDouble((String) commissionObj));
            
            Object fraisBagagesObj = data.get("fraisBagages");
            parametres.setFraisBagages(fraisBagagesObj instanceof Number ? ((Number) fraisBagagesObj).doubleValue() : Double.parseDouble((String) fraisBagagesObj));
            
            Object fraisAnimauxObj = data.get("fraisAnimaux");
            parametres.setFraisAnimaux(fraisAnimauxObj instanceof Number ? ((Number) fraisAnimauxObj).doubleValue() : Double.parseDouble((String) fraisAnimauxObj));
            
            Object majorationNuitObj = data.get("majorationNuit");
            parametres.setMajorationNuit(majorationNuitObj instanceof Number ? ((Number) majorationNuitObj).doubleValue() : Double.parseDouble((String) majorationNuitObj));
            
            Object majorationWeekendObj = data.get("majorationWeekend");
            parametres.setMajorationWeekend(majorationWeekendObj instanceof Number ? ((Number) majorationWeekendObj).doubleValue() : Double.parseDouble((String) majorationWeekendObj));
            
            parametresDAO.updateParametres(parametres);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Paramètres enregistrés avec succès");
            out.print(gson.toJson(result));
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            out.print(gson.toJson(error));
        }
    }
}