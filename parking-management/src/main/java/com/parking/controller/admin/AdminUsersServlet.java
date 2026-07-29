package com.parking.controller.admin;

import com.parking.metier.ParkingService;
import com.parking.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private ParkingService parkingService;
    
    @Override
    public void init() throws ServletException {
        parkingService = new ParkingService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole())) {
            response.sendRedirect("../dashboard");
            return;
        }
        
        List<User> allUsers = parkingService.getAllUsers();
        request.setAttribute("allUsers", allUsers);
        request.setAttribute("user", user);
        
        request.getRequestDispatcher("/WEB-INF/admin/users.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addUser(request, response);
        } else if ("update".equals(action)) {
            updateUser(request, response);
        } else if ("delete".equals(action)) {
            deleteUser(request, response);
        }
        
        response.sendRedirect("users");
    }
    
    private void addUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String role = request.getParameter("role");
            String userType = request.getParameter("userType");
            String vehiclePlate = request.getParameter("vehiclePlate");
            
            User user = new User(username, password, fullName, email, role, userType);
            user.setPhone(phone);
            user.setVehiclePlate(vehiclePlate);
            
            boolean success = parkingService.registerUser(user);
            
            if (success) {
                request.getSession().setAttribute("message", "User added successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to add user");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Invalid input: " + e.getMessage());
        }
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String username = request.getParameter("username");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String userType = request.getParameter("userType");
            String vehiclePlate = request.getParameter("vehiclePlate");
            
            User user = parkingService.getUserById(id);
            if (user != null) {
                user.setUsername(username);
                user.setFullName(fullName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setUserType(userType);
                user.setVehiclePlate(vehiclePlate);
                
                boolean success = parkingService.updateUser(user);
                
                if (success) {
                    request.getSession().setAttribute("message", "User updated successfully");
                } else {
                    request.getSession().setAttribute("error", "Failed to update user");
                }
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid user ID");
        }
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean success = parkingService.deleteUser(id);
            
            if (success) {
                request.getSession().setAttribute("message", "User deleted successfully");
            } else {
                request.getSession().setAttribute("error", "Failed to delete user");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid user ID");
        }
    }
}