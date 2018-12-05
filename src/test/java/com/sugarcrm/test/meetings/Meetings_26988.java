package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
* @author Ashish Jabble <ajabble@sugarcrm.com>
*/
public class Meetings_26988 extends SugarTest {
	VoodooControl meetingsCtrl, layoutSubPanelCtrl, listViewSubPanelCtrl, saveBtnCtrl;
	DataSource meetingDS = new DataSource();

	public void setup() throws Exception {
		meetingDS = testData.get(testName);
		sugar().meetings.api.create(meetingDS);
		sugar().login();

		// TODO: VOOD-542
		meetingsCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		listViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		saveBtnCtrl = new VoodooControl("input", "id" ,"savebtn");

		// Go to Admin > Studio > Meetings > Layouts > Listview. 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		meetingsCtrl.click();
		VoodooUtils.waitForReady();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		listViewSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// Enable "Modified Date" to Default column.
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='date_modified']").dragNDropViaJS(moveHere);
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that default sorting is "Date Modified" column in Meeting list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_26988_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Edit second meeting record
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(2);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.getEditField("description").set(testName);
		sugar().meetings.recordView.save();

		// Verify this record as top most in list
		sugar().meetings.navToListView();
		sugar().meetings.listView.verifyField(1, "name", meetingDS.get(1).get("name"));

		// Verify 3rd record, if you inline edit the existing record, it will stay on the same row
		sugar().meetings.listView.editRecord(3);
		sugar().meetings.listView.getEditField(3, "name").set(meetingDS.get(0).get("name")+"_1"); // Need to modify with new name
		sugar().meetings.listView.saveRecord(3);
		sugar().meetings.listView.verifyField(1, "name", meetingDS.get(1).get("name")); // modify the current record

		// Verify records after refreshing the browser, it will be displayed on the 1st row.
		VoodooUtils.refresh();
		VoodooUtils.waitForReady();
		sugar().meetings.listView.verifyField(1, "name", meetingDS.get(0).get("name")+"_1"); // Verify that modified name should be on top
		sugar().meetings.listView.verifyField(2, "name", meetingDS.get(1).get("name"));
		sugar().meetings.listView.verifyField(3, "name", meetingDS.get(2).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}