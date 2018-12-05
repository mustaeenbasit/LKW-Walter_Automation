package com.sugarcrm.test.accounts;

import org.junit.Assert;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_17142 extends SugarTest {
	int numberOfAccounts;

	public void setup() throws Exception {
		DataSource accountNames = testData.get(testName);
		numberOfAccounts = accountNames.size();
		sugar().accounts.api.create(accountNames);
		sugar().login();
		sugar().accounts.navToListView();

		// Making all the records as FAVORITE
		for(int i=1;i<=numberOfAccounts;i++)
		{
			sugar().accounts.listView.toggleFavorite(i);
		}
	}

	/**
	 * Recently viewed records display format
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17142_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// String type array to store the account names viewed from the record view of Accounts
		String accountName[] = new String[3];
		sugar().navbar.navToModule(sugar().accounts.moduleNamePlural);

		// Clicking the last account record in the Accounts list view
		sugar().accounts.listView.clickRecord(numberOfAccounts);

		// Asserting the disability of next icon ">"
		// TODO: VOOD-622,VOOD-730 
		Assert.assertTrue("The next button is still enabled.",new VoodooControl("button", "css", ".next-row").isDisabled());

		// Viewing the Account records from the record view
		for (int i = 0; i < numberOfAccounts-1; i++)
		{
			sugar().accounts.recordView.gotoPreviousRecord();
			accountName[i] = sugar().accounts.recordView.getDetailField("name").getText();
		}

		// Clicking the Accounts drop down to view the Actions, Recently Viewed and Favorite options
		sugar().navbar.clickModuleDropdown(sugar().accounts);

		// TODO: VOOD-771
		// Asserting the Actions in the Accounts Drop Down
		sugar().accounts.menu.getControl("createAccount").assertVisible(true);
		sugar().accounts.menu.getControl("viewAccounts").assertVisible(true);
		sugar().accounts.menu.getControl("viewAccountReports").assertVisible(true);
		sugar().accounts.menu.getControl("importAccounts").assertVisible(true);

		// Asserting that Recently Viewed Record is being displayed in the Accounts Drop down
		new VoodooControl("i", "css", ".dropdown-menu .fa-clock-o.active").assertVisible(true);

		// Asserting that Favorite record is being displayed in the Accounts drop down
		new VoodooControl("i", "css", ".dropdown-menu .fa-favorite.active").assertVisible(true);

		int recentlyViewedCountInDropDown = 0;

		// String type array to store the Names of the Recently viewed account records i.e displayed in the drop-down
		String dropDownRecentlyViewed[] = new String[3];

		// The recently viewed record is displayed at the 7th index
		for(int j=7;j<=10;j++){
			if(j<10){
				new VoodooControl("i", "css", ".dropdown.active li:nth-child("+j+") a ").assertVisible(true);	
				dropDownRecentlyViewed[recentlyViewedCountInDropDown] = new VoodooControl("i", "css", ".dropdown.active li:nth-child("+j+") a ").getText();
				recentlyViewedCountInDropDown++;
			}

			// Verifying that the element at index 10 is a divider
			else if (j==10) {
				new VoodooControl("i", "css", ".dropdown.active li:nth-child("+j+")").getClass().getSimpleName().equalsIgnoreCase("divider");
			}
		}

		// Asserting that only 3 recently viewed records are displayed in the Accounts drop down   		
		Assert.assertTrue("The recently viewed count is not 3",recentlyViewedCountInDropDown==numberOfAccounts-1); 

		// Asserting the Reverse Chronological Order
		for(int a=0,b=recentlyViewedCountInDropDown-1;a<numberOfAccounts-1;a++,b--){
			Boolean isSameAccount = accountName[a].equals(dropDownRecentlyViewed[b]);
			Assert.assertTrue("Accounts are not displayed in Reverse chronological order.",isSameAccount);
		}

		// Closing the Accounts Drop down
		sugar().navbar.clickModuleDropdown(sugar().accounts);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}