package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24263 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Create Opportunity_Verify that opportunity is not duplicated when using cancel function
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24263_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to record view of created opportunities Record 
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		
		// Click "Copy" button in "Opportunity" detail view
		sugar().opportunities.recordView.copy();
		
		// Click "Cancel" button
		sugar().opportunities.createDrawer.cancel();
		
		// Verify "Opportunities" record view is displayed
		sugar().opportunities.recordView.getControl("moduleIDLabel").hover();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-2199 - Need Lib Support to assert Tooltip text on Tooltip control on a Sidecar view
		new VoodooControl("div", "css", ".tooltip .tooltip-inner").assertContains(sugar().opportunities.moduleNameSingular, true);
		sugar().opportunities.recordView.getDetailField("name").assertEquals(sugar().opportunities.getDefaultData().get("name"), true);

		// Verify copied opportunity is not created i.e. only one record exist in listview
		sugar().opportunities.navToListView();
		int rowCount = sugar().opportunities.listView.countRows();
		Assert.assertTrue("Record count in Opportunities listview is not equal to 1", rowCount == 1);
		
		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}