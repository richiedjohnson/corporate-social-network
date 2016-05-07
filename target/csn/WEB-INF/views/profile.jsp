<div style="margin-top: 30px; height:290px; width: 350px; background-color: #DEDEDE; padding-top:20px;">
	<font color="red" style="font-size: small">${requestScope.message}</font>
	<table cellpadding="5" style="font-size: small; width: 80%">
	 <tr><td>Employee Id</td><td><font color="#113388" style="font-size: small">${requestScope.user.empId}</font></td></tr>
	 <tr><td>First Name</td><td><font color="#113388" style="font-size: small">${requestScope.user.name}</font></td></tr>
	 <tr><td>Email</td><td><font color="#113388" style="font-size: small">${requestScope.user.email}</font></td></tr>
	 <tr><td>DoB</td><td><font color="#113388" style="font-size: small">${requestScope.user.dob}</font></td></tr>
	 <tr><td>Department</td><td><font color="#113388" style="font-size: small">${requestScope.user.department}</font></td></tr>
	 <tr><td>Location</td><td><font color="#113388" style="font-size: small">${requestScope.user.location}</font></td></tr>
	 <tr><td>Building</td><td><font color="#113388" style="font-size: small">${requestScope.user.building}</font></td></tr>
	 <tr><td>Floor</td><td><font color="#113388" style="font-size: small">${requestScope.user.floor}</font></td></tr>
	 <tr><td colspan="2" align="right"><a href="/csn/user-profile-edit" style="text-decoration: none">Update</a></td></tr>
	</table>
</div>