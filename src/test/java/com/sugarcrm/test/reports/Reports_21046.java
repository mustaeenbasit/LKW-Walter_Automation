package com.sugarcrm.test.reports;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Reports_21046 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that reports search condition are cleared when clicking "Clear" button.
	 * @throws Exception
	 */
	@Test
	public void Reports_21046_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet reportData = testData.get(testName).get(0);
		VoodooControl moduleTitle = new VoodooControl("h2", "css", ".moduleTitle h2");

		// Navigating to "View Reports" menu
		sugar().navbar.selectMenuItem(sugar().reports, "viewReports");
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		moduleTitle.assertElementContains(reportData.get("search"), true);

		// Setting Subject in Title Field
		// TODO: VOOD-822
		new VoodooControl("input", "css", "input[name='name']").set(reportData.get("subject"));

		// Setting Module type
		VoodooControl moduleName = new VoodooControl("select", "css", "select[name='report_module[]']");
		moduleName.set(sugar().accounts.moduleNamePlural);

		// Setting My Favorites & My Items as checked
		new VoodooControl("input","css","input[name='favorite']").click();
		new VoodooControl("input","css","input[name='current_user_only']").click();

		// Setting Assigned to User
		VoodooControl assignedUser = new VoodooControl("select", "css", "select[name='assigned_user_id[]']");
		assignedUser.set(reportData.get("assignedTo"));

		// Checking if Assigned to User is selected
		VoodooControl selectedAdministrator = new VoodooControl("option", "css", "select[name='assigned_user_id[]'] option:nth-of-type(2)");
		boolean ifSelectedAdministrator = selectedAdministrator.hasAttribute("selected");
		assertTrue("administrator not selected",ifSelectedAdministrator);

		// Clicking on Clear search
		new VoodooControl("input", "css", "input[name='clear']").click();
		VoodooUtils.waitForReady();

		// Verifying that Title is Empty
		moduleName.assertContains("", true);

		// Verifying that  My favorite & My Items are unchecked
		new VoodooControl("input","css","input[name='favorite']").assertChecked(false);
		new VoodooControl("input","css","input[name='current_user_only']").assertChecked(false);

		// Verify that Assigned to list is Empty i.e Nothing remains Selected
		// TODO: VOOD-1379
		boolean ifSelectedAdmin2 = (boolean) new VoodooControl("option", "css", "select[name='assigned_user_id[]'] option:nth-of-type(1)").hasAttribute("selected");
		ifSelectedAdministrator = selectedAdministrator.hasAttribute("selected");
		boolean ifSelectedqaUser = (boolean) new VoodooControl("option", "css", "select[name='assigned_user_id[]'] option:nth-of-type(3)").hasAttribute("selected");
		assertTrue("Clear not successful. One of the values remain selected", !ifSelectedAdmin2 && !ifSelectedAdministrator && !ifSelectedqaUser);

		// Verify that Module list is Empty i.e Nothing remains Selected
		boolean ifSelectedAccounts= (boolean) new VoodooControl("option", "css", "select[name='report_module[]'] option[value='"+sugar().accounts.moduleNamePlural+"']").hasAttribute("selected");
		assertTrue("Clear not successful. Accounts remain selected in the list",!ifSelectedAccounts);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}