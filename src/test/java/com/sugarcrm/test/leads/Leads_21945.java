package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_21945 extends SugarTest {
	DataSource accountDs = new DataSource();

	public void setup() throws Exception {
		// Create a lead record	and multiple accounts records	
		sugar().leads.api.create();
		accountDs = testData.get(testName);
		sugar().accounts.api.create(accountDs);
		sugar().login();
	}

	/**
	 * Convert Lead_Verify that account can be created when converting a lead.
	 * @throws Exception
	 */
	@Test
	public void Leads_21945_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String accountData = sugar().accounts.defaultData.get("name");

		// Click "Leads" tab on navigation bar
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.clickRecord(1);

		// Click on Action - Edit button and select "Convert" option. 
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-585
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// Account name is populated with the new account data
		new VoodooControl("input", "css", "[data-module='Accounts'] [name='name']").set(accountData);

		// Click "Create Account" button
		new VoodooControl("a", "css", "[data-module='Accounts'] [name='associate_button']").click();

		// Click "Save and Convert"  button
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();
		
		// Check the new account is linked in Account Name field 
		VoodooControl accountName = new VoodooControl("a", "css", ".converted-results tr:nth-of-type(2) a");
		accountName.assertEquals(accountData,true);

		// Click new account name link near Account Associated field.
		accountName.click();
		sugar().accounts.recordView.showMore();

		// Verify the account record information displayed is correct & The account's description is transferred from the lead.
		sugar().accounts.recordView.getDetailField("name").assertEquals(accountData, true);
		sugar().accounts.recordView.getDetailField("description").assertContains(sugar().leads.defaultData.get("description"), true);

		//  Navigate to Account list view 
		sugar().accounts.navToListView();

		// Verify List view sort by “Name” ascending order after sort
		sugar().accounts.listView.sortBy("headerName", true);
		for (int i = 0; i < accountDs.size(); i++) {
			sugar().accounts.listView.verifyField(i + 1, "name", accountDs.get(i).get("name"));
		}

		// search the new account created while lead conversion & verify the searched record is displayed.
		sugar().accounts.listView.setSearchString(accountData);
		sugar().accounts.listView.verifyField(1, "name", accountData);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}