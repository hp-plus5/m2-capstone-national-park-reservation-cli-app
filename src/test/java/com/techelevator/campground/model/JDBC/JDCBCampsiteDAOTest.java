package com.techelevator.campground.model.JDBC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campsite;

public class JDCBCampsiteDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCCampsiteDAO sut;
	private LocalDate arrivalDate = LocalDate.parse("2040-01-01");
	private LocalDate departureDate = LocalDate.parse("2040-01-02");
	private Long siteId;
	private Long reservationId;
	private Long parkId;
	private Long campgroundId;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");

		dataSource.setAutoCommit(false);
		// Normally when you use the data source it automatically opens and closes a
		// transaction after each command
		// The above code will change this so that it does not auto commit after each
		// transaction
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dataSource.destroy();
	}

	@Before
	public void setUp() throws Exception {
		sut = new JDBCCampsiteDAO(dataSource);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
// park creation
	// park: dates
		String sEstablishDate = "2001/04/01";
		Date EstablishDate = new SimpleDateFormat("YYYY/MM/dd").parse(sEstablishDate);
	// park: creation
		String sqlNewPark = "INSERT INTO park (name, location, establish_date, area, visitors, description) VALUES (?, ?, ?, ?, ?, ?) RETURNING park_id";
		parkId = jdbcTemplate.queryForObject(sqlNewPark, Long.class, "Name Test", "Location Test", EstablishDate, 10000,
				2000, "Description of Park");
// camp ground creation
		String sqlNewCampground = "INSERT INTO campground (park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES (?, ?, ?, ?, ?) RETURNING campground_id";
		campgroundId = jdbcTemplate.queryForObject(sqlNewCampground, Long.class, parkId, "Name Campground", 01, 12, 10);
// camp site creation
		String sqlnewSite = "INSERT INTO site(campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) "
				+ "VALUES(?, ?, ?, ?, ?, ?) RETURNING site_id";
		siteId = jdbcTemplate.queryForObject(sqlnewSite, Long.class, campgroundId, 637, 0, false, 0, false);
// reservation creation
		String sqlNewReservation = "INSERT INTO reservation(site_id, name, from_date, to_date, create_date) "
				+ "VALUES(?, ?, ?, ?, ?) RETURNING reservation_id";
		reservationId = jdbcTemplate.queryForObject(sqlNewReservation, Long.class, siteId, "Balls",
				LocalDate.parse("2030-01-01"), LocalDate.parse("2030-01-02"), LocalDate.parse("2030-01-01"));
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testGetSelectedCampsitesWithValidDate() {
		System.out.println(siteId);
		System.out.println(reservationId);
		System.out.println(sut.getSelectedCampsites(campgroundId, arrivalDate, departureDate));
		Campsite[] selectedSites = sut.getSelectedCampsites(campgroundId, arrivalDate, departureDate);

		for (Campsite camp : selectedSites) {
			if (camp.getSiteNumber() == 637) {
				assertEquals(siteId, camp.getSiteId());
				assertEquals(637, camp.getSiteNumber());
				return;
			}
		}
		fail("Campsite not selected");
	}
}

/*
 * Some old shit
 * 
 * setup sut = new JDBCCampsiteDAO(dataSource); jdbcTemplate = new
 * JdbcTemplate(dataSource); String sqlnewSite =
 * "INSERT INTO site(campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) "
 * + "VALUES(?, ?, ?, ?, ?, ?) RETURNING site_id"; newCampsiteId =
 * jdbcTemplate.queryForObject(sqlnewSite, Long.class, 1, 1, 0, false, 0,
 * false);
 * 
 * String sqlNewReservation =
 * "INSERT INTO reservation(site_id, name, from_date, to_date, create_date) " +
 * "VALUES(?, ?, ?, ?, ?) RETURNING reservation_id"; existingReservationId =
 * jdbcTemplate.queryForObject(sqlNewReservation, Long.class, newCampsiteId,
 * "Mr. Death", LocalDate.parse("2018-01-01"), LocalDate.parse("2018-02-01"),
 * LocalDate.parse("2018-01-01"));
 * 
 * testGetSelectedCampsitesWithValidDate
 * 
 * Campsite[] newCampsites = sut.getSelectedCampsites(newCampsiteId,
 * LocalDate.parse("2019-01-01"), LocalDate.parse("2019-02-01")); for(Campsite
 * camp : newCampsites) { if (camp.getSiteId() == newCampsiteId) {
 * assertEquals(newCampsiteId, camp.getSiteId()); return; } }
 * 
 * fail("Site not returned");
 * 
 */
