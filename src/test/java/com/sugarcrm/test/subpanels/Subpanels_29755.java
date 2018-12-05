package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_29755 extends SugarTest {
	CallRecord myCall;
	StandardSubpanel callsSubpanel;
	
	public void setup() throws Exception {
		sugar.contacts.api.create();
		myCall = (CallRecord) sugar.calls.api.create();
		sugar.login();
		
		// Linking the call record with the contact record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		callsSubpanel = sugar.leads.recordView.subpanels.get(sugar.calls.moduleNamePlural);
		callsSubpanel.linkExistingRecord(myCall);
	}

	/**
	 * Verify that Resolve Conflict drawer is not showing at Subpanels of All Sidecar modules
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_29755_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet status = testData.get(testName).get(0);
		
		// Inline edit the record for Status field & Save
		FieldSet editStatus1 = new FieldSet();
		editStatus1.put("status", status.get("editStatus1"));
		callsSubpanel.editRecord(1, editStatus1);
		
		// Again inline edit the same record for Status field & Save
		FieldSet editStatus2 = new FieldSet();
		editStatus2.put("status", status.get("editStatus2"));
		callsSubpanel.editRecord(1, editStatus2);
		
		// Verifying that Resolve Conflicts panel doesn't appears
		// TODO: VOOD-1558
		VoodooControl conflictPanel = new VoodooControl("span", "css", ".resolve-conflicts-headerpane.fld_title");
		conflictPanel.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}