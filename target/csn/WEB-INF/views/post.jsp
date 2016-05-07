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
</script>
<div
	style="margin: 10px; padding: 10px; background-color: #DEDEDE; font-family: sans-serif; width: 90%;"
	align="center" class="clearfix">


	<div id="post-${requestScope.post.postId}"
		style="background-color: #FFFFFF; width: 40%; padding: 20px;"
		align="left">
		<div style="width: 100%" align="left">
			<b><font color="#113388" style="font-size: medium;"><c:out
						value="${requestScope.post.postedByName}" /></font></b> <font style="font-size: x-small">posted</font>
			<b> <font color="#113388" style="font-size: medium;">"<c:out
						value="${requestScope.post.title}" />"
			</font></b> <br> <font color="grey" style="font-size: x-small"><c:out
					value="${requestScope.post.postedAt}" /></font>
		</div>
		<hr>
		<div style="width: 100%" align="left">
			<font style="font-size: small;"><c:out value="${requestScope.post.content}" /></font>
		</div>
		<c:if test="${!empty requestScope.post.attachmentName}">
			<div style="width: 100%; height: 200px;">
				<br />
				<c:if test="${!empty requestScope.post.attachmentName}">
					<div style="width: 100%; height: 200px;">
						<c:choose>
							<c:when test="${fn:endsWith(requestScope.post.attachmentName, 'txt')}">
								<iframe src="data:text/html;base64,${requestScope.post.base64Encoded}"
									width="100%" height="200px"></iframe>
							</c:when>
							<c:when test="${fn:endsWith(requestScope.post.attachmentName, 'jpg')}">
								<img src="data:image/jpg;base64,${requestScope.post.base64Encoded}"
									width="100%" height="200px"></img>
							</c:when>
							<c:when test="${fn:endsWith(requestScope.post.attachmentName, 'png')}">
								<img src="data:image/png;base64,${requestScope.post.base64Encoded}"
									width="100%" height="200px"></img>
							</c:when>
							<c:when test="${fn:endsWith(post.attachmentName, 'mp4')}">
								<video src="data:video/mp4;base64,${post.base64Encoded}" width="100%" height="200px"></video>
							</c:when>
							<c:when test="${fn:endsWith(post.attachmentName, 'mp3')}">
								<audio controls src="data:audio/mp3;base64,${post.base64Encoded}" 
										style="margin-top:80px; margin-left:80px" width="100%" height="200px"></audio>
								</c:when>
						</c:choose>
					</div>
				</c:if>

			</div>
		</c:if>
		<br />
		<div style="width: 100%">
			<div style="width: 10%; float: left;" align="left">
				<a href="#"><img alt="" src="/csn/resources/like.png"
					style="height: 24px; width: 24px;" /><font color="grey"
					style="font-size: x-small; margin-top: 10px; margin-bottom: 10px;"><c:out
							value="${requestScope.post.likes}" /></font></a>
			</div>
			<div style="width: 10%; float: left;" align="left">
				<a href="#"><img alt="" src="/csn/resources/comment3.png"
					style="height: 24px; width: 24px;" /><font color="grey"
					style="font-size: x-small; margin-top: 10px; margin-bottom: 10px"><c:out
							value="${requestScope.post.comments}" /></font></a>
			</div>
		</div>


		<div>
			<br />
			<br />
			<hr width="80%">
			<br />
			<form method="post" action="/csn/posts/${requestScope.post.postId}/comment?post=true">
				<a href="${requestScope.post.postId}/like?post=true"><img alt=""
					src="/csn/resources/like2.png" style="height: 24px; width: 24px;" /></a>
				&nbsp;&nbsp;
				<textarea rows="4px" cols="45px" name="comment"></textarea>
				<input type="image" src="/csn/resources/comment2.png" height="24px"
					width="24px" />
			</form>
			<br />
			<hr width="80%">
			<br />
			
			<c:if test="${requestScope.post.likes != 0}">
			 <font color="#113388" style="font-size: small;">Liked By - </font> 
			 <font color="grey" style="font-size: x-small;"><c:out value="${requestScope.post.likedBy}" /> </font>
			</c:if>
			<br>
			<br>
			<c:if test="${!empty requestScope.post.commentList}">
			<font color="#113388" style="font-size: small;">Comments - </font> 
			<br/><br/>
			<c:choose>
					<c:when test="${!empty requestScope.post.commentList}">
						<c:forEach var="comment" items="${requestScope.post.commentList}">
						<div style="width: 100%">
						<font color="grey" style="font-size: x-small;"><b>
						<c:out value="${comment.when}" />(<c:out value="${comment.byName}" />) : </b>
						<c:out value="${comment.text}" /></font>
						</div>
						<br/>
						</c:forEach>
					</c:when>
				</c:choose>
				</c:if>
		</div>
	</div>
	<br />

</div>