package com.sugarcrm.test.ListView;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_17149 extends SugarTest {

	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.contacts.api.create();
		sugar.login();
	}

	/**
	 * Related-to widget select value from "search for more" on inline edit mode
	 * 
	 * @throws Exception
	 */
	@Test
	public void ListView_17149_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.contacts.navToListView();

		// Click the inline edit of an existing Contact,On the "Account Name" field
		sugar.contacts.listView.editRecord(1);
		sugar.contacts.listView.getEditField(1, "relAccountName").click();

		// Verify that a search field appears and the "Search for more..." link appears below search field.
		VoodooSelect relAccount = (VoodooSelect)sugar.contacts.listView.getEditField(1, "relAccountName");
		relAccount.selectWidget.getControl("searchBox").assertVisible(true);

		// Click "Search for more"
		relAccount.clickSearchForMore();

		// Verify that "Search and select Accounts" page appears displaying all the available accounts 
		sugar.accounts.searchSelect.assertVisible(true);

		// Verify radio buttons before each record.
		sugar.accounts.searchSelect.getControl("selectInput01").assertAttribute("type", "radio", true);

		// Click the radio button on one of the listed accounts.
		sugar.accounts.searchSelect.selectRecord(1);
		sugar.alerts.getWarning().confirmAlert();

		// Verify selected account is displayed on "Account Name" field while still in inline edit mode.
		sugar.contacts.listView.getEditField(1, "relAccountName").assertContains(sugar.accounts.getDefaultData().get("name"), true);
		sugar.contacts.listView.cancelRecord(1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}