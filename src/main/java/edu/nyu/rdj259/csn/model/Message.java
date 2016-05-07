package edu.nyu.rdj259.csn.model;

public class Message {
	
	private String from;
	private String fromName;
	private String at;
	private String content;
	private String to;
	private boolean newMsg;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAt() {
		return at;
	}
	public void setAt(String at) {
		this.at = at;
	}
	public boolean isNewMsg() {
		return newMsg;
	}
	public void setNewMsg(boolean newMsg) {
		this.newMsg = newMsg;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	

}
