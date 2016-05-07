<script type="text/javascript">
$().ready(function() {
    $.ajax({
        type: "GET",
        url: "/csn/data/locations",
        data: "{}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(msg) {
            $("#location").get(0).options.length = 0;
            $("#location").get(0).options[0] = new Option("Select location", "-1");   
            $.each(msg, function(index, item) {
                $("#location").get(0).options[$("#location").get(0).options.length] = new Option(item['name'],item['id']);
            });
        },
        error: function() {
            alert("Failed to load locations");
        }
    });
    $.ajax({
        type: "GET",
        url: "/csn/data/departments?work=true",
        data: "{}",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(msg) {
            $("#department").get(0).options.length = 0;
            $("#department").get(0).options[0] = new Option("Select department", "-1");   
            $.each(msg, function(index, item) {
                $("#department").get(0).options[$("#department").get(0).options.length] = new Option(item['name'],item['id']);
            });
        },
        error: function() {
            alert("Failed to load departments");
        }
    });
    
    $("#location").bind("change", function() {
    	GetBuildings($(this).val());
    });
}); 

function GetBuildings(locId) {
    if (locId > 0) {
        $("#building").get(0).options.length = 0;
        $("#building").get(0).options[0] = new Option("Loading building", "-1"); 
 
        $.ajax({
            type: "GET",
            url: "/csn/data/locations/"+locId+"/buildings",
            data: "{}",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(msg) {
                $("#building").get(0).options.length = 0;
                $("#building").get(0).options[0] = new Option("Select building", "-1"); 
                $.each(msg, function(index, item) {
                    $("#building").get(0).options[$("#building").get(0).options.length] = new Option(item['name'],item['id']);
                });
            },
            error: function() {
                $("#building").get(0).options.length = 0;
                alert("Failed to load buildings");
            }
        });
    }
    else {
        $("#building").get(0).options.length = 0;
    }
    
    $("#building").bind("change", function() {
    	GetWorkLocs(locId,$(this).val());
    });
} 

function GetWorkLocs(locId,buildId) {
    if (buildId > 0) {
        $("#workLoc").get(0).options.length = 0;
        $("#workLoc").get(0).options[0] = new Option("Loading workLoc", "-1"); 
 
        $.ajax({
            type: "GET",
            url: "/csn/data/locations/"+locId+"/buildings/"+buildId+"/workLocations",
            data: "{}",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(msg) {
                $("#workLoc").get(0).options.length = 0;
                $("#workLoc").get(0).options[0] = new Option("Select floor", "-1"); 
                $.each(msg, function(index, item) {
                    $("#workLoc").get(0).options[$("#workLoc").get(0).options.length] = new Option(item['floorNumber'],item['id']);
                });
            },
            error: function() {
                $("#workLoc").get(0).options.length = 0;
                alert("Failed to load workLoc");
            }
        });
    }
    else {
        $("#workLoc").get(0).options.length = 0;
    }
} 

</script>
<div style="margin-top: 30px; height:420px; width: 350px; background-color: #DEDEDE; padding-top:20px;">
	<form action="register" method="post">
	<font color="red" style="font-size: small">${requestScope.message}</font>
	<table cellpadding="5">
	 <tr><td>Employee Id</td><td><input type="text" name="empId"/></td></tr>
	 <tr><td>First Name</td><td><input type="text" name="firstName"/></td></tr>
	 <tr><td>Last Name</td><td><input type="text" name="lastName"/></td></tr>
	 <tr><td>Password</td><td><input type="password" name="password"/></td></tr>
	 <tr><td>Confirm Password</td><td><input type="password" name="confirmPassword"/></td></tr>
	 <tr><td>Email</td><td><input type="text" name="email"/></td></tr>
	 <tr><td>DoB</td><td><input type="text" name="dob"/></td></tr>
	 <tr><td>Department</td><td><select name="department" id="department" style="width: 180px;"></select></td></tr>
	 <tr><td>Location</td><td><select name="location" id="location" style="width: 180px;"></select></td></tr>
	 <tr><td>Building</td><td><select name="building" id="building" style="width: 180px;"></select></td></tr>
	 <tr><td>Floor</td><td><select name="workLoc" id="workLoc" style="width: 180px;"></select></td></tr>
	 <tr><td colspan="2" align="right"><button type="submit">Register</button></td></tr>
	</table>
	</form>
</div>