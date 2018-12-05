package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Leads_28280 extends SugarTest {
	VoodooControl layoutSubPanelCtrl, leadsButtonCtrl;
	ContactRecord myContactRecord;
	
	public void setup() throws Exception {
		sugar().leads.api.create();
		myContactRecord = (ContactRecord) sugar().contacts.api.create();
		sugar().login();
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		leadsButtonCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		
		// TODO: VOOD-1026
		// studio > leads > layouts > Convert Lead > add RLI module 
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		leadsButtonCtrl.click();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "css", "#layoutsBtn tr:nth-child(1) td").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "select[name='convertSelectNewModule']").set(sugar().calls.moduleNamePlural);
		new VoodooControl("input", "css", "input[name='addModule']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "select[name='convertSelectNewModule']").set(sugar().meetings.moduleNamePlural);
		new VoodooControl("input", "css", "input[name='addModule']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Logout as Admin and login as qauser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that Lead and Contact are in the guests when convert a lead
	 *
	 * @throws Exception
	 */
	@Test
	public void Leads_28280_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();
		
		// TODO: VOOD-585 -Need to have method (library support) to define Convert function in Leads
		// Click on convert link
		new VoodooControl("a", "css", ".fld_lead_convert_button.detail a").click();
		VoodooUtils.waitForReady();
		
		// Confirm with the Contacts info
		new VoodooControl("a", "css", "div[data-module='Contacts'] .fld_associate_button.convert-panel-header a").click();
		
		// Confirm with Accounts info
		VoodooControl accFieldInput = new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input");
		accFieldInput.waitForVisible();
		accFieldInput.set(testName);
		new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert-panel-header a").click();
		
		// Confirm with Opportunities info
		VoodooControl oppFieldInput = new VoodooControl("input", "css", "div[data-module='Opportunities'] .fld_name.edit input");
		oppFieldInput.waitForVisible();
		oppFieldInput.set(testName);
		new VoodooControl("a", "css", "div[data-module='Opportunities'] .fld_associate_button.convert-panel-header a").click();
		
		// Confirm with Calls info
		VoodooControl callFieldInput = new VoodooControl("input", "css", "div[data-module='Calls'] .fld_name.edit input");
		callFieldInput.waitForVisible();
		callFieldInput.set(testName);
		
		// Related to contact with a call
		new VoodooSelect("div", "css", ".fld_parent_name.edit .flex-relate-module").set(sugar().contacts.moduleNameSingular);
		new VoodooSelect("div", "css", ".fld_parent_name.edit .flex-relate-record").set(myContactRecord.getRecordIdentifier());
		new VoodooControl("a", "css", "div[data-module='Calls'] .fld_associate_button.convert-panel-header a").click();
		
		// Confirm with Meetings info
		VoodooControl meetingFieldInput = new VoodooControl("input", "css", "div[data-module='Meetings'] .fld_name.edit input");
		meetingFieldInput.waitForVisible();
		meetingFieldInput.set(testName);
		
		// Related to lead with a meeting
		new VoodooSelect("div", "css", "div[data-module='Meetings'] .fld_parent_name.edit .flex-relate-module").scrollIntoView();
		new VoodooSelect("div", "css", "div[data-module='Meetings'] .fld_parent_name.edit .flex-relate-module").set(sugar().leads.moduleNameSingular);
		new VoodooSelect("div", "css", "div[data-module='Meetings'] .fld_parent_name.edit .flex-relate-record").set(sugar().leads.getDefaultData().get("fullName"));
		new VoodooControl("a", "css", "div[data-module='Meetings'] .fld_associate_button.convert-panel-header a").click();
		
		// Save the conversion
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();
		
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		
		StandardSubpanel callSubPanel = sugar().leads.recordView.subpanels.get(sugar().calls.moduleNamePlural);
		StandardSubpanel meetingSubPanel = sugar().leads.recordView.subpanels.get(sugar().meetings.moduleNamePlural);
		
		// Click on call sub-panel preview panel
		callSubPanel.scrollIntoView();
		callSubPanel.clickPreview(1);
		sugar().previewPane.showMore();
		
		// TODO: VOOD-1428
		VoodooControl inviteeParticipants = new VoodooControl("span", "css", ".detail.fld_invitees .participants");
		
		// Verify that in Call preview, 4 call attendees: user Sally, Contact (from Related To field), the Lead (is just converted Lead), the Contact (converted from the Lead)
		inviteeParticipants.assertContains(myContactRecord.getRecordIdentifier(), true);
		inviteeParticipants.assertContains(sugar().leads.getDefaultData().get("fullName"), true);
		inviteeParticipants.assertContains(sugar().leads.getDefaultData().get("fullName"), true);
		inviteeParticipants.assertContains(sugar().users.getQAUser().get("userName"), true);
		
		// Click on meeting sub-panel preview panel
		meetingSubPanel.clickPreview(1);
		VoodooUtils.waitForReady();
		sugar().previewPane.showMore();
		
		// Verify that in Meetings, 3 meeting attendees: user Sally, the Lead (is just converted Lead), the Contact (converted from the Lead).
		inviteeParticipants.assertContains(sugar().leads.getDefaultData().get("fullName"), true);
		inviteeParticipants.assertContains(sugar().leads.getDefaultData().get("fullName"), true);
		inviteeParticipants.assertContains(sugar().users.getQAUser().get("userName"), true);
		
		// Verify that the Lead you are converting has assigned to is not qauser
		sugar().leads.recordView.getDetailField("relAssignedTo").assertContains(sugar().users.getQAUser().get("userName"), false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}