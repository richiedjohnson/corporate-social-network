<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div id="main"
	style="margin: 10px; padding: 10px; background-color: #DEDEDE; font-family: sans-serif; width: 90%;"
	align="center" class="clearfix">

	    <div
			style="width: 40%;background-color: #FFFFFF; padding: 5px; margin-left: 7px"
			align="center">
			<form action="/csn/user-search" method="get">
			<input style="width: 80%" type="text" name="searchKey"/><button type="submit">Search</button>
			</form>
		</div>

	<div style="width: 40%; margin-top: 5px;" align="center">
		<div
			style="width: 100%; float: left; background-color: #FFFFFF; padding: 5px"
			align="center">
			<div style="width: 100%">
				<font style="font-size: small; color: #113388"><b>People</b></font>
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
	</div>
</div>