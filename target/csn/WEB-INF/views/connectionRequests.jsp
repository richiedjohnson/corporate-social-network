<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div
	style="margin: 10px; padding: 10px; background-color: #DEDEDE; font-family: sans-serif; width: 90%;"
	align="center" class="clearfix">


	<div
		style="background-color: #FFFFFF; width: 40%; padding: 20px;"
		align="center">

		<br>
				<font color="#113388">Connection Request</font>
				<hr width="70%">
				<br>
				<c:if test="${empty requestScope.conRequests}">
				 <font style="font-size: small;" color="#113388">You are all set. You have responded to all requests.</font>
				</c:if>
				<table style="font-size: small;width: 80%;border-collapse:collapse;">
					<c:forEach var="conreq" items="${requestScope.conRequests}"
						varStatus="loopCounter">
						<tr>
							<td style="border-bottom: 1px solid black;">
							<a href="/csn/users/${conreq.empId}" style="text-decoration: none"><c:out value="${conreq.name}" /></a>
							</td>
							<td style="border-bottom: 1px solid black;">
							<c:out value="${conreq.relation}" />
							</td>
							<td align="right" style="border-bottom: 1px solid black;"><form
									action="/csn/connect-accept?empId=${conreq.empId}"
									method="post" style="">
									<button type="submit"
										style="background-color: #113388; color: #FFFFFF; font-size: x-small">Accept</button>
								</form></td>
								<td align="left" style="border-bottom: 1px solid black;">
								<form
									action="/csn/connect-reject?empId=${conreq.empId}"
									method="post">
									<button type="submit"
										style="background-color: #113388; color: #FFFFFF; font-size: x-small">Deny</button>
								</form></td>
						</tr>
					</c:forEach>
				</table>


	</div>	

</div>