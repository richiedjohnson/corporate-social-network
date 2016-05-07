package edu.nyu.rdj259.csn.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;

public class Post {

	private int postId;
	private String title;
	private String content;
	private Timestamp postedAt;
	private String postedBy;
	private String postedByName;
	private String visibility;
	private int likes;
	private int comments;
	private String attachmentName;
	private byte[] attachmentContent;
	private String base64Encoded;
	private int locId;
	private String locationName;
	private String likedBy;
	private List<Comment> commentList;

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(Timestamp postedAt) {
		this.postedAt = postedAt;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public byte[] getAttachmentContent() {
		return attachmentContent;
	}

	public void setAttachmentContent(byte[] attachmentContent) {
		this.attachmentContent = attachmentContent;
		if(attachmentName.toLowerCase().endsWith(".txt")){
			String header = "<style> html, body {font-family:Helvetica, Arial, sans-serif;"
					+ "font-size: x-small;color:grey;} </style>";
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				outputStream.write(header.getBytes());
				outputStream.write(attachmentContent);
				attachmentContent = outputStream.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		byte[] encodeBase64;
		try {
			encodeBase64 = Base64.getEncoder().encode(attachmentContent);
			String base64Encoded = new String(encodeBase64, "UTF-8");
			this.base64Encoded = base64Encoded;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

	public int getLocId() {
		return locId;
	}

	public void setLocId(int locId) {
		this.locId = locId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getPostedByName() {
		return postedByName;
	}

	public void setPostedByName(String postedByName) {
		this.postedByName = postedByName;
	}

	public String getBase64Encoded() {
		return base64Encoded;
	}

	public void setBase64Encoded(String base64Encoded) {
		this.base64Encoded = base64Encoded;
	}

	public String getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(String likedBy) {
		this.likedBy = likedBy;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	
	

}
