package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Users_24749 extends SugarTest{
	StandardSubpanel leadsSubpanel;
	public void setup() throws Exception {
		sugar().contacts.api.create();
		LeadRecord myLead = (LeadRecord) sugar().leads.api.create();
		sugar().login();
		
		// Navigating to the Contact record created above
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// Linking the contact with the Lead created above
		leadsSubpanel = sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadsSubpanel.scrollIntoViewIfNeeded(false);
		leadsSubpanel.linkExistingRecord(myLead);
	}
	/**
	 * Click user name on Leads subpanel of Contact module list view with a valid user login
	 * @throws Exception
	 */
	@Test
	public void Users_24749_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Clicking the 'related to user' link in Lead Subpanel Record
		VoodooControl relUserLink = leadsSubpanel.getDetailField(1 , "relAssignedTo");
		relUserLink.scrollIntoViewIfNeeded(false);
		relUserLink.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify that user is navigated to the Employee Detail page of "Administrator"
		// TODO: VOOD-1041 (Need lib support of employees module)
		new VoodooControl("span", "id", "first_name").assertEquals(customData.get("userName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
