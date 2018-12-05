package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_17154 extends SugarTest {
	DataSource createAccounts = new DataSource();

	public void setup() throws Exception {
		createAccounts = testData.get(testName);

		// Create 6 Account Records
		sugar().accounts.api.create(createAccounts);
		sugar().login();
	}

	/**
	 * Related-to widget select value create mode
	 * @throws Exception
	 */
	@Test
	public void ListView_17154_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToModule(sugar().contacts.moduleNamePlural);
		sugar().contacts.listView.create();
		VoodooSelect relatedAccount = (VoodooSelect)sugar().contacts.createDrawer.getEditField("relAccountName");
		sugar().contacts.createDrawer.getEditField("relAccountName").click();

		// Assert that a "search" field appears and the "Search for more..." link appears below search field
		relatedAccount.selectWidget.getControl("searchForMoreLink").assertVisible(true);

		// Search a string i.e "Account"
		relatedAccount.selectWidget.getControl("searchBox").set(createAccounts.get(0).get("name"));
		VoodooUtils.waitForReady();

		// Assert that 5 accounts matching your criteria are displayed below search field 
		// TODO: VOOD-629
		int totalAccountRecords = createAccounts.size();
		for (int i=1; i<=totalAccountRecords-1; i++){
			new VoodooControl("span", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child("+i+") .select2-result-label .select2-match").assertVisible(true);
			new VoodooControl("div", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child("+i+") .select2-result-label").assertContains(createAccounts.get(totalAccountRecords-i).get("name"), true);
		}

		// 6th element does not exists
		new VoodooControl("div", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child("+totalAccountRecords+") .select2-result-label").assertExists(false);

		// Click on the first Account option
		new VoodooControl("span", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child(1) .select2-result-label").click();

		// Assert that the selected account is displayed on "Account Name" field
		sugar().contacts.createDrawer.getEditField("relAccountName").assertContains(createAccounts.get(totalAccountRecords-1).get("name"), true);
		sugar().alerts.getWarning().cancelAlert();

		// Create a Contact record
		sugar().contacts.createDrawer.getEditField("lastName").set(sugar().contacts.getDefaultData().get("lastName"));
		sugar().contacts.createDrawer.save();

		// Verify the Contact full name and related Account name
		sugar().contacts.listView.verifyField(1, "fullName", sugar().contacts.getDefaultData().get("lastName"));
		sugar().contacts.listView.verifyField(1, "relAccountName",createAccounts.get(totalAccountRecords-1).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}