package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Tags_28627 extends SugarTest {
	FieldSet tagData = new FieldSet();
	VoodooControl accountsCtrl, studioCtrl, layoutsCtrl, listViewCtrl, saveBtnCtrl,userTypeCtrl;
	UserRecord myUser;

	public void setup() throws Exception {
		tagData = testData.get(testName).get(0);
		sugar.login();

		// Create another admin user so that dashlet changes occurs in it,will delete user itself in the cleanup	
		myUser = (UserRecord)sugar.users.create();
		sugar.users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-563
		new VoodooControl("select", "id", "UserType").set(tagData.get("userType"));
		VoodooUtils.focusDefault();
		sugar.users.editView.save();
		VoodooUtils.waitForReady();

		// Logout from admin user and login from newly made admin user(Chris)
		sugar.logout();
		sugar.login(myUser);
		sugar.accounts.api.create();
		sugar.tags.api.create();
	}

	/**
	 * [Tags] Verify user is able to view tags associated to a record on dashlet list views
	 * @throws Exception
	 */
	@Test
	public void Tags_28627_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Navigate to admin
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar.admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate to Accounts in studio
		// TODO: VOOD-1504 - Support Studio Module Fields View
		accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsCtrl.click();

		// List View
		// TODO: VOOD-1507
		layoutsCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutsCtrl.click();
		listViewCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		listViewCtrl.click(); 

		// Move tags field from "Hidden" to "Default"
		new VoodooControl("li", "css", ".draggable[data-name='tag']").dragNDrop(new VoodooControl("li", "css", ".draggable[data-name='billing_address_country']"));

		// Save & Deploy
		saveBtnCtrl = new VoodooControl("input", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigate to Accounts module
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);

		// Edit existing account record to add tags
		String tagName = sugar.tags.getDefaultData().get("name");
		sugar.accounts.recordView.edit(); 
		VoodooSelect editTagCtrl = (VoodooSelect)sugar.accounts.recordView.getEditField("tags");
		editTagCtrl.set(tagName);
		sugar.accounts.recordView.save();
		sugar.navbar.navToModule(sugar.home.moduleNamePlural);

		// TODO: VOOD-592,VOOD-960
		sugar.home.dashboard.clickCreate();
		sugar.home.dashboard.getControl("title").set(testName);
		sugar.home.dashboard.addRow();
		sugar.home.dashboard.addDashlet(1, 1);
		new VoodooControl("input", "css", ".span4.search").set(tagData.get("search"));
		new VoodooControl("a", "css", "tr.single .fld_title a").click();
		VoodooUtils.waitForReady();

		// Need to remove "MyAccounts" filter so as to show all Accounts record in the dashlet
		// TODO: VOOD-960,VOOD-592
		new VoodooControl("span", "css", ".choice-filter-close").click();
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		sugar.home.dashboard.save();
		new VoodooControl("th", "css", "[data-fieldname='tag']").assertVisible(true);
		new VoodooControl("span", "css", ".fld_tag").assertEquals(tagName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																						
}