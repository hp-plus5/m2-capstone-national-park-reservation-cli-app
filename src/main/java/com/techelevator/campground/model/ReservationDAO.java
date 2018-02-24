package com.techelevator.campground.model;

import java.time.LocalDate;

public interface ReservationDAO {
	public Long enterReservation(int siteId, String reservationName, LocalDate fromDate, LocalDate toDate);

}
