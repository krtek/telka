package cz.krtinec.telka.provider;

import java.util.Date;

import junit.framework.TestCase;

import cz.krtinec.telka.dto.State;

public class ProviderUtilsTest extends TestCase {
	private static final long MINUTE = 1000 * 60;
	
	public void testDetermineState() {		
		assertEquals(State.OVER, ProviderUtils.determineState(
				new Date(System.currentTimeMillis() - (60 * MINUTE)), 
				new Date(System.currentTimeMillis() - (30 * MINUTE))));
	
		assertEquals(State.RUNNING, ProviderUtils.determineState(
				new Date(System.currentTimeMillis() - (30 * MINUTE)), 
				new Date(System.currentTimeMillis() + (30 * MINUTE))));

		assertEquals(State.WILL_RUN, ProviderUtils.determineState(
				new Date(System.currentTimeMillis() + (30 * MINUTE)), 
				new Date(System.currentTimeMillis() + (60 * MINUTE))));

		assertEquals(State.WILL_RUN, ProviderUtils.determineState(
				new Date(System.currentTimeMillis() + (12 * 60 * MINUTE)), 
				new Date(System.currentTimeMillis() + (13 * 60 * MINUTE))));		
	}
}
