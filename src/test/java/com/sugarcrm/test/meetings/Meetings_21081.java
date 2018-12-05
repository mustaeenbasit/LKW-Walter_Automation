package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_21081 extends SugarTest {
	VoodooControl contactsSubPanelCtrl;

	public void setup() throws Exception {
		// Create two meetings record with different name
		FieldSet fs = new FieldSet();
		fs.put("name", testName);
		sugar().meetings.api.create();
		sugar().meetings.api.create(fs);
		sugar().login();

		// Go to admin > studio > meetings > fields > date_start and check Mass Update option
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Meetings");
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "date_start").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "massupdate").click(); // Click to checked check-box
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that selected meetings record information is not updated by Mass Update with invalid date format for "Start Date" text field.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_21081_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		sugar().meetings.navToListView();
		sugar().meetings.listView.toggleSelectAll();
		sugar().meetings.listView.openActionDropdown();
		sugar().meetings.listView.massUpdate();

		// TODO VOOD-768
		new VoodooSelect("div", "css" ,"[data-voodoo-name='massupdate'] div.select2-container.select2.mu_attribute").set(customFS.get("selectStartDate"));
		new VoodooControl("input", "css" ,".massupdate.fld_date_start [aria-label='Start Date']").set(customFS.get("wrongStartDate")); // Set wrong format date
		new VoodooControl("a", "css" ,".massupdate.fld_update_button a").click();

		// Get Invalid Value: Start Date message
		new VoodooControl("span", "css", ".massupdate.fld_date_start.error .help-block").assertEquals(customFS.get("message"), true);

		// Cancel massUpdate
		new VoodooControl("a", "css" ,".massupdate a.cancel_button").click();

		// Verify that the selected meetings records start date information is not updated.  
		sugar().meetings.listView.verifyField(1, "date_start_date", sugar().meetings.getDefaultData().get("date_start_date"));
		sugar().meetings.listView.verifyField(2, "date_start_date", sugar().meetings.getDefaultData().get("date_start_date"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}