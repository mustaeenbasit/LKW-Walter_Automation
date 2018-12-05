package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_19081 extends SugarTest {
	CallRecord myCall;
	
	public void setup() throws Exception {
		sugar.login();
		myCall = (CallRecord) sugar.calls.api.create();
		myCall.navToRecord();
	}

	/**
	 * Verify that duplicating a call can be cancelled.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_19081_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		sugar.calls.recordView.copy();
		sugar.calls.recordView.getEditField("name").set(ds.get(0).get("name"));
		VoodooUtils.waitForAlertExpiration(); // to prevent Unsaved changes warning to appear when cancel too quickly
		sugar.calls.createDrawer.cancel();
		
		sugar.calls.navToListView();
		
		// Verify that only one call exists in the list view 
		sugar.calls.listView.getControl("favoriteStar01").assertExists(true);
		sugar.calls.listView.getControl("favoriteStar02").assertExists(false);			
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}