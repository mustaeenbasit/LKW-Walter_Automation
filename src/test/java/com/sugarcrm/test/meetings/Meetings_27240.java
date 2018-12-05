package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_27240 extends SugarTest {
	ContactRecord myContact;
	LeadRecord myLead;
	FieldSet customData;
		
	public void setup() throws Exception {
		sugar().login();

		customData = testData.get(testName).get(0);
		sugar().meetings.api.create();
		myContact = (ContactRecord) sugar().contacts.api.create();
		myLead = (LeadRecord) sugar().leads.api.create();
	}

	/**
	 * Verify that meeting preview correctly for recurring meeting from list view
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27240_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		
		// TODO: VOOD-1169
		// Fill Repeat Type = Weekly
		new VoodooSelect("a", "css", ".fld_repeat_type.edit a").set(customData.get("repeat_type"));
		
		// Fill Repeat Occurrences = 3 times.
		new VoodooControl("input", "css", ".fld_repeat_count.edit input").set(customData.get("repeat_count"));

		// Selecting contact
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().contacts.getDefaultData().get("lastName"));		

		// Selecting Lead
		sugar().meetings.recordView.clickAddInvitee();
		sugar().meetings.recordView.selectInvitee(sugar().leads.getDefaultData().get("lastName"));

		sugar().meetings.recordView.save();
		sugar().alerts.waitForLoadingExpiration(30000); // Sometimes, more wait is required
		
		// Open the newly created meeting record
		sugar().meetings.navToListView();
		// Observe that 3 Meetings exist in listview.
		new VoodooControl("tr", "css", ".table.dataTable > tbody > tr:nth-child(1)").assertVisible(true);
		new VoodooControl("tr", "css", ".table.dataTable > tbody > tr:nth-child(2)").assertVisible(true);
		new VoodooControl("tr", "css", ".table.dataTable > tbody > tr:nth-child(3)").assertVisible(true);
		new VoodooControl("tr", "css", ".table.dataTable > tbody > tr:nth-child(4)").assertVisible(false);

		// Verify that data in preview pane for each recurring meeting is correct
		for(int i=1; i <= Integer.parseInt(customData.get("repeat_count")); i++){
			// Click Preview button on Listview rec
		    // TODO: VOOD-1217
			sugar().meetings.listView.previewRecord(i);
		    sugar().alerts.waitForLoadingExpiration();
		    
		    // Confirm RHS Meeting Preview
		    // TODO: VOOD-1217
		    new VoodooControl("div", "css", "div.preview-headerbar[data-voodoo-name='preview-header']").assertVisible(true);
		    new VoodooControl("div", "css", "div.preview-data span.fld_picture.detail").assertContains("Me", true);;
		    sugar().previewPane.getPreviewPaneField("name").assertContains(sugar().meetings.getDefaultData().get("name"), true);
		    sugar().previewPane.getPreviewPaneField("status").assertContains(sugar().meetings.getDefaultData().get("status"), true);
		    new VoodooControl("div", "css", "div.preview-data span.fld_duration.detail").assertContains((new VoodooControl("td", "css", ".table.dataTable > tbody > tr:nth-child("+i+") td:nth-of-type(4)").getText()), true);
		    new VoodooControl("div", "css", "div.preview-data span.fld_location.detail").assertContains(sugar().meetings.getDefaultData().get("location"), true);
		    new VoodooControl("div", "css", "div.preview-data span.fld_description.detail").assertContains(sugar().meetings.getDefaultData().get("description"), true);
		    new VoodooControl("div", "css", "div.preview-data span.fld_invitees.detail").assertContains("Administrator", true);
		    new VoodooControl("div", "css", "div.preview-data span.fld_invitees.detail").assertContains(myContact.getRecordIdentifier(), true);
		    new VoodooControl("div", "css", "div.preview-data span.fld_invitees.detail").assertContains(myLead.getRecordIdentifier(), true);
		    new VoodooControl("div", "css", "div.preview-data span.fld_assigned_user_name.detail").assertContains("Administrator", true);
		    new VoodooControl("div", "css", "div.preview-data span.fld_team_name.detail").assertContains("Global", true);
		}
	    
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}