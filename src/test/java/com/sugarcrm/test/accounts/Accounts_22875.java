package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22875 extends SugarTest {
		
	public void setup() throws Exception {
		sugar().login();
	}
	
	/**
	 * TC 22875: Create Account - Quick _Verify that  an account is quick created from "New Account" box.
	 * @throws Exception
	 */
	@Test
	public void Accounts_22875_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);		
		
		// Go to quick create account
		sugar().navbar.quickCreateAction(sugar().accounts.moduleNamePlural);
		
		// Populate fields and save
		sugar().accounts.createDrawer.showMore();
		sugar().accounts.createDrawer.setFields(ds.get(0));
		sugar().accounts.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();
		
		// Verify that the "Home" module is displayed after account is created using Quick Create 
		sugar().home.dashboard.assertContains("My Dashboard", true);
		
		// Verify that account has been created
		sugar().accounts.navToListView();
		sugar().accounts.listView.setSearchString(testName);
		sugar().accounts.listView.getControl("emptyListViewMsg").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}