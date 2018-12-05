package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Accounts_17052 extends SugarTest {	
	AccountRecord myAccount;
		
	public void setup() throws Exception {
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Date Created/Modified group field display format on detail view
	 * @throws Exception
	 */
	@Test
	public void Accounts_17052_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myAccount.navToRecord();
		sugar().accounts.recordView.showMore();
		
		sugar().accounts.recordView.getDetailField("dateEnteredBy").assertExists(true);
		new VoodooControl ("span", "css", "span[data-fieldname='date_entered_by'] span.fld_date_entered.detail").assertExists(true);
		new VoodooControl ("span", "css", "span[data-fieldname='date_entered_by'] span.fld_created_by_name.detail").assertExists(true);
		sugar().accounts.recordView.getDetailField("date_entered_date").hover();
		
		// TODO: VOOD-597
		// Verifying no pencil icon should be available for Date Created.
		new VoodooControl ("i", "css", "div[data-name='date_entered_by'] .record-edit-link-wrapper.hide .fa.fa-pencil").assertExists(true);
		
		sugar().accounts.recordView.getDetailField("date_modified_date").assertExists(true);
		sugar().accounts.recordView.getDetailField("dateModifiedBy").assertExists(true);
		sugar().accounts.recordView.getDetailField("date_entered_date").hover();
		
		// TODO: VOOD-597
		// Verifying no pencil icon should be available for Date Modified.
		new VoodooControl ("i", "css", "div[data-name='date_modified_by'] .record-edit-link-wrapper.hide .fa.fa-pencil").assertExists(true);		
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}