package com.sugarcrm.test.tags;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Tags_28623 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * [Tags] Verify user is able to view tags associated to a record on sub-panel list views
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28623_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin -> Studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// Select any sidecar modules, such as Accounts > Sub-panels > Calls
		// TODO: VOOD-542 and VOOD-1511
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "subpanelsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Buttons tr td tr:nth-child(2) a.studiolink").click();
		VoodooUtils.waitForReady();

		// Drag and drop "Tags" column from Hidden column to Default column
		new VoodooControl("li", "css", "#Hidden .draggable[data-name='tag']").dragNDrop(new VoodooControl("li", "id", "topslot0"));

		// Click "Save & Deploy"
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();

		// Go to Calls module -> Create a new Call -> for Related To field select Accounts module and select any existing Account 
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		sugar().calls.createDrawer.showMore();
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.getEditField("relatedToParentType").set(sugar().accounts.moduleNameSingular);
		sugar().calls.createDrawer.getEditField("relatedToParentName").set(sugar().accounts.getDefaultData().get("name"));

		// Enter "call1" for Tags field and click "Save"
		String tagName = testName + "_" + sugar().calls.moduleNamePlural;
		sugar().calls.createDrawer.getEditField("tags").set(tagName);
		sugar().calls.createDrawer.save();
		if (sugar().alerts.getSuccess().queryVisible())
			sugar().alerts.getSuccess().closeAlert();

		// Navigate to Accounts module -> open the Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Verify that in Account record -> Calls sub-panel, verify the "Tags" column exist and newly created Call has a Tag
		StandardSubpanel callsModuleCtrl = sugar().accounts.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		callsModuleCtrl.expandSubpanel();
		callsModuleCtrl.getDetailField(1, "tags").assertEquals(tagName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}