package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
public class KnowledgeBase_28866 extends SugarTest {
	VoodooControl moduleCtrl,layoutCtrl,recordViewCtrl,saveBtnCtrl;

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to Admin > studio > KnowledgeBase > Layout > Remove "Team" from record view
		// TODO: VOOD-542 and VOOD-1506
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		moduleCtrl = new VoodooControl("a", "id", "studiolink_KBContents");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		recordViewCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "[data-name='team_name']").dragNDrop(new VoodooControl("div", "css", "[data-name='description']"));
		saveBtnCtrl = new VoodooControl("td", "id", "publishBtn");
		saveBtnCtrl.click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Knowledge Base: Verify that preview and record view are synced
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28866_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KnowledgeBase list view and open preview panel
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.previewRecord(1);

		// Verify "Teams" field is not shown preview panel on Knowledge Base
		sugar().previewPane.showMore();
		sugar().previewPane.getPreviewPaneField("relTeam").assertVisible(false);
		sugar().previewPane.closePreview();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}