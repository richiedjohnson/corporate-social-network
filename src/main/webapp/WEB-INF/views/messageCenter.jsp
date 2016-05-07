<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script>

var timer;
function setup(divid, recipient){
	$('div[id^="friend-"]').css({'background-color':'#E6F0FF'});
	$("#"+divid+"-div").css({'background-color':'#113388'});
	$('div[id^="name-"]').css({'color':'#113388'});
	$("#name-"+divid).css({'color':'#FFFFFF'});
	//msgBtn
	$("input#empIn").val(recipient);
	window.clearTimeout(timer);
	updateMessages(recipient);
};
function updateMessages(recipient) {
	  $.ajax({
      type: "GET",
      url: "/csn/data/messages?recipient="+recipient,
      data: "{}",
      contentType: "application/json; charset=utf-8",
      dataType: "json",
	    timeout: 2000,
	    success: function(msg) { 
	      $("#msgBox").html('');
	      var emp = '<%= session.getAttribute("emp") %>';
	      $.each(msg, function(index, item) {
	    		if(emp==item['from']){
	    			$("#msgBox").append("<div style='width: 100%;height: auto; margin-top: 5px' align='right'><div><font style='font-size:xx-small;' color='grey'>"+item['at']+"</font><div><div style='font-size: small;color: #113388; background-color: #ededed; border-radius: 5%; max-width: 500px;word-wrap: break-word;padding: 10px;display:inline-block;'>"+item['content']+"</div></div>");
	    		}else{
	    			$("#msgBox").append("<div style='width: 100%;height: auto;margin-top: 5px' align='left'><div><font style='font-size:xx-small;' color='grey'>"+item['fromName']+'@'+item['at']+"</font></div><div style='font-size: small;color: #113388; background-color: #c4D9FF;border-radius: 5% 5%; max-width: 500px;word-wrap: break-word;padding: 10px;display:inline-block;'>"+item['content']+"</div></div>");
	    		}
          });
	      var m = $("#msgBox");
	      m.scrollTop(m.prop("scrollHeight"));
	      timer = window.setTimeout(updateMessages, 1000,recipient);
	    },
	    error: function (XMLHttpRequest, textStatus, errorThrown) {
	      $("#msgBox").html('Timeout contacting server..');
	      window.clearTimeout(timer);
	      timer = window.setTimeout(updateMessages, 60000,recipient);
	    }
	});
}

$().ready(function() {

	var user = getParameterByName('user');
	if(user == null){
	 user = '<%= request.getAttribute("user") %>';
	}
	if(user != null){
		setup('friend-'+user,user);
	}
	
});

function sendMessage(){
	var msgVal = $('textarea#textMsg').val();
	var to = $('input#empIn').val();
	$.ajax({
	      type: "POST",
	      url: "/csn/data/messages",
	      data: JSON.stringify({"name":to,"msg":msgVal}),
	      contentType: "application/json; charset=utf-8",
	      dataType: "json",
		    timeout: 2000,
		    success: function(cont) { 
		    	$('textarea#textMsg').val('');
		    }
	          });
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
</script>
<div id="main"
	style="margin: 10px; padding: 10px; background-color: #DEDEDE; font-family: sans-serif; width: 90%; overflow: auto;overflow-x:hidden"
	align="center" class="clearfix">

	<div style="width: 25%; float: left; max-height:430px; background-color: #FFFFFF; padding-top:15px;
		padding-bottom:20px;overflow: auto;overflow-x:hidden" align="center">
		
		<c:choose>
					<c:when test="${!empty requestScope.friends}">
						<c:forEach var="friend" items="${requestScope.friends}"
							varStatus="loopCounter">
							
		<div style="margin-top: 3px;height: 40px">
			<a id="friend-${friend.empId}" href="#" onclick="setup(this.id,'${friend.empId}')" style="text-decoration: none; height: 40px">
			<div id="friend-${friend.empId}-div" style="width: 90%; background-color: #E6F0FF;height: 100%">
					<div style="float: left; width: 20%; margin-top: 10px;">
						<img alt="" src="/csn/resources/user.png" width="24px" height="24px"/>
					</div>
					<div id="name-friend-${friend.empId}" style="float: left; width: 80%; font-size: small; color: #113388; 
						margin-top: 10px; text-align: left;">${friend.name}</div>
				</div></a>
		</div>
		
		</c:forEach>
		</c:when>
		<c:otherwise>
		<font style="font-size: small;" color="#113388">You have no friends.</font>
		</c:otherwise>
		</c:choose>

	</div>

	<div style="width: 73%; float: left; background-color: #E6F0FF;height:430px;margin-left:10px;">
	<div id="msgBox" style="background-color: #FFFFFF; 
		 max-height:320px; overflow: auto;overflow-x:hidden; padding: 20px">
		
		</div>
		<div style="width: 90%;" align="center">
		<textarea id="textMsg" rows="4px" cols="60px" style="float: left;margin-left:150px"></textarea>
		<button id="msgBtn" style="width: 60px;height: 30px;float: left; margin-top: 15px; margin-left:10px" onclick="sendMessage()">Send</button>
		<input id="empIn" type="hidden" />
		</div>
	</div>
</div>