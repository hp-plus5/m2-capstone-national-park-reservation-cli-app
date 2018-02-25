package com.techelevator.campground.model;

public class Campsite {

	private Long siteId;
	private Long campgroundId;
	private int siteNumber;
	private int maxOccupancy;
	private boolean accessible;
	private int maxRVLength;
	private boolean utilities;
	
	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Long getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}

	public int getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}

	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public boolean isAccessible() {
		return accessible;
	}

	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}

	public int getMaxRVLength() {
		return maxRVLength;
	}

	public void setMaxRVLength(int maxRVLength) {
		this.maxRVLength = maxRVLength;
	}

	public boolean isUtilities() {
		return utilities;
	}

	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}

	public String toString() {
		String accessibleAsString = "";
		String maxRVLengthAsString = "";
		String utilitiesAsString = "";
		// Turns the variable accessible into a nice string
		if (accessible) {
			accessibleAsString = "Yes";
		} else {
			accessibleAsString = "No";
		}
		// Turns the variable maxRVLength into a nice string
		if (maxRVLength == 0) {
			maxRVLengthAsString = "N/A";
		} else {
			maxRVLengthAsString = "" + maxRVLength;
		}
		// Turns the variable utilities into a nice string
		if (utilities) {
			utilitiesAsString = "Yes";
		} else {
			utilitiesAsString = "N/A";
		}
		// Returns the Campsite as a nice string
		return String.format("%-10s%-19s%-13s%-19s%-8s", siteNumber, maxOccupancy, accessibleAsString,
				maxRVLengthAsString, utilitiesAsString);
	}
}
