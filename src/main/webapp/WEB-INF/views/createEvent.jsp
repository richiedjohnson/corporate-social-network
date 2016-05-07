<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script>
$().ready(function() {
	var err = getParameterByName('errorMsg');
	if(err != null){
		alert(getParameterByName('errorMsg'));
	}
	
});
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function enableFeilds(){
	var value = $('#visibility').val();
	if(value == 'RP'){
		$('#project').css('display','block');
		$('#friends').css('display','none');
	}else if(value == 'RC'){
		$('#project').css('display','none');
		$('#friends').css('display','block');
	}
}

$().ready(function() {
	var loc = getParameterByName('loc');
	$('#locName').val(loc);
	var lat = getParameterByName('lat');
	$('#locLat').val(lat);
	var lng = getParameterByName('lng');
	$('#locLng').val(lng);
	
});

function addFriend(){
	var frenz = $('#friends-text').val();
	frenz = frenz + $('#friendsList').val()+",";
	$('#friends-text').val(frenz);
}
</script>
<div
	style="margin: 10px; padding: 10px; background-color: #DEDEDE; font-family: sans-serif; width: 90%;"
	align="center" class="clearfix">


	<div style="background-color: #FFFFFF; width: 40%; padding: 20px; font-size: small; color: #113388"
		align="center">
		<form action="/csn/create-event" method="post">
		<table style="font-size: small; color: #113388">
		<tr><td>Location name</td><td><input id="locName" type="text" name="locName" style="width: 200px"/><a href="/csn/get-loc"><img src="/csn/resources/loc.png" width="24px" height="24px" style="margin-top: 5px"/></a></td></tr>
		<tr><td>Location latitude</td><td><input id="locLat" type="text" name="locLat" style="width: 200px"/></td></tr>
		<tr><td>Location longitude</td><td><input id="locLng" type="text" name="locLng" style="width: 200px"/> </td></tr>
		<tr><td>Event name</td><td><input type="text" name="eventName" style="width: 200px"/></td></tr>
		<tr><td>Event at</td><td><input type="text" name="at" style="width: 200px"/></td></tr>
		<tr><td>Event till</td><td><input type="text" name="till" style="width: 200px"/></td></tr>
		<tr><td>Visibility</td><td><select name="visibility" id="visibility" style="width: 200px" onchange="enableFeilds()">
		 <option value="F">Connections</option>
		 <option value="FF">Connections of connections</option>
		 <option value="P">Public</option>
		 <option value="RP">Restriced to project</option>
		 <option value="RC">Restriced to connection</option>
		</select></td></tr>
		<tr style="display: none" id="project"><td>Project</td><td><select name="project">
		 <c:forEach var="project" items="${requestScope.projects}"
			varStatus="loopCounter">
			 <option value="${project.id}"><c:out value="${project.name}" /></option>
			</c:forEach>
		</select></td></tr>
		<tr style="display: none" id="friends"><td>Friends</td><td><select name="friends" id="friendsList">
		 <c:forEach var="friend" items="${requestScope.friends}"
			varStatus="loopCounter">
			 <option value="${friend.empId}"><c:out value="${friend.name}" /></option>
			</c:forEach>
		</select> &nbsp; <button type="button" onclick="addFriend()" style="background-color: #113388; color: #FFFFFF">Add</button> &nbsp; 
		<input type="text" readonly="readonly" name="friendsList" id="friends-text"/></td></tr>
		<tr>
		<td colspan="2" align="right"><button type="submit" style="background-color: #113388; color: #FFFFFF">Create Event</button></td></tr>
		</table>
		</form>
	</div>
	<br />

</div>