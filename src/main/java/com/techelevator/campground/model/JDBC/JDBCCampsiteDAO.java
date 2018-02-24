package com.techelevator.campground.model.JDBC;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campsite;
import com.techelevator.campground.model.CampsiteDAO;

public class JDBCCampsiteDAO implements CampsiteDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCCampsiteDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public Campsite[] getSelectedCampsites(Long campgroundId, LocalDate arrivalDate, LocalDate departureDate) {
		List<Campsite> reserveableCampsites = new ArrayList<>();

		String sqlReserveableCampsites = "SELECT site.site_number, site.max_occupancy, site.accessible, site.max_rv_length, site.utilities "
				+ "FROM site " + "WHERE campground_id = ? AND site_id NOT IN ("
				+ "SELECT site_id FROM reservation WHERE (? <= reservation.to_date AND ? >= reservation.from_date)"
				+ ") " 
				+ "LIMIT 5";
		/*
		 * This is some magic
		 */

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlReserveableCampsites, campgroundId, arrivalDate,
				departureDate);
		while (results.next()) {
			Campsite theCampsite = mapRowToCampsite(results);
			reserveableCampsites.add(theCampsite);
		}
		return reserveableCampsites.toArray(new Campsite[reserveableCampsites.size()]);
	}

	private Campsite mapRowToCampsite(SqlRowSet results) {
		Campsite theCampsite = new Campsite();
		theCampsite.setSiteNumber(results.getInt("site_number"));
		theCampsite.setMaxOccupancy(results.getInt("max_occupancy"));
		theCampsite.setAccessible(results.getBoolean("accessible"));
		theCampsite.setMaxRVLength(results.getInt("max_rv_length"));
		theCampsite.setUtilities(results.getBoolean("utilities"));
		return theCampsite;
	}
}
