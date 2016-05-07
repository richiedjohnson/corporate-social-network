<div style="margin-top: 100px; height:180px; width: 350px; background-color: #DEDEDE; padding-top:20px">
	<form action="authenticate" method="post">
	<font color="red" style="font-size: small">${requestScope.message}</font>
	<table cellpadding="5">
	 <tr><td>Employee Id</td><td><input type="text" name="empId"/></td></tr>
	 <tr><td>Password</td><td><input type="password" name="password"/></td></tr>
	 <tr><td><a href="#" style="text-decoration: none">Forgot password?</a></td>
	 <td align="right"><button type="submit">Login</button></td></tr>
	</table>
	</form>
	<hr width="80%">
	<div>
	  Haven't registered to the network yet? <br/> <a href="register" style="text-decoration: none">Register</a> here!
	</div>
</div>