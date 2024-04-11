package com.ticketbooking.controller;

import java.util.Scanner;

import com.ticketbooking.dao.ticketBookingDao;
import com.ticketbooking.exception.ticketIdExceptions;

public class ticketbookingController {
	ticketBookingDao movieDAO = new ticketBookingDao();
	Scanner sc = new Scanner(System.in);

	// Helper function for mobile number
	public boolean mobileValidate(String mobile_no) {
		return mobile_no.matches("\\d{10}");
	}

	// Helper function for name
	public boolean nameValidate(String name) {
		return name.matches("[a-zA-Z ]+");
	}

	// Show Available movie for booking

	public void showAvailableMovies() {
		movieDAO.showAvailableMovies();
	}

	// Search Movie
	public void searchMovies() {
		String title;
		System.out.print("Enter the movie name: ");
		while (!sc.hasNextLine() || (title = sc.nextLine()).isEmpty()) {
			System.out.print("Enter a valid name: ");
		}
		movieDAO.searchMovie(title);
	}

	// Book Movie
	public void bookMovie() {
		String name, mobile_no;
		int id, no_ticket;
		System.out.print("Enter the id of the movie: ");
		while (!sc.hasNextInt() || ((id = sc.nextInt()) <= 0)) {
			System.out.print("Enter a valid movie id: ");
			sc.nextLine();
		}
		sc.nextLine();

		System.out.print("Enter the number of tickets: ");
		while (!sc.hasNextInt() || ((no_ticket = sc.nextInt()) <= 0)) {
			System.out.print("Enter a valid number: ");
			sc.nextLine();
		}
		sc.nextLine();

		System.out.print("Enter your name: ");
		while (!sc.hasNextLine() || (name = sc.nextLine()).isEmpty() || !nameValidate(name)) {
			System.out.print("Enter a valid name: ");
		}

		System.out.print("Enter your mobile number: ");
		while (!sc.hasNextLine() || (mobile_no = sc.nextLine()).isEmpty() || !mobileValidate(mobile_no)) {
			System.out.print("Enter a valid mobile number: ");
		}

		movieDAO.bookMovie(name, mobile_no, id, no_ticket);
	}

	// Cancel Booking
	public void cancelTicket() throws ticketIdExceptions {
		System.out.print("Enter the ticket id to cancel: ");
		String ticketId = sc.next();
		movieDAO.cancelTicket(ticketId);
	}

	// Update Booking
	public void updateTickets() throws ticketIdExceptions {
		int additionalticket;
		System.out.print("Enter the ticket id: ");
		String ticketid = sc.next();
//         sc.nextLine();
		System.out.print("Enter the number of tickets: ");
		while (!sc.hasNextInt() || ((additionalticket = sc.nextInt()) <= 0)) {
			System.out.print("Enter a valid number: ");
			sc.nextLine();
		}

		movieDAO.updateTicket(ticketid, additionalticket);
	}

}
