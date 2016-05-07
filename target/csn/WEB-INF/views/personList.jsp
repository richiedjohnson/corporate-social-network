<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div style="margin: 10px;">
	<h4>List of Persons</h4>
	<table style="width: 600px" class="reference">
		<tbody>
			<tr>
				<th>Sr. No.</th>
				<th>EmpId</th>
				<th>Email</th>
				<th>Password</th>
				<th>IsAdmin</th>
			</tr>
			<c:forEach var="user" items="${requestScope.persons}"
				varStatus="loopCounter">
				<tr>
					<td><c:out value="${loopCounter.count}" /></td>
					<td><c:out value="${user.empId}" /></td>
					<td><c:out value="${user.email}" /></td>
					<td><c:out value="${user.password}" /></td>
					<td><c:out value="${user.admin}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>