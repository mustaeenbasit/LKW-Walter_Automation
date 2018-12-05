package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_17156 extends SugarTest {
	DataSource createAccounts;
	String accountNames[] = new String[6];

	public void setup() throws Exception {
		createAccounts = testData.get(testName);

		// Create 6 Account Records
		sugar.accounts.api.create(createAccounts);
		sugar.login();
		sugar.accounts.navToListView();
		for(int i=0; i<=createAccounts.size()-1;i++)
		{
			accountNames[i] = sugar.accounts.listView.getDetailField(i+1, "name").getText().toString().trim();
		}
	}

	/**
	 * Verify flex-relate field widget on create 
	 * @throws Exception
	 */
	@Test
	public void ListView_17156_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooSelect relatedTo = (VoodooSelect)sugar.notes.createDrawer.getEditField("relRelatedToModule");
		VoodooSelect relatedToValue = (VoodooSelect)sugar.notes.createDrawer.getEditField("relRelatedToValue");
		sugar.navbar.navToModule(sugar.notes.moduleNamePlural);
		sugar.notes.listView.create();
		relatedTo.click();

		//  Assert that the list of modules is displayed in Related to drop down and may be selected
		// TODO: VOOD-629
		// Assert the existence and selection of 2 BWC modules and 2 Sidecar modules in the RelatedTo drop down
		// BWC module : Quote
		relatedTo.selectWidget.getControl("searchBox").set(sugar.quotes.moduleNameSingular);
		new VoodooControl("span", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child(1) .select2-result-label").click();
		VoodooUtils.waitForReady();
		relatedTo.assertEquals(sugar.quotes.moduleNameSingular, true);

		// BWC module : Contract
		relatedTo.click();
		relatedTo.selectWidget.getControl("searchBox").set(sugar.contracts.moduleNameSingular);
		new VoodooControl("span", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child(1) .select2-result-label").click();
		VoodooUtils.waitForReady();
		relatedTo.assertEquals(sugar.contracts.moduleNameSingular, true);

		// Sidecar module : Call
		relatedTo.click();
		relatedTo.selectWidget.getControl("searchBox").set(sugar.calls.moduleNameSingular);
		new VoodooControl("span", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child(1) .select2-result-label").click();
		VoodooUtils.waitForReady();
		relatedTo.assertEquals(sugar.calls.moduleNameSingular, true);

		// Assert and Select Account module in the "Related To" first drop down
		relatedTo.click();
		relatedTo.selectWidget.getControl("searchBox").set(sugar.accounts.moduleNameSingular);
		new VoodooControl("span", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child(1) .select2-result-label").click();
		VoodooUtils.waitForReady();
		relatedTo.assertEquals(sugar.accounts.moduleNameSingular, true);

		// Click related to 2nd drop down
		relatedToValue.click();

		// Assert that a "search" field appears and the "Search for more..." link appears below search field
		relatedToValue.selectWidget.getControl("searchForMoreLink").assertVisible(true);

		// Search a string i.e "Account"
		relatedToValue.selectWidget.getControl("searchBox").set(createAccounts.get(0).get("name"));
		VoodooUtils.waitForReady();

		// Assert that 5 accounts matching your criteria are displayed below search field 
		int totalAccountRecords = createAccounts.size();
		for (int i=1; i<=totalAccountRecords-1; i++){
			new VoodooControl("span", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child("+i+") .select2-result-label .select2-match").assertVisible(true);

			// Assert that the listed options match the module selected
			new VoodooControl("div", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child("+i+") .select2-result-label").assertContains(accountNames[i-1], true);
		}

		// 6th element does not exists
		new VoodooControl("div", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child("+totalAccountRecords+") .select2-result-label").assertExists(false);

		// Click on the first Account option
		new VoodooControl("span", "css", ".select2-drop-active .select2-results:nth-child(2) li:nth-child(1) .select2-result-label").click();

		// Assert that the selected account is displayed on "Related To" field
		relatedToValue.assertContains(accountNames[0], true);

		// Create Note record
		sugar.notes.createDrawer.getEditField("subject").set(testName);
		sugar.notes.createDrawer.save();

		// Assert the RelatedTo account value and the note subject
		sugar.notes.listView.verifyField(1, "subject", testName);
		sugar.notes.listView.verifyField(1, "relRelatedToValue", accountNames[0]);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}