package cz.krtinec.telka.dto;

public enum State {
	OVER("bezel"),    	//uz bezel
	RUNNING("bezi"),	//bezi
	WILL_RUN("pobezi");	//pobezi
	
	private String desc;
	private State(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return desc;
	}
	
	
}
