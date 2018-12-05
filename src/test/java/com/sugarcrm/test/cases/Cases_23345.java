package com.sugarcrm.test.cases;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.records.CaseRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_23345 extends SugarTest {
	FieldSet customData;
	StandardSubpanel subContacts;
	ContactRecord relContact;
	CaseRecord myCase;
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		relContact = (ContactRecord)sugar().contacts.api.create();
		myCase = (CaseRecord)sugar().cases.api.create();
		sugar().login();

		// Link a contact to a case
		myCase.navToRecord();
		subContacts = sugar().cases.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		subContacts.scrollIntoView();
		subContacts.linkExistingRecord(relContact);
	}

	/**
	 * Edit Contact_Verify that modification of contact for case can be canceled.
	 * @throws Exception
	 * */
	@Test
	public void Cases_23345_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit related contact inline
		subContacts.scrollIntoView();
		subContacts.editRecord(1);

		// TODO: VOOD-503
		new VoodooSelect("div", "css", "[data-voodoo-name='salutation'] div").set(customData.get("salutation"));
		new VoodooControl("input", "css", "[data-voodoo-name='first_name'] input").set(customData.get("firstName"));
		new VoodooControl("input", "css", "[data-voodoo-name='last_name'] input").set(customData.get("lastName"));
		new VoodooControl("input", "css", "[data-voodoo-name='phone_work'] input").set(customData.get("phoneWork"));

		// cancel record
		subContacts.scrollIntoView();
		subContacts.cancelAction(1);

		// Put data need to be checked on subpanel into fieldset
		FieldSet contactSubData = new FieldSet();
		contactSubData.put("fullName", relContact.get("fullName"));
		contactSubData.put("phoneWork", relContact.get("phoneWork"));

		// Verify that contact isn't updated
		subContacts.verify(1, contactSubData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
