package com.sugarcrm.test.ListView;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class ListView_17159 extends SugarTest {
	DataSource accountsDS = new DataSource();

	public void setup() throws Exception {
		accountsDS = testData.get(testName+"_Accounts");

		// Create 5 Account Records
		sugar().accounts.api.create(accountsDS);
		sugar().login();
	}

	/**
	 * Verify "related-to" widget on create window
	 * @throws Exception
	 */
	@Test
	public void ListView_17159_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create Contact -> Click on Account name dropdown
		DataSource ds = testData.get(testName);
		sugar().navbar.selectMenuItem(sugar().contacts, "create"+sugar().contacts.moduleNameSingular);
		VoodooSelect relatedAccount = (VoodooSelect)sugar().contacts.createDrawer.getEditField("relAccountName");
		relatedAccount.click();
		VoodooControl searchAndSelect = relatedAccount.selectWidget.getControl("searchForMoreLink");
		VoodooControl searchBox = relatedAccount.selectWidget.getControl("searchBox");

		// Verify that a search field and a "Search and Select..." link appears.
		searchAndSelect.assertVisible(true);
		searchAndSelect.assertEquals(ds.get(0).get("placeholder"), true);

		// Search a string i.e "Account"
		searchBox.set(accountsDS.get(0).get("name"));
		VoodooUtils.waitForReady();

		// Verify that a list of account records are displayed that begin with the entered character, as well as the "Search and Select..." link. 
		// TODO: VOOD-629
		int totalAccountRecords = accountsDS.size();
		Assert.assertTrue("Count not equals to "+totalAccountRecords, new VoodooControl("div", "css", ".select2-drop-active .select2-results:nth-child(2) li").count() == totalAccountRecords);	
		searchAndSelect.assertVisible(true);

		// Right now we donot have mechanism to close dropdown without select any option from it.
		// TODO: VOOD-1437 and CB-252 - ESC key needed to get rid from Select dropdown
		searchBox.append(""+'\uE00C');

		// Verify that the "Account Name" drop down is closed and the account name field will have place holder "Select account...".
		relatedAccount.assertEquals(ds.get(1).get("placeholder"), true);

		// TODO: VOOD-1411 - Once resolved cancel method will work
		sugar().contacts.createDrawer.getControl("cancelButton").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}