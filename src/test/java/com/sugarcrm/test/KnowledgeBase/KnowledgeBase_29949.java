package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29949 extends SugarTest {
	VoodooControl moduleCtrl,subpanelBtn,notesSubpanelCtrl,saveBtnCtrl,studioCtrl;
	NoteRecord notesRecord;

	public void setup() throws Exception {
		notesRecord = (NoteRecord) sugar().notes.api.create();
		sugar().contacts.api.create();
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enabling KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigating to Admin-> Studio-> Knowledge Base-> Sub-panels-> Notes and adding Related To field to Default
		// TODO: VOOD-542
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();
		moduleCtrl = new VoodooControl("a", "id", "studiolink_KBContents");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1511
		subpanelBtn = new VoodooControl("td", "id", "subpanelsBtn");
		subpanelBtn.click();
		VoodooUtils.waitForReady();
		notesSubpanelCtrl = new VoodooControl("a", "css", "#Buttons tr td:nth-child(3) a");
		notesSubpanelCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl drophereCtrl = new VoodooControl("li", "css",  "[data-name='date_modified']");
		new VoodooControl("li", "css", "[data-name='parent_name']").dragNDrop(drophereCtrl);
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that "Related To" field should not be shown blank in Notes module and in Notes sub-panel of Knowledge Base.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29949_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Adding value in Related to field on notes record view
		sugar().notes.navToListView();
		sugar().notes.listView.clickRecord(1);
		sugar().notes.recordView.edit();
		sugar().notes.recordView.getEditField("relRelatedToModule").set(sugar().contacts.moduleNameSingular);
		sugar().notes.recordView.getEditField("relRelatedToValue").set(sugar().contacts.getDefaultData().get("lastName"));
		sugar().notes.recordView.save();

		// Asserting the value of related to field in notes record view 
		VoodooControl relatedToCtrl = sugar().notes.recordView.getDetailField("relRelatedToValue");
		relatedToCtrl.assertEquals(sugar().contacts.getDefaultData().get("fullName"), true);

		// Navigating to KB list view and link existing notes record in Notes subpanel
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		StandardSubpanel notesSubpanel = sugar().knowledgeBase.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.linkExistingRecord(notesRecord);

		// Asserting the value of Related to field in Notes subpanel of KB
		String kbName = sugar().knowledgeBase.getDefaultData().get("name");
		notesSubpanel.getDetailField(1, "relRelatedToValue").assertEquals(kbName, true);

		// Navigating to notes record view
		notesSubpanel.getDetailField(1, "subject").click();

		// Asserting the value of Related to field in Notes record view
		relatedToCtrl.assertEquals(kbName, true);

		// Asserting the value of Related to field in Notes List view
		sugar().notes.navToListView();
		sugar().notes.listView.getDetailField(1, "relRelatedToValue").assertEquals(kbName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}