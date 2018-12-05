package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class Dashlets_26226 extends SugarTest {
	VoodooControl saveBtnCtrl;
	
	public void setup() throws Exception {
		//Create Opportunity and RevLineItem
		OpportunityRecord myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		RevLineItemRecord myRLI = (RevLineItemRecord)sugar().revLineItems.api.create();

		sugar().login();

		// Link RLI to Opportunity
		FieldSet fs =  new FieldSet();
		fs.put("relOpportunityName", myOpp.getRecordIdentifier());
		myRLI.edit(fs);

		// Go to Opportunity Record View
		myOpp.navToRecord();
	}

	/**
	 * Tooltip are correct in Toggle bottons in Dashlets
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_26226_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);

		// Toggle to My Dashboard
		VoodooControl dashboard = sugar().accounts.dashboard.getControl("dashboard");
		if(!dashboard.queryContains(customFS.get("selectDashboard"), true))
			sugar().dashboard.chooseDashboard(customFS.get("selectDashboard"));

		sugar().opportunities.dashboard.edit();
		sugar().opportunities.dashboard.addRow();
		sugar().opportunities.dashboard.addDashlet(7, 1);
		VoodooUtils.waitForReady();

		// TODO: VOOD-670, VOOD-1660
		// Inactive Tasks
		new VoodooControl("input", "css", "div[data-voodoo-name='filtered-search'] input").set(customFS.get("selectDashlet"));//.set(customFS.get("selectActivityInDashlet"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".drawer.active .fld_save_button a").click(); // Save dashlet
		VoodooUtils.waitForReady();

		// Save Dash-board
		saveBtnCtrl = new VoodooControl("a", "css", "div[data-voodoo-name*='dashboard-headerpane'] .btn-toolbar .fld_save_button a");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Planned Activities: Mouse hover the user or group icon and Verify tooltip text
		new VoodooControl("i", "css", "[data-voodoo-name='planned-activities'] .fa.fa-user").hover();
		VoodooControl tooltipVerifyCtrl = new VoodooControl("div", "css", ".tooltip.fade.bottom.in .tooltip-inner");
		tooltipVerifyCtrl.waitForVisible();
		tooltipVerifyCtrl.assertContains(customFS.get("myActivity"), true);
		new VoodooControl("i", "css", "[data-voodoo-name='planned-activities'] .fa.fa-users").hover();
		tooltipVerifyCtrl.waitForVisible();
		tooltipVerifyCtrl.assertContains(customFS.get("teamActivity"), true);

		// Active Tasks: Mouse hover the user or group icon and Verify tooltip text
		new VoodooControl("i", "css", "[data-voodoo-name='active-tasks'] .fa.fa-user").hover();
		tooltipVerifyCtrl.waitForVisible();
		tooltipVerifyCtrl.assertContains(customFS.get("myTasks"), true);
		new VoodooControl("i", "css", "[data-voodoo-name='active-tasks'] .fa.fa-users").hover();
		tooltipVerifyCtrl.waitForVisible();
		tooltipVerifyCtrl.assertContains(customFS.get("teamTasks"), true);

		// History: Mouse hover the user or group icon and Verify tooltip text
		new VoodooControl("i", "css", "[data-voodoo-name='history'] .fa.fa-user").hover();
		tooltipVerifyCtrl.waitForVisible();
		tooltipVerifyCtrl.assertContains(customFS.get("myHistory"), true);
		new VoodooControl("i", "css", "[data-voodoo-name='history'] .fa.fa-users").hover();
		tooltipVerifyCtrl.waitForVisible();
		tooltipVerifyCtrl.assertContains(customFS.get("teamHistory"), true);

		// InActive Tasks: Mouse hover the user or group icon and Verify tooltip text
		new VoodooControl("i", "css", "[data-voodoo-name='inactive-tasks'] .fa.fa-user").hover();
		tooltipVerifyCtrl.waitForVisible();
		tooltipVerifyCtrl.assertContains(customFS.get("myTasks"), true);
		new VoodooControl("i", "css", "[data-voodoo-name='inactive-tasks'] .fa.fa-users").hover();
		tooltipVerifyCtrl.waitForVisible();
		tooltipVerifyCtrl.assertContains(customFS.get("teamTasks"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}