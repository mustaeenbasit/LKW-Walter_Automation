package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_29689 extends SugarTest {
	ContactRecord myContacts;
	FieldSet fs = new FieldSet();
	
	public void setup() throws Exception {
		fs = testData.get(testName).get(0);
		sugar().opportunities.api.create();
		myContacts = (ContactRecord)sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Verify that Inline editing is working for Role field at Contacts subpanel of Opportunity module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_29689_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opportunity module
		sugar().opportunities.navToListView();

		// open the record 
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel contactsSubPanel = (StandardSubpanel) sugar().opportunities.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSubPanel.scrollIntoViewIfNeeded(false);

		// link contacts record 
		contactsSubPanel.linkExistingRecord(myContacts);

		//  Click on Edit option under action drop down of a record in contacts subpanel
		contactsSubPanel.editRecord(1);

		// Enter the value in Role field
		// TODO: VOOD-609
		new VoodooSelect("span", "css", ".fld_opportunity_role.edit ").set(fs.get("role_name"));

		// Save the edited record
		contactsSubPanel.saveAction(1);

		// Verify that after Inline editing, Value should be displayed for Role field
		// TODO: VOOD-609
		new VoodooControl("div", "css", ".list.fld_opportunity_role div").assertEquals(fs.get("role_name"),true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}