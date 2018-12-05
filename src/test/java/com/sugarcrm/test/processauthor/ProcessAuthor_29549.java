package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29549 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();

		// Log-In as admin
		sugar().login();

		// Import a Process Definition -> with an Action element to the design page -> and Action type -> Assign User
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();
	}

	/**
	 * Verify the help text for the "Assign to" user search function in PA 
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29549_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Imported Process Definition design page
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("design01").click();
		VoodooUtils.waitForReady();

		// Right click -> Action type -> Setting
		// TODO: VOOD-1369 and VOOD-1539
		new VoodooControl("div", "css", "#jcore_designer .custom_shape.adam_activity").rightClick();
		new VoodooControl("a", "css", "#jcore_designer .adam-menu .adam-list li:nth-child(1) a").click();
		VoodooUtils.waitForReady();

		FieldSet processInfo = testData.get(testName).get(0);

		// Verify that the widget should say "Select..."
		// TODO: VOOD-1369 and VOOD-1539
		new VoodooControl("label", "css", ".adam-window .adam-window-header label").assertEquals(processInfo.get("headerName"), true);
		new VoodooControl("span", "css", ".adam-window .adam-window-body .adam-field div span").assertEquals(processInfo.get("selectText"), true);

		// Click on the Cancel button of opened pop up
		// TODO: VOOD-1369 and VOOD-1539
		new VoodooControl("a", "css", ".adam-window .adam-panel-footer a:nth-child(2) .adam-button-label").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}