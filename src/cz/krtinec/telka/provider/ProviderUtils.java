package cz.krtinec.telka.provider;

import java.util.Date;

import cz.krtinec.telka.dto.State;

public class ProviderUtils {
	static State determineState(Date start, Date stop) {
		final Date NOW = new Date();
		
		if (stop.compareTo(NOW) < 0) {
			return State.OVER;
		} else if (start.compareTo(NOW) > 0) {
			return State.WILL_RUN;
		} else {
			return State.RUNNING;
		}			
	}

}
