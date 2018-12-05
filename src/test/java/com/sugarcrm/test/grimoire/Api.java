package com.sugarcrm.test.grimoire;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.*;
import com.sugarcrm.test.SugarTest;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

public class Api extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Ignore ("VOOD-444: Support for relationships via API")
	@Test
	public void createRecords() throws Exception {
		VoodooUtils.voodoo.log.info("Running createRecords...");

		// Create default record.
		// Create a new Account record using default data via the API.
		AccountRecord myAccount = (AccountRecord)sugar().accounts.api.create();
		// Using the UI, verify the account was created with the correct data.
		myAccount.verify();

		// Create custom record.
		FieldSet record = new FieldSet();
		record.put("name", "Record1");

		// Create a new Account record using custom data via the API.
		myAccount = (AccountRecord)sugar().accounts.api.create(record);
		// Using the UI, verify the account was created with the correct data.
		myAccount.verify();

		// Create multiple records.
		DataSource recordList = new DataSource();
		record = new FieldSet();
		record.put("name", "Record2");
		recordList.add(record);
		record = new FieldSet();
		record.put("name", "Record3");
		recordList.add(record);
		record = new FieldSet();
		record.put("name", "Record4");
		recordList.add(record);

		// Create a new Account record using default data via the API.
		ArrayList<Record> myAccounts = sugar().accounts.api.create(recordList);
		// Using the UI, verify the account was created with the correct data.
		for(Record createdRecord : myAccounts)
		{
			createdRecord.verify();
		}

		VoodooUtils.voodoo.log.info("createRecords complete.");
	}

	@Test
	public void callsMeetingsAPIcreate() throws Exception {
		VoodooUtils.voodoo.log.info("Running callsMeetingsAPIcreate...");

		// Create Default Call, including start date and end date
		CallRecord myCall = (CallRecord)sugar().calls.api.create();
		// Verify data
		myCall.verify();

		// Create Default Call, including start date and end date
		MeetingRecord myMeeting = (MeetingRecord)sugar().meetings.api.create();
		// Verify data
		myMeeting.verify();

		VoodooUtils.voodoo.log.info("callsMeetingsAPIcreate complete.");
	}

	@Test
	public void createViaDataSourceWithDates() throws Exception {
		VoodooUtils.voodoo.log.info("Running createViaDataSourceWithDates...");

		FieldSet account1 = new FieldSet();
		account1.put("name", "Account 1");
		account1.put("date_entered_date", "05/19/2015");
		account1.put("date_entered_time", "12:00pm");

		FieldSet account2 = new FieldSet();
		account2.put("name", "Account 2");
		account2.put("date_entered_date", "05/21/2015");
		account2.put("date_entered_time", "01:00pm");

		FieldSet account3 = new FieldSet();
		account3.put("name", "Account 3");
		account3.put("date_entered_date", "05/20/2015");
		account3.put("date_entered_time", "02:00pm");

		DataSource accountData = new DataSource();
		accountData.add(account1);
		accountData.add(account2);
		accountData.add(account3);

		sugar().accounts.api.create(accountData);

		sugar().accounts.navToListView();
		sugar().accounts.listView.sortBy("headerDateentered", true);
		sugar().accounts.listView.verifyField(1, "name", account1.get("name"));
		sugar().accounts.listView.verifyField(2, "name", account3.get("name"));
		sugar().accounts.listView.verifyField(3, "name", account2.get("name"));

		VoodooUtils.voodoo.log.info("createViaDataSourceWithDates complete.");
	}

	@Test
	public void restVsDisplay() throws Exception {
		VoodooUtils.voodoo.log.info("Running restVsDisplay()...");

		// Create Default Contact
		ContactRecord myCon = (ContactRecord)sugar().contacts.api.create();
		myCon.verify();

		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.cancel();

		sugar().accounts.navToListView();

		VoodooUtils.voodoo.log.info("restVsDisplay() complete.");
	}

	public void cleanup() throws Exception {}
}
