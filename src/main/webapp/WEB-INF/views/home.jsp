<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script>

function updateNotifications() {
	  $.ajax({
      type: "GET",
      url: "/csn/data/updates",
      data: "{}",
      contentType: "application/json; charset=utf-8",
      dataType: "json",
	    timeout: 2000,
	    success: function(msg) { 
	      $("#notifications").html('');
	      var status = msg[1];
	      $("#notifications").append("<div style='font-size:x-small;color:#113388;align:left;width:100%;padding-bottom:5px; height:20px; line-height:20px;'>&nbsp;<img src='resources/new-msg.png' height='18px' width='18px' style='top:50%;float:left' /> <div style='top:50%;float:left;width:50%'>&nbsp;<a href='/csn/message-center' style='text-decoration:none'><b>"+status['messages']+"</b> new messages.</a></div></div>");
	      $("#notifications").append("<div style='font-size:x-small;color:#113388;align:left;width:100%;padding-bottom:5px; height:20px; line-height:20px;'><img src='resources/new-conn.png' height='24px' width='24px' style='top:50%;float:left' /> <div style='top:50%;float:left;width:50%'><a href='/csn/connection-requests' style='text-decoration:none'><b>"+status['connections']+"</b> new connection requests.</a></div></div>");
	      var newPost = status['posts'];
	      if(newPost > 0){
	    	  $("#msgContainer").prepend('	<div id="newPost" style="position: fixed; top: 0px; left:600px; background: #EEEEEE; width:150px; height:30px; font-size: small;"><a href="/csn/home" style="text-decoration: none">New posts..Click to load!</a></div>');
	      }else{
	    	  $("#newPost").remove();
	      }
	      window.setTimeout(updateNotifications, 5000);
	    },
	    error: function (XMLHttpRequest, textStatus, errorThrown) {
	      $("#msgContainer").html('Timeout contacting server..');
	      window.setTimeout(updateNotifications, 60000);
	    }
	});
	  $.ajax({
	        type: "GET",
	        url: "/csn/data/message-center",
	        data: "{}",
	        contentType: "application/json; charset=utf-8",
	        dataType: "json",
		    timeout: 2000,
		    success: function(msg) { 
		    	$("#msgContainer").html('');
		      $.each(msg, function(index, item) {
		    	  if(item['newMsg']==true){
		    		  $("#msgContainer").append("<div style='font-size:x-small;color:green;align:left;width:100%;padding-bottom:5px;'>"+item['at']+": (<b>"+item['fromName']+"</b>) "+item['content']+"</div>");
		    	  }else{
		    		  $("#msgContainer").append("<div style='font-size:x-small;color:#113388;align:left;width:100%;padding-bottom:5px;'>"+item['at']+": (<b>"+item['fromName']+"</b>) "+item['content']+"</div>");
		    	  }
	          });
		      //window.setTimeout(update, 5000);
		    },
		    error: function (XMLHttpRequest, textStatus, errorThrown) {
		      $("#msgContainer").html('Timeout contacting server..');
		      window.setTimeout(updateNotifications, 60000);
		    }
		});
	}
$().ready(function() {
	updateNotifications();
	var fOn = getParameterByName('focus');
	$("#"+fOn).attr("tabindex",-1).focus();
	var err = getParameterByName('errorMsg');
	if(err != null){
		alert(getParameterByName('errorMsg'));
	}
	
});
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function advSearch(){
	$("#adv-search").css('display','block');
}
</script>

