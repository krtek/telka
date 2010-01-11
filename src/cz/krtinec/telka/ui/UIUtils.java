package cz.krtinec.telka.ui;

import java.util.Date;

import android.util.Log;

public class UIUtils {
	
	static int determinePercent(Date start, Date stop) {
		Date now = new Date();
		
		long length = stop.getTime() - start.getTime();
		long actual = now.getTime() - start.getTime();
		
		double onePercent = length / 100;
		int percent = (int) (actual / onePercent);
		//Log.d("UIUtils", start + " - " + stop + ":" + percent);
		return percent;
		
	}

}
