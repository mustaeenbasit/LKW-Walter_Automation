package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29471 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Warning Messages Should Be Displayed when to make changes with Active process definition in Process Author
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29471_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		// Importing Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + customData.get("version") + ".bpm");
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		Alert warningAlert = sugar().alerts.getWarning();
		warningAlert.confirmAlert();

		// Create a new contact to trigger the process
		sugar().contacts.create();

		// Go back to the process definition list view 
		sugar().processDefinitions.navToListView();

		// Select the newly created process definition > design 
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		VoodooControl designCtrl = sugar().processDefinitions.listView.getControl("design01");
		designCtrl.click();
		VoodooUtils.waitForReady();

		// Verify Warning Message
		warningAlert.assertContains(customData.get("warningMessage"), true);

		// Click Cancel
		warningAlert.cancelAlert();

		// Verify Click cancel will return to the process definition list view
		// TODO: VOOD-1887, Once this VOOD is resolved below 
		// line should suffice for the list view visibility check. 
		// sugar().processDefinitions.listView.assertVisible(true);
		sugar().processDefinitions.listView.getDetailField(1, "name").assertEquals(testName, true);

		// Again, Select the newly created process definition > design 
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		designCtrl.click();

		// Click confirm 
		warningAlert.confirmAlert();

		// Verify Click Confirm will go to the process design page
		// TODO: VOOD-1539 - Support PMSE Design Canvas/ toolbar buttons/ title
		new VoodooControl("div", "id", "jcore_designer").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}