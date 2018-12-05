package com.sugarcrm.test.accounts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_18252 extends SugarTest {
	String defaultEmail;
	FieldSet emailData = new FieldSet();

	public void setup() throws Exception {
		FieldSet fs = new FieldSet();
		for (int i = 1; i < 4; i++) {
			fs.put("name", testName + i);
			sugar().accounts.api.create(fs);
		}
		sugar().login();

		// Updating account record with invalid email address 
		// TODO: VOOD-1005
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(2);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		emailData = testData.get(testName).get(0);
		sugar().accounts.recordView.getEditField("emailAddress").set(emailData.get("emailAddress1"));
		VoodooControl emailAddButton = new VoodooControl("a", "css", ".btn.addEmail");
		emailAddButton.click();
		new VoodooControl("button", "css", "[data-emailproperty='invalid_email']").click();
		sugar().accounts.recordView.save();

		// Updating account record with more than one valid email address
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(3);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();
		defaultEmail = sugar().accounts.getDefaultData().get("emailAddress");
		sugar().accounts.recordView.getEditField("emailAddress").set(defaultEmail);

		// Click on "+" to add Email
		emailAddButton.click();
		new VoodooControl("input", "css", ".fld_email.edit div:nth-of-type(2) input").set(emailData.get("emailAddress2"));
		sugar().accounts.recordView.save();
	}

	/**
	 * Check email field inline edit function in list view
	 * @throws Exception
	 */
	@Test
	public void Accounts_18252_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to accounts list view and asserting the email values for all the three records
		sugar().accounts.navToListView();
		VoodooControl firstRecordCtrl = sugar().accounts.listView.getDetailField(1, "emailAddress");
		firstRecordCtrl.assertEquals(defaultEmail, true);
		VoodooControl thirdRecordCtrl = sugar().accounts.listView.getDetailField(3, "emailAddress");
		thirdRecordCtrl.assertEquals("", true);

		// Asserting invalid email address content
		// TODO: VOOD-1349
		VoodooControl invalidEmail = new VoodooControl("del", "css", "tbody tr:nth-of-type(2) .fld_email.list del");
		invalidEmail.assertEquals(emailData.get("emailAddress1"), true);
		Assert.assertTrue("This is not a invalid Email address", invalidEmail.queryAttributeContains("data-original-title", emailData.get("dataTitleInvalid")));

		// Editing first record email address and asserting it doesn't affects other two records email address
		sugar().accounts.listView.editRecord(1);
		sugar().accounts.listView.getEditField(1, "emailAddress").set(emailData.get("emailAddress3"));
		sugar().accounts.listView.saveRecord(1);
		firstRecordCtrl.assertEquals(emailData.get("emailAddress3"), true);
		invalidEmail.assertEquals(emailData.get("emailAddress1"), true);
		thirdRecordCtrl.assertEquals("", true);

		// Editing second record email address and asserting it doesn't affects other two records email address
		sugar().accounts.listView.editRecord(2);
		sugar().accounts.listView.getEditField(2, "emailAddress").set(emailData.get("emailAddress2"));
		sugar().accounts.listView.saveRecord(2);
		firstRecordCtrl.assertEquals(emailData.get("emailAddress3"), true);
		invalidEmail.assertEquals(emailData.get("emailAddress2"), true);
		Assert.assertTrue("This is not a invalid Email address", invalidEmail.queryAttributeContains("data-original-title", emailData.get("dataTitleInvalid")));
		thirdRecordCtrl.assertEquals("", true);

		// Editing third record email address and asserting it doesn't affects other two records email address
		sugar().accounts.listView.editRecord(3);
		sugar().accounts.listView.getEditField(3, "emailAddress").set(emailData.get("emailAddress4"));
		sugar().accounts.listView.saveRecord(3);
		firstRecordCtrl.assertEquals(emailData.get("emailAddress3"), true);
		invalidEmail.assertEquals(emailData.get("emailAddress2"), true);
		thirdRecordCtrl.assertEquals(emailData.get("emailAddress4"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}