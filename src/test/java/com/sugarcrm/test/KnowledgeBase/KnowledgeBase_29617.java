package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29617 extends SugarTest {
	VoodooControl langSaveButton,addbutton,keyLanguage,languageName;
	DataSource kbData = new DataSource();

	public void setup() throws Exception {
		kbData = testData.get(testName);
		sugar().login();

		// Enable the KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Select the Settings option
		sugar().navbar.selectMenuItem(sugar().knowledgeBase,"configure");

		// Adding a new localization language
		// TODO: VOOD-1762 : Need library support for adding/removing languages in Knowledge Base
		addbutton = new VoodooControl("button", "css", ".edit.fld_languages button[name='add']");
		keyLanguage = new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) input[name='key_languages']");
		languageName = new VoodooControl("input", "css", ".edit.fld_languages div:nth-child(2) div:nth-of-type(2) input[name='value_languages']");
		addbutton.click();
		keyLanguage.set(kbData.get(0).get("langKey"));
		languageName.set(kbData.get(0).get("langValue"));

		// Save the language
		langSaveButton = new VoodooControl("a", "css", ".layout_KBContents.drawer.active a[name='save_button']");
		langSaveButton.click();
		sugar().alerts.getSuccess().closeAlert();

		// Page takes more than adequate time to load
		VoodooUtils.waitForReady(30000);
	}

	/**
	 * Verify that KB articles are deleted when the relevant Language is deleted 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29617_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create 1 Localization
		sugar().knowledgeBase.listView.create();
		VoodooControl nameCtrl = sugar().knowledgeBase.createDrawer.getEditField("name");
		nameCtrl.set(kbData.get(0).get("langValue"));
		sugar().knowledgeBase.createDrawer.showMore();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.createDrawer.getEditField("language").set(kbData.get(0).get("langValue"));
		sugar().knowledgeBase.createDrawer.save();
		sugar().knowledgeBase.listView.openRowActionDropdown(1);

		// TODO: VOOD-568 : Library support for all of the actions in the RowActionDropdown
		new VoodooControl("a", "css", ".list.fld_create_localization_button a").click();
		VoodooUtils.waitForReady();
		nameCtrl.set(testName);
		sugar().knowledgeBase.createDrawer.save();

		// Total No. of Rows is equal to 2
		int rowCount = sugar().knowledgeBase.listView.countRows();
		Assert.assertTrue("Total number of Rows not equal to 2", rowCount == 2);

		// Verify both records are shown one having language English and other as Russian
		sugar().knowledgeBase.listView.verifyField(1, "language", kbData.get(1).get("langValue"));
		sugar().knowledgeBase.listView.verifyField(2, "language", kbData.get(0).get("langValue"));

		// Select the Settings option
		sugar().navbar.selectMenuItem(sugar().knowledgeBase,"configure");
		new VoodooControl("button", "css", ".edit.fld_languages button[name='remove'][data-index='1']").click();
		sugar().alerts.getWarning().confirmAlert();
		langSaveButton.click();

		// Page takes more than adequate time to load
		VoodooUtils.waitForReady(30000);

		// Verify only record available is having language as Russian
		int totalRowCount = sugar().knowledgeBase.listView.countRows();
		Assert.assertTrue("Total number of Rows not equal to 1", totalRowCount == 1);
		sugar().knowledgeBase.listView.verifyField(1, "language", kbData.get(1).get("langValue"));

		// Adding a new localization language
		// TODO: VOOD-1762 : Need library support for adding/removing languages in Knowledge Base
		sugar().navbar.selectMenuItem(sugar().knowledgeBase,"configure");
		addbutton.click();
		keyLanguage.set(kbData.get(2).get("langKey"));
		languageName.set(kbData.get(2).get("langValue"));

		// Save the Chinese language
		langSaveButton.click();

		// Page takes more than adequate time to load
		VoodooUtils.waitForReady(30000);	

		// Navigate to Settings and remove the first language(English)
		sugar().navbar.selectMenuItem(sugar().knowledgeBase,"configure");
		new VoodooControl("button", "css", ".edit.fld_languages button[name='remove'][data-index='0']").click();
		sugar().alerts.getWarning().confirmAlert();
		langSaveButton.click();

		// Page takes more than adequate time to load
		VoodooUtils.waitForReady(30000);

		// Verify KB Listview is Empty
		sugar().knowledgeBase.listView.getControl("emptyListViewMsg").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}