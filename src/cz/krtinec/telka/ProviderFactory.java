package cz.krtinec.telka;

import java.util.List;

import cz.krtinec.telka.provider.ProgrammeProvider;

public class ProviderFactory {
	private static IProgrammeProvider provider;
	
	public static IProgrammeProvider getProvider() {
		if (provider == null) {
			provider = new ProgrammeProvider();
		}
		return provider;
	}

}
