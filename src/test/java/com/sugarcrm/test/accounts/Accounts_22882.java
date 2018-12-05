package com.sugarcrm.test.accounts;

import junit.framework.Assert;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22882 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Account Reports_Verify that the report view is displayed by clicking "Account Reports" 
	 * navigation shortcuts
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22882_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Go to Accounts Module
		sugar().accounts.navToListView();
		sugar().navbar.clickModuleDropdown(sugar().accounts);
		
		// Click "View Account Reports" from navigation shortcuts
		sugar().accounts.menu.getControl("viewAccountReports").click();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that Account view transfers to Report module view
		FieldSet moduleTitle = testData.get(testName).get(0);
		Assert.assertTrue("Report module view is not displayed", sugar().reports.listView.getModuleTitle().contentEquals(moduleTitle.get("moduleTitle")));
		
		// Verify that only 5 Reports, based on the Account module are displayed in the reports list view
		int row = sugar().reports.listView.countRows();
		Assert.assertTrue("Number of reports in listview not equals to 5.", row == 5);
		
		// TODO: VOOD-822 -Need lib support of reports module
		for (int i=0; i<5; i++){
			new VoodooControl("td", "css", ".list.view tbody tr:nth-child("+(i+3)+") td:nth-child(5)").assertContains(sugar().accounts.moduleNamePlural, true);
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}