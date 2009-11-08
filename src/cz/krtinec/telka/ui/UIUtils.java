package cz.krtinec.telka.ui;

import java.util.Date;

public class UIUtils {
	
	static int determinePercent(Date start, Date stop) {
		Date now = new Date();
		
		long length = stop.getTime() - start.getTime();
		long actual = now.getTime() - start.getTime();
		
		double onePercent = length / 100;
		int percent = (int) (actual / onePercent);
		//Log.d("ProgrammeView", p.start + " - " + p.stop + ":" + percent);
		return percent;
		
	}

}
