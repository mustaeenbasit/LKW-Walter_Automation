package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24423 extends SugarTest {
	OpportunityRecord myOpp;
	ContactRecord myContact;
	StandardSubpanel contactsSubpanel;
	DataSource contactDS;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();
		contactDS = testData.get(testName);
	}

	/**
	 * Test Case 24423: Edit Contract_Verify that no information of the contract record is changed when clicking save
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24423_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Link contact with opportunity
		contactsSubpanel = sugar().opportunities.recordView.subpanels.get("Contacts");
		myOpp.navToRecord();
		contactsSubpanel.linkExistingRecord(myContact);

		// Modify Role field value for contact record
		contactsSubpanel.editRecord(1);
		new VoodooSelect("a","css",".fld_opportunity_role.edit a").set(contactDS.get(0).get("contact_role"));
		contactsSubpanel.saveAction(1);
		
		// Verify that contact record is still displaying in the subpanel and role is correctly changed
		FieldSet fs = new FieldSet();
		fs.put("role",contactDS.get(0).get("contact_role"));
		fs.put("fullName",myContact.getRecordIdentifier());
		contactsSubpanel.verify(1, fs, true );
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}