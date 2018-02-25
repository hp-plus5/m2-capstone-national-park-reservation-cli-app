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
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;

public class JDBCParkDAOTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO sut;
	private Long newParkId;
	JdbcTemplate jdbcTemplate;
	Park newPark;

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
		sut = new JDBCParkDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sqlNewPark = "INSERT INTO park(name, location, establish_date, area, visitors, description) "
				+ "VALUES(?, ?, ?, ?, ?, ?) RETURNING park_id";
		newParkId = jdbcTemplate.queryForObject(sqlNewPark, Long.class, "Testy McParkerson", "Space Helicopter",
				LocalDate.parse("2018-01-03"), 666, 13, "THE PARK OF DEATH");
		
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testGetAllParks() {
		Park[] parkArray = sut.getAllParks();

		for(Park park : parkArray) {
			if(park.getName().equals("Testy McParkerson")) {
				assertEquals("Space Helicopter", park.getLocation());
				return;
			}
		}
		fail("Could not get park");
	}

}
