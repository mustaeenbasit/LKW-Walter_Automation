package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_30721 extends SugarTest {
	VoodooControl accountsStudioCtrl;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Account record layout is updated after adding new field from studio.
	 * @throws Exception
	 */
	@Test
	public void Studio_30721_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-542 -> need lib support for studio
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountsStudioCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsStudioCtrl.click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1504
		new VoodooControl("td", "id", "fieldsBtn").click();;
		VoodooUtils.waitForReady();

		// Add field and save
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("input", "id", "field_name_id").set(testName);
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// Adding field to record view from layout
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		accountsStudioCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1506
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#availablefields [data-name='"
				+ testName.toLowerCase() + "_c']").dragNDrop(new VoodooControl(
				"div", "css", ".le_panel [data-name='phone_office']"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigating to account module
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// TODO: VOOD-1036
		// Verifying newly added field is displaying under record layout of account
		VoodooControl accountCustomField = new VoodooControl("div", "css",
				".layout_Accounts .record-label[data-name='"
						+ testName.toLowerCase() + "_c']");
		accountCustomField.assertVisible(true);
		accountCustomField.assertEquals(testName.replace("_", " "), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}