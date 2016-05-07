<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" type="text/css" href="/csn/resources/style.css">
<script>
/* When the user clicks on the button, 
toggle between hiding and showing the dropdown content */
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {

    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
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
									<img alt="" src="/csn/resources/user.png"
										style="float: none; padding: 2px"> <font color="#113388"
										style="font-size: x-small; float: none;"><c:out
											value="${friend.name}" /></font>
								</div></a>
						</c:forEach>
						<div
							style="float: left; width: 70px; padding: 1px; margin-top: 20px;">
							<a href="/csn/friends?empId=${requestScope.user.empId}" style="text-decoration: none"> <font
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
									<img alt="" src="/csn/resources/project.png"
										style="float: none; padding: 2px" width="50px" height="50px"> 
										<font color="#113388"
										style="font-size: x-small; float: none;"><c:out
											value="${project.name}" /></font>
								</div></a>
						</c:forEach>
						<div
							style="float: left; width: 70px; padding: 1px; margin-top: 20px;">
							<a href="/csn/projects?empId=${requestScope.user.empId}" style="text-decoration: none"> <font
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
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

	<div style="float: left; width: 3%">&nbsp;</div>
	<div style="width: 55%; float: left; font-size: small; color: #113388">
	<div
			style="background: white; min-width: 100%; padding-top: 15px;padding-bottom: 15px;">
			<table style="width: 80%">
			<tr>
			 <td><img src="/csn/resources/user.png" height="100px" width="100px"/></td>
			 <td>
			  <div style="font-size: x-large;color: #113388"><c:out
							value="${requestScope.user.name}" /></div>
			  <div style="color: grey"> <c:out value="${requestScope.user.department}" />
			  <br>Floor<c:out value="${requestScope.user.floor}" />, <c:out value="${requestScope.user.building}" />
			  <br><c:out value="${requestScope.user.location}" />
			  <c:if test="${requestScope.user.connected}">
			  	<br><br>Connected as <c:out value="${requestScope.user.relation}" />
			  </c:if>
			  <br> <br> 
			  <c:if test="${requestScope.user.connected}">
			  	<form action="/csn/message-center" method="get">
			  	<button style="background-color: #113388; color:#FFFFFF" type="submit">Message</button>
			  	<input name="user" type="hidden" value="${requestScope.user.empId}" />
			  	</form>
			  </c:if>
			  <c:if test="${!requestScope.user.connected}">
					<div class="dropdown">
						<button onclick="myFunction()" class="dropbtn">Connect as</button>
						<div id="myDropdown" class="dropdown-content">
						<c:forEach var="relation" items="${requestScope.relations}" varStatus="loopCounter">
							<a href="/csn/users/${requestScope.user.empId}/access?relId=${relation.id}"><c:out value="${relation.name}" /></a> 
						</c:forEach>
						</div>
					</div>
			</c:if>
			<br/>
			<font style="font-size: x-small;" color="red"><c:out value="${requestScope.showMessage}" /></font>
			  </div>
			 </td>
			</tr>
			</table>
			<hr style="width: 80%"/>
			<div style="width: 100%">
			<div style="width: 60%; float: left;">http://localhost:8080/csn/users/<c:out value="${requestScope.user.empId}" /></div>
			<div style="width: 39%; float: left;"><c:out value="${requestScope.user.email}" /></div>
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
					<c:when test="${fn:endsWith(post.attachmentName, 'jpg')}">
						<img src="data:image/jpg;base64,${post.base64Encoded}" width="100%" height="200px"></img>
					</c:when>
				</c:choose>	
				</div>
				</c:if>
				 
				</div>
				</c:if>
				<br />
				<div style="width: 100%;">
					<div style="width: 10%; float: left;" align="left">
						<a href="/csn/posts/${post.postId}"><img alt="" src="/csn/resources/like.png"
							style="height: 24px; width: 24px;" /><font color="grey"
							style="font-size: x-small; margin-top: 10px; margin-bottom: 10px;"><c:out
									value="${post.likes}" /></font></a>
					</div>
					<div style="width: 10%; float: left;" align="left">
						<a href="/csn/posts/${post.postId}"><img alt="" src="/csn/resources/comment3.png"
							style="height: 24px; width: 24px;" /><font color="grey"
							style="font-size: x-small; margin-top: 10px; margin-bottom: 10px"><c:out
									value="${post.comments}" /></font></a>
					</div>
					<br/>
				</div>
				<br/>
			</div>
			<br />
		</c:forEach>
	</div>
	<div style="float: left; width: 3%">&nbsp;</div>
	
	<div style="width: 27%; float: left;">
	  &nbsp;
	</div>
</div>