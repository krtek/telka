package cz.krtinec.telka.dto;

import java.io.Serializable;

public enum State implements Serializable {
	OVER("Over"),    	//uz bezel
	RUNNING("Running"),	//bezi
	WILL_RUN("Will run");	//pobezi
	
	private String desc;
	private State(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return desc;
	}
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return this.equals(RUNNING);
	}
	
	
}
