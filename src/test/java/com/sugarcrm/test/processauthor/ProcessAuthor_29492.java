package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29492 extends SugarTest {

	public void setup() throws Exception {
		// Log-In as an Admin
		sugar().login();
	}

	/**
	 * Verify the Field selector page is render properly for KB email template
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29492_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource kbFieldsData = testData.get(testName);

		// Create a process email template and click Save and Design button
		sugar().navbar.navToModule(sugar().processEmailTemplates.moduleNamePlural);
		sugar().processEmailTemplates.listView.create();
		sugar().processEmailTemplates.createDrawer.getEditField("name").set(testName);
		sugar().processEmailTemplates.createDrawer.getEditField("targetModule").set(kbFieldsData.get(0).get("fieldName"));
		sugar().processEmailTemplates.createDrawer.getControl("saveAndDesignButton").click();
		VoodooUtils.waitForReady();

		// On the design view, click the Subject Field Selector icon
		sugar().processEmailTemplates.designView.getControl("subjectSelector").click();
		VoodooUtils.waitForReady();

		String fieldSelectorTitle = kbFieldsData.get(1).get("fieldName");
		VoodooControl fieldSelectorHeaderPaneTitle = sugar().processEmailTemplates.searchSelect.getControl("moduleTitle");

		// Assert that the Field Selector drawer is visible
		fieldSelectorHeaderPaneTitle.assertEquals(fieldSelectorTitle, true);

		// Assert that all the fields for KB module have been loaded in the drawer
		// TODO: VOOD-1859 - Need Lib support for Fields selector of Process Email Template's Design View. 
		int kbFieldCount = new VoodooControl("tr", "css", ".layout_pmse_Emails_Templates .dataTable tr.single").countWithClass();
		for(int i = 1; i <= kbFieldCount; i++) {
			new VoodooControl("div", "css", ".layout_pmse_Emails_Templates .dataTable .single:nth-child("+ i +") td:nth-child(2) div[data-original-title=\'" + kbFieldsData.get(i+1).get("fieldName") +"\']").assertVisible(true);
		}

		// Cancel the Field Selector drawer
		VoodooControl cancelButton = new VoodooControl("a", "css", ".compose-varbook-headerpane.fld_cancel_button a");
		cancelButton.click();

		// Click the Field Selector icon in the email template body editor
		sugar().processEmailTemplates.designView.getControl("fieldsSelector").click();
		VoodooUtils.waitForReady();
		fieldSelectorHeaderPaneTitle.assertEquals(fieldSelectorTitle, true);

		// Assert that all the fields for KB module have been loaded in the drawer
		// TODO: VOOD-1859 - Need Lib support for Fields selector of Process Email Template's Design View. 
		for(int j = 1; j <= kbFieldCount; j++) {
			new VoodooControl("div", "css", ".layout_pmse_Emails_Templates .dataTable .single:nth-child("+ j +") td:nth-child(2) div[data-original-title=\'" + kbFieldsData.get(j+1).get("fieldName") +"\']").assertVisible(true);
		}

		// Cancel the Field Selector Drawer
		cancelButton.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}