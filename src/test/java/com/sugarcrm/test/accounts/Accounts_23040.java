package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_23040 extends SugarTest {

	public void setup() throws Exception {
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		sugar().accounts.api.create();
		sugar().login();
		
		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);
		
		// Set email settings individually
		sugar().inboundEmail.create();
	}

	/**
	 * Account Detail - Emails sub-panel - Compose Email_Verify that new "Compose Email" is correctly 
	 * created for Activities under "Activities" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_23040_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel emailSubpanel = sugar().accounts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubpanel.composeEmail();
		sugar().accounts.recordView.composeEmail.getControl("addressBook").click();
		
		// TODO: VOOD-1423 -Need lib support for Accounts > recordView > composeEmail > ToAddress > AddressBook	 
		VoodooControl checkbox = new VoodooControl("input", "css", ".flex-list-view tr:nth-child(1) td:nth-child(1) input[type='checkbox']");
		checkbox.waitForVisible();
		checkbox.click();
		new VoodooControl("a", "css", "a[name='done_button']").click();
		
		FieldSet fs = testData.get(testName).get(0);
		sugar().accounts.recordView.composeEmail.getControl("subject").set(fs.get("subject"));
		sugar().accounts.recordView.composeEmail.addBodyMessage(fs.get("body"));
		sugar().accounts.recordView.composeEmail.getControl("sendButton").click();
		VoodooUtils.waitForReady();
		
		// Verify newly composed email is displayed correctly on the Record view in Emails subpanel
		emailSubpanel.assertContains(fs.get("subject"), true);
		emailSubpanel.assertContains(fs.get("mail_status_sent"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}