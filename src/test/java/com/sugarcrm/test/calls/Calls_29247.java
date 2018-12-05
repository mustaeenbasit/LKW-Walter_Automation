package com.sugarcrm.test.calls;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_29247 extends SugarTest {
	DataSource callData = new DataSource();

	public void setup() throws Exception {
		callData = testData.get(testName);
		sugar().login();
		sugar().calls.navToListView();

		// TODO: VOOD-444
		for (int i = 0; i <= 2; i++) {
			sugar().calls.listView.create();
			sugar().calls.createDrawer.getEditField("name").set(callData.get(i).get("name"));
			sugar().calls.createDrawer.showMore();
			sugar().calls.createDrawer.getEditField("teams").set(callData.get(i).get("teams"));
			sugar().calls.createDrawer.save();
		}
	}

	/**
	 * Verify that searching should work for "teams" field in advance search 
	 * @throws Exception
	 */
	@Test
	public void Calls_29247_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Setting-Up Teams filter in list view of Calls module
		// TODO: VOOD-1874
		FieldSet filterData = testData.get(testName +"_filterData").get(0);
		sugar().calls.listView.openFilterDropdown();
		sugar().calls.listView.selectFilterCreateNew();
		new VoodooSelect("div", "css", "div[data-filter='field']").set(filterData.get("displayValue"));
		new VoodooSelect("div", "css", "div[data-filter='operator']").set(filterData.get("operator"));
		VoodooSelect searchInput = new VoodooSelect("ul", "css", ".detail.fld_team_name");
		searchInput.set(filterData.get("value"));
		VoodooUtils.waitForReady();

		// Save filter entering a name
		sugar().calls.listView.filterCreate.getControl("filterName").set(testName);
		VoodooUtils.waitForReady();
		sugar().calls.listView.filterCreate.save();
		try {
			// Verify that call having team as East is displayed.
			Assert.assertTrue("Total no. of Rows not equal to 1", sugar().calls.listView.countRows() == 1);
			sugar().calls.listView.getDetailField(1,"name").assertEquals(callData.get(0).get("name"), true);
		} 
		finally {
			// Remove custom filter  
			sugar().calls.listView.getControl("searchFilterCurrent").click();
			VoodooUtils.waitForReady();
			sugar().calls.listView.filterCreate.delete();
			sugar().alerts.getWarning().confirmAlert();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");	
	}

	public void cleanup() throws Exception {}
}