package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Accounts_22036 extends SugarTest {
	DataSource accountData = new DataSource();;

	public void setup() throws Exception {
		accountData = testData.get(testName);
		sugar().accounts.api.create(accountData);
		sugar().login();
	}

	/**
	 * Check List view default sort order
	 * @throws Exception
	 */
	@Test
	public void Accounts_22036_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		sugar().accounts.navToListView();
		VoodooControl dateEntered = sugar().accounts.listView.getDetailField(1, "date_entered_date");
		dateEntered.scrollIntoViewIfNeeded(false);

		// Verify List view sort by “Created Date” descending order by default
		for(int i=1; i<= accountData.size(); i++){
			sugar().accounts.listView.verifyField(i, "date_entered_date", accountData.get(accountData.size()-i).get("date_entered_date"));
		}

		// Verify List view sort by “Created Date” ascending order after sort
		sugar().accounts.listView.sortBy("headerDateentered", true);
		dateEntered.scrollIntoViewIfNeeded(false);
		for(int i=0; i< accountData.size(); i++){
			sugar().accounts.listView.verifyField(i+1, "date_entered_date", accountData.get(i).get("date_entered_date"));
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}