package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Meetings_21176 extends SugarTest {	
	LeadRecord lead1, lead2;
	AccountRecord myAccount;

	public void setup() throws Exception {
		// 2 Leads & 1 account
		myAccount = (AccountRecord)sugar().accounts.api.create();
		lead1 = (LeadRecord)sugar().leads.api.create();
		FieldSet fs = new FieldSet();
		fs.put("firstName", "");
		fs.put("lastName", testName);
		lead2 = (LeadRecord)sugar().leads.api.create(fs);
		sugar().login();

		// TODO: MAR-2697 Once resolved, then lead should be link from accounts record view (problem: account name is not populated in listview, however account name in its lead record view, that's why account record search is not working in meetings invitee)
		// Link account with lead record
		myAccount.navToRecord();
		sugar().accounts.recordView.subpanels.get("Leads").scrollIntoViewIfNeeded(false);
		sugar().accounts.recordView.subpanels.get("Leads").linkExistingRecord(lead1);
	}

	/**
	 * Search leads as invitees by Account during Meeitng creation.
	 * @throws Exception
	 */
	@Test
	public void Meetings_21176_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Meeting -> create -> search invitee
		sugar().meetings.navToListView();
		sugar().meetings.listView.create();

		// Search invitee by account record
		sugar().meetings.createDrawer.clickAddInvitee();
		sugar().meetings.createDrawer.selectInvitee(myAccount.getRecordIdentifier());

		// Verify the related lead are searched out
		VoodooControl inviteeCtrl = sugar().meetings.createDrawer.getControl("invitees");
		inviteeCtrl.assertContains(lead1.getRecordIdentifier(), true);
		inviteeCtrl.assertContains(lead2.getRecordIdentifier(),false);
		sugar().meetings.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}