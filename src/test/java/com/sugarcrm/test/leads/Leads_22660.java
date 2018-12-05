package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_22660 extends SugarTest {

	public void setup() throws Exception {
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		// Create a lead record
		sugar().leads.api.create();
		// Login to SugarCRM as an admin
		sugar().login();
		// Configure email server
		sugar().admin.setEmailServer(emailSetup);
	}

	/**
	 * Compose Email_Verify that the composed email record is not created for lead when using "Cancel" function.
	 * @throws Exception
	 */
	@Test
	public void Leads_22660_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to the Leads list view and click the lead name to open it in record view
		sugar().navbar.navToModule(sugar().leads.moduleNamePlural);
		sugar().leads.listView.clickRecord(1);

		// Compose an email via email Subpanel i.e in the lead's record view
		StandardSubpanel emailsSubpanel = sugar().leads.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailsSubpanel.scrollIntoViewIfNeeded(false);
		emailsSubpanel.composeEmail();

		// Enter To: value
		sugar().leads.recordView.composeEmail.getControl("toAddress").set(sugar().users.getQAUser().get("userName"));
		// TODO: VOOD-843
		new VoodooControl("div", "class", "select2-result-label").click();
		VoodooUtils.waitForReady();
		// Enter Subject
		sugar().leads.recordView.composeEmail.getControl("subject").set(testName);
		// Enter the Body content
		sugar().leads.recordView.composeEmail.addBodyMessage(testName);

		// Click on the cancel link in the email composer
		sugar().leads.recordView.composeEmail.cancel();

		// Expand the subapanel and assert that there is no email record in the subpanel
		emailsSubpanel.scrollIntoViewIfNeeded(false);
		emailsSubpanel.expandSubpanel();
		Assert.assertTrue("The Email subpanel contains Records!!", emailsSubpanel.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}