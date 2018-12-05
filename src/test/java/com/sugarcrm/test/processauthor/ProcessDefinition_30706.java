package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessDefinition_30706 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();	
	}

	/**
	 * Verify that Route form should not becomes Approval/Reject form if Route is selected in BWC mdoule
	 * @throws Exception
	 */
	@Test
	public void ProcessDefinition_30706_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");

		// Navigate to process Definition list view and open the action drop down of the process definition record
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create a Quote Record
		sugar().quotes.create();

		// Navigating to process and Select Show Process
		sugar().processes.navToListView();
		sugar().processes.listView.openActionDropdown();

		// TODO: VOOD-1706
		new VoodooControl("a", "css", ".list.fld_edit_button a").click();
		VoodooUtils.waitForReady();
		FieldSet formData = testData.get(testName).get(0);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "RouteBtn").queryAttributeContains("value", formData.get("formType"));
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}