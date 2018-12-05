package com.sugarcrm.test.contacts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.FieldSet;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Contacts_24193 extends SugarTest {
	OpportunityRecord myOpp;

	public void setup() throws Exception {
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		sugar().login();		
	}

	/**
	 * Verify that contact role can be correctly updated from the contacts subpanel of the related opportunity record view
	 * @throws Exception
	 */
	@Test
	public void Contacts_24193_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet contactFS = testData.get(testName).get(0);

		// Create a related contact from the contacts subpanel
		myOpp.navToRecord();
		StandardSubpanel contactsSub = sugar().opportunities.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contactsSub.addRecord();
		sugar().contacts.createDrawer.getEditField("lastName").set(contactFS.get("lastName"));
		sugar().contacts.createDrawer.save();

		// Inline-edit "Role" field of related contact and save record
		contactsSub.editRecord(1);

		// TODO: VOOD-609, VOOD-1708
		new VoodooSelect("span", "css", "span[data-voodoo-name='opportunity_role']").set(contactFS.get("role"));
		contactsSub.saveAction(1);

		// Verify that updated role was correctly saved
		new VoodooControl("span", "css", ".fld_opportunity_role").assertContains(contactFS.get("role"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}