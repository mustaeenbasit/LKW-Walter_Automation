package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_20070 extends SugarTest {
	DataSource meetingData;

	public void setup() throws Exception {
		meetingData = testData.get(testName);
		sugar.meetings.api.create(meetingData);
		sugar.login();
	}
	/**
	 * Search out open (Scheduled) items in Meetings module (using 'is any of') filter for Scheduled
	 */
	@Test
	public void ListView_20070_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Meetings list view
		sugar.meetings.navToListView();
		sugar.meetings.listView.openFilterDropdown();
		sugar.meetings.listView.selectFilterCreateNew();

		// Setting-Up filter in list view of Meetings module
		// TODO: VOOD-1478
		FieldSet filterData = testData.get(testName+"_1").get(0);
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(filterData.get("filterField"));
		new VoodooSelect("span", "css", ".detail.fld_filter_row_operator").set(filterData.get("operator"));
		new VoodooSelect("ul", "css", ".select2-choices").set(filterData.get("filter_value_Scheduled"));
		VoodooUtils.waitForReady();

		// Verifying Meetings appearing in list view are coming as per above filter	
		int listCount = sugar.meetings.listView.countRows();
		for (int i = 1; i <= listCount ; i++) 
			sugar.meetings.listView.verifyField(i, "status", meetingData.get(1).get("status"));

		// Close the filter in list view of Meetings module 
		// TODO: VOOD-1478
		new VoodooControl("a", "css", ".filter-close").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}