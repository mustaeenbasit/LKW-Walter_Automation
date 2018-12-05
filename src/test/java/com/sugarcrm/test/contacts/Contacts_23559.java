package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23559 extends SugarTest {
	ContactRecord myContact;
	StandardSubpanel contactSub;
	FieldSet directReportContact;

	public void setup() throws Exception {
		directReportContact = testData.get(testName).get(0);
		myContact = (ContactRecord) sugar().contacts.api.create(directReportContact);

		sugar().login();
	}

	/**
	 * Verify that the selected contacts are displayed in "Direct Report"
	 * sub-panel of "Contact Detail View" page
	 * @throws Exception
	 */
	@Test
	public void Contacts_23559_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myContact.navToRecord();

		// Click "Link existing record" button in "Direct Reports" sub-panel of "Contact Detail View" page.
		contactSub = sugar().contacts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactSub.linkExistingRecord(myContact);

		// Verifying direct reports records are displayed in "Direct Reports" sub-panel of "Contact Record View" page.
		contactSub.verify(1, directReportContact, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
