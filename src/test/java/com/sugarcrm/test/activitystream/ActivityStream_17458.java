package com.sugarcrm.test.activitystream;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class ActivityStream_17458 extends SugarTest{
	AccountRecord myAccount;
	ContactRecord myContact;
	
	public void setup() throws Exception {
		sugar.login();
		myContact = (ContactRecord)sugar.contacts.api.create();
		myAccount = (AccountRecord)sugar.accounts.api.create();
	}
	
	/**
	 * activity stream shouldn't contain wrong context when displaying activity streams on record detail view
	 * @author Eric Yang
	 * @throws Exception
	 */
	@Test
	public void ActivityStream_17458_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Update the account record
		FieldSet newData = new FieldSet();
		newData.put("name", "ActivityStream_17458_AcctName");
		myAccount.edit(newData);

		// Verify the activity stream comment
		myContact.assertCommentContains(myContact.getRecordIdentifier(), 1, true);
						
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}