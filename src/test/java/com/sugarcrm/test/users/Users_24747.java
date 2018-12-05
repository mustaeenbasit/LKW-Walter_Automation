package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Users_24747 extends SugarTest{
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}
	/**
	 * Click user name on Account list view with a normal user login.
	 * @throws Exception
	 */
	@Test
	public void Users_24747_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.getDetailField(1, "relAssignedTo").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that user navigated to the Employee Detail page of "Administrator"
		// TODO: VOOD-1041 (Need lib support of employees module)
		new VoodooControl("span", "id", "first_name").assertEquals(customData.get("userName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
