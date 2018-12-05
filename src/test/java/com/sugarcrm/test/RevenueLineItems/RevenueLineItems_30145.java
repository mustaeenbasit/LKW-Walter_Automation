package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_30145 extends SugarTest {
	public void setup() throws Exception {
		sugar().revLineItems.api.create();
		sugar().login();
	}

	/**
	 * Verify Calendar icon should be visible in the mass update form of RLI/Opportunity for all date type fields.
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_30145_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource massUpdateOptions = testData.get(testName);
		
		// Navigate to RLI list view
		sugar().revLineItems.navToListView();

		// Check select box for one record and select "Mass Update" action
		sugar().revLineItems.listView.checkRecord(1);
		sugar().revLineItems.listView.openActionDropdown();
		sugar().revLineItems.listView.massUpdate();
		
		for(int i=0 ; i < massUpdateOptions.size() ; i++) {
			
			// Setting options such as 'Support Starts', 'Support Expires', 'Purchased' and 'Book Value Date'
			sugar().revLineItems.massUpdate.getControl("massUpdateField02").set(massUpdateOptions.get(i).get("option"));
			VoodooUtils.waitForReady();
			
			// Verifying that Calendar icon is visible in the "mass update value" for the above date type fields 
			// TODO: VOOD-1003
			new VoodooControl("i", "css", ".input-append.date .fa-calendar").assertVisible(true);
		}
	
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}