package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_26597 extends SugarTest {
	ContactRecord myContact;

	public void setup() throws Exception {
		sugar.calls.api.create();
		myContact = (ContactRecord) sugar.contacts.api.create();
		sugar.login();
	}

	/**
	 * No authentication required pop up in call's edit view if the call has
	 * related contact/lead
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_26597_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		sugar.calls.recordView.getEditField("relatedToParentType").set(sugar.contacts.moduleNameSingular);
		sugar.calls.recordView.getEditField("relatedToParentName").set(myContact.getRecordIdentifier());
		sugar.calls.recordView.save();

		// Open call edit view again
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();

		// Verify there has no alert window pop up
		sugar.alerts.getAlert().assertVisible(false);
		sugar.calls.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}