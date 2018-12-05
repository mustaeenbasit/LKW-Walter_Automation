package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29089 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().cases.api.create();
		AccountRecord accRecord = (AccountRecord) sugar().accounts.api.create();
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().knowledgeBase);

		// TODO: VOOD-444 - Once resolved account link via Case API is possible
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		sugar().cases.recordView.edit();
		sugar().cases.recordView.getEditField("relAccountName").set(accRecord.getRecordIdentifier());
		sugar().cases.recordView.save();
	}

	/**
	 * Verify that body field is displayed correctly when adding it in listview of KB
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29089_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate to admin >> studio >> KB >> List view
		// TODO: VOOD-1507: Support Studio Module ListView Layouts View 
		VoodooControl studioLinkKB = new VoodooControl("a", "id", "studiolink_KBContents");
		VoodooControl layoutsButton = new VoodooControl("a", "css", "#layoutsBtn a");
		VoodooControl kbBody = new VoodooControl("li", "css", "[data-name='kbdocument_body']");
		VoodooControl language = new VoodooControl("li", "css", "[data-name='language']");
		VoodooControl saveAndDeployBtn = new VoodooControl("input", "id", "savebtn");       

		studioLinkKB.click();
		VoodooUtils.waitForReady();
		layoutsButton.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnlistview a").click();
		VoodooUtils.waitForReady();

		// Dragging and dropping "Knowledge Base Body" field from Hidden column to Default column
		kbBody.dragNDrop(language);
		saveAndDeployBtn.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Goto KB module and select edit for first item on list view
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.editRecord(1);

		// Verify that the Body field editing is disabled
		// TODO: VOOD-1445: Need lib support for enhanced disabled check in parent controls of a child
		VoodooControl bodyFieldControl = new VoodooControl("td", "css", "[data-type='htmleditable_tinymce'] span");
		Assert.assertTrue(bodyFieldControl.isDisabled());

		// Hovering over the body field in subpanel
		// TODO: VOOD-1489 : Need Library Support for All fields moved from Hidden to Default for All Modules
		VoodooControl bodyTextControl = new VoodooControl("div", "css", ".fld_kbdocument_body div");
		bodyTextControl.hover();

		// Verify tooltip displays kb body text
		// TODO: VOOD-1292
		VoodooControl bodyTooltip = new VoodooControl("div", "css", ".tooltip-inner");
		bodyTooltip.assertEquals(sugar().knowledgeBase.getDefaultData().get("body"), true);

		sugar().knowledgeBase.listView.cancelRecord(1);

		// Navigate to admin >> studio >> Cases >> Subpanels >> KB
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		VoodooControl studioLinkCases = new VoodooControl("a", "id", "studiolink_Cases");
		VoodooControl subpanelsButton = new VoodooControl("a", "css", "td#subpanelsBtn a");

		// TODO: VOOD-1511: Support Studio Module Subpanels Layout View 
		VoodooControl kbButton = new VoodooControl("a", "css", "#Buttons tr:nth-child(2) td:nth-child(2) a");
		studioLinkCases.click();
		VoodooUtils.waitForReady();
		subpanelsButton.click();
		VoodooUtils.waitForReady();
		kbButton.click();
		VoodooUtils.waitForReady();

		// Dragging and dropping "Knowledge Base Body" field from Hidden column to Default column
		kbBody.dragNDrop(language);
		saveAndDeployBtn.click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// Navigating to Cases module and clicking first record
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		StandardSubpanel kbSubpanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);
		kbSubpanel.scrollIntoViewIfNeeded(false);

		// TODO: RS-1029 - As 'V' on subpanel icon is not clickable therefore using below work around
		// to click the link existing record option
		new VoodooControl("a", "css", ".layout_KBContents .btn.dropdown-toggle").click();
		new VoodooControl("a", "css", ".layout_KBContents .fld_select_button a").click();

		sugar().knowledgeBase.searchSelect.selectRecord(1);
		sugar().knowledgeBase.searchSelect.link();

		// Inline editing the record in KB subpanel
		kbSubpanel.editRecord(1);

		// Verify that the Body field editing is disabled
		Assert.assertTrue("KB subpanel is enabled", bodyFieldControl.isDisabled());

		// Hovering over the body field in subpanel
		bodyTextControl.hover();

		// Verify tooltip displays kb body text
		bodyTooltip.assertEquals(sugar().knowledgeBase.getDefaultData().get("body"), true);
		kbSubpanel.cancelAction(1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}