package com.sugarcrm.test.processauthor;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ProcessAuthor_29406 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the Status field is available in Process Definition record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29406_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource statusDS = testData.get(testName);
		sugar().navbar.selectMenuItem(sugar().processDefinitions, "createProcessDefinition");

		// Verify "Status" Field is shown in the record view and is set to "Disabled" by default.
		sugar().processDefinitions.createDrawer.getEditField("status").assertEquals(statusDS.get(1).get("status"), true);
		sugar().processDefinitions.createDrawer.cancel();

		// Import Process definition => Lead start event (new record) -> activity > Static Assignment > End event
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/"+testName+".bpm");

		// Enable Imported Process Definition
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Verify the Status is show "Enabled" in list view.
		sugar().processDefinitions.listView.verifyField(1, "status", statusDS.get(0).get("status"));

		// Click row action drop down and select "Design" to open designer canvas.
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("design01").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1539
		new VoodooControl("button", "css", ".btn-close-designer").click();
		VoodooUtils.waitForReady();

		// Verify it will bring back to record view
		sugar().processDefinitions.recordView.assertVisible(true);

		// Trigger the process by new lead record
		sugar().leads.create();

		// Verify Process is working properly (Process Definition + Record name)
		sugar().processes.navToListView();
		sugar().processes.myProcessesListView.verifyField(1, "pr_definition", testName);
		sugar().processes.myProcessesListView.verifyField(1, "rec_name", sugar().leads.getDefaultData().get("fullName"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}