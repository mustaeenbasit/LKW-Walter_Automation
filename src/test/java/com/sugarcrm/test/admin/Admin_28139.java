package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Admin_28139 extends SugarTest {
	FieldSet fs;

	public void setup() throws Exception {
		fs = testData.get(testName).get(0);

		sugar().login();
	}

	/**
	 * Verify that First Day of Week is shown correctly in Detail view.
	 * @throws Exception
	 */
	@Test
	public void Admin_28139_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to User Profile Detail View 
		sugar().navbar.navToProfile();

		// Click Edit to open EditView for User Profile.
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("tab4").click();

		// TODO: VOOD-563 need lib support for user profile edit page
		// Change the First Day of Week to "Wednesday"
		new VoodooControl("select", "css", "#calendar_options [name='fdow']").click();
		new VoodooControl("option", "css", "#calendar_options [label='Wednesday']").click();
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "tab2").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify DetailView should show Wednesday only.
		new VoodooControl("slot", "css", "#calendar_options tr:nth-child(6) td:nth-child(2) slot").assertContains(fs.get("firstDayOfWeek"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}