package cz.krtinec.telka.provider;

import java.util.Date;

import android.util.Log;

import cz.krtinec.telka.dto.State;

public class ProviderUtils {
	static final Date NOW = new Date();
		
	public static final State determineState(Date start, Date stop) {		
		NOW.setTime(System.currentTimeMillis());
		final int compareTo = stop.compareTo(NOW);		
		if (compareTo < 0) {		
			return State.OVER;
		} else if (start.compareTo(NOW) > 0) {		
			return State.WILL_RUN;
		} else {		
			return State.RUNNING;
		}			
	}

}
