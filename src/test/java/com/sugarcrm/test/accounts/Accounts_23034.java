package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23034 extends SugarTest {
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
	public void Accounts_23034_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		StandardSubpanel taskSub = sugar().accounts.recordView.subpanels.get("Tasks");
		taskSub.addRecord();
		sugar().tasks.createDrawer.getEditField("subject").set(ds.get(0).get("subject"));
		sugar().tasks.createDrawer.getEditField("status").set(ds.get(0).get("status"));
		sugar().tasks.createDrawer.save();
		VoodooUtils.waitForAlertExpiration();
		
		// TODO VOOD-609
		new VoodooControl("span", "css" ,"div.layout_Tasks span.fld_name.list").assertContains(ds.get(0).get("subject"), true);
		new VoodooControl("span", "css" ,"div.layout_Tasks span.fld_status.list").assertContains(ds.get(0).get("status"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	
	public void cleanup() throws Exception {}
}