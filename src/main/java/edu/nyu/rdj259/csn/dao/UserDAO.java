package edu.nyu.rdj259.csn.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import edu.nyu.rdj259.csn.model.Comment;
import edu.nyu.rdj259.csn.model.Event;
import edu.nyu.rdj259.csn.model.Message;
import edu.nyu.rdj259.csn.model.Post;
import edu.nyu.rdj259.csn.model.Project;
import edu.nyu.rdj259.csn.model.ConnectionRequest;
import edu.nyu.rdj259.csn.model.Role;
import edu.nyu.rdj259.csn.model.ProjectUser;
import edu.nyu.rdj259.csn.model.Updates;
import edu.nyu.rdj259.csn.model.User;

public class UserDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public User getUserProfile(String empId){
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * from csn_user natural join profile "
				+ "natural join department natural join work_location natural join building "
				+ "natural join location where emp_id=?", empId);
		User user = new User();
		for (Map<String, Object> row : rows) {
			user.setEmpId((String) row.get("emp_id"));
			user.setEmail((String) row.get("email"));
			user.setPassword((String) row.get("password"));
			user.setAdmin((Boolean) row.get("is_admin"));
			user.setBuilding((String) row.get("building_name"));
			user.setDepartment((String) row.get("dept_name"));
			user.setDob((Date) row.get("dob"));
			user.setFloor((int)row.get("floor_number"));
			user.setLocation((String) row.get("loc_name"));
			user.setName((String) row.get("first_name")+" "+(String)row.get("last_name"));
		}
		return user;
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM csn_user");
		for (Map<String, Object> row : rows) {
			User user = new User();
			user.setEmpId((String) row.get("emp_id"));
			user.setEmail((String) row.get("email"));
			user.setPassword((String) row.get("password"));
			user.setAdmin((Boolean) row.get("is_admin"));
			users.add(user);
		}
		return users;
	}
	
	public Post fetchPost(int postId){
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM post "
				+ "JOIN profile on (posted_by = emp_id) LEFT JOIN post_multimedia_attachment using (post_id) "
				+ "LEFT JOIN post_location using (post_id) LEFT JOIN location using(loc_id) WHERE post_id=?",postId);
		Post post = new Post();
		for(Map<String, Object> row : rows){
			post.setPostId((int)row.get("post_id"));
			post.setContent((String)row.get("content"));
			post.setTitle((String)row.get("title"));
			post.setPostedAt((Timestamp)row.get("posted_at"));
			post.setPostedBy((String)row.get("posted_by"));
			post.setVisibility((String)row.get("visibility"));

			post.setAttachmentName((String)row.get("att_name"));
			if(row.get("att_content") != null){
				post.setAttachmentContent((byte[])row.get("att_content"));
			}
			
			if(row.get("loc_id") != null){
				post.setLocId((int)row.get("loc_id"));
			}
			post.setLocationName((String)row.get("loc_name"));
			post.setPostedByName((String)row.get("first_name")+" "+(String)row.get("last_name"));
		}
		//post.setComments((int)row.get("comments"));
		rows = jdbcTemplate.queryForList("SELECT * FROM posts_liked NATURAL JOIN profile WHERE post_id=?",postId);
		String likedBy = "";
		int likeCount = 0;
		for(Map<String, Object> row:rows){
			likeCount++;
			likedBy = likedBy + (String)row.get("first_name")+" "+(String)row.get("last_name")+", ";
		}
		post.setLikes(likeCount);
		if(likeCount > 0){
			post.setLikedBy(likedBy.substring(0, likedBy.trim().length()-1));
		}else{
			post.setLikedBy("");
		}
		
		rows = jdbcTemplate.queryForList("SELECT * FROM post_comments NATURAL JOIN profile WHERE post_id=?",postId);
		int commentCount = 0;
		List<Comment> commentsList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		for(Map<String, Object> row:rows){
			commentCount++;
			Comment comment = new Comment();
			comment.setBy((String)row.get("emp_id"));
			comment.setByName((String)row.get("first_name")+" "+(String)row.get("last_name"));
			comment.setText((String)row.get("comment"));
			comment.setWhen(sdf.format((Timestamp)row.get("added_at")));
			commentsList.add(comment);
		}
		post.setCommentList(commentsList);
		post.setComments(commentCount);
		return post;
	}
	
	public void updateAccess(String empId,String type){
		jdbcTemplate.update("UPDATE last_accessed SET accessed_at=now() WHERE (emp_id,access_type)=(?,?)",empId,type);
	}
	
	public void like(int postId, String empId){
		jdbcTemplate.update("INSERT INTO posts_liked (post_id,emp_id) values (?,?)",postId,empId);
	}
	
	public void unlike(int postId, String empId){
		jdbcTemplate.update("DELETE FROM posts_liked where (post_id,emp_id) = (?,?)",postId,empId);
	}
	
	public void createComments(int postId, String empId, String comment){
		jdbcTemplate.update("INSERT INTO post_comments (post_id,emp_id,comment) values (?,?,?)",postId,empId,comment);
	}

	public boolean authenticate(String empId, String password) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("AUTHENTICATE");
		Map<String, Object> inMap = new HashMap<>();
		inMap.put("USER_NAME_IN", empId);
		inMap.put("PASSWORD_IN", password);
		SqlParameterSource in = new MapSqlParameterSource(inMap);

		Map<String, Object> resultMap = simpleJdbcCall.execute(in);
		return ((String) resultMap.get("status")).equals("1") ? true : false;
	}

	public boolean register(String empId, String password, String firstName, String lastName, String email, int deptId,
			int workLocId, Date dob) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("REGISTER");
		Map<String, Object> inMap = new HashMap<>();
		inMap.put("USER_NAME_IN", empId);
		inMap.put("PASSWORD_IN", password);
		inMap.put("EMAIL_IN", email);
		inMap.put("FIRST_NAME_IN", firstName);
		inMap.put("LAST_NAME_IN", lastName);
		inMap.put("DOB_IN", dob);
		inMap.put("DEPT_ID_IN", deptId);
		inMap.put("WORK_LOC_ID_IN", workLocId);
		SqlParameterSource in = new MapSqlParameterSource(inMap);

		try {
			simpleJdbcCall.execute(in);
			jdbcTemplate.update("INSERT INTO last_accessed (emp_id,access_type) values (?,'P')",empId);
			jdbcTemplate.update("INSERT INTO last_accessed (emp_id,access_type) values (?,'C')",empId);
			jdbcTemplate.update("INSERT INTO last_accessed (emp_id,access_type) values (?,'M')",empId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void updateProfile(String empId, String password, String firstName, String lastName, String email, int deptId,
			int workLocId, Date dob) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>"+empId);
		jdbcTemplate.update("UPDATE csn_user SET password=?, email=? WHERE emp_id=?",password,email,empId);
		jdbcTemplate.update("UPDATE profile SET first_name=?, last_name=?, dob=?, dept_id=?, "
				+ "work_loc_id=? WHERE emp_id=?",firstName,lastName,dob,deptId,workLocId,empId);
	}
	
	@SuppressWarnings("unchecked")
	public Updates fetchUpdates(String empId){
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM last_accessed "
				+ "where emp_id = ?",empId);
		Updates updates = new Updates();
		for(Map<String, Object> row :rows){
			if(((String)row.get("access_type")).equals("P")){
				SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("FETCH_POSTS");
				Map<String, Object> inMap = new HashMap<>();
				inMap.put("EMP_ID_IN", empId);
				inMap.put("FROM_DATETIME_IN", (Timestamp)row.get("accessed_at"));
				inMap.put("TO_DATETIME_IN", null);
				inMap.put("SEARCH_STRING_IN", null);
				SqlParameterSource in = new MapSqlParameterSource(inMap);

				Map<String, Object> result = simpleJdbcCall.execute(in);
				
				List<Map<String, Object>> posts = (List<Map<String, Object>>)result.get("#result-set-1");
				updates.setPosts(posts.size());
			}else if(((String)row.get("access_type")).equals("C")){
				int val = jdbcTemplate.queryForObject("SELECT count(requester) FROM connection_request "
						+ "where requested_connection=? and requested_at > ? and not is_approved", 
						new Object[]{empId,(Timestamp)row.get("accessed_at")} ,Integer.class);
				updates.setConnections(val);
			}else if(((String)row.get("access_type")).equals("M")){
				int val = jdbcTemplate.queryForObject("SELECT count(msg_from) FROM message where msg_to=? "
						+ "and created_at > ?", 
						new Object[]{empId,(Timestamp)row.get("accessed_at")} ,Integer.class);
				updates.setMessages(val);
			}
		}
		return updates;
	}
	
	@SuppressWarnings("unchecked")
	public List<Post> fetchPosts(String empId, Date from, Date to, String searchKey){
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("FETCH_POSTS");
		Map<String, Object> inMap = new HashMap<>();
		inMap.put("EMP_ID_IN", empId);
		inMap.put("FROM_DATETIME_IN", from);
		inMap.put("TO_DATETIME_IN", to);
		inMap.put("SEARCH_STRING_IN", searchKey);
		SqlParameterSource in = new MapSqlParameterSource(inMap);

		Map<String, Object> result = simpleJdbcCall.execute(in);
		List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("#result-set-1");
		
		List<Post> posts = new ArrayList<>();
		for(Map<String, Object> row:rows){
			Post post = new Post();
			post.setPostId((int)row.get("post_id"));
			post.setContent((String)row.get("content"));
			post.setTitle((String)row.get("title"));
			post.setPostedAt((Timestamp)row.get("posted_at"));
			post.setPostedBy((String)row.get("posted_by"));
			post.setVisibility((String)row.get("visibility"));
			post.setComments((int)row.get("comments"));
			post.setLikes((int)row.get("likes"));
			post.setAttachmentName((String)row.get("att_name"));
			if(row.get("att_content") != null){
				post.setAttachmentContent((byte[])row.get("att_content"));
			}
			
			if(row.get("loc_id") != null){
				post.setLocId((int)row.get("loc_id"));
			}
			post.setLocationName((String)row.get("loc_name"));
			post.setPostedByName((String)row.get("first_name")+" "+(String)row.get("last_name"));
			posts.add(post);
			}
		return posts;
	}
	
	@SuppressWarnings("unchecked")
	public List<Event> fetchEvents(String empId, boolean limited){
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("FETCH_EVENTS");
		Map<String, Object> inMap = new HashMap<>();
		inMap.put("EMP_ID_IN", empId);
		SqlParameterSource in = new MapSqlParameterSource(inMap);

		Map<String, Object> result = simpleJdbcCall.execute(in);
		List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("#result-set-1");
		
		List<Event> events = new ArrayList<>();
		int cnt = 0;
		for(Map<String, Object> row:rows){
			if(limited && cnt == 5 ){
				break;
			}
			cnt++;
			Event event = new Event();
			event.setId((int)row.get("event_id"));
			event.setName((String)row.get("event_name"));
			event.setAt((Timestamp)row.get("event_at"));
			event.setHostedBy((String)row.get("event_hosted_by"));
			event.setHostedByName((String)row.get("first_name")+" "+(String)row.get("last_name"));
			event.setTill((Timestamp)row.get("event_till"));
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
			event.setHostedWhen(sdf.format(event.getAt())+" to "+sdf.format(event.getTill()));
			event.setLocId((int)row.get("loc_id"));
			event.setLocName((String)row.get("loc_name"));
			event.setVisibility((String)row.get("visibility"));
			events.add(event);
			}
		return events;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<User> fetchFriends(String empId, Boolean limited){
		List<User> users = new ArrayList<>();
		int ONE = 1;
		int ZERO = 0;
		
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("FETCH_FRIENDS");
		Map<String, Object> inMap = new HashMap<>();
		inMap.put("EMP_ID_IN", empId);
		inMap.put("LIMIT_TO_5", limited==true?ONE:ZERO);
		SqlParameterSource in = new MapSqlParameterSource(inMap);

		Map<String, Object> result = simpleJdbcCall.execute(in);
		List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("#result-set-1");
		for(Map<String, Object> row:rows){
			User user = new User();
			if(((String)row.get("name")).length() > 10){
				user.setName(((String)row.get("name")).substring(0, 10)+"..");
			}else{
				user.setName((String)row.get("name"));
			}
			
			user.setEmpId((String)row.get("emp_id"));
			users.add(user);
		}
		return users;
	}
	
	public List<Project> fetchProjects(String empId, Boolean limited){
		List<Project> projects = new ArrayList<>();
		List<Map<String, Object>> rows = null;
		if(!limited){
			rows = jdbcTemplate.queryForList("SELECT * FROM user_project_roles "
					+ "natural join project where emp_id = ?",empId);
		}else{
			rows = jdbcTemplate.queryForList("SELECT * FROM user_project_roles "
					+ "natural join project where emp_id = ? LIMIT 0,5",empId);
		}
		
		for(Map<String, Object> row : rows){
			Project project = new Project();
			project.setId((int)row.get("proj_id"));
			project.setName((String)row.get("proj_name"));
			projects.add(project);
		}
		return projects;
	}
	
	public List<User> fetchSearchResult(String searchKey){
		searchKey = "%"+searchKey+"%";
		List<User> users = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT emp_id, concat(first_name,' ',last_name) as name "
				+ "FROM csn_user natural join profile where emp_id like ? or first_name like ? or last_name like ?", 
				searchKey,searchKey,searchKey);
		for(Map<String, Object> row : rows){
			User user = new User();
			if(((String)row.get("name")).length() > 10){
				user.setName(((String)row.get("name")).substring(0, 10)+"..");
			}else{
				user.setName((String)row.get("name"));
			}
			user.setEmpId((String)row.get("emp_id"));
			users.add(user);
		}
		return users;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> fetchConnectionRecommendations(String empId, boolean limited){
		List<User> users = new ArrayList<>();
		
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("FETCH_RECOMMENDATIONS");
		Map<String, Object> inMap = new HashMap<>();
		inMap.put("EMP_ID_IN", empId);
		SqlParameterSource in = new MapSqlParameterSource(inMap);

		Map<String, Object> result = simpleJdbcCall.execute(in);
		List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("#result-set-1");
		int cnt = 0;
		for(Map<String, Object> row:rows){
			if(limited && cnt == 5 ){
				break;
			}
			cnt++;
			User user = new User();
			if(((String)row.get("name")).length() > 10){
				user.setName(((String)row.get("name")).substring(0, 10)+"..");
			}else{
				user.setName((String)row.get("name"));
			}
			user.setEmpId((String)row.get("emp_id"));
			users.add(user);
		}
		return users;
	}
	
	public Map<Integer, Message> fetchMessageGist(String empId){
		Map<Integer, Message> messages = new HashMap<>();
		
		List<Map<String, Object>> lastUpdated = jdbcTemplate.queryForList("SELECT * FROM last_accessed "
				+ "where emp_id = ? and access_type='M'",empId);
		Timestamp lastSeen = null;
		for(Map<String, Object> row : lastUpdated){
			lastSeen = (Timestamp)row.get("accessed_at");
		}
		
		List<Map<String, Object>> rows = null;
		
			rows = jdbcTemplate.queryForList("SELECT * from message m1 join profile ON (msg_from=emp_id)"
					+ " where msg_to=? and created_at = "
					+ "(SELECT max(created_at) from message m2 "
					+ "where m2.msg_from=m1.msg_from and m2.msg_to = m1.msg_to) "
					+ "order by created_at desc LIMIT 0,5",empId);
		
		int cnt = 1;
		for(Map<String, Object> row : rows){
			Message message = new Message();
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
			message.setAt(sdf.format((Timestamp)row.get("created_at")));
			if(((Timestamp)row.get("created_at")).after(lastSeen) ){
				message.setNewMsg(true);
			}
			if(((String)row.get("content")).length() >= 20){
				message.setContent(((String)row.get("content")).substring(0, 20)+"...");
			}else{
				message.setContent((String)row.get("content"));
			}
			message.setFrom((String)row.get("msg_from"));
			message.setFromName((String)row.get("first_name")+" "+(String)row.get("last_name"));
			messages.put(cnt,message);
			cnt++;
		}
		return messages;
	}
	
	public Project fetchProject(int projId){
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * from project where proj_id=?",projId);
		Project project = new Project();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		for(Map<String, Object> row: rows){
			project.setId((int)row.get("proj_id"));
			project.setName((String)row.get("proj_name"));
			project.setCreatedOn(sdf.format((Timestamp)row.get("created_on")));
		}
		return project;
	}
	
	public List<ProjectUser> fetchProjectMembers(int projId){
		List<ProjectUser> users = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT emp_id,concat(first_name,' ',last_name) "
				+ "as name, role_id FROM user_project_roles "
				+ "natural join profile where proj_id=?",projId);
		for(Map<String, Object> row : rows){
			ProjectUser user = new ProjectUser();
			user.setEmpId((String)row.get("emp_id"));
			user.setName((String)row.get("name"));
			user.setRole((int)row.get("role_id"));
			users.add(user);
		}
		return users;
	}
	
	public List<Post> fetchProjectPosts(int projId){
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM post "
				+ "NATURAL JOIN project_post_visibility NATURAL JOIN tmp_posts_stats "
				+ "JOIN profile on (posted_by = emp_id) LEFT JOIN post_multimedia_attachment using (post_id) "
				+ "LEFT JOIN post_location using (post_id) LEFT JOIN location using(loc_id) "
				+ "WHERE visibility = 'RP' AND proj_id =? ORDER BY posted_at DESC",projId);
		List<Post> posts = new ArrayList<>();
		for(Map<String, Object> row:rows){
			Post post = new Post();
			post.setPostId((int)row.get("post_id"));
			post.setContent((String)row.get("content"));
			post.setTitle((String)row.get("title"));
			post.setPostedAt((Timestamp)row.get("posted_at"));
			post.setPostedBy((String)row.get("posted_by"));
			post.setVisibility((String)row.get("visibility"));
			post.setComments((int)row.get("comments"));
			post.setLikes((int)row.get("likes"));
			post.setAttachmentName((String)row.get("att_name"));
			if(row.get("att_content") != null){
				post.setAttachmentContent((byte[])row.get("att_content"));
			}
			
			if(row.get("loc_id") != null){
				post.setLocId((int)row.get("loc_id"));
			}
			post.setLocationName((String)row.get("loc_name"));
			post.setPostedByName((String)row.get("first_name")+" "+(String)row.get("last_name"));
			posts.add(post);
			}
		return posts;
	}
	
	public List<ConnectionRequest> fetchProjectConnectionsRequests(int projId){
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT emp_id,concat(first_name,' ',last_name) as name "
				+ "FROM project_connection_request NATURAL JOIN profile where proj_id=? and not is_accepted",projId);
		List<ConnectionRequest> requests = new ArrayList<>();
		for(Map<String, Object> row:rows){
			ConnectionRequest request = new ConnectionRequest();
			request.setEmpId((String)row.get("emp_id"));
			request.setName((String)row.get("name"));
			requests.add(request);
		}
		return requests;
	}

	public User fetchUserDetails(String detailsFor, String detailsOf){
		User user = new User();
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT emp_id, concat(first_name,' ',last_name) "
				+ "as name, email, dob, dept_name, floor_number, building_name, loc_name "
				+ "FROM csn_user NATURAL JOIN profile LEFT JOIN work_location using (work_loc_id) "
				+ "LEFT JOIN building using (building_id) NATURAL JOIN department "
				+ "NATURAL JOIN location WHERE emp_id=?",detailsOf);
		
		for(Map<String, Object> row:rows){
			user.setEmpId((String)row.get("emp_id"));
			user.setName((String)row.get("name"));
			user.setEmail((String)row.get("email"));
			user.setDob((Date)row.get("dob"));
			user.setDepartment((String)row.get("dept_name"));
			user.setFloor((int)row.get("floor_number"));
			user.setBuilding((String)row.get("building_name"));
			user.setLocation((String)row.get("loc_name"));
		}
		
		rows = jdbcTemplate.queryForList("SELECT * from connection_request NATURAL JOIN connection_relation "
				+ "where (requester=? and requested_connection=?) "
				+ "or (requester=? and requested_connection=?)",detailsFor,detailsOf,detailsOf,detailsFor);
		for(Map<String, Object> row:rows){
			boolean connected = ((Boolean)row.get("is_approved"));//==1?true:false;
			user.setConnected(connected);
			if(connected){
				user.setRelation((String)row.get("rel_name"));
			}
		}
		
		return user;
	}
	
	@SuppressWarnings("unchecked")
	public List<Post> fetchUserPosts(String postsFor, String postsOf){
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("FETCH_POSTS_OF_USER");
		Map<String, Object> inMap = new HashMap<>();
		inMap.put("FOR_EMP_ID_IN", postsFor);
		inMap.put("OF_EMP_ID_IN", postsOf);
		SqlParameterSource in = new MapSqlParameterSource(inMap);

		Map<String, Object> result = simpleJdbcCall.execute(in);
		List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("#result-set-1");
		
		List<Post> posts = new ArrayList<>();
		for(Map<String, Object> row:rows){
			Post post = new Post();
			post.setPostId((int)row.get("post_id"));
			post.setContent((String)row.get("content"));
			post.setTitle((String)row.get("title"));
			post.setPostedAt((Timestamp)row.get("posted_at"));
			post.setPostedBy((String)row.get("posted_by"));
			post.setVisibility((String)row.get("visibility"));
			post.setComments((int)row.get("comments"));
			post.setLikes((int)row.get("likes"));
			post.setAttachmentName((String)row.get("att_name"));
			if(row.get("att_content") != null){
				post.setAttachmentContent((byte[])row.get("att_content"));
			}
			
			if(row.get("loc_id") != null){
				post.setLocId((int)row.get("loc_id"));
			}
			post.setLocationName((String)row.get("loc_name"));
			post.setPostedByName((String)row.get("first_name")+" "+(String)row.get("last_name"));
			posts.add(post);
			}
		return posts;
	}
	
	public List<Role> fetchProjectRoles(){
		List<Role> roles = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM project_role order by(role_id) desc");
		
		for(Map<String, Object> row:rows){
			Role role = new Role();
			role.setId((int)row.get("role_id"));
			role.setName((String)row.get("role"));
			roles.add(role);
		}
		return roles;
	}
	
	public List<Role> fetchRelations(){
		List<Role> roles = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * from connection_relation");
		
		for(Map<String, Object> row:rows){
			Role role = new Role();
			role.setId((int)row.get("rel_id"));
			role.setName((String)row.get("rel_name"));
			roles.add(role);
		}
		return roles;
	}
	
	public void requestConnection(String from,String to, int relId){
		jdbcTemplate.update("INSERT INTO connection_request (requester,requested_connection,rel_id) values (?,?,?)",from,to,relId);
	}
	
	public List<ConnectionRequest> fetchConnectionRequests(String empId){
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT requester, "
				+ "concat(first_name, ' ', last_name) as name, rel_name from connection_request NATURAL JOIN connection_relation"
				+ " JOIN profile on (requester = emp_id) where is_approved = false AND requested_connection = ?",empId);
		List<ConnectionRequest> requests = new ArrayList<>();
		for(Map<String, Object> row:rows){
			ConnectionRequest request = new ConnectionRequest();
			request.setEmpId((String)row.get("requester"));
			request.setName((String)row.get("name"));
			request.setRelation((String)row.get("rel_name"));
			requests.add(request);
		}
		return requests;
	}
	
	public void acceptConnectionRequest(String requester, String requested){
		jdbcTemplate.update("UPDATE connection_request SET is_approved = true "
				+ "WHERE requester=? AND requested_connection=?",requester,requested);
	}
	
	public void rejectConnectionRequest(String requester, String requested){
		jdbcTemplate.update("DELETE FROM connection_request "
				+ "WHERE requester=? AND requested_connection=?",requester,requested);
	}
	
	public void requestProjectAccess(String empId, int projId){
		jdbcTemplate.update("INSERT INTO project_connection_request (proj_id,emp_id) values (?,?)",projId,empId);
	}
	
	public void acceptProjectAccess(String empId, int projId, int roleId){
		jdbcTemplate.update("UPDATE project_connection_request SET is_accepted = true WHERE proj_id=? AND emp_id=?",projId,empId);
		jdbcTemplate.update("INSERT INTO user_project_roles (proj_id,emp_id,role_id) values (?,?,?)",projId,empId,roleId);
	}
	
	public void rejectProjectAccess(String empId, int projId){
		jdbcTemplate.update("DELETE FROM project_connection_request WHERE proj_id=? AND emp_id=?",projId,empId);
	}
	
	public Map<Integer,Message> getMessages(String empId, String recipient){
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT created_at,msg_from, msg_to, content, "
				+ "concat(first_name,' ',last_name) as name from message "
				+ "join profile on (emp_id=msg_from) where (msg_from=? and msg_to=?) "
				+ "or (msg_from=? and msg_to=?) order by (created_at)",empId,recipient,recipient,empId);
		Map<Integer,Message> messages = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		int cnt = 0;
		for(Map<String, Object> row:rows){
			Message message = new Message();
			message.setAt(sdf.format((Timestamp)row.get("created_at")));
			message.setFromName((String)row.get("name"));
			message.setFrom((String)row.get("msg_from"));
			message.setTo((String)row.get("msg_to"));
			message.setContent((String)row.get("content"));
			messages.put(cnt, message);
			cnt++;
		}
		return messages;
	}
	
	public void sendMessage(String empId, String to, String message){
		jdbcTemplate.update("INSERT INTO message (msg_from,msg_to,content) values (?,?,?)",empId,to,message);
	}
	
	public void createPost(String title, String content, String postedBy, String visibility, String visibleTo,
			String attachmentName, byte[] attachmentContent, String locReqd) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("CREATE_POST");
		Map<String, Object> inMap = new HashMap<>();
		inMap.put("TITLE_IN", title);
		inMap.put("CONTENT_IN", content);
		inMap.put("POSTED_BY_IN", postedBy);
		inMap.put("VISIBILITY_IN", visibility);
		inMap.put("VISIBLE_TO_IN", visibleTo);
		inMap.put("ATT_NAME_IN", attachmentName);
		inMap.put("ATT_CONTENT_IN", attachmentContent);
		if(locReqd.equalsIgnoreCase("on")){
			List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT loc_id from profile "
					+ "natural join work_location natural join building where emp_id=?",postedBy);
			inMap.put("LOC_ID_IN", (int)rows.get(0).get("loc_id"));
		}else{
			inMap.put("LOC_ID_IN", null);
		}
		SqlParameterSource in = new MapSqlParameterSource(inMap);
		simpleJdbcCall.execute(in);
		updateAccess(postedBy, "P");
	}
	
	public void createEvent(String locName, String locLat, String locLng, String eventName, Date eventAt,
			Date eventTill, String visibility, String visibleTo, String hostedBy) {
		
		jdbcTemplate.update("INSERT INTO location (loc_name,longitude,latitude) values (?,?,?)",locName,locLng,locLat);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT loc_id from location where loc_name=?",locName);
		int locId = (int)rows.get(0).get("loc_id");
		
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("CREATE_EVENT");
		Map<String, Object> inMap = new HashMap<>();
		inMap.put("EVENT_NAME_IN", eventName);
		inMap.put("EVENT_AT_IN", eventAt);
		inMap.put("EVENT_TILL_IN", eventTill);
		inMap.put("HOSTED_BY_IN", hostedBy);
		inMap.put("VISIBILITY_IN", visibility);
		inMap.put("VISIBLE_TO_IN", visibleTo);
		inMap.put("LOC_ID_IN", locId);
		SqlParameterSource in = new MapSqlParameterSource(inMap);
		simpleJdbcCall.execute(in);
	}
}
