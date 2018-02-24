package com.techelevator.campground;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Campsite;
import com.techelevator.campground.model.CampsiteDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.JDBC.JDBCCampgroundDAO;
import com.techelevator.campground.model.JDBC.JDBCCampsiteDAO;
import com.techelevator.campground.model.JDBC.JDBCParkDAO;
import com.techelevator.campground.model.JDBC.JDBCReservationDAO;
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
	private CampsiteDAO campsiteDAO;
	private ReservationDAO reservationDAO;

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

		campsiteDAO = new JDBCCampsiteDAO(datasource);

		reservationDAO = new JDBCReservationDAO(datasource);
	}

	public void run() {
		while (true) {
			Park[] allParks = parkDAO.getAllParks();

			Park chosenPark = (Park) menu.getChoiceFromOptions(allParks);

			menu.printPark(chosenPark);

			handleParkMenu(chosenPark);

		}

	}

	private void printAllCampgrounds(Park chosenPark) {
		// Display's selected park
		System.out.println("\nPark Campgrounds for: " + chosenPark);

		// Makes a list of the campgrounds within the passed in park
		Campground[] campgrounds = campgroundDAO.getCampgroundPerPark(chosenPark);

		// CHecks that there are campgrounds within the selected park
		if ((campgrounds.length - 1) >= 0) {
			// Prints campground heading
			System.out.println("     Name                           Open          Close          Daily Fee");

			// Prints out available campgrounds
			menu.displayCampgroundMenuOptions(campgrounds);

		} else {
			// Prints that there are no results
			System.out.println("\n*** No results ***");
		}

	}

	private void handleParkMenu(Park chosenPark) {
		// Displays Park Menu
		String choice = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);

		// Handle user choice
		if (choice.equals(CAMP_MENU_VIEW_CAMPGROUND)) {
			// Displays all selected camp grounds from the chosen park
			printAllCampgrounds(chosenPark);
			handleCampgroundMenu(chosenPark);

			// handleViewActiveCampgrounds(chosenPark);
		}

		// This is some bonus business

		// else if (choice.equals(CAMP_MENU_SEARCH_FOR_RESERVATION)) {
		// Campground[] allCampgroundsFromPark =
		// campgroundDAO.getCampgroundPerPark(chosenPark);
		// //menu.displayCampgroundMenuOptions(allCampgroundsFromPark);
		// listCampgroundSkeleton(allCampgroundsFromPark);
		// }

		else if (choice.equals(CAMP_MENU_RETURN_TO_PREVIOUS_SCREEN)) {
			return;
		}
	}

	// Displays the Campground Menu and handles user choice
	private void handleCampgroundMenu(Park chosenPark) {
		// Displays the Campground Menu Options and returns user input as choice
		String choice = (String) menu.getChoiceFromOptions(SELECTED_CAMPGROUND_MENU_OPTIONS);

		// Handles selection of Search For Reservation
		if (choice.equals(CAMP_MENU_SEARCH_FOR_RESERVATION)) {
			handleReservationSearchMenu(chosenPark);

		} else if (choice.equals(CAMP_MENU_RETURN_TO_PREVIOUS_SCREEN)) {
			return;
		}
	}

	private void handleReservationSearchMenu(Park chosenPark) {
		Object userChoice = null;
		while (userChoice == null) {
			// Displays a list of campgrounds within the chosen park
			printAllCampgrounds(chosenPark);
			// Gets a list of campgrounds for the chosen park
			Campground[] allCampgroundsFromPark = campgroundDAO.getCampgroundPerPark(chosenPark);
			// Returns the user selected object that is then cast into a campground
			userChoice = menu.getChoiceFromCampgroundOptions(allCampgroundsFromPark);
			if(userChoice.equals("Exit")) {
				return;
			} else {
				Campground selectedCampground = (Campground) menu.getChoiceFromCampgroundOptions(allCampgroundsFromPark);
				// Ask for the arrival date
				LocalDate arrivalDate = menu
						.getDateFromUser("What is the arrival date? Please enter a date in this format YYYY-MM-DD: ");
				// Ask for the departure date
				LocalDate departureDate = menu
						.getDateFromUser("What is the departure date? Please enter a date in this format YYYY-MM-DD: ");
				// Get available reservations within the desired search dates and campsite id
				Campsite[] selectedCampsites = campsiteDAO.getSelectedCampsites(chosenPark.getId(), arrivalDate,
						departureDate);
				// Get the amount of days for the desired stay and save it to a big decimal
				BigDecimal durationOfStay = new BigDecimal(ChronoUnit.DAYS.between(arrivalDate, departureDate));
				// Calculate the price of the stay based on the duration of stay and the daily
				// fee of the campground
				BigDecimal calculatedPrice = durationOfStay.multiply(selectedCampground.getDailyFee());
				// Prints all of the available sites for the desired date range along with the
				// newly calculated price

				menu.displayAvailableReservations(selectedCampsites, calculatedPrice);
				// Prompts the user for the desired campsite
				userChoice = menu.getChoiceFromCampsiteOptions(selectedCampsites);
				if (userChoice.equals("Exit")) {
					userChoice = null;
				} else {
					Campsite chosenSite = (Campsite) userChoice;
					// Prompts the user for the desired name to reserve under
					String reservationName = menu.getReservationName();
					// Get's the site ID of the chosen campsite
					int siteId = chosenSite.getSiteNumber();
					Long reservationId = reservationDAO.enterReservation(siteId, reservationName, arrivalDate,
							departureDate);
					reservationConfirmation(reservationId);
					return;
				}
			}
			

		}

	}

	private void reservationConfirmation(Long reservationId) {
		System.out.println("Thank you! The resrvation has been made and the reservation ID is: " + reservationId);
		System.exit(0);
	}

}
