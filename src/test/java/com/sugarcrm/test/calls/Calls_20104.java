package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_20104 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		customDS = testData.get(testName+"_callsData");
		sugar().calls.api.create(customDS);
		sugar().login();

		// Assigned team 'West' with a call record
		sugar().calls.navToListView();
		sugar().calls.listView.clickRecord(1);
		sugar().calls.recordView.edit();
		sugar().calls.recordView.getEditField("teams").set("West");
		sugar().calls.recordView.save();
	}

	/**
	 * Clearing the advanced search criteria should clear the currently selected Team selection
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_20104_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		// Go to calls listView
		sugar().calls.navToListView();
		sugar().calls.navToListView();
		sugar().calls.listView.openFilterDropdown();
		sugar().calls.listView.selectFilterCreateNew();

		// TODO: VOOD-1766
		// Select 'Teams' on the first dropdown, 'is' on the second dropdown and 'West' in the third dropdown. Wait for the results to load.
		VoodooSelect reordSetCtrl = new VoodooSelect("span", "css", "span.detail.fld_team_name");
		new VoodooSelect("span", "css", "span.detail.fld_filter_row_name").set("Teams");
		new VoodooSelect("span", "css",  "span.detail.fld_filter_row_operator").set("is");
		
		// Search with team "West"
		reordSetCtrl.set(customDS.get(1).get("teams"));
		VoodooUtils.waitForReady();
		
		// Verify that the results displayed should only be those that are assigned to team 'West'.
		sugar().calls.listView.verifyField(1, "name", customDS.get(1).get("name"));

		// Search with team "Global"
		reordSetCtrl.set(customDS.get(0).get("teams"));
		VoodooUtils.waitForReady();
		
		// Verify that the results displayed should only be those that are assigned to team 'Global'.
		sugar().calls.listView.verifyField(1, "name", customDS.get(0).get("name"));

		// Search with team "West" again
		reordSetCtrl.set(customDS.get(1).get("teams"));
		VoodooUtils.waitForReady();
		
		// Verify that the results displayed should only be those that are assigned to team 'West'.
		sugar().calls.listView.verifyField(1, "name", customDS.get(1).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}