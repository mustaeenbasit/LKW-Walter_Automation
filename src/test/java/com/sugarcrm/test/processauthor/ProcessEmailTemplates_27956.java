package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooFileField;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Basant Chandak <bchandak@sugarcrm.com>
 */
public class ProcessEmailTemplates_27956 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify only .pet file can be import to Process Email Template
	 * @throws Exception
	 */
	@Test
	public void ProcessEmailTemplates_27956_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Process Email Templates import page
		FieldSet errorMessage = testData.get(testName + "_error").get(0);
		sugar().navbar.selectMenuItem(sugar().processEmailTemplates, "importProcessEmailTemplates");

		// Uploading non .pet format file
		VoodooFileField browseToImport = new VoodooFileField("input", "css", "[name='emailtemplates_import']");
		browseToImport.set("src/main/resources/data/" + "AccountsModuleFields.csv");
		VoodooControl import_Ctrl = new VoodooControl("a","css", "[name='emailtemplates_finish_button']");
		import_Ctrl.click();
		sugar().alerts.getAlert().getControl("confirmAlert").click();

		// Verifying proper error message is shown
		sugar().alerts.getError().assertEquals(errorMessage.get("errorMessage"), true);
		sugar().alerts.getError().closeAlert();

		// Upload .pet format file
		sugar().navbar.selectMenuItem(sugar().processEmailTemplates, "importProcessEmailTemplates");
		browseToImport.set("src/test/resources/data/" + testName + ".pet");
		VoodooUtils.waitForReady();
		import_Ctrl.click();

		// Success message is getting displayed
		sugar().alerts.getSuccess().assertVisible(true);
		sugar().processEmailTemplates.listView.clickRecord(1);

		// Verify RecordView is displayed correctly.
		sugar().processEmailTemplates.recordView.assertVisible(true);

		// Verifying that data is displayed correctly on Record view
		sugar().processEmailTemplates.recordView.getDetailField("name").assertEquals(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}