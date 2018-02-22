package com.techelevator.campground;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.JDBC.JDBCParkDAO;
import com.techelevator.campground.view.Menu;

public class CampgroundCLI {
	
	private Menu menu;
	private ParkDAO parkDAO;

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
	}

	public void run() {
		Park[] allParks = parkDAO.getAllParks();
		Park chosenPark = (Park)menu.getChoiceFromOptions(allParks);
		
		menu.printPark(chosenPark);
	}
}
