package com.sugarcrm.test.subpanels;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CallRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_update extends SugarTest {
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
	public void Subpanels_update_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet editedData = new FieldSet();
		editedData.put("name", "Call with Sugar");
		editedData.put("status", "Held");
		
		// Edit the record using the UI.
		callsSubpanel.editRecord(1, editedData);

		// Verify the record was edited.
		// TODO: VOOD-1424 - Once story resolved below code should use verify method
		callsSubpanel.getDetailField(1, "name").assertEquals(editedData.get("name"), true);
		callsSubpanel.getDetailField(1, "status").assertEquals(editedData.get("status"), true);
		callsSubpanel.getDetailField(1, "assignedTo").assertEquals("Administrator", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}