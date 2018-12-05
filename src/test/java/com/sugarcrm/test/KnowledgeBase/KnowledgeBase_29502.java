package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29502 extends SugarTest {
	DataSource kbData = new DataSource();

	public void setup() throws Exception {
		kbData = testData.get(testName);
		FieldSet status = new FieldSet();
		status.put("status", kbData.get(0).get("status"));

		// Create KB record with status = published
		sugar().knowledgeBase.api.create(status);

		// Log-In as an admin
		sugar().login();

		// Enable the KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify the related revision doc is displayed and updated correctly in subpanel after inline editing
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29502_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create 1 Revision
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		sugar().knowledgeBase.listView.openRowActionDropdown(1);

		// TODO: VOOD-568 - library support for all of the actions in the RowActionDropdown
		new VoodooControl("a", "css", ".list.fld_create_revision_button a").click();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.save();

		// Open the parent KB article to its Record view
		sugar().knowledgeBase.listView.clickRecord(1);

		// Expand Revisions Subpanel and inline edit the revision twice
		new VoodooControl("div", "css", ".closed div[data-voodoo-name='panel-top-for-revisions']").scrollIntoViewIfNeeded(true);		

		// TODO: VOOD-1760 - Need library support for Localizations and Revisions in KB
		VoodooControl rowActionDropDown = new VoodooControl("a", "css", "div[data-voodoo-name='subpanel-for-revisions'] .single td:nth-child(9) a .fa.fa-caret-down");
		VoodooControl editButton = new VoodooControl("a", "css", ".list.fld_edit_button a[name='edit_button']");
		VoodooControl kbNameEditField = new VoodooControl("input", "css", "[data-voodoo-name='subpanel-for-revisions'] .single td:nth-child(2) input[name='name']");
		VoodooControl inlineSaveButton = new VoodooControl("span", "css", "[data-voodoo-name='subpanel-for-revisions'] .single span.edit.fld_inline-save");

		for(int i = 0; i < kbData.size(); i++) {
			// Open the rowAction dropdown
			rowActionDropDown.click();

			// Click the Edit option
			editButton.click();

			// Change the KB revision name and save it
			kbNameEditField.set(kbData.get(i).get("revisionName"));
			inlineSaveButton.click();
			VoodooUtils.waitForReady();
		}

		// Assert that the Related revision doc is correctly updated and available in subpanel
		new VoodooControl("span", "css", "[data-voodoo-name='subpanel-for-revisions'] .single .fld_name.list").assertEquals(kbData.get(1).get("revisionName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}