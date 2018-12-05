package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.test.SugarTest;

public class Contacts_24160 extends SugarTest {
	CampaignRecord myCampaign;

	public void setup() throws Exception {
		sugar().contacts.api.create();
		myCampaign = (CampaignRecord) sugar().campaigns.api.create();

		// Login as admin user
		sugar().login();
	}

	/** Edit contact_Verify that quick search is enabled for campaign to relate to a contact.
	 * @throws Exception
	 */
	@Test
	public void Contacts_24160_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to contacts record view and edit
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		VoodooUtils.waitForReady();

		// Fill Campaign field with a letter, and make sure there are matching campaign records
		// TODO: VOOD-1418
		VoodooControl selectCampaignRecordCtrl = new VoodooControl("li", "css", ".select2-result");
		new VoodooControl("a", "css", ".fld_campaign_name.edit").scrollIntoViewIfNeeded(true);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#select2-drop div input").set(myCampaign.getRecordIdentifier().substring(0, 1));
		VoodooUtils.waitForReady();

		// Verify that the Campaign record is searched out
		selectCampaignRecordCtrl.assertContains(myCampaign.getRecordIdentifier(), true);
		selectCampaignRecordCtrl.click();

		// Cancel the edit view of the contact
		sugar().contacts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}