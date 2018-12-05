package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24292 extends SugarTest {
	ContactRecord myContact;
	OpportunityRecord myOpp;
	StandardSubpanel contactsSubpanel;
	DataSource contactDS;

	public void setup() throws Exception {
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();
		contactDS = testData.get(testName);

		sugar().login();

		// Go to opportunity record view and link contact from contacts subpanel
		myOpp.navToRecord();
		contactsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubpanel.linkExistingRecord(myContact);
	}

	/**
	 * Test Case 24292: Edit Contact_Verify that inline editing of a contact related to an opportunity can be cancelled
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24292_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		contactsSubpanel.scrollIntoView();
		// Click "edit" link for a contact record in "Contacts" sub-panel
		contactsSubpanel.editRecord(1);
		new VoodooControl("div", "css", ".fld_opportunity_role.edit div").click();
		// Change the role of the contact in "Role" drop down list
		// TODO: VOOD-503
		new VoodooSelect("div", "css", "#select2-drop input").set(contactDS.get(0).get("role"));

		// Cancel modifying fields of related contact
		contactsSubpanel.cancelAction(1);

		// Verify that editing was correctly cancelled
		contactsSubpanel.verify(1, contactDS.get(0), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}