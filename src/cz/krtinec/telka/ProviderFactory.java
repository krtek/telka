package cz.krtinec.telka;

import java.util.List;

import android.content.Context;

import cz.krtinec.telka.provider.ProgrammeProvider;

public class ProviderFactory {
	private static IProgrammeProvider provider;
	
	public static IProgrammeProvider getProvider(Context context) {
		if (provider == null) {
			provider = new ProgrammeProvider(context);
		}
		return provider;
	}

}
