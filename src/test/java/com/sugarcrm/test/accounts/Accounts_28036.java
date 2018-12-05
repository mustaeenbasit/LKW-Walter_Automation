package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_28036 extends SugarTest {
	VoodooControl moduleCtrl, layoutCtrl, searchBtnCtrl, filterSearchBtnCtrl, restoreDefault, saveBtnCtrl;
	FieldSet filterData = new FieldSet();

	public void setup() throws Exception {
		filterData = testData.get(testName).get(0);
		sugar().login();

		// Create two Accounts record with different email addresses 
		sugar().accounts.navToListView();
		for (int i = 1; i < 3; i++) {
			sugar().accounts.listView.create();
			sugar().accounts.createDrawer.showMore();
			sugar().accounts.createDrawer.getEditField("name").set(testName + i);
			sugar().accounts.createDrawer.getEditField("emailAddress").set(filterData.get("emailAddress" + i));
			sugar().accounts.createDrawer.save();
			sugar().accounts.createDrawer.showLess();
		}
	}

	/**
	 * Verify email can be added and search in the list view filter.
	 *
	 * @throws Exception
	 */
	@Test
	public void Accounts_28036_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to admin > studio > Accounts (any modules that has email field) > Layout > Search
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938 Need library support for studio subpanel
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		searchBtnCtrl = new VoodooControl("td", "id", "searchBtn");
		filterSearchBtnCtrl = new VoodooControl("td", "id", "FilterSearchBtn");
		restoreDefault = new VoodooControl("input", "id", "historyRestoreDefaultLayout");
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		searchBtnCtrl.click();
		VoodooUtils.waitForReady();
		filterSearchBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Drag Email address field from Hidden panel to Default panel 
		VoodooControl dropCtrl = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "#Hidden [data-name='email']").dragNDrop(dropCtrl);

		// Save & Deploy
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to Accounts list view
		sugar().accounts.navToListView();

		// Create a new filter
		sugar().accounts.listView.openFilterDropdown();
		sugar().accounts.listView.selectFilterCreateNew();

		for (int i = 1; i < 3; i++) {
			sugar().accounts.listView.filterCreate.setFilterFields("emailAddress", filterData.get("email"), filterData.get("operator"), filterData.get("emailAddress" + i), 1);
			VoodooUtils.waitForReady();

			// Verify that the record should be returned accordingly
			sugar().accounts.listView.verifyField(1, "name", testName + i);
			sugar().accounts.listView.verifyField(1, "emailAddress", filterData.get("emailAddress" + i));

			// Verify that the second record should not populate.
			sugar().accounts.listView.getControl("checkbox03").assertVisible(false);
		}
		// Cancel the filter
		sugar().accounts.listView.filterCreate.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}