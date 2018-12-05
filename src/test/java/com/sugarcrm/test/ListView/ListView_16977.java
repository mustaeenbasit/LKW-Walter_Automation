package com.sugarcrm.test.ListView;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_16977 extends SugarTest {
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 *  Verify Create button opens create drawer 
	 */
	@Test
	public void ListView_16977_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Accounts
		sugar.accounts.navToListView();
		sugar.accounts.listView.create();
		sugar.accounts.createDrawer.getControl("cancelButton").assertVisible(true);
		
		// Contacts
		sugar.contacts.navToListView();
		sugar.contacts.listView.create();
		sugar.contacts.createDrawer.getControl("cancelButton").assertVisible(true);
		
		// Opportunities
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.create();
		sugar.opportunities.createDrawer.getControl("cancelButton").assertVisible(true);
		
		// Leads
		sugar.leads.navToListView();
		sugar.leads.listView.create();
		sugar.leads.createDrawer.getControl("cancelButton").assertVisible(true);
		
		// Tasks
		sugar.tasks.navToListView();
		sugar.tasks.listView.create();
		sugar.tasks.createDrawer.getControl("cancelButton").assertVisible(true);
		
		// Notes
		sugar.notes.navToListView();
		sugar.notes.listView.create();
		sugar.notes.createDrawer.getControl("cancelButton").assertVisible(true);
		
		// Cases
		sugar.cases.navToListView();
		sugar.cases.listView.create();
		sugar.cases.createDrawer.getControl("cancelButton").assertVisible(true);
		
		// Targets
		sugar.targets.navToListView();
		sugar.targets.listView.create();
		sugar.targets.createDrawer.getControl("cancelButton").assertVisible(true);
		
		// Target Lists
		sugar.targetlists.navToListView();
		sugar.targetlists.listView.create();
		sugar.targetlists.createDrawer.getControl("cancelButton").assertVisible(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}