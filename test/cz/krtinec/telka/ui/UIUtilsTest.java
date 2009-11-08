package cz.krtinec.telka.ui;

import java.util.Date;

import junit.framework.TestCase;

public class UIUtilsTest extends TestCase {
	private static final long MINUTE = 1000 * 60;
	
	public void testDeterminePercent() {
		Date now = new Date();
		int fifty = UIUtils.determinePercent(
				new Date(now.getTime() - 30 * MINUTE), new Date(now.getTime() + 30 * MINUTE));
		System.out.println(fifty);
		assertTrue(49 <= fifty && fifty <= 51);
		
		int twentyFive = UIUtils.determinePercent(
				new Date(now.getTime() - 15 * MINUTE), new Date(now.getTime() + 45 * MINUTE)); 
		System.out.println(twentyFive);
		assertTrue(24 <= twentyFive && twentyFive <= 26); 

	}

}
