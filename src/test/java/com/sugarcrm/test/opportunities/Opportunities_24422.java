package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24422 extends SugarTest {
	ContactRecord myContact;
	OpportunityRecord myOpp;
	StandardSubpanel contactsSubpanel;
	VoodooControl contactRecordRaw;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();
	}

	/**
	 * Test Case 24422: Select Contact_Verify that cancel function for selecting contacts works successfully
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24422_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myOpp.navToRecord();
		contactsSubpanel = sugar().opportunities.recordView.subpanels.get("Contacts");
		contactRecordRaw = new VoodooControl("div", "css", "div[data-voodoo-name=Contacts] .fld_name.list div");
		contactsSubpanel.clickLinkExisting();
		VoodooUtils.pause(1500); //TODO - Should be removed when VOOD-497 is fixed
		new VoodooControl("input", "css", ".single .list input").click();
		new VoodooControl("a", "name", "close").click();
		VoodooUtils.waitForAlertExpiration();
		contactsSubpanel.expandSubpanel();
		// Verify that no any contacts exists in the contacts subpanel of the opportunity
		contactRecordRaw.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}