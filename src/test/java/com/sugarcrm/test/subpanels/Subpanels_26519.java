package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;

public class Subpanels_26519 extends SugarTest {
	FieldSet contactRecord;
	ContactRecord myContact;

	public void setup() throws Exception {
		contactRecord = testData.get(testName).get(0);
		sugar.accounts.api.create();
		myContact = (ContactRecord) sugar.contacts.api.create();
		sugar.login();
	}

	/**
	 * Verify inline edit Email address working properly in Contacts subpanel
	 * @throws Exception
	 */
	@Test
	public void Subpanels_26519_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Link Existing Contact in the Accounts subpanel
		sugar.accounts.navToListView();
		sugar.accounts.listView.clickRecord(1);
		StandardSubpanel contactsSubpanel = sugar.accounts.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecord(myContact);
		
		// In Contacts sub panel, inline edit the email address and save
		sugar.accounts.recordView.showDataView();
		sugar.alerts.waitForLoadingExpiration();
		contactsSubpanel.scrollIntoView();
		contactsSubpanel.editRecord(1);
		contactsSubpanel.scrollIntoView();
		VoodooControl horizontalScrollBar = new VoodooControl("div", "css", "[data-voodoo-name='Contacts'] div.flex-list-view-content");
		horizontalScrollBar.scrollHorizontally(800);
		
		// TODO: VOOD-866: Lib support to provide inline edit email address field under Contacts subpanel
		new VoodooControl("input", "css", ".fld_email.edit input").set(contactRecord.get("email"));
		contactsSubpanel.saveAction(1);
		
		// Verify the new value does get saved
		contactsSubpanel.verify(1, contactRecord, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}