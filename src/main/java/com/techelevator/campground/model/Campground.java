package com.techelevator.campground.model;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;

public class Campground {

	private Long campgroundId;
	private Long parkId;
	private String name;
	private int openFromDate;
	private int openToDate;
	private BigDecimal dailyFee;
	
	public Long getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}
	public Long getParkId() {
		return parkId;
	}
	public void setParkId(Long parkId) {
		this.parkId = parkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOpenFromDate() {
		return openFromDate;
	}
	public void setOpenFromDate(int openFromDate) {
		this.openFromDate = openFromDate;
	}
	public int getOpenToDate() {
		return openToDate;
	}
	public void setOpenToDate(int openToDate) {
		this.openToDate = openToDate;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	public String toMonth(int month) {
	 return new DateFormatSymbols().getMonths()[month-1];
	}
	@Override
	public String toString() {
		return String.format("%-33s%-14s%-15s$%.2f", name, toMonth(openFromDate), toMonth(openToDate), dailyFee);
	}
}
