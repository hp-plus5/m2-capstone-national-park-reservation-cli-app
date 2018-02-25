package com.techelevator.campground.model.JDBC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Park;

public class JDBCCampgroundDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO sut;
	private Long newCampgroundId;
	private Long newParkId;
	private Park newPark;

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
		sut = new JDBCCampgroundDAO(dataSource);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		// creating a park
		String sEstablishDate = "2001/04/01";
		Date EstablishDate = new SimpleDateFormat("YYYY/MM/dd").parse(sEstablishDate);

		String sNewPark = "INSERT INTO park (name, location, establish_date, area, visitors, description) VALUES (?, ?, ?, ?, ?, ?) RETURNING park_id";
		newParkId = jdbcTemplate.queryForObject(sNewPark, Long.class, "Name Test", "Location Test", EstablishDate, 10000,
				2000, "Description of Park");
		String sqlGetNewPark = "SELECT * FROM park " + "WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetNewPark, newParkId);

		while (results.next()) {
			newPark = mapRowToPark(results);
		}
		// creating a campground
		String newCampground = "INSERT INTO campground (park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES (?, ?, ?, ?, ?) RETURNING campground_id";
		newCampgroundId = jdbcTemplate.queryForObject(newCampground, Long.class, newParkId, "Name Test", 01, 12, 10);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testGetCampgroundPerPark() {
		Campground[] allCampgroundsPerPark = sut.getCampgroundPerPark(newPark);

		for (Campground camp : allCampgroundsPerPark) {
			if (camp.getCampgroundId().equals(newCampgroundId) && camp.getName().equals("Name Test")) {
				assertEquals("Name Test", camp.getName());
				return;
			}
		}
		fail("Campground Not Found");
	}

	private Park mapRowToPark(SqlRowSet results) {
		Park thePark;
		thePark = new Park();
		thePark.setId(results.getLong("park_id"));
		thePark.setName(results.getString("name"));
		thePark.setLocation(results.getString("location"));
		thePark.setEstablishDate(results.getDate("establish_date").toLocalDate());
		thePark.setArea(results.getInt("area"));
		thePark.setVisitors(results.getInt("visitors"));
		thePark.setDescription(results.getString("description"));
		return thePark;
	}
}