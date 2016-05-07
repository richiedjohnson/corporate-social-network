package edu.nyu.rdj259.csn.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import edu.nyu.rdj259.csn.dao.UserDAO;
import edu.nyu.rdj259.csn.model.ProjectUser;
import edu.nyu.rdj259.csn.model.User;

@Controller
public class AuthController {
	
	@Autowired
	private UserDAO userDAO;
	
    @RequestMapping(value="/login",method=RequestMethod.GET)
    public String loginPage() {
        return "index";
    }
    
    @RequestMapping(value="/authenticate",method=RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request) {
    	String empId = request.getParameter("empId");
    	String password = request.getParameter("password");

        boolean auth = userDAO.authenticate(empId, password);
        if(auth){
            ModelAndView model = new ModelAndView("redirect:home");
            request.getSession().setAttribute("emp", empId);
            return model;
        }else{
        	Map<String, String> outParams = new HashMap<>();
        	outParams.put("message", "Unable to vaildate user credentials.");
        	return new ModelAndView("index", outParams);
        }
    }
    
    @RequestMapping(value="/logout",method=RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request) {
            request.getSession().setAttribute("emp", null);
        	return new ModelAndView("index");
    }
    
    @RequestMapping(value="/create-post",method=RequestMethod.GET)
    public ModelAndView createPostPage(HttpServletRequest request) {
    	String emp = (String)request.getSession().getAttribute("emp");
    	Map<String, Object> params = new HashMap<>();
    	params.put("projects", userDAO.fetchProjects(emp, false));
    	params.put("friends", userDAO.fetchFriends(emp, false));
    	return new ModelAndView("createPost",params);
    }
    //
    
