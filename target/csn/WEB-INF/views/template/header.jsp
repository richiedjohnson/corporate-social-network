<div style="background-color: #113388; width: 100%; height: 100px;">
	<div align="left" style="padding-top: 35px; padding-left: 10px; width: 70%; float: left;">
		<a href="/csn/home" style="text-decoration: none"><font color="#FFFFFF"
			style="font-size: x-large;">Corporate
			Social Network</font></a>
	</div>
	<div style="width: 26%; float: left; margin-top: 80px" align="right">
	 <% if(session.getAttribute("emp") != null){ %>
	 <a href="/csn/user-profile" style="text-decoration: none"><font style="color: #FFFFFF">Profile</font></a>
	 &nbsp; | &nbsp;
	 <a href="/csn/logout" style="text-decoration: none"><font style="color: #FFFFFF">Logout</font></a>
	 <% } %>
	</div>
</div>