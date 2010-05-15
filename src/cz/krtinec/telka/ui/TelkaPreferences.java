package cz.krtinec.telka.ui;

import cz.krtinec.telka.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class TelkaPreferences extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		addPreferencesFromResource(R.xml.preferences);
	}
}
