package com.sugarcrm.test.contacts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Alexander Petrovets <apetrovets@sugarcrm.com>
 */

public class Contacts_24240 extends SugarTest {
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Test Case 24240: Check the "Reports to" field after create a direct report from subpanel
	 * @throws Exception
	 */
	@Test
	public void Contacts_24240_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		StandardSubpanel directReportsSub = sugar().contacts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		directReportsSub.addRecord();
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		sugar().contacts.createDrawer.save();

		// Reload the contact record and verify that "Reports To" field does not contain related contact's name value
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(2);
		sugar().contacts.recordView.showMore();
		VoodooUtils.waitForReady();

		// Verify that 'Report To' field is blank
		// TODO: VOOD-911
		new VoodooControl("span", "css", ".fld_report_to_name.detail").assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}