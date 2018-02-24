package com.techelevator.campground.model.JDBC;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {
	private JdbcTemplate jdbcTemplate;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	LocalDate localDate = LocalDate.now();

	public JDBCReservationDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	public Long enterReservation(int siteId, String reservationName, LocalDate fromDate, LocalDate toDate) {
		String sqlenterReservation = "INSERT INTO reservation(site_id, name, from_date, to_date, create_date) "
				+ "VALUES(?, ?, ?, ?, ?) RETURNING reservation_id";
		Long results = jdbcTemplate.queryForObject(sqlenterReservation, Long.class, siteId, reservationName, fromDate, toDate,
				localDate);
		return results;
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
