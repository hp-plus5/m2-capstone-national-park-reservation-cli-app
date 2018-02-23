package com.techelevator.campground;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.JDBC.JDBCCampgroundDAO;
import com.techelevator.campground.model.JDBC.JDBCParkDAO;
import com.techelevator.campground.view.Menu;

public class CampgroundCLI {

	private static final String CAMP_MENU_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";

	private static final String CAMP_MENU_VIEW_CAMPGROUND = "View Campgrounds";
	private static final String CAMP_MENU_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMP_MENU_VIEW_CAMPGROUND,
			CAMP_MENU_SEARCH_FOR_RESERVATION, CAMP_MENU_RETURN_TO_PREVIOUS_SCREEN };
	private static final String[] SELECTED_CAMPGROUND_MENU_OPTIONS = new String[] { CAMP_MENU_SEARCH_FOR_RESERVATION,
			CAMP_MENU_RETURN_TO_PREVIOUS_SCREEN };

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);

		parkDAO = new JDBCParkDAO(datasource);

		campgroundDAO = new JDBCCampgroundDAO(datasource);
	}

	public void run() {
		Park[] allParks = parkDAO.getAllParks();

		Park chosenPark = (Park) menu.getChoiceFromOptions(allParks);

		menu.printPark(chosenPark);

		handleCampgroundCommands(chosenPark);

		Campground[] allCampgroundsFromPark = campgroundDAO.getCampgroundPerPark(chosenPark);
		Campground chosenCampground = (Campground) menu.getChoiceFromOptions(allCampgroundsFromPark);
		
		handleCampgroundAfterSelection(chosenPark); //THIS IS KEY< BUT WHY??!?!?!?!?!?!?!?!?!?!
	}

	private void handleCampgroundAfterSelection(Park chosenPark) {
		//System.out.println("Select a Command");
		String choice = (String) menu.getChoiceFromOptions(SELECTED_CAMPGROUND_MENU_OPTIONS);
		if (choice.equals(CAMP_MENU_SEARCH_FOR_RESERVATION)) {
			// handleSearchForReservation();
		} else if (choice.equals(CAMP_MENU_RETURN_TO_PREVIOUS_SCREEN)) {
			// handleRetrunToPreviousScreen();
		}
	}

	private void handleCampgroundCommands(Park chosenPark) {
		//System.out.println("Select a Command");
		String choice = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
		if (choice.equals(CAMP_MENU_VIEW_CAMPGROUND)) {
			handleViewActiveCampgrounds(chosenPark);
		} else if (choice.equals(CAMP_MENU_SEARCH_FOR_RESERVATION)) {
			// handleCampgroundCommandsList();
		} else if (choice.equals(CAMP_MENU_RETURN_TO_PREVIOUS_SCREEN)) {
			// handleRetrunToPreviousScreen();
		}
	}

	private void handleViewActiveCampgrounds(Park chosenPark) {
		System.out.println("\nPark Campgrounds\n" + chosenPark.getName());
		Campground[] campgrounds = campgroundDAO.getCampgroundPerPark(chosenPark);
		listCampgrounds(campgrounds);
	}

	private void listCampgrounds(Campground[] campgrounds) {
		System.out.println();
		if ((campgrounds.length - 1) >= 0) {
			System.out.println("     Name                           Open          Close          Daily Fee");
		} else {
			System.out.println("\n*** No results ***");
		}
	}
}
