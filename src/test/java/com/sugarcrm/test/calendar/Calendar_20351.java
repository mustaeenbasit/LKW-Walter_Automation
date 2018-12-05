package com.sugarcrm.test.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Calendar_20351 extends SugarTest {
	String dateString;
	
	public void setup() throws Exception {
		// Getting today's date
		SimpleDateFormat f = new SimpleDateFormat("EEEEE MMMMM d yyyy");
		dateString = f.format(new Date());
		sugar.login();
	}

	/**
	 * Verify that "Calendar" page is displayed when clicking "Today" link on Navigation shortcuts.
	 * @throws Exception
	 */
	@Test
	public void Calendar_20351_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.navbar.selectMenuItem(sugar.calendar, "today");

		// Verify that today's date is displayed on the calendar header
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-863: Library support for Calendar module
		new VoodooControl("h3", "css", ".monthHeader div:nth-child(2) h3")
				.assertEquals(dateString, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}