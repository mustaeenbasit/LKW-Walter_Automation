package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_31217 extends SugarTest {
	public void setup() throws Exception {
		// Creating test KB record
		sugar().knowledgeBase.api.create();

		// Login with Admin
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		
		// Navigate to system settings
		sugar().admin.navToSystemSettings();
		VoodooUtils.focusFrame("bwc-frame");

		// Enable the Preview Editing checkbox
		// TODO: VOOD-1903 Additional System Settings support
		new VoodooControl("input", "css", "input[name='preview_edit'].checkbox").click();

		// Save change settings
		sugar().admin.systemSettings.getControl("save").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that record should not be saved from preview pane if Expiration date is less than Published date.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_31217_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Initializing test data
		FieldSet customData = testData.get(testName).get(0);
		
		// Navigate to the Knowledge Base module
		sugar().knowledgeBase.navToListView();

		// Click on Preview icon
		sugar().knowledgeBase.listView.previewRecord(1);
		
		// TODO: VOOD-2064 Need Lib support for Preview Pane's Edit View controls.
		new VoodooControl("i", "css", "i[class='fa fa-pencil']").click();
		VoodooUtils.waitForReady();

		// Click on 'Show more...' on preview pane
		new VoodooControl("button", "css", ".preview-pane .more").click();
		VoodooUtils.waitForReady();
		
		// Enter Expiration date lesser than Published date 
		new VoodooControl("input", "css", ".fld_active_date.edit input").set(sugar().knowledgeBase.getDefaultData().get("date_expiration"));
		
		// Enter Published date greater than Expiration date
		new VoodooControl("input", "css", ".fld_exp_date.edit input").set(sugar().knowledgeBase.getDefaultData().get("date_publish"));
		
		// Click 'Save' button
		VoodooControl saveBtn = new VoodooControl("a", "css", ".preview-header.fld_save_button a");
		saveBtn.click();
		
		// Verify that record is not saved i.e. 'Save' button is still visible
		saveBtn.assertVisible(true);
		
		// Verify that 'Publish Date' input box shows error (i.e. red in color)
		new VoodooControl("span", "css", ".fld_active_date").assertAttribute("class", "error");
		
		// Hover over 'i' icon in 'Publish Date' input box
		new VoodooControl("i", "css", ".preview-pane .fa-exclamation-circle").hover();
		
		// Verify error message is displayed on mousehover on Publish Date
		new VoodooControl("div", "css", ".tooltip-inner").assertEquals(customData.get("errorMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}