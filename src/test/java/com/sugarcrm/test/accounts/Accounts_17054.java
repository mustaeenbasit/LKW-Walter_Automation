package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17054 extends SugarTest {	
	AccountRecord myAccount;
	UserRecord qauser;

	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		qauser=  new UserRecord(sugar().users.getQAUser());
		qauser.login();		
	}

	/**
	 * Date Created/Modified group field display format on edit view
	 * @throws Exception
	 */
	@Test
	public void Accounts_17054_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet modifiedAccount = testData.get(testName).get(0);
		myAccount.navToRecord();

		// Editing the Account record created by Administrator from qaUser's SugarCRM account
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("name").set(modifiedAccount.get("accountName"));
		sugar().accounts.recordView.getEditField("description").set(modifiedAccount.get("accountDescription"));
		sugar().accounts.recordView.getEditField("workPhone").set(modifiedAccount.get("accountPhone"));
		sugar().accounts.recordView.getEditField("emailAddress").set(modifiedAccount.get("accountEmail"));

		// TODO: VOOD-597
		// Asserting that the "date entered" and "created By" values exist
		VoodooControl dateEntered = new VoodooControl ("span", "css", "span[data-fieldname='date_entered_by'] span.fld_date_entered.detail");
		dateEntered.assertExists(true);
		String create = dateEntered.getText();
		new VoodooControl ("span", "css", "span[data-fieldname='date_entered_by'] span.fld_created_by_name.detail").assertExists(true);

		// Asserting that the "date entered" and "created By" field is read only
		new VoodooControl ("input", "css", "span[data-fieldname='date_entered_by'] span.fld_date_entered.detail input").assertExists(false);
		new VoodooControl ("input", "css", "span[data-fieldname='date_entered_by'] span.fld_created_by_name.detail input").assertExists(false);

		// Asserting that the "modified date" and "modified By" values exist
		VoodooControl dateModified = new VoodooControl ("span", "css", "span[data-fieldname='date_modified_by'] span.fld_date_modified.detail");
		dateModified.assertExists(true);
		String befmodify = dateModified.getText();
		VoodooControl modifiedBy = new VoodooControl ("span", "css", "span[data-fieldname='date_modified_by'] span.fld_modified_by_name.detail");
		modifiedBy.assertExists(true);

		// Asserting that the "date Modified" and "modified By" field is read only
		new VoodooControl ("input", "css", "span[data-fieldname='date_modified_by'] span.fld_date_modified.detail input").assertExists(false);
		new VoodooControl ("input", "css", "span[data-fieldname='date_modified_by'] span.fld_modified_by_name.detail input").assertExists(false);

		// Saving the account record
		sugar().accounts.recordView.save();
		sugar().alerts.getProcess();
		sugar().alerts.waitForLoadingExpiration();

		// Editing the same account again
		sugar().accounts.recordView.edit();

		// Asserting that values for "date modified" and "modified By" field changed
		dateEntered.assertEquals(create, true);
		dateModified.assertEquals(befmodify, false);
		modifiedBy.assertContains(qauser.get("userName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}