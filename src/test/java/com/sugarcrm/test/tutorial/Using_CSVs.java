package com.sugarcrm.test.tutorial;

import java.util.List;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Using_CSVs extends SugarTest {
	DataSource accounts, otherAccounts;
	FieldSet thirdAccount;

	public void setup() throws Exception {
		// Syntactic sugar -- save the DataSource and one data row under
		// convenient names.  The first and second row are not saved to
		// demonstrate the less convenient full syntax
		accounts = testData.get("Using_CSVs");
		thirdAccount = accounts.get(2);
		otherAccounts = testData.get("Using_CSVs_OtherAccounts");
		
		sugar.login();
	}

	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create a new Account Record using the first row of data.
		AccountRecord myAccount =
			(AccountRecord)sugar.accounts.create(testData.get("Using_CSVs").get(0));
		myAccount.verify(testData.get("Using_CSVs").get(0));
		
		// That was wordy and inconvenient.  But the DataSource was saved in a
		// var (line 21), so let's try...
		myAccount.edit(accounts.get(1));
		myAccount.verify(accounts.get(1));
		
		// Still pretty long.  Since we saved one of the FieldSets in a var as
		// well (line 22), we can do...
		myAccount.edit(thirdAccount);
		// And, since Voodoo 2 remembers the data you used to create a record,
		// even this verification will work:
		myAccount.verify();

		// Let's clean up before proceeding.
		sugar.accounts.api.deleteAll();
		
		// We can also create multiple records from a DataSource.
		List<Record> accountRecords = sugar.accounts.api.create(otherAccounts);
		for(Record account : accountRecords)
		{
			account.verify();
		}

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}
