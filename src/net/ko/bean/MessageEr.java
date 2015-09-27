package net.ko.bean;

public class MessageEr {
	private String id;
	private String message;
	private String er;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEr() {
		return er;
	}
	public void setEr(String er) {
		this.er = er;
	}

	public MessageEr(String id, String message, String er) {
		super();
		this.id = id;
		this.message = message;
		this.er = er;
	}
	public MessageEr() {
		id="";
		message="";
		er="";
	}
}
