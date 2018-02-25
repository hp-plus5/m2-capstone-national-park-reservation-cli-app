package com.techelevator.campground.model.JDBC;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Reservation;

public class JDBCReservationDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCReservationDAO sut;
	private int newSiteId;
	JdbcTemplate jdbcTemplate;
	Reservation newReservation;

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
		sut = new JDBCReservationDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sqlnewSite = "INSERT INTO site(campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) "
				+ "VALUES(?, ?, ?, ?, ?, ?) RETURNING site_id";
		newSiteId = jdbcTemplate.queryForObject(sqlnewSite, int.class, 1, 1, 0, false, 0, false);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testEnterReservation() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		Long reservationId = sut.enterReservation(newSiteId, "Testy McTesterson", LocalDate.parse("2018-01-03"),
				LocalDate.parse("2018-01-05"));
		String sqlGetNewReservation = "SELECT * FROM reservation " + "WHERE reservation_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetNewReservation, reservationId);

		while(results.next()) {
			newReservation = mapRowToReservation(results);
		}
		assertEquals("Testy McTesterson", newReservation.getName());
	}
	
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation theReservation = new Reservation();
		theReservation.setReservationId(results.getLong("reservation_id"));
		theReservation.setSiteId(results.getInt("site_id"));
		theReservation.setName(results.getString("name"));
		theReservation.setFromDate(results.getDate("from_date").toLocalDate());
		theReservation.setToDate(results.getDate("to_date").toLocalDate());
		theReservation.setCreateDate(results.getDate("create_date").toLocalDate());
		return theReservation;
	}

}
