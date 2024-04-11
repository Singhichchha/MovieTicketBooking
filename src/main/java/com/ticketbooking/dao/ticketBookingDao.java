package com.ticketbooking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

import com.ticketbooking.databaseutil.ticketDButil;
import com.ticketbooking.exception.ticketIdExceptions;

public class ticketBookingDao {

	public ticketBookingDao() {
		System.out.println("------------------------------");
		System.out.println(" Movie Ticket Booking System ");
		System.out.println("------------------------------");
	}

	// Available Movies
	
	public void showAvailableMovies() {
		try (Connection mycon = ticketDButil.TicketConnection();) {
			PreparedStatement preparedStatement = mycon.prepareStatement("SELECT * FROM THEATRE");
			ResultSet resultSet = preparedStatement.executeQuery();

			System.out.println("Movies Available for Booking");
			System.out.println(
					"ID\tTheatre Name\t Movie Name\t\t\tDuration    Time Slot\t    Date\t  TheatreLocation\t\t\tPrice\tAvailableSeats");
			while (resultSet.next()) {
				System.out.printf("%-6d  %-15s  %-31s  %-10d %-10s  %-14s %-38s  %-10d  %-15d%n",
						resultSet.getInt("movie_id"), resultSet.getString("th_name"), resultSet.getString("movie_name"),
						resultSet.getInt("duration"), resultSet.getString("slot"), resultSet.getString("movie_date"), resultSet.getString("location"),
						resultSet.getInt("price"), resultSet.getInt("avail_seats"));
			}

		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}


	//Search Movie
	
	public void searchMovie(String title) {
		try (Connection mycon = ticketDButil.TicketConnection()) {
			
			String query = "SELECT * FROM THEATRE WHERE movie_name = ?";
			PreparedStatement preparedStatement = mycon.prepareStatement(query);
			preparedStatement.setString(1, title);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				System.out.println("Movie Found:");
				
				System.out.println(
						"ID\tTheatre Name\t Movie Name\t\t\tDuration\tTime Slot\t Theatre Location\t\t\tPrice\tAvailable Seats");
				do {
					System.out.printf("%-6d  %-15s  %-31s  %-13d  %-14s  %-38s  %-10d  %-15d%n",
							resultSet.getInt("movie_id"), resultSet.getString("th_name"), resultSet.getString("movie_name"),
							resultSet.getInt("duration"), resultSet.getString("slot"), resultSet.getString("location"),
							resultSet.getInt("price"), resultSet.getInt("avail_seats"));
				} while (resultSet.next());
			}else{
				System.out.println("Movie not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	// Book Movie

	public void bookMovie(String name, String mobile_no, int id, int no_ticket) {
		try (Connection mycon = ticketDButil.TicketConnection()) {
			// Fetch the price of the ticket from the THEATRE table
			String priceQuery = "SELECT price, avail_seats FROM THEATRE WHERE movie_id = ?";
			PreparedStatement priceStatement = mycon.prepareStatement(priceQuery);
			priceStatement.setInt(1, id);
			ResultSet resultSet = priceStatement.executeQuery();
			
			if(resultSet.next()) {
				int price = resultSet.getInt("price");
				int avail_seats = resultSet.getInt("avail_seats");

				// Check if enough seats are available
				if (avail_seats < no_ticket) {
					System.out.println("Not Enough Seats are Available!");
					return;
				}

				// Calculate the total price
				int total_price = no_ticket * price;

				LocalDateTime now = LocalDateTime.now();
				// Create b_id from the first 3 characters of name and all the characters of
				// mobile_no
				String b_id = name.substring(0, 3) + mobile_no.substring(0, 3) + now.getSecond();

				// Insert the booking details into the BOOKING table
				String query = "INSERT INTO BOOKING (booking_id, user_name, mobile_no, movie_id, booked_seat, price, booking_date) VALUES (?, ?, ?, ?, ?, ?, NOW())";
				PreparedStatement bookingStatement = mycon.prepareStatement(query);
				bookingStatement.setString(1, b_id);
				bookingStatement.setString(2, name);
				bookingStatement.setString(3, mobile_no);
				bookingStatement.setInt(4, id);
				bookingStatement.setInt(5, no_ticket);
				bookingStatement.setInt(6, total_price);
				int rowsAffected = bookingStatement.executeUpdate();
				if (rowsAffected > 0) {
					// Update the number of available seats in the THEATRE table
					String updateQuery = "UPDATE THEATRE SET avail_seats = avail_seats - ? WHERE movie_id = ?";
					PreparedStatement updateStatement = mycon.prepareStatement(updateQuery);
					updateStatement.setInt(1, no_ticket);
					updateStatement.setInt(2, id);
					updateStatement.executeUpdate();
					System.out.println("Booking Done! Total price: " + total_price);
					System.out.println("Your ticket id is: " + b_id);
				} else {
					System.out.println("Invalid input");
				}
			}else {
				throw new ticketIdExceptions("No booking found with ticketid: " + id);
			}
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}



	// Cancel Movie
	
	public void cancelTicket(String ticketId) {
		try (Connection mycon = ticketDButil.TicketConnection();) {
			// Fetch the booking details from the BOOKING table
			String bookingQuery = "SELECT movie_id, booked_seat FROM BOOKING WHERE booking_id = ?";
			PreparedStatement bookingStatement = mycon.prepareStatement(bookingQuery);
			bookingStatement.setString(1, ticketId);
			ResultSet resultSet = bookingStatement.executeQuery();
			if (!resultSet.next()) {
				throw new ticketIdExceptions("No booking found with ticketid: " + ticketId);
			}
			int movie_id = resultSet.getInt("movie_id");
			int booked_seat = resultSet.getInt("booked_seat");

			// Delete the booking from the BOOKING table
			String deleteQuery = "DELETE FROM BOOKING WHERE booking_id = ?";
			PreparedStatement deleteStatement = mycon.prepareStatement(deleteQuery);
			deleteStatement.setString(1, ticketId);
			int rowsAffected = deleteStatement.executeUpdate();
			if (rowsAffected > 0) {
				// Update the number of available seats in the THEATRE table
				String updateQuery = "UPDATE THEATRE SET avail_seats = avail_seats + ? WHERE movie_id = ?";
				PreparedStatement updateStatement = mycon.prepareStatement(updateQuery);
				updateStatement.setInt(1, booked_seat);
				updateStatement.setInt(2, movie_id);
				updateStatement.executeUpdate();
				System.out.println("Booking cancelled successfully!");
			} else {
				throw new ticketIdExceptions("No booking found with ticketid: " + ticketId);
			}

		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}


	// UPDATING TICKETS
	
	public void updateTicket(String ticketid, int additionalticket) throws ticketIdExceptions {
		try (Connection mycon = ticketDButil.TicketConnection()) {

			// Fetch the existing booking details
			String fetchQuery = "SELECT t1.booked_seat, t1.price, t1.movie_id, t2.avail_seats, t2.price FROM BOOKING as t1 INNER JOIN THEATRE as t2 ON t1.movie_id = t2.movie_id WHERE t1.booking_id = ?";
			PreparedStatement fetchStatement = mycon.prepareStatement(fetchQuery);
			fetchStatement.setString(1, ticketid);
			ResultSet resultSet = fetchStatement.executeQuery();

			if (resultSet.next()) {
				int tprice = resultSet.getInt("t2.price");
				int tseats = resultSet.getInt("t2.avail_seats");
				int bid = resultSet.getInt("t1.movie_id");

				// Check if enough seats are available
				if (tseats < additionalticket) {
					System.out.println("Not enough seats available");
					return;
				}

				// Calculate the total price
				int finalPrice = additionalticket * tprice;
				System.out.println("Your new price: " + finalPrice);

				// Update the booking
				String updateQuery = "UPDATE BOOKING SET booked_seat = booked_seat + ?, price = price + ? WHERE booking_id = ?";
				PreparedStatement updateStatement = mycon.prepareStatement(updateQuery);
				updateStatement.setInt(1, additionalticket);
				updateStatement.setInt(2, finalPrice);
				updateStatement.setString(3, ticketid);
				updateStatement.executeUpdate();

				// Update Theater Table
				String update = "UPDATE THEATRE SET avail_seats = avail_seats - ? WHERE movie_id = ?";
				updateStatement = mycon.prepareStatement(update);
				updateStatement.setInt(1, additionalticket);
				updateStatement.setInt(2, bid);
				updateStatement.executeUpdate();

				System.out.println("Booking updated successfully!");
			} else {
				throw new ticketIdExceptions("No booking found with ticketid: " + ticketid);
			}
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}


}
