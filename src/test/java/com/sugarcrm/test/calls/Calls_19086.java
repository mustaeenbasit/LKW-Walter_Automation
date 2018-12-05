package com.sugarcrm.test.calls;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import org.junit.Test;

public class Calls_19086 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		sugar.login();
		ds = testData.get(testName);

		// Create 5 call records in certain order
		for(int i=0;i<5 ;i++)
			sugar.calls.api.create(ds.get(i));
	}

	/**
	*  Search call_Verify that call search condition are cleared when clicking "Clear" button.
	*  
	*  @throws Exception
	*/
	@Test
	public void Calls_19086_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		// Search the call
		sugar.calls.listView.setSearchString(ds.get(0).get("name"));
		
		// Verify that one call is found
		sugar.calls.listView.verifyField(1, "name", ds.get(0).get("name"));
			
		// Clear Search from filter bar
		new VoodooControl("i","css",".filter-view.search.layout_Calls .fa.fa-times.add-on").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Verify that filter bar is cleared and all Calls are displayed in the list view
		for(int i=0, j=4; i < 5 ;i++, j--)
			sugar.calls.listView.verifyField((i+1), "name", ds.get(j).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}