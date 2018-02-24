package com.techelevator.campground.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.techelevator.campground.model.Campsite;
import com.techelevator.campground.model.Park;

//TODO: Come back in and reorder this please

public class Menu {

	private PrintWriter out;
	private Scanner in;
	private boolean mainMenu = true;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		if (userInput.equals("Q") || userInput.equals("q") && mainMenu) {
			System.exit(0);
		}
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will
			// be null
		}
		if (choice == null) {
			out.println("\n*** " + userInput + " is not a valid option ***\n");
		}
		mainMenu = false;
		return choice;
	}

	// private Object getChoiceFromUserInput(Object[] options) {
	// return getChoiceFromUserInput(options, "");
	// }

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		showQuit();
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}

	public Object getChoiceFromCampgroundOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			out.print("Which campground (enter 0 to cancel)? ");
			// TODO: don't handle when 0 is entered
			out.flush();
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	public void displayCampgroundMenuOptions(Object[] options) {
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println("#" + optionNum + " " + options[i]);
		}
		out.flush();
	}

	private void showQuit() {
		if (mainMenu) {
			out.println("Q) Quit");
		}
	}

	public void printPark(Park chosenPark) {
		out.printf(
				"\n%s National Park \nLocation: %s \nEstablished: %s \nArea: %,d square kilometers \nAnnual Visitors: %,d \n\n%s\n",
				chosenPark.getName(), chosenPark.getLocation(), chosenPark.getEstablishDate(), chosenPark.getArea(),
				chosenPark.getVisitors(), chosenPark.getDescription());
		out.flush();
	}

	public LocalDate getDateFromUser(String prompt) {
		while (true) {
			// Print out the passed in prompt to user
			out.print(prompt);
			// Flush because we want this to display to the user
			out.flush();
			// Get input from user
			String userDate = in.nextLine();
			// Is this a real date?
			try {
				return LocalDate.parse(userDate);
			} catch (DateTimeParseException ex) {
				out.println("Please enter a valid date in this format: 2007-12-03");
				out.flush();
			}
		}
	}

	public void displayAvailibleReservations(Campsite[] availibleSites, BigDecimal calculatedPrice) {
		// TODO: move logic to somewhere else
		if (availibleSites.length == 0) {
			out.println("\nWe're sorry, there are no availible campsites for the specified date range");
			out.flush();
		} else {
			out.println("\nSite ID   Maximum Occupancy  Accesible?  Maximum RV Length  Utility  Daily Fee");
			for (Campsite site : availibleSites) {
				out.println(site + " $" + calculatedPrice);
			}
			out.flush();
		}
	}

	public Object getChoiceFromCampsiteOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			out.print("Which site should be reserved (enter 0 to cancel)? ");
			out.flush();
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	public String getReservationName() {
		out.print("What name should the reservation be made under? ");
		out.flush();
		String name = in.nextLine();
		// TODO: do we care how the user enters their name?
		return name;
	}

}
