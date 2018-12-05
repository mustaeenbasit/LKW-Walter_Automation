package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17003 extends SugarTest {
	VoodooControl accountsSubpanelCtrl;
	FieldSet customData;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();

		// Navigate to Admin > Studio > Accounts > Fields > officePhone
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-517, VOOD-542
		accountsSubpanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubpanelCtrl.click(); // Click on Accounts module
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Buttons td:nth-of-type(2) tr:nth-of-type(2) a").click(); // Click on Fields
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#field_table #phone_office").click(); // Click on Office Phone field
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[name='required']").click(); // Set checkbox "required" to checked
		new VoodooControl("input", "name", "fsavebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Validation check for phone type field on create form
	 * @throws Exception
	 */
	@Test
	public void Accounts_17003_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
				
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(customData.get("name"));
		sugar().accounts.createDrawer.getControl("saveButton").click();
		sugar().alerts.waitForLoadingExpiration(30000); // Required more wait to complete save action
	
		// Verify that show a error message about the phone field
		sugar().accounts.createDrawer.getEditField("workPhone").assertAttribute("class", "required", true);
		sugar().alerts.getError().assertContains(customData.get("error_message"), true);
		sugar().alerts.getAlert().closeAlert();
		sugar().accounts.createDrawer.cancel();
		
		// Verify that the record not saved
		sugar().accounts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}