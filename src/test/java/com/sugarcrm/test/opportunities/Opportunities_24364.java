package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.candybean.datasource.DataSource;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Opportunities_24364 extends SugarTest {
	OpportunityRecord myOpp;
	AccountRecord myAcct;
	StandardSubpanel contactsSubpanel;
	DataSource contactDS;

	public void setup() throws Exception {
		sugar().login();
		myOpp = (OpportunityRecord) sugar().opportunities.api.create();
		myAcct = (AccountRecord) sugar().accounts.api.create();
		contactDS = testData.get(testName);
	}

	/**
	 * Test Case 24345: In-Line Create Task_Verify that task can be in-line created from Tasks sub-panel for an opportunity
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24364_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		String contactFullName = ((contactDS.get(0).get("firstName")) + " " + (contactDS.get(0).get("lastName")));
		contactsSubpanel = sugar().opportunities.recordView.subpanels.get("Contacts");
		// Open opportunity record view and inline-create a related contact
		myOpp.navToRecord();
		contactsSubpanel.addRecord();
		sugar().contacts.createDrawer.getEditField("firstName").set(contactDS.get(0).get("firstName"));
		sugar().contacts.createDrawer.getEditField("lastName").set(contactDS.get(0).get("lastName"));
		sugar().contacts.createDrawer.getEditField("relAccountName").set(myAcct.getRecordIdentifier());
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.createDrawer.save();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.waitForAlertExpiration();
		// Verify the contact is successfully created and visible in contact subpanel of the opportunity
		myOpp.navToRecord();
		contactsSubpanel.expandSubpanel();
		new VoodooControl("a", "css", ".fld_full_name.list a").assertContains(contactFullName, true);
		new VoodooControl("a", "css", ".fld_account_name.list a").assertContains(myAcct.getRecordIdentifier(), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}