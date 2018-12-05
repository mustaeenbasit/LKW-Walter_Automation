package com.sugarcrm.test.accounts;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_20271 extends SugarTest{
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// Create two Account records with different name
		sugar().accounts.api.create();
		FieldSet accountData = new FieldSet();
		accountData.put("name", customData.get("account_name"));
		sugar().accounts.api.create(accountData);
		sugar().login();

		// Go to Account recordView
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Select My Dashboard
		VoodooControl dashboardTitle =  sugar().accounts.dashboard.getControl("dashboard");
		if( !dashboardTitle.queryContains("My Dashboard", true) ) {
			sugar().cases.dashboard.chooseDashboard("My Dashboard");
		}
		sugar().accounts.dashboard.clickCreate();
		sugar().accounts.dashboard.getControl("title").set(customData.get("dashboard_title"));
		sugar().accounts.dashboard.addRow();

		// TODO: VOOD-958 - add dashlet control not working under dashboard module.
		// NOT WORKING: sugar().accounts.dashboard.addDashlet(1,1);
		VoodooControl addDashletCtrl = new VoodooControl("div", "css", ".add-dashlet strong");
		addDashletCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-960 - Dashlet selection
		new VoodooControl("a", "css", ".list-view .list.fld_title a").click();
		VoodooControl saveCtrl = new VoodooControl("a", "css", "#drawers .fld_save_button a");
		saveCtrl.click();
		VoodooUtils.waitForReady();

		// Add new row
		sugar().accounts.dashboard.addRow();
		VoodooUtils.waitForReady();
		addDashletCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-960 - Dashlet selection
		// select inactive task
		VoodooControl inactiveLinkCtrl = new VoodooControl("a", "css", ".list-view .dataTable tbody tr:nth-child(15) .list.fld_title a");
		inactiveLinkCtrl.scrollIntoViewIfNeeded(false);
		inactiveLinkCtrl.click();
		saveCtrl.click();
		VoodooUtils.waitForReady();
		sugar().accounts.dashboard.save();
	}

	/**
	 * Verify an "overdue" badge should be shown if due date is prior to current date
	 * @throws Exception;
	 */
	@Test
	public void Accounts_20271_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-960 - Dashlet selection 
		// Go to Active tasks dashlet and click + icon to create task
		new VoodooControl("i", "css", ".dashlet-row li .dashlet-header .fa.fa-plus").click();
		new VoodooControl("a", "css", ".dashlet-row li .dashlet-header .dropdown-menu li").click();
		VoodooUtils.waitForReady();

		// Add subject
		sugar().tasks.createDrawer.getEditField("subject").set(customData.get("task_name"));

		// Prior Date time to current  
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		localCalendar.add(Calendar.DATE, -1);
		Format formatter = new SimpleDateFormat("MM/dd/yyyy");
		String dueDate = formatter.format(localCalendar.getTime());
		sugar().tasks.createDrawer.getEditField("date_due_date").set(dueDate);
		sugar().tasks.createDrawer.save();

		// TODO: VOOD-960 - Dashlet selection 
		// Expected result: Overdue badge should be shown
		VoodooControl overdueBadgeCtrl = new VoodooControl("span", "css", ".listed li span.label");
		overdueBadgeCtrl.waitForVisible();
		overdueBadgeCtrl.assertVisible(true);
		overdueBadgeCtrl.assertContains(customData.get("assert_text"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}