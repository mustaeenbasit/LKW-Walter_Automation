package com.sugarcrm.test.tags;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_27777 extends SugarTest {
	VoodooControl accountsCtrl, studioCtrl, viewBtnCtrl, saveBtnCtrl, layoutCtrl;

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify ability to view tags associated to a record on sidecar module list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_27777_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Navigate to admin
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate to Accounts ->  Layouts -> ListView
		// TODO: VOOD-542 & VOOD-1504
		accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		viewBtnCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		viewBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Move tags field from "Hidden" to "Default"
		new VoodooControl("li", "css", ".draggable[data-name='tag']").dragNDrop(new VoodooControl("li", "css", ".draggable[data-name='billing_address_city']"));

		// Save & Deploy
		saveBtnCtrl = new VoodooControl("input", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		
		// Navigate to Accounts module
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		
		// Add three tags with this Account
		DataSource customDS = testData.get(testName);
		VoodooControl addTagCtrl = sugar().accounts.createDrawer.getEditField("tags");
		for (FieldSet tagData : customDS) {
			addTagCtrl.set(tagData.get("tagName"));
		}
		sugar().accounts.createDrawer.save();
			
		// As a sugar user, I can view the list of tags associated to a record while the tag field is added as a list view column for any sidecar module. 
		VoodooControl tagListCtrl = sugar().accounts.listView.getDetailField(1, "tags");
		for (FieldSet tagData : customDS) {
			tagListCtrl.assertContains(tagData.get("tagName"), true);
		}
		
		// Go to Tags listView
		sugar().tags.navToListView();
		
		// Verify that new tag created and display in tags listView
		int i = 1;
		for (FieldSet tagData : customDS) {
			sugar().tags.listView.assertContains(tagData.get("tagName"), true);
			i++;
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																						
}