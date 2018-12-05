package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27241 extends SugarTest {
	FieldSet customData;
		
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().meetings.api.create();
		sugar().login();
	}

	/**
	 * Verify that all recurring meetings can be deleted from listview
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27241_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		sugar().meetings.recordView.showMore();
		
		// TODO: VOOD-1169
		// Fill Repeat Type = Monthly
		new VoodooSelect("a", "css", ".fld_repeat_type.edit a").set(customData.get("repeat_type"));
		
		// Fill Repeat Occurrences = 3 times.
		new VoodooControl("input", "css", ".fld_repeat_count.edit input").set(customData.get("repeat_count"));
				
		sugar().meetings.recordView.save();
		sugar().alerts.waitForLoadingExpiration(50000);
		sugar().meetings.navToListView();
		
		// Observe that 3 Meetings exist in listview.
		new VoodooControl("tr", "css", ".table.dataTable > tbody > tr:nth-child(1)").assertVisible(true);
		new VoodooControl("tr", "css", ".table.dataTable > tbody > tr:nth-child(2)").assertVisible(true);
		new VoodooControl("tr", "css", ".table.dataTable > tbody > tr:nth-child(3)").assertVisible(true);
		
		// In Meetings list view action drop down, select "Delete All recurrences"
		// TODO: VOOD-1257
		sugar().meetings.listView.openRowActionDropdown(2);
		new VoodooControl("a", "css", ".fld_delete_recurrence_button.list a").click();
		
		// Click on Confirm in yellow message bar
		sugar().alerts.confirmAllWarning();
		sugar().alerts.waitForLoadingExpiration();
		
		// Verify that there is no Meeting in listview.
		// TODO: VOOD-1252
		new VoodooControl("tr", "css", ".table.dataTable > tbody > tr:nth-child(1)").assertVisible(false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}