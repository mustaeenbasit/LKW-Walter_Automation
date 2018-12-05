package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_23035 extends SugarTest {
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().login();
		myAccount = (AccountRecord) sugar().accounts.api.create();
		myAccount.navToRecord();
	}

	/**
	 * Verify that new task is correctly created on "Tasks" sub-panel.
	 * @throws Exception
	 */
	@Test
	public void Accounts_23035_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().accounts.recordView.subpanels.get("Tasks").addRecord();;
		sugar().tasks.createDrawer.getEditField("subject").set(myAccount.getRecordIdentifier());
		sugar().tasks.createDrawer.cancel();
		VoodooUtils.waitForAlertExpiration();
		
		// TODO VOOD-609
		new VoodooControl("tr", "css" ,"div.layout_Tasks div.flex-list-view-content table tbody tr").assertExists(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}