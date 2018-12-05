package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29652 extends SugarTest {
	VoodooControl studioLink,accountBtnCtrl;

	public void setup() throws Exception {
		// Log-In as admin
		sugar().login();

		// Navigate to Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// Click on studio link  
		studioLink = sugar().admin.adminTools.getControl("studio");
		studioLink.click();
		VoodooUtils.waitForReady();

		// Click on Accounts in studio panel
		// TODO: VOOD-1506
		accountBtnCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Move to Accounts RecordView
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		// Select Tabs type View for "Business Card"
		new VoodooControl("select", "css", "#panels div:nth-child(2) span:nth-child(3) select").click();
		new VoodooControl("option", "css", "#panels div:nth-child(2) span:nth-child(3) select option:nth-child(2)").click();
		VoodooUtils.waitForReady();

		// Select Tabs type View for "Show More"
		new VoodooControl("select", "css", "#panels div:nth-child(3) span:nth-child(3) select").click();
		new VoodooControl("select", "css", "#panels div:nth-child(3) span:nth-child(3) select option:nth-child(2)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * [Process Author] Verify the tab layout should be visible for any module in My Processes
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29652_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet processInfo = testData.get(testName).get(0);

		// Import the Process Definition 
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + processInfo.get("version") + ".bpm");
		sugar().processDefinitions.navToListView();
		sugar().processDefinitions.listView.openRowActionDropdown(1);

		// Enable the imported Process Definition
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Navigate to Accounts and create a Record to trigger the Process
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(sugar().accounts.getDefaultData().get("name"));
		sugar().accounts.createDrawer.save();

		// Navigate to Process List View
		sugar().processes.navToListView();

		// Edit method used here to click the showProcess option
		sugar.processes.listView.editRecord(1);

		// Verify Process record is shown in Tabs
		VoodooControl tabs = new VoodooControl("ul", "id", "recordTab");
		tabs.assertVisible(true);
		tabs.assertContains(processInfo.get("tab1"),true );
		tabs.assertContains(processInfo.get("tab2"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}