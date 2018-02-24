package com.techelevator.campground.model;

import java.time.LocalDate;

public interface CampsiteDAO {
	public Campsite[] getSelectedCampsites(Long campgroundId, LocalDate arrivalDate, LocalDate departureDate);
}