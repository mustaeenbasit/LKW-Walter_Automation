package com.sugarcrm.test.admin;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_20222 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Sorting the scheduler by status column can be done successfully
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_20222_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Goto Scheduler
		new VoodooControl("a", "id", "scheduler").click();
		
		VoodooUtils.focusFrame("bwc-frame");

		// Sort Status
		new VoodooControl("a", "css", "form#MassUpdate > table > tbody > tr:nth-child(2) > th:nth-child(7) > div > a").click();

		String firstValue = new VoodooControl("td", "css", "form#MassUpdate > table > tbody > tr:nth-child(3) > td:nth-child(7)").getText();
	
		// Sort Status again
		new VoodooControl("a", "css", "form#MassUpdate > table > tbody > tr:nth-child(2) > th:nth-child(7) > div > a").click();

		String secondValue = new VoodooControl("td", "css", "form#MassUpdate > table > tbody > tr:nth-child(3) > td:nth-child(7)").getText();

		// Assert - Before and After values of Status column of the first row are different 
		assertTrue("Sorting could not be done!!!", !firstValue.contentEquals(secondValue));
		
		// Sort Status again
		new VoodooControl("a", "css", "form#MassUpdate > table > tbody > tr:nth-child(2) > th:nth-child(7) > div > a").click();

		String thirdValue = new VoodooControl("td", "css", "form#MassUpdate > table > tbody > tr:nth-child(3) > td:nth-child(7)").getText();

		// Assert - Sort order is restored 
		assertTrue("Sorting could not be done!!!", firstValue.contentEquals(thirdValue));
		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}