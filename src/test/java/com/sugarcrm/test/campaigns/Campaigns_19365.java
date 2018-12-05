package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.CampaignRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Campaigns_19365 extends SugarTest {
	LeadRecord lead;
	ContactRecord contact;
	CampaignRecord myCampaign;
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		lead = (LeadRecord) sugar().leads.api.create();
		contact = (ContactRecord) sugar().contacts.api.create();
		myCampaign = (CampaignRecord) sugar().campaigns.api.create();
		sugar().login();
		
		myCampaign.navToRecord();
		// TODO: VOOD-1725: Campaigns of type 'Newsletter' display type as blank on detail view when created via API
		sugar().campaigns.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().campaigns.editView.getEditField("type").set(customData.get("type"));
		VoodooUtils.focusDefault();
		sugar().campaigns.editView.save();
		VoodooUtils.focusDefault();
	}

	/**
	 * Newsletter - Opt in newsletter in Leads/Contacts module_Verify that leads or 
	 * contacts can opt in a newsletter in detail view.
	 * @throws Exception
	 * */
	@Test
	public void Campaigns_19365_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Leads
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();
		
		// Select the 'Manage Subscriptions' options
		// TODO: VOOD-695: Lib support for primary buttom dropdown
		VoodooControl manageSubscriptions = new VoodooControl("a", "css", ".fld_manage_subscription_button.detail a");
		manageSubscriptions.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1728: Need library support for Manage Subscriptions in Contacts and Leads
		VoodooControl availableNewsletter = new VoodooControl("li", "css", "#disabled_ul li:nth-child(1)");
		VoodooControl subscribedNewsletters = new VoodooControl("ul", "id", "enabled_ul");
		VoodooControl saveButton = new VoodooControl("button", "id", "save_button");
		
		// Move the newsletter created above into Subscribed Newsletters
		availableNewsletter.dragNDrop(subscribedNewsletters);
		saveButton.click();
		VoodooUtils.focusDefault();
		
		// Navigating to Contacts
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.openPrimaryButtonDropdown();
		
		// Select the 'Manage Subscriptions' options
		manageSubscriptions.click();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Move the newsletter created above into Subscribed Newsletters
		availableNewsletter.dragNDrop(subscribedNewsletters);
		saveButton.click();
		VoodooUtils.focusDefault();
		
		// Go to Campaigns list view.
		sugar().navbar.navToModule(sugar().campaigns.moduleNamePlural);
		sugar().campaigns.listView.clickRecord(1);
		
		// TODO: VOOD-1028: Need library support to Link Existing Record in Campaign detail view 
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".oddListRowS1 [sugar='slot1b'] a").click();
		VoodooUtils.focusDefault();
		
		// Verify that the target list displays the entry count as 2
		sugar().targetlists.recordView.getDetailField("entryCount").assertEquals(customData.get("entryCount"), true);
		
		StandardSubpanel leadSubpanel = sugar().targetlists.recordView.subpanels
				.get(sugar().leads.moduleNamePlural); 
		StandardSubpanel contactSubpanel = sugar().targetlists.recordView.subpanels
				.get(sugar().contacts.moduleNamePlural); 
		
		// Verifying that the lead is displayed in Lead Subpanel
		leadSubpanel.expandSubpanel();
		VoodooUtils.waitForReady();
		leadSubpanel.getDetailField(1, "fullName").assertEquals(lead.get("fullName"), true);
		leadSubpanel.collapseSubpanel();
		
		// Verifying that the contact is displayed in Contact Subpanel
		contactSubpanel.expandSubpanel();
		contactSubpanel.getDetailField(1, "fullName").assertEquals(contact.get("fullName"), true);
		contactSubpanel.collapseSubpanel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}