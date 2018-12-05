package com.sugarcrm.test.cases;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Cases_27068 extends SugarTest {
	FieldSet customData;
	CaseRecord myCase;
	AccountRecord myAccount;
	ContactRecord myContact;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myAccount = (AccountRecord)sugar().accounts.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name", customData.get("name"));
		myCase = (CaseRecord)sugar().cases.api.create(fs);
		myContact = (ContactRecord) sugar().contacts.api.create();
		sugar().login();
		fs.clear();
		fs.put("relAccountName", myAccount.getRecordIdentifier());
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.setFields(fs);
		sugar().alerts.confirmAllAlerts();
		sugar().contacts.recordView.save();
	}

	/**
	 * Verify that no email address is pre-populated in TO when Contact has no email address
	 * @throws Exception
	 */
	@Ignore("TY-505")
	@Test
	public void Cases_27068_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myCase.navToRecord();

		// Link a contact record under Contact subpanel and click on email compose
		StandardSubpanel contactSub = sugar().cases.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		StandardSubpanel composeEmail = sugar().cases.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		composeEmail.hover();
		contactSub.linkExistingRecord(myContact);
		sugar().cases.recordView.setRelatedSubpanelFilter(sugar().emails.moduleNamePlural);
		composeEmail.composeEmail();

		// Verify that the field is blank and it is a required field
		sugar().accounts.recordView.composeEmail.getControl("toAddress").assertAttribute("value", "", true);
		sugar().accounts.recordView.composeEmail.getControl("subject").assertContains(customData.get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
