package com.taxis.controller;

import com.taxis.dao.ParametresDAO;
import com.taxis.model.Parametres;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/parametres")
public class ParametresServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ParametresDAO parametresDAO;
    
    @Override
    public void init() {
        parametresDAO = new ParametresDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Parametres parametres = parametresDAO.getParametres();
            request.setAttribute("parametres", parametres);
            request.getRequestDispatcher("/views/admin/parametres.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Parametres parametres = new Parametres();
            
            parametres.setPrixPriseEnCharge(Double.parseDouble(request.getParameter("prixBase")));
            parametres.setPrixKmJour(Double.parseDouble(request.getParameter("prixKmJour")));
            parametres.setPrixKmNuit(Double.parseDouble(request.getParameter("prixKmNuit")));
            parametres.setPrixAttenteMin(Double.parseDouble(request.getParameter("prixAttente")));
            parametres.setCommissionEntreprise(Double.parseDouble(request.getParameter("commission")));
            parametres.setFraisBagages(Double.parseDouble(request.getParameter("fraisBagages")));
            parametres.setFraisAnimaux(Double.parseDouble(request.getParameter("fraisAnimaux")));
            parametres.setMajorationNuit(Double.parseDouble(request.getParameter("majorationNuit")));
            parametres.setMajorationWeekend(Double.parseDouble(request.getParameter("majorationWeekend")));
            
            parametresDAO.updateParametres(parametres);
            response.sendRedirect("parametres?success=1");
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("parametres?error=1");
        }
    }
}