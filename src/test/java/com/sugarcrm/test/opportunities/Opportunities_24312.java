package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24312 extends SugarTest {

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		sugar().login();

		// Set email settings in admin
		sugar().admin.setEmailServer(emailSetup);
	}

	/**
	 * Verify that a new email record with "In Draft" status is created in "Email" sub-panel after saving a new composed email in draft
	 *
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24312_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel emailsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailsSubpanel.scrollIntoViewIfNeeded(false);
		emailsSubpanel.composeEmail();

		// Go to Addressbook and select ToAddress
		sugar().opportunities.recordView.composeEmail.getControl("addressBook").click();

		// TODO: VOOD-1423 -Need lib support for Opportunity > recordView > composeEmail > ToAddress > AddressBook	 
		VoodooControl checkbox = new VoodooControl("input", "css", ".flex-list-view tr:nth-child(1) td:nth-child(1) input[type='checkbox']");
		checkbox.waitForVisible();
		checkbox.click();
		new VoodooControl("a", "css", "a[name='done_button']").click();

		// Fill composeEmail fields(subject & body) and save as draft
		FieldSet customFS = testData.get(testName).get(0);
		sugar().opportunities.recordView.composeEmail.getControl("subject").set(customFS.get("subject"));
		VoodooUtils.waitForReady();
		sugar().opportunities.recordView.composeEmail.addBodyMessage(customFS.get("body"));
		sugar().opportunities.recordView.composeEmail.getControl("actionDropdown").click();
		VoodooUtils.waitForReady();
		sugar().opportunities.recordView.composeEmail.getControl("saveDraft").click();
		VoodooUtils.waitForReady();

		// Verify that a new composed email record with "In Draft" status is displayed in "Email" sub-panel for opportunity recordView.
		emailsSubpanel.scrollIntoViewIfNeeded(false);
		emailsSubpanel.assertContains(customFS.get("subject"), true);
		emailsSubpanel.assertContains(customFS.get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}