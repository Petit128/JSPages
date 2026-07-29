package com.taxis.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        
     // Dans AuthFilter.java, ajoutez ces chemins dans les pages publiques :
        if (uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".jpg") ||
            uri.contains("/login") || uri.contains("/logout") || uri.equals(contextPath + "/") ||
            uri.equals(contextPath) || uri.contains("/client/request") || uri.contains("/api/courses/request") ||
            uri.contains("/api/courses/") || uri.contains("/calcul-prix") || uri.contains("/index.jsp") ||
            uri.contains("/register") || uri.contains("/client/login") || uri.contains("/client/dashboard")) {
            chain.doFilter(request, response);
            return;
        }
        // Vérifier l'authentification
        Object user = session.getAttribute("user");
        
        if (user == null) {
            res.sendRedirect(contextPath + "/");
            return;
        }
        
        chain.doFilter(request, response);
    }
}