<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div id="main"
	style="margin: 10px; padding: 10px; background-color: #DEDEDE; font-family: sans-serif; width: 90%;"
	align="center" class="clearfix">

	<div style="width: 40%;" align="center">
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
	
</div>