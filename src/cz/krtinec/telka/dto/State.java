package cz.krtinec.telka.dto;

import android.util.AttributeSet;

public enum State {
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
