package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.sugar.records.MeetingRecord;
import com.sugarcrm.test.SugarTest;

public class Meetings_19744 extends SugarTest {	
	ContactRecord myCon;
	VoodooControl meetingPanelCtrl, moduleCtrl, layoutCtrl, searchBtnCtrl, filterSearchBtnCtrl, restoreDefault, saveBtnCtrl;
	
	public void setup() throws Exception {
		sugar().login();
		myCon = (ContactRecord) sugar().contacts.api.create();
		
		// Relate meeting with contact 
		FieldSet contactData = new FieldSet();
		contactData.put("relatedToParentType", sugar().contacts.moduleNameSingular);
		contactData.put("relatedToParentName", sugar().contacts.getDefaultData().get("lastName"));
		MeetingRecord myMeeting = (MeetingRecord) sugar().meetings.create(contactData);
		
		// Add 'Related to' field on basic search layout
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");		
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-938 Need library support for studio sub-panel
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		searchBtnCtrl = new VoodooControl("td", "id", "searchBtn");
		filterSearchBtnCtrl = new VoodooControl("td", "id", "FilterSearchBtn");
		restoreDefault = new VoodooControl("input", "id", "historyDefault");
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");

		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		searchBtnCtrl.click();
		VoodooUtils.waitForReady();
		filterSearchBtnCtrl.click();
		VoodooUtils.waitForReady();
		// Drag Related To (parent_name) field from Hidden panel to Default panel 
		// TODO: VOOD-542
		VoodooControl dropCtrl = new VoodooControl("li", "css" ,"td#Default li:nth-of-type(1)");
		new VoodooControl("li", "css" ,"td#Hidden li[data-name='parent_name']").dragNDropViaJS(dropCtrl);
		
		// Save & Deploy
		saveBtnCtrl.click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that Meetings matching "Contact" search condition can be displayed in basic search panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_19744_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Create filter
		sugar().meetings.navToListView();
		sugar().meetings.listView.openFilterDropdown();
		sugar().meetings.listView.selectFilterCreateNew();
		
		// TODO: VOOD-1488
		VoodooSelect filterFieldCtrl = new VoodooSelect("div", "css", "div[data-filter='field']");
		VoodooSelect relatedModuleCtrl = new VoodooSelect("div", "css",  "div[data-filter='value'] [class='flex-relate-module'] a");
		VoodooSelect relatedRecordCtrl = new VoodooSelect("div", "css",  "div[data-filter='value'] [class='flex-relate-record'] a");
		
		// Select Filter Type = "Related to",  Related Module = Contact, Related records = Default Contact name
		filterFieldCtrl.set(testData.get(testName).get(0).get("filterType"));
		relatedModuleCtrl.set(sugar().contacts.moduleNameSingular);
		relatedRecordCtrl.set(myCon.getRecordIdentifier());
		
		// Verify that search out meeting record is Related to Contact
		String fullName = sugar().contacts.getDefaultData().get("firstName") + " " + sugar().contacts.getDefaultData().get("lastName");
		sugar().meetings.listView.verifyField(1, "relatedToParentName", fullName);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
