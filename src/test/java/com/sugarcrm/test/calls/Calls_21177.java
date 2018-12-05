package com.sugarcrm.test.calls;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Calls_21177 extends SugarTest {
	AccountRecord myAcc;
	LeadRecord myLead;
	DataSource leadDS;

	public void setup() throws Exception {
		leadDS = testData.get(testName);

		// 1 Call, 1 Account, 3 Lead records created
		sugar.calls.api.create();
		myAcc = (AccountRecord) sugar.accounts.api.create();
		myLead = (LeadRecord)sugar.leads.api.create();
		sugar.leads.api.create(leadDS);
		sugar.login();

		// Link 1 Lead with account
		myAcc.navToRecord();
		StandardSubpanel conSub = sugar.accounts.recordView.subpanels.get(sugar.leads.moduleNamePlural);
		conSub.linkExistingRecord(myLead);
	}

	/**
	 * Search leads as invitees by Account during Call creation. 
	 * @throws Exception
	 */
	@Ignore("TR-1185, MAR-2609")
	@Test
	public void Calls_21177_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();

		// Search invitee by account record
		sugar.calls.recordView.clickAddInvitee();
		sugar.calls.recordView.selectInvitee(myAcc);
		sugar.calls.recordView.save();

		// Verify the related contact are searched out
		sugar.calls.recordView.getControl("invitees").assertContains(myLead.getRecordIdentifier(), true);
		sugar.calls.recordView.getControl("invitees").assertContains(leadDS.get(0).get("firstName")+" "+leadDS.get(0).get("lastName"),false);
		sugar.calls.recordView.getControl("invitees").assertContains(leadDS.get(1).get("firstName")+" "+leadDS.get(1).get("lastName"),false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}