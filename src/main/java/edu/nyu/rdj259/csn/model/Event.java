package edu.nyu.rdj259.csn.model;

import java.sql.Timestamp;

public class Event {
	
	private int id;
	private String name;
	private Timestamp at;
	private String hostedBy;
	private String hostedByName;
	private Timestamp till;
	private String visibility;
	private int locId;
	private String locName;
	private String hostedWhen;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getAt() {
		return at;
	}
	public void setAt(Timestamp at) {
		this.at = at;
	}
	public String getHostedByName() {
		return hostedByName;
	}
	public void setHostedByName(String hostedByName) {
		this.hostedByName = hostedByName;
	}
	public Timestamp getTill() {
		return till;
	}
	public void setTill(Timestamp till) {
		this.till = till;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public int getLocId() {
		return locId;
	}
	public void setLocId(int locId) {
		this.locId = locId;
	}
	public String getLocName() {
		return locName;
	}
	public void setLocName(String locName) {
		this.locName = locName;
	}
	public String getHostedWhen() {
		return hostedWhen;
	}
	public void setHostedWhen(String hostedWhen) {
		this.hostedWhen = hostedWhen;
	}
	public String getHostedBy() {
		return hostedBy;
	}
	public void setHostedBy(String hostedBy) {
		this.hostedBy = hostedBy;
	}
	

	
}
