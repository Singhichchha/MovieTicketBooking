package com.ticketbooking.ticketInterface;

import java.sql.SQLException;
import java.util.*;

import com.ticketbooking.controller.ticketbookingController;
import com.ticketbooking.exception.ticketIdExceptions;

public class ticketInterface 
{
	public void Interface() throws SQLException, ticketIdExceptions {
		ticketbookingController ticketbookingController = new ticketbookingController();
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        while (true) {
            System.out.println();
            System.out.println("1. Show Available Movies for booking");
            System.out.println("2. Search Movies");
            System.out.println("3. Book Tickets");
            System.out.println("4. Cancel Tickets");
            System.out.println("5. Add more in my ticket");
            System.out.println("6. Exit");
            System.out.println();
            System.out.print("Enter your choice: ");
            
            while(!sc.hasNextInt() || ((choice = sc.nextInt()) <= 0)) {
            	 System.out.print("Enter a valid choice: ");
            	 sc.nextLine();
            }

            if (choice == 1) {
                ticketbookingController.showAvailableMovies();
            } 
            
            // SEARCH MOVIES
            
            else if (choice == 2) {
            	sc.nextLine();
            	ticketbookingController.searchMovies();
            } 
            
         // BOOK MOVIE
            
            else if (choice == 3) {
            	ticketbookingController.bookMovie();
            } 
            
            // DELETE BOOKING 
     
            else if (choice == 4) {
            	ticketbookingController.cancelTicket();
            }
            
            // UPDATE TICKETS

           else if (choice == 5) {
        	    sc.nextLine();
        	    ticketbookingController.updateTickets();
            } 
            
           else if (choice == 6) {
               System.out.println("Exiting from the booking system!");
               break;
           }
            
           else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
        
    }
	
	
}


