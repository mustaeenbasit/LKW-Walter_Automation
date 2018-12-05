package com.sugarcrm.test.contacts;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class Contacts_23724 extends SugarTest {
	ContactRecord myCont;
	LeadRecord myLead;

	public void setup() throws Exception {
		myLead = (LeadRecord) sugar().leads.api.create();
		myCont = (ContactRecord) sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Select lead_Verify that a related lead can be selected into a contact from contact detail view.
	 */
	@Test
	public void Contacts_23724_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myCont.navToRecord();
		// Leads default full name
		FieldSet leadFullnameData = new FieldSet();
		leadFullnameData.put("fullName", sugar().leads.getDefaultData().get("fullName"));

		// Link Lead record
		StandardSubpanel leadsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.linkExistingRecord(myLead);

		// Verify the lead is related to the contact
		leadsSubpanel.verify(1, leadFullnameData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
