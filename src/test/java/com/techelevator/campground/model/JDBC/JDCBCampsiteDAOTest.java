package com.techelevator.campground.model.JDBC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Campsite;

public class JDCBCampsiteDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCCampsiteDAO sut;
	private LocalDate arrivalDate = LocalDate.parse("2040-01-01");
	private LocalDate departureDate = LocalDate.parse("2040-01-02");
	private Long createdSiteId;
	private Long reservationId;

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
		String sqlnewSite = "INSERT INTO site(campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) "
				+ "VALUES(?, ?, ?, ?, ?, ?) RETURNING site_id";
		createdSiteId = jdbcTemplate.queryForObject(sqlnewSite, Long.class, 1, 666, 0, false, 0, false);

		String sqlNewReservation = "INSERT INTO reservation(site_id, name, from_date, to_date, create_date) "
				+ "VALUES(?, ?, ?, ?, ?) RETURNING reservation_id";
		reservationId = jdbcTemplate.queryForObject(sqlNewReservation, Long.class, createdSiteId, "Balls", LocalDate.parse("2030-01-01"),
				LocalDate.parse("2030-01-02"), LocalDate.parse("2030-01-01"));

	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testGetSelectedCampsitesWithValidDate() {
		System.out.println(createdSiteId);
		System.out.println(reservationId);
		System.out.println(sut.getSelectedCampsites(createdSiteId, arrivalDate, departureDate));
		Campsite[] selectedSites = sut.getSelectedCampsites(createdSiteId, arrivalDate, departureDate);

		for (Campsite camp : selectedSites) {
			if (camp.getSiteNumber() == 666) {
				assertEquals(createdSiteId, camp.getSiteId());
				assertEquals(666, camp.getSiteNumber());
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
