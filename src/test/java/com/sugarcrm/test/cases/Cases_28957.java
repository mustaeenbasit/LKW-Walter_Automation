package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Cases_28957 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that date & time fields do not allow insertion of invalid date/time
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_28957_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		// Quick create menu is implemented
		sugar().navbar.quickCreateAction(sugar().tasks.moduleNamePlural);

		// Set start date
		sugar().tasks.createDrawer.getEditField("date_start_date").set(customData.get("wrong_date"));
		sugar().tasks.createDrawer.getEditField("subject").click();

		// When date field looses focus, the date field should become empty.
		sugar().tasks.createDrawer.getEditField("date_start_date").assertContains("", true);

		// Set end date
		sugar().tasks.createDrawer.getEditField("date_due_date").set(customData.get("wrong_date"));

		// When date field looses focus, the date field should become empty.
		sugar().tasks.createDrawer.getEditField("date_due_date").assertContains("", true);

		// Cancel quick createDrawer
		sugar().tasks.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
