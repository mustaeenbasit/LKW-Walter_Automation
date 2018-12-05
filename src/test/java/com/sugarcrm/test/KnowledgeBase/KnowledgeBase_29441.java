package com.sugarcrm.test.KnowledgeBase;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.KBRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29441 extends SugarTest {
	VoodooControl moduleCtrl, layoutCtrl, listViewCtrl, saveBtnCtrl;
	StandardSubpanel kbSubpanel;

	public void setup() throws Exception {
		// Create a KB record with default data
		KBRecord kbRecord = (KBRecord) sugar().knowledgeBase.api.create();
		sugar().cases.api.create();

		// Login as admin
		sugar().login();
		
		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Link the KB record to Case record
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		kbSubpanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);
		kbSubpanel.scrollIntoViewIfNeeded(false);
		// TODO: RS-1029 - Non-standard class for addRecord (+ button) and link Existing record link in KB sub panel 
		// of Case record view hinders automation
		// Uncomment below line# 30, when RS-1029 gets resolved and remove line #31, #32, #33 and #34 
		// kbSubpanel.linkExistingRecord(kbRecord);
		kbSubpanel.getControl("expandSubpanelActions").click();
		new VoodooControl("a", "css", ".fld_select_button.panel-top-for-cases a").click();
		sugar().knowledgeBase.searchSelect.selectRecord(1);
		sugar().knowledgeBase.searchSelect.link();

		// Define controls for Knowledge Base in studio
		// TODO: VOOD-542
		moduleCtrl = new VoodooControl("a", "id", "studiolink_KBContents");

		// TODO: VOOD-1506
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		listViewCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		saveBtnCtrl = new VoodooControl("input", "id", "savebtn");

		// Admin add "Revision" in KB list view from studio
		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Studio -> KB -> Layouts -> List View
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		listViewCtrl.click();
		VoodooUtils.waitForReady();

		// Enable "Revision" 
		new VoodooControl("li", "css","#Available li[data-name='revision']").dragNDrop(new VoodooControl("td", "id", "Default"));
		VoodooUtils.waitForReady();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that "Revision" should not allow to be edited in KB list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29441_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to KB list view, Select any KB record and edit it
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.editRecord(1);
		sugar().knowledgeBase.listView.getEditField(1, "name").set(testName);

		// Verify that the "Revision" is UN-editable
		// TODO: VOOD-1036, VOOD-1445
		VoodooControl revisionFieldCtrl = new VoodooControl("span", "css", ".list.fld_revision");
		revisionFieldCtrl.assertAttribute("class", "edit", false);

		// Click on Save
		sugar().knowledgeBase.listView.saveRecord(1);

		// Open a Case
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// Click on edit of a KB record in KB sub panel.
		kbSubpanel.editRecord(1);

		// Verify that the "Revision" is UN-editable
		revisionFieldCtrl.assertAttribute("class", "edit", false);

		// Cancel
		kbSubpanel.cancelAction(1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}