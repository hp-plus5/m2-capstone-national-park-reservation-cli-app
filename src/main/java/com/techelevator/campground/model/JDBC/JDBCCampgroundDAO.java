package com.techelevator.campground.model.JDBC;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Campground[] getCampgroundPerPark(Park park) {
		List<Campground> allCampgroundsPerPark = new ArrayList<>();

		String sqlGetAllCampgroundsPerPark = "SELECT * " + "FROM campground " + "WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampgroundsPerPark, park.getId());
		while (results.next()) {
			Campground theCampground = mapRowToCampground(results);
			allCampgroundsPerPark.add(theCampground);
		}
		return allCampgroundsPerPark.toArray(new Campground[allCampgroundsPerPark.size()]);
	}

	private Campground mapRowToCampground(SqlRowSet results) {
		Campground theCampground;
		theCampground = new Campground();
		theCampground.setCampgroundId(results.getLong("campground_id"));
		theCampground.setParkId(results.getLong("park_id"));
		theCampground.setName(results.getString("name"));
		theCampground.setOpenFromDate(results.getInt("open_from_mm"));
		theCampground.setOpenToDate(results.getInt("open_to_mm"));
		theCampground.setDailyFee(results.getBigDecimal("daily_fee"));
		return theCampground;
	}
}
