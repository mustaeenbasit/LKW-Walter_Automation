package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29399 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *Verify the Imported Process Definitions should default set to disabled
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29399_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Importing Enabled Process Definition
		FieldSet customData = testData.get(testName).get(0);
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + customData.get("status1") + customData.get("version") + ".bpm");
		sugar().processDefinitions.navToListView();

		// Verify status of Enabled Process Definition
		sugar().processDefinitions.listView.getDetailField(1, "status").assertEquals(customData.get("status2").substring(1, 9), true);

		// Import Disabled Process Definition
		// TODO: VOOD-1979, Replace "Import Code" with below line once this VOOD is resolved.
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + customData.get("status2") + customData.get("version") + ".bpm");
		sugar().navbar.selectMenuItem(sugar().processDefinitions, "importProcessDefinition");
		new VoodooFileField("input", "css", ".fld_project_import.edit input").set("src/test/resources/data/" + testName + customData.get("status2") + customData.get("version") + ".bpm");
		// TODO: VOOD-1365
		new VoodooControl("a", "css", "span[data-voodoo-name='project_finish_button'] a").click();
		VoodooUtils.waitForReady();

		// Navigate to Process Definitions list view
		sugar().processDefinitions.navToListView();

		// Sort Process Definitions to verify the status accordingly
		sugar().processDefinitions.listView.sortBy("headerName", false);

		// Verify status of Disabled process definition
		sugar().processDefinitions.listView.getDetailField(2, "status").assertEquals(customData.get("status2").substring(1, 9), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}