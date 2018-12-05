package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Studio_30195 extends SugarTest {
	VoodooControl accountsCtrl, subpanelsCtrl, saveDeployCtrl, meetingsSubpanelCtrl, restoreDefaultCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Restore Default functionality for subpanels in Studio
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_30195_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio > Accounts > Subpanels
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1511
		accountsCtrl= new VoodooControl("a", "id", "studiolink_Accounts");
		accountsCtrl.click();
		VoodooUtils.waitForReady();
		subpanelsCtrl=new VoodooControl("td", "id", "subpanelsBtn");
		subpanelsCtrl.click();
		VoodooUtils.waitForReady();

		// Choose a subpanel such as Meetings
		meetingsSubpanelCtrl = new VoodooControl("td", "css", "#Buttons tr:nth-child(1) td:nth-child(2)");
		meetingsSubpanelCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl defaultPanelCtrl = new VoodooControl("ul", "css", "#Default ul");
		VoodooControl hiddenPanelCtrl = new VoodooControl("ul", "css", "#Hidden ul");
		VoodooControl favoriteHiddenFieldCtrl = new VoodooControl("li", "css", "#Hidden .draggable[data-name='my_favorite']");
		VoodooControl favoriteDefaultFieldCtrl = new VoodooControl("li", "css", "#Default .draggable[data-name='my_favorite']");

		// Verify fields in Default panel
		DataSource defaultPanelData = testData.get(testName);
		for (int i = 0; i < defaultPanelData.size(); i++) {
			defaultPanelCtrl.assertContains(defaultPanelData.get(i).get("defaultFields"), true);
		}

		// Verify fields in Hidden Panel
		DataSource HiddenPanelData = testData.get(testName+"_customData");
		for (int i = 0; i < HiddenPanelData.size(); i++) {
			hiddenPanelCtrl.assertContains(HiddenPanelData.get(i).get("hiddenFields"), true);
		}

		// Move field from Hidden column to Default column
		favoriteHiddenFieldCtrl.dragNDropViaJS(defaultPanelCtrl);
		VoodooUtils.waitForReady();

		// Save and Deploy
		saveDeployCtrl = new VoodooControl("input", "id", "savebtn");
		saveDeployCtrl.click();
		VoodooUtils.waitForReady();

		// Verify moved field is available in Default column
		favoriteHiddenFieldCtrl.assertExists(false);
		favoriteDefaultFieldCtrl.assertExists(true);

		// Click Restore Default
		restoreDefaultCtrl = new VoodooControl("input", "id","historyRestoreDefaultLayout");
		restoreDefaultCtrl.click();

		// Verify "Favorite" field should move back to hidden
		favoriteHiddenFieldCtrl.assertExists(true);

		// Save & Deploy
		saveDeployCtrl.click();

		// Verify data in Hidden & Default panel restores to Default.
		// Verify fields in Default panel
		for (int i = 0; i < defaultPanelData.size(); i++) {
			defaultPanelCtrl.assertContains(defaultPanelData.get(i).get("defaultFields"), true);
		}

		// Verify fields in Hidden Panel
		for (int i = 0; i < HiddenPanelData.size(); i++) {
			hiddenPanelCtrl.assertContains(HiddenPanelData.get(i).get("hiddenFields"), true);
		}

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}