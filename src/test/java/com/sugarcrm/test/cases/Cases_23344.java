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

public class Cases_23344 extends SugarTest {
	FieldSet customData;
	StandardSubpanel subContacts;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		ContactRecord relContact = (ContactRecord)sugar().contacts.api.create();
		CaseRecord myCase = (CaseRecord)sugar().cases.api.create();
		sugar().login();

		// Link a contact to a case
		myCase.navToRecord();
		subContacts = sugar().cases.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		subContacts.scrollIntoView();
		subContacts.linkExistingRecord(relContact);
	}

	/**
	 * Edit Contact_Verify that the information of a contact for case
	 * can be modified when using "Edit" function in "Contacts" sub-panel.
	 * @throws Exception
	 * */
	@Test
	public void Cases_23344_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit related contact inline and Save
		subContacts.scrollIntoView();
		subContacts.editRecord(1);

		// TODO: VOOD-503
		new VoodooSelect("div", "css", "[data-voodoo-name='salutation'] div").set(customData.get("salutation"));
		new VoodooControl("input", "css", "[data-voodoo-name='first_name'] input").set(customData.get("firstName"));
		new VoodooControl("input", "css", "[data-voodoo-name='last_name'] input").set(customData.get("lastName"));
		new VoodooControl("input", "css", "[data-voodoo-name='phone_work'] input").set(customData.get("phoneWork"));
		subContacts.saveAction(1);
		if(sugar().alerts.getSuccess().queryVisible()){
			sugar().alerts.getSuccess().closeAlert();
		}

		FieldSet contactsData = new FieldSet();
		contactsData.put("fullName", customData.get("fullName"));
		contactsData.put("phoneWork", customData.get("phoneWork"));

		// Verify that contact is updated
		subContacts.verify(1, contactsData, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
