package com.sugarcrm.test.tasks;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tasks_21008 extends SugarTest {
	DataSource customData = new DataSource();

	public void setup() throws Exception {
		customData = testData.get(testName);
		sugar().tasks.api.create(customData);
		sugar().login();	
	}

	/**
	 * Search Tasks: Verify that tasks can be searched by "Subject"
	 * @throws Exception
	 */	
	@Test
	public void Tasks_21008_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// Go to List View
		sugar().tasks.navToListView();
		
		// Create Filter for "Subject"
		sugar().tasks.listView.openFilterDropdown();
		sugar().tasks.listView.selectFilterCreateNew();

		// Enter filter name
		FieldSet filterData = testData.get(testName + "_filterData").get(0);
		sugar().tasks.listView.filterCreate.setFilterFields(filterData.get("field").toLowerCase(), filterData.get("field"), filterData.get("operator"), filterData.get("value"), 1);
		VoodooUtils.waitForReady();
		sugar().tasks.listView.filterCreate.getControl("filterName").set(testName);
		sugar().tasks.listView.filterCreate.save();
		VoodooUtils.waitForReady();

		// Verify search should return results according to the filter created
		for(int i = 0; i < customData.size(); i++) {
			if ( i>=2 ) {
				sugar().tasks.listView.assertContains(customData.get(i).get("subject"), false);
			} else {
				sugar().tasks.listView.assertContains(customData.get(i).get("subject"), true);
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete."); 
	}

	public void cleanup() throws Exception {}
}