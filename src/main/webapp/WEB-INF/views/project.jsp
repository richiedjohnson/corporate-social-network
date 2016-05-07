<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div id="main"
	style="margin: 10px; padding: 10px; background-color: #DEDEDE; font-family: sans-serif; width: 90%;"
	align="center" class="clearfix">

	<div style="width: 27%; float: left;">
		<div
			style="width: 100%; float: left; background-color: #FFFFFF; padding: 5px"
			align="center">
			<div style="width: 100%">
				<font style="font-size: small; color: #113388"><b>Members</b></font>
			</div>
			<div style="width: 75%" align="center">
						<c:forEach var="friend" items="${requestScope.members}"
							varStatus="loopCounter">
							<a href="/csn/users/${friend.empId}"><div
									style="float: left; width: 70px; padding: 1px">
									<img alt="" src="/csn/resources/user.png"
										style="float: none; padding: 2px"> <font color="#113388"
										style="font-size: x-small; float: none;"><c:out
											value="${friend.name}" /></font>
								</div></a>
						</c:forEach>
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
							value="${requestScope.project.name}" /></div>
			  <div style="color: grey"> Created on : <c:out value="${requestScope.project.createdOn}" />
			  <br> <br> 
			  <c:if test="${requestScope.member == null}">
			  <form action="/csn/projects/${requestScope.project.id}/access" method="post">
			  	<button style="background-color: #113388; color:#FFFFFF" type="submit">Connect</button>
			  	</form>
			  </c:if>
			   <font style="font-size: x-small;" color="red"><c:out value="${requestScope.showMessage}" /></font>
			  </div>
			 </td>
			</tr>
			</table>
		</div>
		<br />
		<c:if test="${!empty requestScope.conRequests}">
			<div style="font-size: small;background-color: #FFFFFF; width: 65%;height: 200px; overflow: scroll;">
				<br>
				<font color="#113388">Connection Request</font>
				<hr width="70%">
				<br>
				<table style="font-size: small;width: 80%;border-collapse:collapse;">
					<c:forEach var="conreq" items="${requestScope.conRequests}"
						varStatus="loopCounter">
						<tr>
							<td style="border-bottom: 1px solid black;">
							<a href="/csn/users/${conreq.empId}" style="text-decoration: none"><c:out value="${conreq.name}" /></a>
							</td>
							<td align="right" style="border-bottom: 1px solid black;"><form
									action="/csn/projects/${requestScope.project.id}/connect-accept?empId=${conreq.empId}"
									method="post" style="">
									<select name="roleId">
										<c:forEach var="role" items="${requestScope.roles}">
											<option value="${role.id}"><c:out value="${role.name}" /></option>
										</c:forEach>
									</select>
									<button type="submit"
										style="background-color: #113388; color: #FFFFFF; font-size: x-small">Accept</button>
								</form></td>
								<td align="left" style="border-bottom: 1px solid black;">
								<form
									action="/csn/projects/${requestScope.project.id}/connect-reject?empId=${conreq.empId}"
									method="post">
									<button type="submit"
										style="background-color: #113388; color: #FFFFFF; font-size: x-small">Deny</button>
								</form></td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<br>
			<br>
		</c:if>
		<c:forEach var="post" items="${requestScope.posts}"
			varStatus="loopCounter">

			<div id="post-${post.postId}" style="background-color: #FFFFFF; width: 95%;padding: 20px"
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