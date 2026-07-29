<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.parking.model.ParkingEntry" %>
<%@ page import="com.parking.model.ParkingSpot" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Entry/Exit Management - Parking Management System</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
        .header {
            background-color: #333;
            color: white;
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .container {
            padding: 20px;
            max-width: 1200px;
            margin: 0 auto;
        }
        .forms-container {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
            margin-bottom: 30px;
        }
        .form-card {
            background-color: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .form-card h3 {
            margin-top: 0;
            color: #333;
            border-bottom: 2px solid #4CAF50;
            padding-bottom: 10px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #666;
            font-weight: bold;
        }
        select, input[type="text"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            width: 100%;
        }
        button:hover {
            background-color: #45a049;
        }
        .table-container {
            background-color: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f8f9fa;
            font-weight: bold;
            color: #333;
        }
        .exit-btn {
            background-color: #dc3545;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        .exit-btn:hover {
            background-color: #c82333;
        }
        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            color: #007bff;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
        .message {
            background-color: #d4edda;
            color: #155724;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>Parking Management System - Entry/Exit</h1>
    </div>
    
    <div class="container">
        <a href="dashboard" class="back-link">← Back to Dashboard</a>
        
        <% if (request.getAttribute("message") != null) { %>
            <div class="message"><%= request.getAttribute("message") %></div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>
        
        <div class="forms-container">
            <!-- Entry Form -->
            <div class="form-card">
                <h3>Record Vehicle Entry</h3>
                <form action="entry-exit" method="post">
                    <input type="hidden" name="action" value="entry">
                    
                    <div class="form-group">
                        <label for="spotId">Select Parking Spot:</label>
                        <select id="spotId" name="spotId" required>
                            <option value="">Choose a spot...</option>
                            <% 
                                List<ParkingSpot> availableSpots = (List<ParkingSpot>) request.getAttribute("availableSpots");
                                for (ParkingSpot spot : availableSpots) {
                            %>
                            <option value="<%= spot.getId() %>">
                                <%= spot.getSpotNumber() %> - <%= spot.getSpotType() %>
                            </option>
                            <% } %>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="vehiclePlate">Vehicle Plate:</label>
                        <input type="text" id="vehiclePlate" name="vehiclePlate" required 
                               pattern="[A-Z0-9-]+" title="Enter valid plate number">
                    </div>
                    
                    <button type="submit">Record Entry</button>
                </form>
            </div>
            
            <!-- Exit Form -->
            <div class="form-card">
                <h3>Record Vehicle Exit</h3>
                <form action="entry-exit" method="post">
                    <input type="hidden" name="action" value="exit">
                    
                    <div class="form-group">
                        <label for="entryId">Select Vehicle to Exit:</label>
                        <select id="entryId" name="entryId" required>
                            <option value="">Choose a vehicle...</option>
                            <% 
                                List<ParkingEntry> activeEntries = (List<ParkingEntry>) request.getAttribute("activeEntries");
                                for (ParkingEntry entry : activeEntries) {
                            %>
                            <option value="<%= entry.getId() %>">
                                <%= entry.getVehiclePlate() %> - Since <%= entry.getEntryTime() %>
                            </option>
                            <% } %>
                        </select>
                    </div>
                    
                    <button type="submit">Record Exit & Calculate Payment</button>
                </form>
            </div>
        </div>
        
        <!-- Active Entries Table -->
        <div class="table-container">
            <h3>Currently Parked Vehicles</h3>
            <table>
                <thead>
                    <tr>
                        <th>Vehicle Plate</th>
                        <th>Spot</th>
                        <th>Entry Time</th>
                        <th>Duration</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        for (ParkingEntry entry : activeEntries) {
                            long duration = System.currentTimeMillis() - entry.getEntryTime().getTime();
                            long hours = duration / (60 * 60 * 1000);
                            long minutes = (duration % (60 * 60 * 1000)) / (60 * 1000);
                    %>
                    <tr>
                        <td><%= entry.getVehiclePlate() %></td>
                        <td><%= entry.getSpotId() %></td>
                        <td><%= entry.getEntryTime() %></td>
                        <td><%= hours %>h <%= minutes %>m</td>
                        <td>
                            <form action="entry-exit" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="exit">
                                <input type="hidden" name="entryId" value="<%= entry.getId() %>">
                                <button type="submit" class="exit-btn">Exit</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>