package cz.krtinec.telka;

import org.acra.CrashReportingApplication;

import android.os.Bundle;

public class CrashReporting extends CrashReportingApplication {

	@Override
	public String getFormId() {
		return "dGlrMzNhakZQdlJ1T3VPUnRpRHdxcGc6MQ";
	}
	
	@Override
	public Bundle getCrashResources() {
	    Bundle result = new Bundle();
	    result.putInt(RES_TOAST_TEXT, R.string.crash_toast_text);
	    return result;
	}

}
