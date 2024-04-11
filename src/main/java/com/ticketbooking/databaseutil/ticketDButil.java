package com.ticketbooking.databaseutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ticketDButil {
	
	public static Connection TicketConnection() {
		Connection mycon = null;
		try {
			
			String dbUrl = "jdbc:mysql://localhost:3306/Ticket";
			String user = "root";
			String pass = "root";
			mycon = DriverManager.getConnection(dbUrl, user, pass);
		}catch(SQLException e) {
			System.out.println("Connection ERROR!");
			e.printStackTrace();
		}
		
		return mycon;
	}

}
