package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Leads_22665 extends SugarTest {
	FieldSet myNote;

	public void setup() throws Exception {
		myNote = testData.get("Leads_22665").get(0);
		sugar().login();
		sugar().leads.api.create();
	}

	/**
	 * Note is not created in the "Notes" sub-panel of a Lead when "Cancel" in
	 * note creation form
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_22665_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		// TODO: VOOD-598. Once it is fixed, please update the following steps
		new VoodooControl(
				"a",
				"css",
				".filtered.tabbable.tabs-left.layout_Notes .subpanel-controls .fld_create_button.small")
				.click();

		sugar().notes.createDrawer.setFields(myNote);
		sugar().notes.createDrawer.cancel();

		// Verify the Note record is not created and linked in Notes sub panel
		sugar().leads.recordView.subpanels.get("Notes");
		// TODO: VOOD-598. Once it is fixed, please update the following steps
		new VoodooControl("a", "css",
				"div.filtered.tabbable.tabs-left.layout_Notes").assertContains(
				myNote.get("subject"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
