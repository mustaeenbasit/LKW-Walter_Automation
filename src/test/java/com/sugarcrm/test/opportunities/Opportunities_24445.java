package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24445 extends SugarTest {
	OpportunityRecord myOpp;
	StandardSubpanel contactsSubpanel;
	FieldSet defContactData;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		defContactData = sugar().contacts.getDefaultData();
	}

	/**
	 * Test Case 24445: In-Line Create Contact_Verify that contact
	 * can be canceled in-line creating from Contacts sub-panel for an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24445_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myOpp.navToRecord();
		contactsSubpanel = sugar().opportunities.recordView.subpanels.get("Contacts");
		contactsSubpanel.addRecord();
		sugar().contacts.createDrawer.showMore();
		sugar().contacts.createDrawer.setFields(defContactData);
		// Cancel creating contact record
		sugar().contacts.createDrawer.cancel();
		// Verify that creating was correctly cancelled and contact was not created
		myOpp.navToRecord();
		contactsSubpanel.expandSubpanel();
		new VoodooControl("div", "css", "div[data-voodoo-name=Contacts] .fld_name.list div").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}