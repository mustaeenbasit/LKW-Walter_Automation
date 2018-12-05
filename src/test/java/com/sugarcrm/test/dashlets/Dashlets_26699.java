package com.sugarcrm.test.dashlets;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Dashlets_26699 extends SugarTest {
	FieldSet customFS = new FieldSet();
	ArrayList<Record> allReords = new ArrayList<Record>();

	public void setup() throws Exception {
		customFS = testData.get(testName).get(0);
		FieldSet emailSetup = testData.get("env_email_setup").get(0);
		sugar().contacts.api.create();

		// Required to set status for meetings, calls & tasks
		FieldSet fs = new FieldSet();
		fs.put("status", customFS.get("status"));
		allReords.add(sugar().meetings.api.create(fs));
		allReords.add(sugar().calls.api.create(fs));
		fs.clear();
		fs.put("status", customFS.get("statusForTask"));
		allReords.add(sugar().tasks.api.create(fs));
		allReords.add(sugar().notes.api.create());
		
		sugar().login();

		// Configure Email settings, In bound, out bound mail settings
		sugar().admin.setEmailServer(emailSetup);
		sugar().inboundEmail.create();

		// To make relationship between calls,meetings,tasks and notes with Contacts
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.subpanels.get(sugar().calls.moduleNamePlural).linkExistingRecord(allReords.get(1));
		sugar().contacts.recordView.subpanels.get(sugar().meetings.moduleNamePlural).linkExistingRecord(allReords.get(0));
		sugar().contacts.recordView.subpanels.get(sugar().tasks.moduleNamePlural).linkExistingRecord(allReords.get(2));
		StandardSubpanel notesSubpanel = sugar().contacts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		notesSubpanel.scrollIntoView();
		notesSubpanel.linkExistingRecord(allReords.get(3));
		StandardSubpanel emailSubpanel = sugar().contacts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubpanel.scrollIntoView();
		emailSubpanel.composeEmail();
		sugar().accounts.recordView.composeEmail.getControl("addressBook").click();

		// TODO: VOOD-1423 -Need lib support for Accounts > recordView > composeEmail > ToAddress > AddressBook	 
		VoodooControl checkbox = new VoodooControl("input", "css", ".flex-list-view tr:nth-child(1) td:nth-child(1) input[type='checkbox']");
		checkbox.waitForVisible();
		checkbox.click();
		new VoodooControl("a", "css", "a[name='done_button']").click();
		VoodooUtils.waitForReady();
		sugar().contacts.recordView.composeEmail.getControl("subject").set(customFS.get("subject"));
		sugar().contacts.recordView.composeEmail.addBodyMessage(customFS.get("emailBody"));

		// Send Email
		sugar().contacts.recordView.composeEmail.getControl("sendButton").click();
		VoodooUtils.waitForReady(25000); // Required more wait to complete above "Send mail" Action
	}

	/**
	 * Verify that in Historical Summary page by default the data is sorted by Date Created in reverse chronological order
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_26699_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Open Historical Summary page by click Historical Summary menu item under Actions dropdown in the contact record view 
		sugar().contacts.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-965 : Support for Historical Summary page
		new VoodooControl("a", "css", ".fld_historical_summary_button.detail a").click();
		VoodooUtils.waitForReady();

		// By default records are sorted by date created in reverse chronological order (Latest modified record appears on top of the History listView)
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable tbody tr:nth-child(1) td[data-type='name'] span a").assertEquals(customFS.get("subject"), true);
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable tbody tr:nth-child(2) td[data-type='name'] span a").assertEquals(allReords.get(3).get("subject"), true);
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable tbody tr:nth-child(3) td[data-type='name'] span a").assertEquals(allReords.get(2).get("subject"), true);
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable tbody tr:nth-child(4) td[data-type='name'] span a").assertEquals(allReords.get(1).get("name"), true);
		new VoodooControl("span", "css", "[data-voodoo-name='history-summary'] .dataTable tbody tr:nth-child(5) td[data-type='name'] span a").assertEquals(allReords.get(0).get("name"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}