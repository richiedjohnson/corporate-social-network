package edu.nyu.rdj259.csn.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import edu.nyu.rdj259.csn.model.Building;
import edu.nyu.rdj259.csn.model.Department;
import edu.nyu.rdj259.csn.model.Location;
import edu.nyu.rdj259.csn.model.WorkLocation;

public class ConfigDataDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Map<Integer, Location> fetchLocations(){
		Map<Integer, Location> locations = new HashMap<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from location natural join building"
				+ " natural join work_location");
		for(Map<String, Object> row : rows){
			Location loc = new Location();
			loc.setId((int)row.get("loc_id"));
			loc.setName((String)row.get("loc_name"));
			loc.setLatitude((String)row.get("latitude"));
			loc.setLongitude((String)row.get("longitude"));
			locations.put(loc.getId(), loc);
		}
		return locations;
	}
	
	public Map<Integer, Building> fetchBuildingsAtLocation(int locationId){
		Map<Integer, Building> buildings = new HashMap<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from building"
				+ " where loc_id=?",locationId);
		for(Map<String, Object> row : rows){
			Building building = new Building();
			building.setId((int)row.get("building_id"));
			building.setName((String)row.get("building_name"));
			buildings.put(building.getId(), building);
		}
		return buildings;
	}
	
	public Map<Integer, WorkLocation> fetchWorkLocations(int locationId, int buildingId){
		Map<Integer, WorkLocation> workLocs = new HashMap<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from work_location natural join building "
				+ "natural join location where loc_id = ? and building_id=?",locationId, buildingId);
		for(Map<String, Object> row : rows){
			WorkLocation workLoc = new WorkLocation();
			workLoc.setId((int)row.get("work_loc_id"));
			workLoc.setFloorNumber((int)row.get("floor_number"));
			workLocs.put(workLoc.getId(), workLoc);
		}
		return workLocs;
	}
	
	public Map<Integer, Department> fetchDepartments(){
		Map<Integer, Department> depts = new HashMap<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from department");
		for(Map<String, Object> row : rows){
			Department dept = new Department();
			dept.setId((int)row.get("dept_id"));
			dept.setName((String)row.get("dept_name"));
			depts.put(dept.getId(), dept);
		}
		return depts;
	}

}
