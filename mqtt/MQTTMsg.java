package com.vogtec.ibx5.mqtt;

public class MQTTMsg {

	private int type;
	private String msg;

	public MQTTMsg() {
		super();
	}

	public MQTTMsg(int type, String msg) {
		super();
		this.type = type;
		this.msg = msg;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
