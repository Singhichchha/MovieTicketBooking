package com.ticketbooking;

import java.sql.SQLException;

import com.ticketbooking.exception.ticketIdExceptions;
import com.ticketbooking.ticketInterface.ticketInterface;

public class ticketMain {
	public static void main(String[] args) throws ticketIdExceptions, SQLException {
		ticketInterface ticketInterface = new ticketInterface();
		ticketInterface.Interface();
	}
}