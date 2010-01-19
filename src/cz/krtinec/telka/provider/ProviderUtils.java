package cz.krtinec.telka.provider;

import java.util.Date;

import android.util.Log;

import cz.krtinec.telka.dto.State;

public class ProviderUtils {	
		
	public static final State determineState(Date start, Date stop) {		
		Date now = new Date();				
		if (stop.compareTo(now) < 0) {		
			return State.OVER;
		} else if (start.compareTo(now) > 0) {		
			return State.WILL_RUN;
		} else {			
			return State.RUNNING;
		}			
	}

}
