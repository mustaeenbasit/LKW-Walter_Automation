package com.sugarcrm.test.subpanels;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.NoteRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_28184 extends SugarTest {
	StandardSubpanel notesSubpanel;
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		NoteRecord myNote = (NoteRecord) sugar().notes.api.create();
		sugar().login();

		// Link Existing note record in the Accounts subpanel
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		notesSubpanel = sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.linkExistingRecord(myNote);
	}

	/**
	 * To verify subpanel under accounts has a link to the related records
	 * @throws Exception
	 */
	@Test
	public void Subpanels_28184_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio > Accounts > Subpanels
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();

		// TODO: VOOD-1511 Support Studio Module Subpanels Layout View
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		new VoodooControl("td", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();

		// Choose a subpanel such as Notes
		new VoodooControl("td", "css", "#Buttons tr:nth-child(1) td:nth-child(4)").click();
		VoodooUtils.waitForReady();
		VoodooControl defaultPanelCtrl = new VoodooControl("ul", "css", "#Default ul");
		VoodooControl hiddenPanelCtrl = new VoodooControl("ul", "css", "#Hidden ul");
		VoodooControl saveDeployCtrl = new VoodooControl("input", "id", "savebtn");
		VoodooControl nameDefaultFieldCtrl = new VoodooControl("li", "css", "#Default .draggable[data-name='name']");
		VoodooControl nameHiddenFieldCtrl = new VoodooControl("li", "css", "#Hidden .draggable[data-name='name']");

		// Move fields from Default column to Hidden column
		nameDefaultFieldCtrl.dragNDropViaJS(hiddenPanelCtrl);

		// Save and Deploy
		saveDeployCtrl.click();
		VoodooUtils.waitForReady();

		// Verify name field is not in Default column
		nameDefaultFieldCtrl.assertExists(false);
		nameHiddenFieldCtrl.assertExists(true);

		// Add the Name field back to the Default Column
		nameHiddenFieldCtrl.dragNDropViaJS(defaultPanelCtrl);

		// Save and Deploy
		saveDeployCtrl.click();
		VoodooUtils.waitForReady();

		// Verify name field is in Default column
		nameDefaultFieldCtrl.assertExists(true);
		nameHiddenFieldCtrl.assertExists(false);
		VoodooUtils.focusDefault();

		// Go to Accounts module
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Verify Notes Subpanel have a link to the related records.
		// TODO: SC-5488, Remove L#85 & Uncomment L#84 once this bug is resolved.
		// notesSubpanel.scrollIntoView();
		notesSubpanel.expandSubpanel();
		VoodooControl horizontalScrollCtrl = new VoodooControl("div", "css", "[data-voodoo-name='Notes'] div.flex-list-view-content");
		notesSubpanel.scrollIntoViewIfNeeded(horizontalScrollCtrl, false);

		// Verify that on clicking the link user will navigate to notes recordView.
		notesSubpanel.getDetailField(1, "subject").click();
		sugar().notes.recordView.getDetailField("subject").assertContains(customData.get("subject"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}