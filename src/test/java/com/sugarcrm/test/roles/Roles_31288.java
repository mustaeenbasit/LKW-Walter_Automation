package com.sugarcrm.test.roles;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_31288 extends SugarTest {
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		sugar().login();
		FieldSet roleRecord = testData.get("env_role_setup").get(0);
		customData = testData.get(testName);

		// Admin -> Role management -> Creating Role
		AdminModule.createRole(roleRecord);

		// Assign QAuser to the Role
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleRecord);
		VoodooUtils.waitForReady();

		// Navigating to Dropdown Editor
		sugar().admin.navToAdminPanelLink("dropdownEditor");
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();

		// Click on 'quote_stage_dom'
		new VoodooControl("td", "css", "#dropdowns tr:nth-child(64) td a").click();
		VoodooUtils.waitForReady();

		// Select above created Role
		new VoodooControl("select", "css", "[name='dropdown_role']").set(roleRecord.get("roleName"));
		VoodooUtils.waitForReady();

		// Uncheck Closed Lost and Closed Dead Column
		new VoodooControl("input", "css", "[type='checkbox'][name='dropdown_keys[Closed Lost]']").click();
		new VoodooControl("input", "css", "[type='checkbox'][name='dropdown_keys[Closed Dead]']").click();
		VoodooUtils.waitForReady();

		// Click on save
		new VoodooControl("input", "css", "#saveBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().logout();
	}

	/**
	 *  Verify List Of Value fields not support for BWC modules
	 * @throws Exception
	 */
	@Test
	public void Roles_31288_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login as QAUser
		sugar().login(sugar().users.getQAUser());

		// Navigating to Quotes Create Drawer
		sugar().navbar.selectMenuItem(sugar().quotes, "createQuote");
		VoodooUtils.focusFrame("bwc-frame");

		// Verifying All the values of the "Quote Stage" drop down list is available for qauser.
		for (int i = 0; i < customData.size(); i++) {
			sugar().quotes.editView.getEditField("quoteStage").assertContains(customData.get(i).get("quotesStage"), true);
		}

		// Cancel the quote drawer
		VoodooUtils.focusDefault();
		sugar().quotes.editView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}