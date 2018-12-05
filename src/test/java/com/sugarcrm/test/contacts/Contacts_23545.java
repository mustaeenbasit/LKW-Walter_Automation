package com.sugarcrm.test.contacts;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23545 extends SugarTest {
	StandardSubpanel leadSub;

	public void setup() throws Exception {
		sugar().login();
		sugar().contacts.create();

		// Add lead record with Contacts module
		sugar().contacts.listView.clickRecord(1);
		leadSub = sugar().contacts.recordView.subpanels.get(sugar().leads.moduleNamePlural);
		leadSub.addRecord();
		sugar().leads.createDrawer.getEditField("lastName").set(testName);
		sugar().leads.createDrawer.save();
	}

	/**
	 * Verify that a related lead can be unlink from contact record view
	 * @throws Exception
	 */
	@Test
	public void Contacts_23545_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verifying lead record is completely unlink from sub-panel
		leadSub.unlinkRecord(1);
		leadSub.expandSubpanel();
		Assert.assertTrue("The subpanel is not empty", leadSub.isEmpty());

		// Verify that lead record still exists
		sugar().leads.navToListView();
		sugar().leads.listView.getDetailField(1, "fullName").assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
