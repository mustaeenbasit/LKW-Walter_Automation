package com.sugarcrm.test.subpanels;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_21629 extends SugarTest {
	StandardSubpanel contactSubpanel;

	public void setup() throws Exception {
		// Create a Account record
		AccountRecord parentAccountRecord = (AccountRecord)sugar.accounts.api.create();
		// Create a Contact record
		ContactRecord relatedContactRecord = (ContactRecord)sugar.contacts.api.create();
		sugar.login();

		// Linking the contact record with the Account
		parentAccountRecord.navToRecord();
		contactSubpanel = sugar.accounts.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactSubpanel.linkExistingRecord(relatedContactRecord);
	}

	/**
	 * Show a message when user clicks on "Unlink" in subpanel to remove relationship
	 * @throws Exception
	 */
	@Ignore("TR-9854 While Unlinking Contact from Account's Subpanel 'Salutation' is not shown in the Warning Message.")
	@Test
	public void Subpanels_21629_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet data = testData.get(testName).get(0);
		String fullName = sugar.contacts.getDefaultData().get("fullName");
		VoodooControl unlinkRecord = contactSubpanel.getControl("unlinkActionRow01");

		// Open row action drop down and click "Unlink"
		contactSubpanel.expandSubpanelRowActions(1);
		unlinkRecord.click();

		// Assert the message in the Warning pop-up
		sugar.alerts.getWarning().assertContains(data.get("warning_message")+" "+fullName+"?", true);

		// Cancel the warning 
		sugar.alerts.getWarning().cancelAlert();

		// Assert that the record exists when we cancel the Unlink warning pop-up
		contactSubpanel.getDetailField(1, "fullName").assertContains(fullName, true);

		// Open row action drop down again and click "Unlink"
		contactSubpanel.expandSubpanelRowActions(1);
		unlinkRecord.click();

		// Assert the message in the Warning pop-up
		sugar.alerts.getWarning().assertContains(data.get("warning_message")+" "+fullName+"?", true);

		// Confirm the Unlink warning
		sugar.alerts.getWarning().confirmAlert();

		// Assert that the related Contact is removed from the subpanel
		Assert.assertTrue(""+fullName+" is not unlinked.", contactSubpanel.isEmpty());

		// Navigate to the Contacts list view and assert that the unlinked contact record still exists
		sugar.contacts.navToListView();
		sugar.contacts.listView.verifyField(1, "fullName", fullName);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}