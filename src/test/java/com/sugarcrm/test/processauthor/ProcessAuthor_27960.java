package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_27960 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify .pbr file can be imported for Business Rules
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_27960_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet errorMessage = testData.get(testName + "_error").get(0);
		sugar().navbar.selectMenuItem(sugar().processBusinessRules, "importBusinessRules");

		// Uploading non .pbr format file
		VoodooFileField browseToImport = new VoodooFileField("input", "css", "[name='businessrules_import']");
		browseToImport.set("src/main/resources/data/" + "AccountsModuleFields.csv");
		// TODO: VOOD-1936 - Implement Process Business Rule/ Process Email Templates Import views and methods
		VoodooControl import_Ctrl = new VoodooControl("a","css", "[name='businessrules_finish_button']");
		import_Ctrl.click();
		sugar().alerts.getWarning().confirmAlert();

		// Verifying proper error message is shown
		sugar().alerts.getError().assertEquals(errorMessage.get("errorMessage"), true);
		sugar().alerts.getError().closeAlert();

		//Click the cancel icon i.e beside the uploaded file 
		new VoodooControl("button", "css", ".fld_businessrules_import.edit.error button.fa-times-circle").click();

		// Upload .pbr format file
		browseToImport.set("src/test/resources/data/" + testName + ".pbr");
		VoodooUtils.waitForReady();
		import_Ctrl.click();

		// Success message is getting displayed
		sugar().alerts.getSuccess().assertVisible(true);
		sugar().processBusinessRules.listView.clickRecord(1);

		// Verify RecordView is displayed correctly.
		sugar().processBusinessRules.recordView.assertVisible(true);

		// Verifying that data is displayed correctly on Record view
		sugar().processBusinessRules.recordView.getDetailField("name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}