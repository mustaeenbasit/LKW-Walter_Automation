package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28700 extends SugarTest{
	public void setup() throws Exception {
		sugar().tags.api.create();
		sugar().login();
	}

	/**
	 *  Verify Change Log is updated when change Assigned To field for Tag record
	 *  @throws Exception
	 */
	@Test
	public void Tags_28700_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet viewChangeLogData = testData.get(testName).get(0);
		
		// Go to a tags Record view
		sugar().tags.navToListView();
		sugar().tags.listView.clickRecord(1);
		
		// Edit the assigned to user name field from Administrator to qauser. 
		sugar().tags.recordView.edit();
		sugar().tags.recordView.getEditField("relAssignedTo").set(viewChangeLogData.get("newValue"));
		sugar().tags.recordView.save();
		
		// Click "View Change Log" button
		// TODO: VOOD-738
		sugar().tags.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".detail.fld_audit_button a").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1990
		// Verify Change Log window is popped up
		new VoodooControl("div", "css", ".layout_Audit.drawer").assertContains(viewChangeLogData.get("viewChangeLogText"), true);
		
		// Verify that the change log is displaying correctly
		new VoodooControl("span", "css", ".list.fld_field_name").assertEquals(viewChangeLogData.get("field"), true);
		new VoodooControl("span", "css", ".list.fld_before").assertEquals(viewChangeLogData.get("oldValue"), true);
		new VoodooControl("span", "css", ".list.fld_after").assertEquals(viewChangeLogData.get("newValue"), true);
		new VoodooControl("span", "css", ".list.fld_created_by_username").assertEquals(viewChangeLogData.get("changedBy"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