<div id="main"
	style="margin: 10px; padding: 10px; background-color: #DEDEDE; font-family: sans-serif; width: 90%;"
	align="center" class="clearfix">

	<div style="width: 27%; float: left;">
		<div
			style="width: 100%; float: left; background-color: #FFFFFF; padding: 5px"
			align="center">
			<div style="width: 100%">
				<font style="font-size: small; color: #113388"><b>Connections</b></font>
			</div>
			<div style="width: 75%" align="center">
				<c:choose>
					<c:when test="${!empty requestScope.friends}">
						<c:forEach var="friend" items="${requestScope.friends}"
							varStatus="loopCounter">
							<a href="/csn/users/${friend.empId}"><div
									style="float: left; width: 70px; padding: 1px">
									<img alt="" src="resources/user.png"
										style="float: none; padding: 2px"> <font color="#113388"
										style="font-size: x-small; float: none;"><c:out
											value="${friend.name}" /></font>
								</div></a>
						</c:forEach>
						<div
							style="float: left; width: 70px; padding: 1px; margin-top: 20px;">
							<a href="/csn/friends?empId=${requestScope.empId}" style="text-decoration: none"> <font
								style="font-size: x-small; color: #113388;">See more</font>
							</a>
						</div>
					</c:when>
					<c:otherwise>
						<br />
						<div style="float: left; width: 130px; padding: 1px">
							<font style="font-size: small; color: grey;">No
								connections so far.</font>
						</div>
						<div style="float: left; width: 70px; padding: 1px;">
							<a href="#" style="text-decoration: none"> <font
								style="font-size: x-small; color: #113388;">Add
									connections</font>
							</a>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div
			style="width: 100%; float: left; background-color: #FFFFFF; padding: 5px; margin-top: 25px"
			align="center">
			<div style="width: 100%">
				<font style="font-size: small; color: #113388"><b>Projects</b></font>
			</div>
			<div style="width: 75%" align="center">
				<c:choose>
					<c:when test="${!empty requestScope.projects}">
						<c:forEach var="project" items="${requestScope.projects}"
							varStatus="loopCounter">
							<a href="/csn/projects/${project.id}"><div
									style="float: left; width: 70px; padding: 1px">
									<img alt="" src="resources/project.png"
										style="float: none; padding: 2px" width="50px" height="50px"> 
										<font color="#113388"
										style="font-size: x-small; float: none;"><c:out
											value="${project.name}" /></font>
								</div></a>
						</c:forEach>
						<div
							style="float: left; width: 70px; padding: 1px; margin-top: 20px;">
							<a href="/csn/projects?empId=${requestScope.empId}" style="text-decoration: none"> <font
								style="font-size: x-small; color: #113388;">See more</font>
							</a>
						</div>
					</c:when>
					<c:otherwise>
						<br />
						<div style="float: left; width: 130px; padding: 1px">
							<font style="font-size: small; color: grey;">No
								connections so far.</font>
						</div>
						<div style="float: left; width: 70px; padding: 1px;">
							<a href="#" style="text-decoration: none"> <font
								style="font-size: x-small; color: #113388;">Add
									connections</font>
							</a>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div
			style="width: 100%; float: left; background-color: #FFFFFF; padding: 5px; margin-top: 25px"
			align="center">
			<div style="width: 100%">
				<font style="font-size: small; color: #113388"><b>Upcoming events</b></font>
				<img alt="" src="resources/event.png" width="24px" height="24px">
			</div>
			<br/>
			<div style="width: 75%" align="center">
				<c:choose>
					<c:when test="${!empty requestScope.events}">
						<c:forEach var="event" items="${requestScope.events}"
							varStatus="loopCounter">
							<a href="#${event.id}" style="text-decoration: none">
										<font color="#113388"
										style="font-size: small"><c:out
											value="${event.name}" /> </font> 
								</a>
								<font color="#113388" style="font-size: xx-small"> hosted by <c:out
											value="${event.hostedByName}" /> on  <c:out
											value="${event.hostedWhen}" /> @<c:out
											value="${event.locName}" /></font>
								<br/><br/>
						</c:forEach>
						<div
							style="float: left; width: 100%; padding: 1px; margin-top: 20px;">
							<a href="#" style="text-decoration: none; float: left; width: 49%"> <font
								style="font-size: x-small; color: #113388;">See more</font>
							</a>
							<a href="/csn/create-event" style="text-decoration: none; float: left;"> <font
								style="font-size: x-small; color: #113388; width: 49%">Create
									events</font>
							</a>
						</div>
					</c:when>
					<c:otherwise>
						<br />
						<div style="float: left; width: 130px; padding: 1px">
							<font style="font-size: small; color: grey;">No
								upcoming events.</font>
						</div>
						<div style="float: left; width: 70px; padding: 1px;">
							<a href="#" style="text-decoration: none"> <font
								style="font-size: x-small; color: #113388;">Create
									events</font>
							</a>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

	<div style="float: left; width: 3%">&nbsp;</div>
	<div style="width: 38%; float: left; font-size: small; color: #113388">
		
		<div
			style="background: white; min-width: 100%; padding-top: 15px; padding-right: 20px">
			<form action="home" method="get">
			 <input type="text" style="width: 80%" name="searchKey"/>
			 <button type="submit" style="background-color: #113388; color: #FFFFFF">Search</button>
			 <div style="width: 80%; padding-top: 10px" align="right"><a href="#" onclick="advSearch()">
			 <font style="font-size: x-small; color: #113388">Advanced Search</font></a></div>
			 <div id="adv-search" style="display: none">
			  From&nbsp;<input type="text" name="from" /> &nbsp;&nbsp; To&nbsp;<input type="text" name="to" />
			 </div>
			 <br/>
			</form>
		</div>
		<div
			style="height: 40px; background: white; min-width: 100%; padding-top: 15px; padding-right: 20px">
			<div style="width: 49%; height: 50px; float: left; margin-top: -5px;">
				<a href="/csn/create-post" style="text-decoration: none; color: #113388"> <img
					src="resources/post.png" height="32px" width="32px" /> Publish post
				</a>
			</div>
			<hr width="0.25px" size="30" style="float: left" />
			<div style="width: 49%; height: 50px; float: left; margin-top: -5px;">
				<a href="/csn/create-post?content=true" style="text-decoration: none; color: #113388"> <img
					src="resources/upload.png" height="32px" width="32px" /> Upload
					content
				</a>
			</div>
		</div>
		<br />
		<c:forEach var="post" items="${requestScope.posts}"
			varStatus="loopCounter">

			<div id="post-${post.postId}" style="background-color: #FFFFFF; width: 95%; padding: 20px;"
				align="left">
				<div style="width: 100%" align="left">
					<b><font color="#113388" style="font-size: medium;"><c:out
								value="${post.postedByName}" /></font></b> <font
						style="font-size: x-small">posted</font> <b> <font
						color="#113388" style="font-size: medium;">"<c:out
								value="${post.title}" />"
					</font></b> <br> <font color="grey" style="font-size: x-small"><c:out
							value="${post.postedAt}" /></font>
							<c:if test="${post.locationName != null}">
							<font color="grey" style="font-size: x-small">&nbsp;@<c:out
							value="${post.locationName}" /></font>
							</c:if>
				</div>
				<hr>
				<div style="width: 100%" align="left">
					<font style="font-size: small;"><c:out
							value="${post.content}" /></font>
				</div>
				<c:if test="${!empty post.attachmentName}">
				<div style="width: 100%;height: 200px;">
				<br/>
				<c:if test="${!empty post.attachmentName}">
				<div style="width: 100%;height: 200px;">
				 <c:choose>
					<c:when test="${fn:endsWith(post.attachmentName, 'txt')}">
						<iframe src="data:text/html;base64,${post.base64Encoded}" width="100%" height="200px"></iframe>
					</c:when>
					<c:when test="${fn:endsWith(post.attachmentName, 'pdf')}">
						<iframe src="data:application/pdf;base64,${post.base64Encoded}" width="100%" height="200px"></iframe>
					</c:when>
					<c:when test="${fn:endsWith(post.attachmentName, 'jpg')}">
						<img src="data:image/jpg;base64,${post.base64Encoded}" width="100%" height="200px"></img>
					</c:when>
					<c:when test="${fn:endsWith(post.attachmentName, 'png')}">
						<img src="data:image/png;base64,${post.base64Encoded}" width="100%" height="200px"></img>
					</c:when>
					<c:when test="${fn:endsWith(post.attachmentName, 'mp4')}">
						<video src="data:video/mp4;base64,${post.base64Encoded}" width="100%" height="200px"></video>
					</c:when>
					<c:when test="${fn:endsWith(post.attachmentName, 'mp3')}">
								<audio controls src="data:audio/mp3;base64,${post.base64Encoded}" 
										style="margin-top:80px; margin-left:80px" width="100%" height="200px"></audio>
					</c:when>
				</c:choose>	
				</div>
				</c:if>
				 
				</div>
				</c:if>
				<br /><br />
				<div style="width: 100%">
					<div style="width: 10%; float: left;" align="left">
						<a href="/csn/posts/${post.postId}"><img alt="" src="resources/like.png"
							style="height: 24px; width: 24px;" /><font color="grey"
							style="font-size: x-small; margin-top: 10px; margin-bottom: 10px;"><c:out
									value="${post.likes}" /></font></a>
					</div>
					<div style="width: 10%; float: left;" align="left">
						<a href="/csn/posts/${post.postId}"><img alt="" src="resources/comment3.png"
							style="height: 24px; width: 24px;" /><font color="grey"
							style="font-size: x-small; margin-top: 10px; margin-bottom: 10px"><c:out
									value="${post.comments}" /></font></a>
					</div>
					<div style="width: 75%; float: left;" align="right">
					<c:choose>
					<c:when test="${post.visibility=='P'}">
					  <a title="Public"><img alt="" src="resources/public.png" style="width: 24px; height: 24px" /></a>
					 </c:when>
					 <c:when test="${post.visibility=='F'}">
					 <a title="Friends"> <img alt="" src="resources/friends.png" style="width: 24px; height: 24px" /> </a>
					 </c:when>
					 <c:when test="${post.visibility=='FF'}">
					  <a title="Friends of Friends"> <img alt="" src="resources/fof.png" style="width: 24px; height: 24px" /></a>
					 </c:when>
					 <c:when test="${post.visibility=='RC'}">
					  <a title="Closed Group"><img alt="" src="resources/closed-group.png" style="width: 24px; height: 24px" /></a>
					 </c:when>
					 <c:when test="${post.visibility=='RP'}">
					  <a title="Project"><img alt="" src="resources/project.png" style="width: 24px; height: 24px" /></a>
					 </c:when>
					</c:choose>
					</div>
				</div>


				<div>
					<br /><br />
					<hr width="80%">
					<br />
					<form method="post" action="/csn/posts/${post.postId}/comment"> 
					<a href="posts/${post.postId}/like"><img alt="" src="resources/like2.png"
						style="height: 24px; width: 24px;" /></a> &nbsp;&nbsp;
					<textarea rows="2px" cols="40px" name="comment"></textarea>
					<input type="image" src="resources/comment2.png" height="24px"
						width="24px" />
					</form>
				</div>
			</div>
			<br />
		</c:forEach>
	</div>
	<div style="float: left; width: 3%">&nbsp;</div>
	
	<div style="width: 27%; float: left;">
	<div
			style="width: 100%; float: left; background-color: #FFFFFF; padding: 5px"
			align="center">
			<div style="width: 100%">
				<font style="font-size: small; color: #113388"><b>Notifications</b></font>
			</div>
			<br/>
			<div id="notifications" style="width: 80%" align="left">
				
			</div>
		</div>
		<div
			style="width: 100%; float: left; background-color: #FFFFFF; padding: 5px; margin-top: 25px"
			align="center">
			<div style="width: 100%">
				<font style="font-size: small; color: #113388"><b>People you may know</b></font>
			</div>
			<div style="width: 75%" align="center">
				<c:choose>
					<c:when test="${!empty requestScope.recommendations}">
						<c:forEach var="friend" items="${requestScope.recommendations}"
							varStatus="loopCounter">
							<a href="/csn/users/${friend.empId}"><div
									style="float: left; width: 70px; padding: 1px">
									<img alt="" src="resources/user.png"
										style="float: none; padding: 2px"> <font color="#113388"
										style="font-size: x-small; float: none;"><c:out
											value="${friend.name}" /></font>
								</div></a>
						</c:forEach>
						<div
							style="float: left; width: 70px; padding: 1px; margin-top: 20px;">
							<a href="/csn/recommendations?empId=${requestScope.empId}" style="text-decoration: none"> <font
								style="font-size: x-small; color: #113388;">See more</font>
							</a>
						</div>
					</c:when>
					<c:otherwise>
						<br />
						<div style="float: left; width: 130px; padding: 1px">
							<font style="font-size: small; color: grey;">No
								recommendations so far.</font>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div
			style="width: 100%; float: left; background-color: #FFFFFF; padding: 5px; margin-top: 25px"
			align="center">
			<div style="width: 100%">
				<font style="font-size: small; color: #113388"><b>Message Gist</b></font>
			</div>
			<br/>
			<div id="msgContainer" style="width: 90%" align="left">
			</div>
			<br/>
			<form action="/csn/message-center">
			<button style="background-color: #113388; color: #FFFFFF" type="submit">Go to message center</button>
			</form>
		</div>
	</div>
</div>