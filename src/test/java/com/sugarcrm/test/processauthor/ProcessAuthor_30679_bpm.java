package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_30679_bpm extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that user can import bpm file via Import Process Definition
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_30679_bpm_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl browseFile = new VoodooFileField("input", "css", ".fld_project_import.edit input");
		FieldSet versionInfo = testData.get(testName).get(0);

		// Navigate to the Process Definitions Module and Import bpm file i.e exported from ver. 7.6.2.0
		sugar().navbar.selectMenuItem(sugar().processDefinitions, "importProcessDefinition");
		browseFile.set("src/test/resources/data/" + testName + versionInfo.get("v3") + ".bpm");

		// TODO: VOOD-1365
		VoodooControl importButton = new VoodooControl("a", "css", "span[data-voodoo-name='project_finish_button'] a");
		importButton.click();
		sugar().alerts.getWarning().getControl("confirmAlert").click();

		// Assert the Success message
		sugar().alerts.getSuccess().assertContains(versionInfo.get("successMessage"), true);

		// Import bpm file i.e exported from ver. 7.6.1.0
		Alert errorAlert = sugar().alerts.getError();
		sugar().navbar.clickModuleDropdown(sugar().processDefinitions);
		sugar().processDefinitions.menu.getControl("importProcessDefinition").click();
		browseFile.set("src/test/resources/data/" + testName + versionInfo.get("v2") + ".bpm");
		importButton.click();

		// Assert the error message
		errorAlert.assertContains(versionInfo.get("errorMessage"), true);

		// Import bpm file i.e exported from ver. 7.6.0.0
		sugar().navbar.clickModuleDropdown(sugar().processDefinitions);
		sugar().processDefinitions.menu.getControl("importProcessDefinition").click();
		browseFile.set("src/test/resources/data/" + testName + versionInfo.get("v1") + ".bpm");
		importButton.click();

		// Assert the error message
		errorAlert.assertContains(versionInfo.get("errorMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}