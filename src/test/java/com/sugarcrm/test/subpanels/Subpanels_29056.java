package com.sugarcrm.test.subpanels;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vaibhav Singhal <vsinghal@sugarcrm.com>
 */
public class Subpanels_29056  extends SugarTest {
	FieldSet customData;
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.login();
		myAccount = (AccountRecord)sugar.accounts.api.create();	
	}

	/**
	 * Verify that 'Search Subject' filter gets cleared upon using 'Related' filter.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Subpanels_29056_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.accounts.navToListView();		
		sugar.accounts.listView.clickRecord(1);
		
		StandardSubpanel callsSubpanel = sugar.accounts.recordView.subpanels.get(sugar.calls.moduleNamePlural);
		StandardSubpanel meetingsSubpanel = sugar.accounts.recordView.subpanels.get(sugar.meetings.moduleNamePlural);
		
		// Create 2 calls from call subpanel and verify records are created in subpanel
		callsSubpanel.scrollIntoViewIfNeeded(false);
		callsSubpanel.addRecord();
		VoodooControl callNameFld = sugar.calls.createDrawer.getEditField("name");
		callNameFld.set(customData.get("relatedCall1"));
		sugar.calls.createDrawer.save();
		sugar.alerts.getSuccess().closeAlert();
		callsSubpanel.addRecord();
		callNameFld.set(customData.get("relatedCall2"));
		sugar.calls.createDrawer.save();
		sugar.alerts.getSuccess().closeAlert();	

		callsSubpanel.getDetailField(1, "name").assertEquals(customData.get("relatedCall2"), true);
		callsSubpanel.getDetailField(2, "name").assertEquals(customData.get("relatedCall1"), true);
		
		// Create 2 meetings from meetings subpanel and verify records are created in subpanel
		meetingsSubpanel.scrollIntoViewIfNeeded(false);
		meetingsSubpanel.addRecord();
		VoodooControl meetNameFld = sugar.meetings.createDrawer.getEditField("name");
		meetNameFld.set(customData.get("relatedMeeting1"));
		sugar.meetings.createDrawer.save();
		sugar.alerts.getSuccess().closeAlert();
		meetingsSubpanel.addRecord();
		meetNameFld.set(customData.get("relatedMeeting2"));
		sugar.meetings.createDrawer.save();
		sugar.alerts.getSuccess().closeAlert();

		// TODO: VOOD-1443 -Incorrect CSS value being returned for subPanel fields.
		VoodooControl meetingRow1 = 
				new VoodooControl("a", "css", "[data-voodoo-name='"+sugar.meetings.moduleNamePlural+"'] tbody tr:nth-of-type(1) .fld_name.list a");
		VoodooControl meetingRow2 = 
				new VoodooControl("a", "css", "[data-voodoo-name='"+sugar.meetings.moduleNamePlural+"'] tbody tr:nth-of-type(2) .fld_name.list a");
		meetingRow1.assertContains(customData.get("relatedMeeting2"), true);
		meetingRow2.assertContains(customData.get("relatedMeeting1"), true);
		
		//meetingsSubpanel.getDetailField(1, "name").assertEquals(customData.get("relatedMeeting2"), true);
		//meetingsSubpanel.getDetailField(2, "name").assertEquals(customData.get("relatedMeeting1"), true);
		
		// Type "call" in related subpanel filter and observer only records with "call" in name are displayed in all subpanels
		sugar.accounts.recordView.getControl("searchFilter").scrollIntoViewIfNeeded(true);
		sugar.accounts.recordView.setSearchString(customData.get("relatedSearch"));
		VoodooUtils.waitForReady();
		callsSubpanel.getDetailField(1, "name").assertEquals(customData.get("relatedCall2"), true);
		callsSubpanel.getDetailField(2, "name").assertVisible(false);
		meetingRow1.assertContains(customData.get("relatedMeeting2"), true);
		meetingsSubpanel.getDetailField(2, "name").assertVisible(false);
		
		// Set related to filter as calls and verify the records
		sugar.accounts.recordView.setRelatedSubpanelFilter(sugar.calls.moduleNamePlural);
		callsSubpanel.getDetailField(1, "name").assertEquals(customData.get("relatedCall2"), true);
		callsSubpanel.getDetailField(2, "name").assertEquals(customData.get("relatedCall1"), true);
		
		// Set related to filter as meetings and verify the records
		sugar.accounts.recordView.setRelatedSubpanelFilter(sugar.meetings.moduleNamePlural);
		meetingRow1.assertContains(customData.get("relatedMeeting2"), true);
		meetingRow2.assertContains(customData.get("relatedMeeting1"), true);
		
		// Clear related to filter, then set related to filter as meetings, then calls and then verify the records
		sugar.accounts.recordView.setRelatedSubpanelFilter(customData.get("defaultFilter"));
		sugar.accounts.recordView.setRelatedSubpanelFilter(sugar.meetings.moduleNamePlural);
		sugar.accounts.recordView.setRelatedSubpanelFilter(sugar.calls.moduleNamePlural);
		callsSubpanel.getDetailField(1, "name").assertEquals(customData.get("relatedCall2"), true);
		callsSubpanel.getDetailField(2, "name").assertEquals(customData.get("relatedCall1"), true);
		meetingsSubpanel.assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