    @RequestMapping(value="/create-post",method=RequestMethod.POST)
    public ModelAndView createPost(HttpServletRequest request, 
    		@RequestParam String title, @RequestParam String content, 
    		@RequestParam String visibility, @RequestParam(required=false, defaultValue="") String project, 
    		@RequestParam(required=false, defaultValue="") String friendsList, @RequestParam(required=false, defaultValue="off") String attachLoc,
    		@RequestParam("uploadedFile") MultipartFile file,
    		@RequestParam(required=false, defaultValue="off") String errMsg) {
    	String emp = (String)request.getSession().getAttribute("emp");
    	Map<String, Object> params = new HashMap<>();
    	String visibleTo = null;
		if (visibility.equals("RC")) {
			if (friendsList.trim().equals("")) {
				params.put("errMsg", "Friends list cannot be empty");
				return new ModelAndView("redirect:/create-post",params);
			}
			visibleTo = friendsList.trim();
		}else if (visibility.equals("RP")) {
			if (project.trim().equals("")) {
				params.put("errMsg", "Project cannot be empty");
				return new ModelAndView("redirect:/create-post",params);
			}
			visibleTo = project.trim();
		}
    	
		if(file.getSize()/10000000 > 1){
			params.put("errMsg", "Max size of file is 1 MB");
			return new ModelAndView("redirect:/create-post",params);
		}else if((!file.getOriginalFilename().trim().equalsIgnoreCase(""))&&(!(file.getOriginalFilename().endsWith("txt") || 
				file.getOriginalFilename().endsWith("mp4") ||
				file.getOriginalFilename().endsWith("mp3") ||
				file.getOriginalFilename().endsWith("png") || 
				file.getOriginalFilename().endsWith("jpg")))){
			params.put("errMsg", "File can be either mp4, mp3, txt, jpg, png");
			return new ModelAndView("redirect:/create-post",params);
		}
		String attName = null;
		byte[] attContent = null;
		
		if(file.getSize() > 0){
			attName = file.getOriginalFilename();
			try {
				attContent = file.getBytes();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		userDAO.createPost(title, content, emp, visibility, visibleTo, attName, attContent, attachLoc);
		return new ModelAndView("redirect:home",params);
    }
    
    @RequestMapping(value="/home",method=RequestMethod.GET)
    public ModelAndView home(HttpServletRequest request, 
    		@RequestParam(value="searchKey", required=false, defaultValue="") String searchKey,
    		@RequestParam(value="from", required=false, defaultValue="") String from,
    		@RequestParam(value="to", required=false, defaultValue="") String to){
    	String emp = (String)request.getSession().getAttribute("emp");
    	Map<String, Object> params = new HashMap<>();
    	
    	if(searchKey.trim().equals("")){
    		searchKey = null;
    	}
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    	Date fromDate = null;
    	Date toDate = null;
    	if(!from.trim().equals("")){
    		try {
				fromDate = sdf.parse(from);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	if(!to.trim().equals("")){
    		try {
				toDate = sdf.parse(to);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	params.put("posts", userDAO.fetchPosts(emp,fromDate,toDate,searchKey));
    	params.put("friends", userDAO.fetchFriends(emp, true));
    	params.put("projects", userDAO.fetchProjects(emp, true));
    	params.put("events", userDAO.fetchEvents(emp, true));
    	params.put("recommendations", userDAO.fetchConnectionRecommendations(emp, true));
    	params.put("empId", emp);
    	userDAO.updateAccess(emp, "P");
    	return new ModelAndView("home",params);
    }
    
    @RequestMapping(value="/friends",method=RequestMethod.GET)
    public ModelAndView allFriends(@RequestParam String empId){
    	Map<String, Object> params = new HashMap<>();
    	params.put("friends", userDAO.fetchFriends(empId, false));
    	return new ModelAndView("allFriends",params);
    }
    
    @RequestMapping(value="/create-event",method=RequestMethod.GET)
    public ModelAndView createEventPage(HttpServletRequest request){
    	Map<String, Object> params = new HashMap<>();
    	String emp = (String)request.getSession().getAttribute("emp");
    	params.put("projects", userDAO.fetchProjects(emp, false));
    	params.put("friends", userDAO.fetchFriends(emp, false));
    	return new ModelAndView("createEvent",params);
    }
    
    @RequestMapping(value="/create-event",method=RequestMethod.POST)
    public ModelAndView createEvent(HttpServletRequest request, @RequestParam String locName, 
    		@RequestParam String locLat, @RequestParam String locLng, @RequestParam String eventName,
    		@RequestParam String at, @RequestParam String till, @RequestParam String visibility,
    		@RequestParam String project, @RequestParam String friendsList){
    	Map<String, Object> params = new HashMap<>();
    	String emp = (String)request.getSession().getAttribute("emp");
    	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    	String visibleTo = null;
    	if(visibility.equals("RC")){
    		visibleTo = friendsList;
    	}else if (visibility.equals("RP")){
    		visibility = project;
    	}
    	try {
			userDAO.createEvent(locName, locLat, locLng, eventName, sdf.parse(at) , sdf.parse(till), 
					visibility, visibleTo, emp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return new ModelAndView("redirect:home",params);
    }
    
    @RequestMapping(value="/get-loc",method=RequestMethod.GET)
    public ModelAndView getMapLocation(HttpServletRequest request){
    	Map<String, Object> params = new HashMap<>();
    	return new ModelAndView("maps",params);
    }
    
    @RequestMapping(value="/projects",method=RequestMethod.GET)
    public ModelAndView allProjects(@RequestParam String empId){
    	Map<String, Object> params = new HashMap<>();
    	params.put("projects", userDAO.fetchProjects(empId, false));
    	return new ModelAndView("allProjects",params);
    }
    
    @RequestMapping(value="/recommendations",method=RequestMethod.GET)
    public ModelAndView allrecommendations(@RequestParam String empId){
    	Map<String, Object> params = new HashMap<>();
    	params.put("friends", userDAO.fetchConnectionRecommendations(empId, false));
    	return new ModelAndView("allRecommendations",params);
    }
    
    @RequestMapping(value="/user-search",method=RequestMethod.GET)
    public ModelAndView searchUsers(@RequestParam String searchKey){
    	Map<String, Object> params = new HashMap<>();
    	params.put("friends", userDAO.fetchSearchResult(searchKey));
    	return new ModelAndView("allRecommendations",params);
    }
    
    @RequestMapping(value="/users/{empId}",method=RequestMethod.GET)
    public ModelAndView fetchUserProfile(HttpServletRequest request,
    		@PathVariable String empId, 
    		@RequestParam(value="showMessage", required=false, defaultValue="") String message){
    	String forEmp = (String)request.getSession().getAttribute("emp");
    	Map<String, Object> params = new HashMap<>();
    	params.put("user", userDAO.fetchUserDetails(forEmp, empId));
    	params.put("posts", userDAO.fetchUserPosts(forEmp, empId));
    	params.put("friends", userDAO.fetchFriends(empId, true));
    	params.put("projects", userDAO.fetchProjects(empId, true));
    	params.put("relations", userDAO.fetchRelations());
    	params.put("showMessage", message);
    	return new ModelAndView("user",params);
    }
    
    @RequestMapping(value="/projects/{projId}",method=RequestMethod.GET)
    public ModelAndView fetchProject(HttpServletRequest request,@PathVariable int projId, 
    		@RequestParam(value="showMessage", required=false, defaultValue="") String message){
    	String emp = (String)request.getSession().getAttribute("emp");
    	Map<String, Object> params = new HashMap<>();
    	params.put("project", userDAO.fetchProject(projId));
    	params.put("showMessage", message);
    	List<ProjectUser> members = userDAO.fetchProjectMembers(projId);
    	params.put("members", members);
    	params.put("roles", userDAO.fetchProjectRoles());
    	for(ProjectUser user : members){
    		if(user.getEmpId().trim().equals(emp.trim())){
    			params.put("member", true);
    			params.put("posts", userDAO.fetchProjectPosts(projId));
    			if(user.getRole() == 1 || user.getRole() == 2){
    				params.put("conRequests", userDAO.fetchProjectConnectionsRequests(projId));
    			}
    			break;
    		}
    	}
    	
    	return new ModelAndView("project",params);
    }
    
    
    @RequestMapping(value="/register",method=RequestMethod.GET)
    public ModelAndView registerPage() {
        return new ModelAndView("register"); 
    }
    
    @RequestMapping(value="/posts/{postId}",method=RequestMethod.GET)
    public ModelAndView getPost(@PathVariable int postId) {
    	Map<String, Object> params = new HashMap<>();
    	params.put("post", userDAO.fetchPost(postId));
    	return new ModelAndView("post",params);
    }
    
    @RequestMapping(value="/connection-requests",method=RequestMethod.GET)
    public ModelAndView getConnectionRequests(HttpServletRequest request) {
    	Map<String, Object> params = new HashMap<>();
    	String emp = (String)request.getSession().getAttribute("emp");
    	params.put("conRequests", 
    			userDAO.fetchConnectionRequests(emp));
    	userDAO.updateAccess(emp, "C");
    	return new ModelAndView("connectionRequests",params);
    }
    
    @RequestMapping(value="/connect-accept",method=RequestMethod.POST)
    public ModelAndView acceptConnectionRequest(HttpServletRequest request, @RequestParam String empId) {
    	userDAO.acceptConnectionRequest(empId, (String)request.getSession().getAttribute("emp"));
    	return new ModelAndView("redirect:/connection-requests"); 
    }
    
    @RequestMapping(value="/connect-reject",method=RequestMethod.POST)
    public ModelAndView rejectConnectionRequest(HttpServletRequest request, @RequestParam String empId) {
    	userDAO.rejectConnectionRequest(empId, (String)request.getSession().getAttribute("emp"));
    	return new ModelAndView("redirect:/connection-requests"); 
    }
    
    
    @RequestMapping(value="/posts/{postId}/like",method=RequestMethod.GET)
    public ModelAndView like(HttpServletRequest request, @PathVariable int postId, 
    		@RequestParam(value="post", required=false, defaultValue="false") String post) {
    	Map<String, Object> map = new HashMap<>();
    	try{
    		userDAO.like(postId, (String)request.getSession().getAttribute("emp"));
    	}catch(DuplicateKeyException e){
    		map.put("errorMsg", "You unliked a post which was liked earlier!");
    		userDAO.unlike(postId, (String)request.getSession().getAttribute("emp"));
    	}finally{
    		map.put("focus", "post-"+postId);
    	}
        if(post.equalsIgnoreCase("true")){
        	return new ModelAndView("redirect:/posts/"+postId,map); 
        }else{
        	return new ModelAndView("redirect:/home",map); 
        }
    }
    
    @RequestMapping(value="/projects/{projectId}/access",method=RequestMethod.POST)
    public ModelAndView requestProjectAccess(HttpServletRequest request, @PathVariable int projectId) {
    	Map<String, Object> map = new HashMap<>();
    	try{
    		userDAO.requestProjectAccess((String)request.getSession().getAttribute("emp"), projectId);
    		map.put("showMessage", "Request sent to admin!");
    	}catch(DuplicateKeyException e){
    		map.put("showMessage", "You have already requested access!");
        }
    	return new ModelAndView("redirect:/projects/"+projectId,map); 
    }
    
    @RequestMapping(value="/users/{userId}/access",method=RequestMethod.GET)
    public ModelAndView requestConnection(HttpServletRequest request, 
    		@PathVariable String userId, @RequestParam int relId) {
    	Map<String, Object> map = new HashMap<>();
    	try{
    		userDAO.requestConnection((String)request.getSession().getAttribute("emp"), userId, relId);
    		map.put("showMessage", "Request sent to user!");
    	}catch(DuplicateKeyException e){
    		map.put("showMessage", "You have already requested access!");
        }
    	return new ModelAndView("redirect:/users/"+userId,map); 
    }
    
    @RequestMapping(value="/message-center",method=RequestMethod.GET)
    public ModelAndView requestConnection(HttpServletRequest request,
    		@RequestParam(value="user", required=false, defaultValue="") String user) {
    	Map<String, Object> map = new HashMap<>();
    	String emp = (String)request.getSession().getAttribute("emp");
    	List<User> friends = userDAO.fetchFriends(emp, false);
    	if(!user.equals("")){
    		map.put("user", user);
    	}else{
    		if(friends.size() > 0){
    			map.put("user", friends.get(0).getEmpId());
    		}
    	}
    	map.put("friends", friends);
    	userDAO.updateAccess(emp, "M");
    	return new ModelAndView("messageCenter",map); 
    }
    
    @RequestMapping(value="/projects/{projectId}/connect-accept",method=RequestMethod.POST)
    public ModelAndView acceptProjectRequest(@PathVariable int projectId, 
    		@RequestParam String empId, @RequestParam int roleId) {
    	userDAO.acceptProjectAccess(empId, projectId, roleId);
    	return new ModelAndView("redirect:/projects/"+projectId); 
    }
    
    @RequestMapping(value="/projects/{projectId}/connect-reject",method=RequestMethod.POST)
    public ModelAndView rejectProjectRequest(@PathVariable int projectId, 
    		@RequestParam String empId) {
    	userDAO.rejectProjectAccess(empId, projectId);
    	return new ModelAndView("redirect:/projects/"+projectId); 
    }
    
    @RequestMapping(value="/posts/{postId}/comment",method=RequestMethod.POST)
    public ModelAndView comment(HttpServletRequest request, @PathVariable int postId, 
    		@RequestParam("comment") String comment,
    		@RequestParam(value="post", required=false, defaultValue="false") String post) {
    	Map<String, Object> map = new HashMap<>();
    	userDAO.createComments(postId, (String)request.getSession().getAttribute("emp"),comment);
    	map.put("focus", "post-"+postId);
    	if(post.equalsIgnoreCase("true")){
        	return new ModelAndView("redirect:/posts/"+postId,map); 
        }else{
        	return new ModelAndView("redirect:/home",map); 
        } 
    }
    
    @RequestMapping(value="/user-profile",method=RequestMethod.GET)
    public ModelAndView fetchProfile(HttpServletRequest request){
    	User user = userDAO.getUserProfile((String)request.getSession().getAttribute("emp"));
    	Map<String, Object> map = new HashMap<>();
    	map.put("user", user);
    	return new ModelAndView("userProfile",map); 
    }
    
    @RequestMapping(value="/user-profile-edit",method=RequestMethod.GET)
    public ModelAndView updateProfilePage(HttpServletRequest request){
    	return new ModelAndView("updateProfile"); 
    }
    
    @RequestMapping(value="/user-profile-edit",method=RequestMethod.POST)
    public ModelAndView updateProfile(HttpServletRequest request, @RequestParam Map<String,String> allRequestParams){
    	Map<String, String> outParams = new HashMap<>();
    	String emp = (String)request.getSession().getAttribute("emp");
    	if(emp == null || emp.equals("") || allRequestParams.get("firstName").equals("") || 
    			allRequestParams.get("lastName").equals("") || allRequestParams.get("password").equals("") || 
    			allRequestParams.get("email").equals("") || allRequestParams.get("department").equals("-1") || 
    			allRequestParams.get("location").equals("-1") || allRequestParams.get("building").equals("-1") || 
    			allRequestParams.get("workLoc").equals("-1") || allRequestParams.get("dob").equals("")){
    		outParams.put("message", "All fields are mandatory.");
    		return new ModelAndView("updateProfile",outParams); 
    	}
    	
    	if(!allRequestParams.get("password").equals(allRequestParams.get("confirmPassword"))){
    		outParams.put("message", "Passwords need to match. Try again!");
    		return new ModelAndView("updateProfile",outParams); 
    	}
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		try {
			userDAO.updateProfile(emp, allRequestParams.get("password"), 
					allRequestParams.get("firstName"), allRequestParams.get("lastName"), allRequestParams.get("email"),
					Integer.parseInt(allRequestParams.get("department")), 
					Integer.parseInt(allRequestParams.get("workLoc")), sdf.parse(allRequestParams.get("dob")));
				return new ModelAndView("redirect:user-profile"); 
		} catch (NumberFormatException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outParams.put("message", "Date format shoud be mm-dd-yyyy");
			return new ModelAndView("updateProfile",outParams);
		}
    }
    
    
    @RequestMapping(value="/register",method=RequestMethod.POST)
    public ModelAndView register(@RequestParam Map<String,String> allRequestParams, Model model) {
    	
    	Map<String, String> outParams = new HashMap<>();
    	
    	if(allRequestParams.get("empId").equals("") || allRequestParams.get("firstName").equals("") || 
    			allRequestParams.get("lastName").equals("") || allRequestParams.get("password").equals("") || 
    			allRequestParams.get("email").equals("") || allRequestParams.get("department").equals("-1") || 
    			allRequestParams.get("location").equals("-1") || allRequestParams.get("building").equals("-1") || 
    			allRequestParams.get("workLoc").equals("-1") || allRequestParams.get("dob").equals("")){
    		outParams.put("message", "All fields are mandatory.");
    		return new ModelAndView("register",outParams); 
    	}
    	
    	if(!allRequestParams.get("password").equals(allRequestParams.get("confirmPassword"))){
    		outParams.put("message", "Passwords need to match. Try again!");
    		return new ModelAndView("register",outParams); 
    	}
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		try {
			if(userDAO.register(allRequestParams.get("empId"), allRequestParams.get("password"), 
					allRequestParams.get("firstName"), allRequestParams.get("lastName"), allRequestParams.get("email"),
					Integer.parseInt(allRequestParams.get("department")), 
					Integer.parseInt(allRequestParams.get("workLoc")), sdf.parse(allRequestParams.get("dob")))){
				return new ModelAndView("confirmRegistration"); 
			}else{
				outParams.put("message", "User is already registered.");
				return new ModelAndView("register",outParams);
			}
		} catch (NumberFormatException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outParams.put("message", "Date format shoud be mm-dd-yyyy");
			return new ModelAndView("register",outParams);
		}
    }
    
    
	

}
