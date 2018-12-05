package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_18602 extends SugarTest {
	DataSource ds1 = new DataSource();
	FieldSet systemSettingsData = new FieldSet();

	public void setup() throws Exception {
		ds1 = testData.get(testName);
		DataSource ds2 = testData.get(testName + "_1");
		// Create 5 quotes records
		sugar().quotes.api.create(ds2);
		sugar().login();

		// Set System settings
		systemSettingsData.put("maxEntriesPerPage", ds1.get(0).get("displyNum"));
		sugar().admin.setSystemSettings(systemSettingsData);
	}

	/**
	 * Test Case 18602: [delete record]-Select all records-Verify that all records are deleted after pagination
	 *
	 * @throws Exception
	 */
	@Test
	public void ListView_18602_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Quotes list view
		sugar().quotes.navToListView();

		// Select all quotes records using 'Select All' item from select dropdown
		// TODO: VOOD-579 Need support for select all records in list view
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "massall_top").click();
		new VoodooControl("span", "css", "#selectLinkTop  span").click();
		new VoodooControl("a", "id", "button_select_all_top").click();

		// Click 'Next' to navigate to the next page
		// TODO: VOOD-766 Need defined control for pagination buttons 
		new VoodooControl("button", "css" ,"#listViewNextButton_top").click();
		VoodooUtils.focusDefault();

		// Click 'Delete'
		sugar().quotes.listView.delete();
		VoodooUtils.acceptDialog();

		// Verify that all quotes records are successfully deleted
		sugar().quotes.listView.assertContains(ds1.get(0).get("message"), true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}
	public void cleanup() throws Exception {}
}