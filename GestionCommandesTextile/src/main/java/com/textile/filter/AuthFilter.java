package com.textile.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthFilter implements Filter {
    
    // Pages publiques qui ne nécessitent pas d'authentification
    private static final List<String> PUBLIC_PAGES = Arrays.asList(
        "/login",
        "/inscription",
        "/index.jsp",
        "/",
        "/index.html"
    );
    
    // Ressources statiques publiques
    private static final List<String> STATIC_RESOURCES = Arrays.asList(
        "/css/",
        "/js/",
        "/images/",
        "/fonts/"
    );
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());
        
        System.out.println("AuthFilter - URI: " + uri);
        System.out.println("AuthFilter - Path: " + path);
        
        // Vérifier si la page est publique
        boolean isPublic = false;
        
        // Vérifier les pages publiques exactes
        for (String publicPage : PUBLIC_PAGES) {
            if (path.equals(publicPage) || path.startsWith(publicPage + "?")) {
                isPublic = true;
                break;
            }
        }
        
        // Vérifier les ressources statiques
        for (String resource : STATIC_RESOURCES) {
            if (path.startsWith(resource)) {
                isPublic = true;
                break;
            }
        }
        
        // Si c'est une page publique, on laisse passer
        if (isPublic) {
            System.out.println("AuthFilter - Page publique: " + path);
            chain.doFilter(request, response);
            return;
        }
        
        // Vérifier l'authentification pour les pages protégées
        HttpSession session = req.getSession(false);
        
        if (session == null || session.getAttribute("utilisateur") == null) {
            System.out.println("AuthFilter - Non authentifié, redirection vers login");
            res.sendRedirect(contextPath + "/login");
            return;
        }
        
        // Vérifier les permissions selon le rôle
        String role = (String) session.getAttribute("role");
        
        if (path.startsWith("/admin") && !"ADMIN".equals(role)) {
            System.out.println("AuthFilter - Accès admin refusé pour rôle: " + role);
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès non autorisé");
            return;
        }
        
        if (path.startsWith("/production") && !"RESPONSABLE_PRODUCTION".equals(role) && !"OPERATEUR".equals(role)) {
            System.out.println("AuthFilter - Accès production refusé pour rôle: " + role);
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès non autorisé");
            return;
        }
        
        if (path.startsWith("/client") && !"CLIENT".equals(role)) {
            System.out.println("AuthFilter - Accès client refusé pour rôle: " + role);
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès non autorisé");
            return;
        }
        
        System.out.println("AuthFilter - Accès autorisé pour: " + role);
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {}
}