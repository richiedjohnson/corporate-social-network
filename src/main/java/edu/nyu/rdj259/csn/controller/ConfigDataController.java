package edu.nyu.rdj259.csn.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.nyu.rdj259.csn.dao.ConfigDataDAO;
import edu.nyu.rdj259.csn.dao.UserDAO;
import edu.nyu.rdj259.csn.model.Building;
import edu.nyu.rdj259.csn.model.Department;
import edu.nyu.rdj259.csn.model.Location;
import edu.nyu.rdj259.csn.model.Message;
import edu.nyu.rdj259.csn.model.SentMessageRequest;
import edu.nyu.rdj259.csn.model.SentMessageResponse;
import edu.nyu.rdj259.csn.model.Updates;
import edu.nyu.rdj259.csn.model.WorkLocation;

@RestController
@RequestMapping("/data")
public class ConfigDataController {
	
	@Autowired
	private ConfigDataDAO configDataDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@RequestMapping(value = "/locations", method = RequestMethod.GET, produces = "application/json")
	public Map<Integer, Location> fetchLocations(@RequestParam(value="work", required=false, 
																	defaultValue="false") String work){
		if(work.equalsIgnoreCase("true")){
			return configDataDAO.fetchLocations();
		}else{
			// change this to return all locations
			return configDataDAO.fetchLocations();
		}
	}
	
	@RequestMapping(value = "/locations/{locId}/buildings", 
			method = RequestMethod.GET, produces = "application/json")
	public Map<Integer, Building> fetchBuildingsAtLocation(@PathVariable int locId){
		return configDataDAO.fetchBuildingsAtLocation(locId);
	}
	
	@RequestMapping(value = "/locations/{locId}/buildings/{buildingId}/workLocations", 
			method = RequestMethod.GET, produces = "application/json")
	public Map<Integer, WorkLocation> fetchWorkLocations(@PathVariable int locId,@PathVariable int buildingId){
		return configDataDAO.fetchWorkLocations(locId, buildingId);
	}
	
	@RequestMapping(value = "/departments", 
			method = RequestMethod.GET, produces = "application/json")
	public Map<Integer, Department> fetchDepartments(){
		return configDataDAO.fetchDepartments();
	}
	
	@RequestMapping(value = "/message-center", 
			method = RequestMethod.GET, produces = "application/json")
	public Map<Integer, Message> fetchMessagesGist(HttpServletRequest request){
		Map<Integer, Message> msg = userDAO.fetchMessageGist((String)request.getSession().getAttribute("emp"));
		return msg;
	}
	
	@RequestMapping(value = "/updates", 
			method = RequestMethod.GET, produces = "application/json")
	public Map<Integer, Updates> checkUpdates(HttpServletRequest request){
		Map<Integer, Updates> msg = new HashMap<>();
		msg.put(1, userDAO.fetchUpdates((String)request.getSession().getAttribute("emp")));
		return msg;
	}
	
	@RequestMapping(value = "/messages", 
			method = RequestMethod.GET, produces = "application/json")
	public Map<Integer, Message> checkMessages(HttpServletRequest request, @RequestParam String recipient){
		return userDAO.getMessages((String)request.getSession().getAttribute("emp"), recipient);
	}
	
	@RequestMapping(value = "/messages", 
			method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody SentMessageResponse sendMessage(HttpServletRequest request, @RequestBody SentMessageRequest msgReq){
		System.out.println(msgReq.getName()+msgReq.getMsg());
		userDAO.sendMessage((String)request.getSession().getAttribute("emp"), msgReq.getName(), msgReq.getMsg());
		return new SentMessageResponse("success");
	}

}
