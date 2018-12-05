package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28460 extends SugarTest {
	VoodooControl removeBtnCtrl, configureSaveBtnCtrl, cancelBtnCtrl;

	public void setup() throws Exception {
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that can add or remove or change language label in Language configure 
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28460_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Go to KB module, click on menu drop down to open Configure
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Define controls for the KB configuration page
		// TODO: VOOD-1762
		VoodooControl addBtnCtrl = new VoodooControl("button", "css", "[name='add']");
		VoodooControl languageCodeCtrl = new VoodooControl("input", "css", "#config-languagesCollapse div:nth-child(2) div div:nth-child(1) input");
		VoodooControl languageLabelCtrl = new VoodooControl("input", "css", "#config-languagesCollapse div:nth-child(2) div div:nth-child(2) input");
		removeBtnCtrl = new VoodooControl("button", "css", ".fld_languages div:nth-child(2).control-group [name='remove']");
		configureSaveBtnCtrl = new VoodooControl("a", "css", ".rowaction.btn.btn-primary[name='save_button']");
		cancelBtnCtrl = new VoodooControl("a", "css", ".config-header-buttons.fld_cancel_button a");

		// Click on + sign.	Enter 'gb' in left column, enter 'Chinese (Simplified)' in the right column. 
		addBtnCtrl.click();
		languageCodeCtrl.set(customData.get("languageCode"));
		languageLabelCtrl.set(customData.get("languageLabel"));

		// Save
		configureSaveBtnCtrl.click();
		VoodooUtils.waitForReady(30000); // Extra wait needed

		// Go to KB module, click on menu drop down to open Configure
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Verify that the new Language is added to the list and saved
		languageCodeCtrl.assertAttribute("value", customData.get("languageCode"), true);
		languageLabelCtrl.assertAttribute("value", customData.get("languageLabel"), true);

		// Cancel the Configuration drawer
		cancelBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Create a new KB and select new language created above and save it
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.getEditField("language").set(customData.get("languageLabel"));
		sugar().knowledgeBase.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();

		// Verify that the new KB with Language Chinese (Simplified) is shown in List view
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(testName, true);
		sugar().knowledgeBase.listView.getDetailField(1, "language").assertEquals(customData.get("languageLabel"), true);

		// Open Configure, click on - sign for above new language and save it
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");
		removeBtnCtrl.click();

		// Confirm warning that Articles in same language will be deleted
		sugar().alerts.getWarning().confirmAlert();
		configureSaveBtnCtrl.click();
		VoodooUtils.waitForReady(30000); // Extra wait needed

		// Verify that on the list View, New KB is deleted
		sugar().knowledgeBase.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}