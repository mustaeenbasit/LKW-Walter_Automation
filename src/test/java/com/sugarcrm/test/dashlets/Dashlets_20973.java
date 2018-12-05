package com.sugarcrm.test.dashlets;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20973  extends SugarTest {
	AccountRecord myAccountRecord;

	public void setup() throws Exception {
		myAccountRecord = (AccountRecord)sugar.accounts.api.create();
		sugar.login();

		// TODO: VOOD-444
		sugar.tasks.navToListView();
		sugar.tasks.listView.create();
		sugar.tasks.createDrawer.getEditField("subject").set(sugar.tasks.getDefaultData().get("subject"));
		sugar.tasks.createDrawer.getEditField("relRelatedToParentType").set(sugar.accounts.moduleNameSingular);
		sugar.tasks.createDrawer.getEditField("relRelatedToParent").set(myAccountRecord.getRecordIdentifier());
		sugar.tasks.createDrawer.save();
	}

	/**
	 * Verify that the task detail view related to this account is displayed by clicking the subject link on To DO tab of Active Tasks dashlet
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20973_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to an account record detail view. 
		myAccountRecord.navToRecord();
		sugar.accounts.dashboard.clickCreate();
		sugar.accounts.dashboard.getControl("title").set(testName);
		sugar.accounts.dashboard.addRow();
		sugar.accounts.dashboard.addDashlet(1,1);

		// TODO: VOOD-963
		//  Add the new dashlet on a dashboard - Active Tasks.
		new VoodooControl("a", "css", "[data-original-title='Active Tasks'] a").click();
		new VoodooControl("a", "css", ".pull-right.dropdown .fld_save_button .btn.btn-primary").click();
		sugar.accounts.dashboard.save();
		sugar.alerts.waitForLoadingExpiration();

		// In this dashlet go to "To Do" tab.
		new VoodooControl("a", "css", "div.dashlet-tabs.tab3 div:nth-child(3) a").click();

		// Click a task subject link.
		new VoodooControl("a", "css", ".dashlet-content li:nth-child(1) a:nth-child(2)").click();
		sugar.alerts.waitForLoadingExpiration();

		// The task detail view is displayed.
		sugar.tasks.recordView.getDetailField("subject").assertEquals(sugar.tasks.getDefaultData().get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}