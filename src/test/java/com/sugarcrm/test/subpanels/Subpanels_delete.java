package com.sugarcrm.test.subpanels;
import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_delete extends SugarTest {
	StandardSubpanel callsSubpanel;
	
	public void setup() throws Exception {
		sugar().leads.api.create();
		CallRecord myCall = (CallRecord)sugar().calls.api.create();
		sugar().login();

		// Lead with call
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		callsSubpanel = sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsSubpanel.linkExistingRecord(myCall);
	}

	@Test
	public void Subpanels_delete_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Delete the record using the UI.
		callsSubpanel.clickLink(sugar().calls.getDefaultData().get("name"), 1);
		sugar().calls.recordView.delete();
		sugar().alerts.getAlert().confirmAlert();
		
		// Verify the record was deleted from its list view as well as from sub panel
		sugar().calls.listView.assertIsEmpty();
		
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		Assert.assertTrue("The subpanel is not empty.", callsSubpanel.countRows()==0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}