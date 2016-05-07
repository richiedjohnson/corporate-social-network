package edu.nyu.rdj259.csn.model;

public class ProjectUser {
	
	private String empId;
	private String name;
	private int role;
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
		return String.format("[empId=%s,name=%s,role=%d]", empId,name,role);
	}

}
