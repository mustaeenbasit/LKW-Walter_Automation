package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29380 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();

		// Log-In as admin
		sugar().login();
	}

	/**
	 * Verify the related field should work in Show Process (Approve/Reject/Route form) for BWC modules
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29380_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Importing Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create a new quote to trigger the process
		sugar().quotes.create();

		// Go to My Process > Show Process
		sugar().processes.navToListView();
		// TODO: VOOD-1706
		sugar().processes.listView.editRecord(1); // same control for the 'Show process'

		VoodooUtils.focusFrame("bwc-frame");
		// Edit Billing contact name, Type in a partial name
		String contactName = sugar().contacts.defaultData.get("fullName");
		VoodooControl billingContactNameCtrl = new VoodooControl("input", "id", "billing_contact_name");
		billingContactNameCtrl.set(contactName.substring(4, 6));
		VoodooUtils.waitForReady();

		// Verify typeahead works
		billingContactNameCtrl.assertEquals(contactName, true);

		// Click on the arrow and select a name
		// TODO: VOOD-776
		new VoodooControl("button", "css", "#btn_billing_contact_name").click();
		VoodooUtils.focusWindow(1);
		new VoodooControl("input", "id", "search_form_clear").click();
		new VoodooControl("input", "id", "search_form_submit").click();
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-child(1) a").click();
		VoodooUtils.focusWindow(0);

		VoodooUtils.focusFrame("bwc-frame");
		// Verify it will fill in the related field after selection.
		billingContactNameCtrl.assertEquals(contactName, true);

		// Cancel
		new VoodooControl("input", "css", "[name='Cancel']").click();
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}