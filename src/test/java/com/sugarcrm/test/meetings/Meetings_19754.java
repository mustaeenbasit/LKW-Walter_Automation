package com.sugarcrm.test.meetings;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Meetings_19754 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that the error message displayed once in Meeting creation (not twice)
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_19754_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log Meeting with name and empty start date and then Click "Save" button
		sugar().navbar.selectMenuItem(sugar().meetings, "create"+sugar().meetings.moduleNameSingular);
		sugar().meetings.createDrawer.getEditField("name").set(testName);
		sugar().meetings.createDrawer.getEditField("date_start_date").set("");
		sugar().meetings.createDrawer.save();

		// Verify An error message should be displayed once
		Assert.assertTrue("Error message alert not equals One", sugar().alerts.getError().count() == 1);
		sugar().alerts.getError().closeAlert();
		sugar().meetings.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}