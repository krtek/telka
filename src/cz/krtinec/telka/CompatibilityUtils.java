package cz.krtinec.telka;

import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;


public class CompatibilityUtils {
	public static boolean hasSettingTargetDensityOnBitmapDrawable(){
		//check if device platform has following method BitmapDrawable.setTargetDensity
		try {
			BitmapDrawable.class.getMethod("setTargetDensity", DisplayMetrics.class);
			return true;
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}
		return false;
	}
}
