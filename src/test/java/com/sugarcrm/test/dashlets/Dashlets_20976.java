package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_20976  extends SugarTest {
	AccountRecord myAccountRecord;

	public void setup() throws Exception {
		myAccountRecord = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Account Detail - Activities dashlet - Create Task_Verify that new task is correctly created on in the Active Tasks dashlet
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_20976_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to an account record detail view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(testName);
		sugar().accounts.dashboard.addRow();
		sugar().accounts.dashboard.addDashlet(1, 1);

		FieldSet dashletSetup = testData.get("env_dashlets_setup").get(0);

		// TODO: VOOD-960 - Dashlet selection 
		// Add the new dashlet on a dashboard - Active Tasks.
		new VoodooControl("input", "css", ".span4.search").set( dashletSetup.get("activeDashlet"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".pull-right.dropdown .fld_save_button .btn.btn-primary").click();
		VoodooUtils.waitForReady();
		sugar().accounts.dashboard.save();

		// TODO: VOOD-963 - Some dashboard controls are needed
		// Inside the dashlet click "+" and "Create Task";
		new VoodooControl("span", "css", ".dashlet-header .btn-toolbar.pull-right .fa.fa-plus").click();
		new VoodooControl("a", "css", ".layout_Home [data-dashletaction='createRecord']").click();
		VoodooUtils.waitForReady();

		// Enter all mandatory fields for new task.
		sugar().tasks.createDrawer.getEditField("subject").set(testName);

		// verify that in the field "Related to" account name is set automatically;
		sugar().tasks.createDrawer.getEditField("relRelatedToParentType").assertEquals(sugar().accounts.moduleNameSingular, true);
		sugar().tasks.createDrawer.getEditField("relRelatedToParent").assertEquals(myAccountRecord.getRecordIdentifier(), true);

		// save the record
		sugar().tasks.createDrawer.save();
		if(sugar().alerts.getSuccess().queryVisible()) {
			sugar().alerts.getSuccess().closeAlert();
		}

		// Refresh the page
		VoodooUtils.refresh();

		// TODO: VOOD-963 - Some dashboard controls are needed
		new VoodooControl("a", "css", "div.dashlet-tabs.tab3 div:nth-child(3) a").click();
		VoodooUtils.waitForReady();

		//  Verify that new task is displayed correctly in the Active Tasks dashlet.
		new VoodooControl("a", "css", ".dashlet-content li:nth-child(1) a:nth-child(2)").assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}