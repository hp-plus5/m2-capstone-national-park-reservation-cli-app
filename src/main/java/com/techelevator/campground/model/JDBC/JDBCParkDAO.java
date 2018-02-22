package com.techelevator.campground.model.JDBC;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public Park[] getAllParks() {
		List<Park> allParks = new ArrayList<>();
		
		String sqlGetAllParks = "SELECT * " + "FROM park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllParks);
		while (results.next()) {
			Park thePark = mapRowToPark(results);
			allParks.add(thePark);
		}
		return allParks.toArray(new Park[allParks.size()]);
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
