package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.Alert;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_30679_pet extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that user can import .pet file via Import Process Definition
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_30679_pet_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl browseFile = new VoodooFileField("input", "css", ".fld_emailtemplates_import.edit input");
		FieldSet versionInfo = testData.get(testName).get(0);

		// Navigate to the Process Email Templates Module and Import pet file i.e exported from ver. 7.6.2.0
		sugar().navbar.selectMenuItem(sugar().processEmailTemplates, "importProcessEmailTemplates");
		browseFile.set("src/test/resources/data/" + testName + versionInfo.get("v3") + ".pet");

		// TODO: VOOD-1936
		VoodooControl importButton = new VoodooControl("a", "css", "span[data-voodoo-name='emailtemplates_finish_button'] a");
		importButton.click();
		sugar().alerts.getWarning().getControl("confirmAlert").click();

		// Assert the Success message
		sugar().alerts.getSuccess().assertContains(versionInfo.get("successMessage"), true);

		// Import pet file i.e exported from ver. 7.6.1.0
		Alert errorAlert = sugar().alerts.getError();
		sugar().navbar.clickModuleDropdown(sugar().processEmailTemplates);
		sugar().processEmailTemplates.menu.getControl("importProcessEmailTemplates").click();
		browseFile.set("src/test/resources/data/" + testName + versionInfo.get("v2") + ".pet");
		importButton.click();

		// Assert the error message
		errorAlert.assertContains(versionInfo.get("errorMessage"), true);

		// Import pet file i.e exported from ver. 7.6.0.0
		sugar().navbar.clickModuleDropdown(sugar().processEmailTemplates);
		sugar().processEmailTemplates.menu.getControl("importProcessEmailTemplates").click();
		browseFile.set("src/test/resources/data/" + testName + versionInfo.get("v1") + ".pet");
		importButton.click();

		// Assert the error message
		errorAlert.assertContains(versionInfo.get("errorMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}