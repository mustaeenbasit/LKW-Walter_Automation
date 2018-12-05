package com.sugarcrm.test.dashlets;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_29687 extends SugarTest {

	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Blank dashlet should not be displayed in MyDashboard
	 * @throws Exception
	 */
	@Test
	public void Dashlets_29687_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to contact record 
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		//  Open the created record and Click on Create Call from Call subpanel -> Provide required info and Save the record.
		sugar().contacts.recordView.subpanels.get(sugar().calls.moduleNamePlural).create(sugar().calls.getDefaultData());

		// Choose My Dashboard
		FieldSet fs = testData.get(testName).get(0);
		VoodooControl dashboardTitle = sugar().contacts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(fs.get("my_dashboard_name"), true)) {
			sugar().dashboard.chooseDashboard(fs.get("my_dashboard_name"));
		}

		sugar().contacts.dashboard.edit();
		sugar().contacts.dashboard.addRow();
		sugar().contacts.dashboard.addDashlet(3, 1);

		// Select List View
		// TODO: VOOD-960 - Dashlet selection, VOOD-670 - More Dashlet Support
		VoodooControl searchDashlet = new VoodooControl("input", "css", ".span4.search");
		searchDashlet.set(fs.get("dashlets_title"));
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".list-view .fld_title a").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("div", "css", ".edit.fld_module div").set(sugar().calls.moduleNamePlural);
		new VoodooControl("input", "css", ".edit.fld_intelligent input").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".btn-toolbar.pull-right.dropdown .detail.fld_save_button [name='save_button']").click();
		VoodooUtils.waitForReady();

		// save dashboard
		// TODO: VOOD-1417 - sugar.contacts.dashboard.save(); not working 
		new VoodooControl("a", "css", "div.preview-headerbar .btn-toolbar .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Verify that call record Available in MyCalls dashlet inside My dashboard
		new VoodooControl("span", "css", "[data-voodoo-name='dashablelist'] .list.fld_name").assertEquals(sugar().calls.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}