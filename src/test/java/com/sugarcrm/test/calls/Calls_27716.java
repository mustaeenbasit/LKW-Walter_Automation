package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class Calls_27716 extends SugarTest {
	UserRecord qaUser;
	ContactRecord myContact;
	LeadRecord leadRecord;
	
	public void setup() throws Exception {
		qaUser =  new UserRecord(sugar.users.getQAUser());
		myContact = (ContactRecord) sugar.contacts.api.create();
		leadRecord = (LeadRecord) sugar.leads.api.create();
		sugar.calls.api.create();
		sugar.login(qaUser);
	}

	/**
	 * Verify that when a Contact/Lead is removed before saving even though it is in "Related To" field
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_27716_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar.calls.navToListView();
		sugar.calls.listView.clickRecord(1);
		sugar.calls.recordView.edit();
		
		// In the "Related To" field, select one Contact
		sugar.calls.createDrawer.getEditField("relatedToParentType").set(sugar.contacts.moduleNameSingular);
		sugar.calls.createDrawer.getEditField("relatedToParentName").set(sugar.contacts.getDefaultData().get("lastName"));
		
		// In the "Related To" field, select one Lead
		sugar.calls.createDrawer.getEditField("relatedToParentType").set(sugar.leads.moduleNameSingular);
		sugar.calls.createDrawer.getEditField("relatedToParentName").set(sugar.leads.getDefaultData().get("lastName"));
		
		// Observe that Contact and Lead are appearing in Guests field
		sugar.calls.createDrawer.getControl("invitees").assertContains(myContact.getRecordIdentifier(), true);
		sugar.calls.createDrawer.getControl("invitees").assertContains(leadRecord.getRecordIdentifier(), true);
		
		// Remove Contact and Lead from guest list
		new VoodooControl("button", "css", "[data-module='Contacts'] .fa-minus").click();
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("button", "css", "[data-module='Leads'] .fa-minus").click();
		sugar.alerts.waitForLoadingExpiration();
		sugar.calls.recordView.save();
		
		// Observe that Contact and Lead are not appearing in Guests field
		sugar.calls.recordView.getControl("invitees").assertContains(myContact.getRecordIdentifier(), false);
		sugar.calls.recordView.getControl("invitees").assertContains(leadRecord.getRecordIdentifier(), false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}