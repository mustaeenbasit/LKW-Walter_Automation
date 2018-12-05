package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24710 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Mass update_Verify that "LBL_SUGAR_LOGIN" field is not displayed in the mass update panel of "Users: Home" page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_24710_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to user management page
		sugar().admin.navToAdminPanelLink("userManagement");
		
		// Select user item1 in list view
		sugar().users.listView.checkRecord(1);
		
		// Select Mass Update
		sugar().users.listView.massUpdate();

		// Verify MassUpdate form does not contain invalid field labels LBL_SUGAR_LOGIN 
		VoodooUtils.focusFrame("bwc-frame");
		DataSource fieldNamesDS = testData.get(testName);
		for (int i = 0; i < fieldNamesDS.size(); i++) {
			// TODO: VOOD-768 need defined control for buttons and input boxes on mass update panel on BWC list view
			new VoodooControl("table", "css", "#mass_update_table").assertContains(fieldNamesDS.get(i).get("InvalidFldNames"), false);
		}
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}