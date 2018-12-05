package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_27225 extends SugarTest {
	FieldSet customData;
		
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that recurring meeting can be deleted in record view
	 *
	 * @throws Exception
	 */
	@Test
	public void Meetings_27225_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: SC-2765
		// After this is resolved, uncomment below lines and remove the next block
		// sugar().navbar.navToModule(sugar().meetings.moduleNamePlural);
		// sugar().meetings.listView.create();
		// sugar().meetings.recordView.getEditField("name").set(sugar().meetings.getDefaultData().get("name"));
		sugar().meetings.api.create();
		sugar().meetings.navToListView();
		sugar().meetings.listView.clickRecord(1);
		sugar().meetings.recordView.edit();
		
		// TODO: VOOD-1169
		// Fill Repeat Type = Weekly
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
		new VoodooControl("tr", "css", ".table.dataTable > tbody > tr:nth-child(4)").assertVisible(false);
		
		// Click the middle rec to open its recordview
		new VoodooControl("tr", "css", ".table.dataTable > tbody > tr:nth-child(2) td[data-type='name'] a").click();
		sugar().alerts.waitForLoadingExpiration();

		// In Meetings record view action drop down, select "Delete All recurrences"
		// TODO: VOOD-1257
		sugar().meetings.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_delete_recurrence_button.detail a").click();
		
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