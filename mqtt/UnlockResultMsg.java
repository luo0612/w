package com.vogtec.ibx5.mqtt;

public class UnlockResultMsg {

	private String whoBeUnlock;// activityName
	private String bikeNum;
	private int unlockState;
	private String token;

	public UnlockResultMsg() {
		super();
	}

	public UnlockResultMsg(String whoBeUnlock, String bikeNum, int unlockState,
			String token) {
		super();
		this.whoBeUnlock = whoBeUnlock;
		this.bikeNum = bikeNum;
		this.unlockState = unlockState;
		this.token = token;
	}

	public String getWhoBeUnlock() {
		return whoBeUnlock;
	}

	public void setWhoBeUnlock(String whoBeUnlock) {
		this.whoBeUnlock = whoBeUnlock;
	}

	public String getBikeNum() {
		return bikeNum;
	}

	public void setBikeNum(String bikeNum) {
		this.bikeNum = bikeNum;
	}

	public int getUnlockState() {
		return unlockState;
	}

	public void setUnlockState(int unlockState) {
		this.unlockState = unlockState;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "UnlockResultMsg [whoBeUnlock=" + whoBeUnlock + ", bikeNum="
				+ bikeNum + ", unlockState=" + unlockState + ", token=" + token
				+ "]";
	}

}
