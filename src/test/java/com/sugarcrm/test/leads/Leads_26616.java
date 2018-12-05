package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_26616 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Create 10 Leads Records
		for (int x = 0; x < 20; x++) {
			sugar().leads.api.create();
		}

		// Navigate to Admin -> System Setting
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "configphp_settings").click();

		// In Admin > System Settings > Set "List View Items" to be 5.
		new VoodooControl("input", "id",
				"ConfigureSettings_list_max_entries_per_page").set("5");
		new VoodooControl("input", "name", "save").click();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Test verifies that clicking on the "more records" link shows more
	 * records.
	 */
	@Test
	public void Leads_26616_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Select checkbox for page records, increase by 5 records and verify.
		sugar().leads.navToListView();

		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.getControl("selectedRecordsAlert").assertContains(
				"5 records", true);

		sugar().leads.listView.showMore();
		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.getControl("selectedRecordsAlert").assertContains(
				"10 records", true);

		sugar().leads.listView.showMore();
		sugar().leads.listView.toggleSelectAll();
		sugar().leads.listView.getControl("selectedRecordsAlert").assertContains(
				"15 records", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}