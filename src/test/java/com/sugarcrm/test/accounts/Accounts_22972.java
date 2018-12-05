package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22972 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 *	Verify that a contact is created in "Full Form" format from "CONTACTS" sub-panel.
	 *
	 *	@throws Exception
	 */
	@Test
	public void Accounts_22972_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to an account record view. 
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel contractsSubpanel = sugar().accounts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		contractsSubpanel.scrollIntoViewIfNeeded(false);
		
		// Click "Create" button on "CONTACTS" sub-panel, then click "Show More" button. 
		contractsSubpanel.addRecord();
		
		// Verify that the contact edit view is displayed.
		sugar().contacts.createDrawer.assertExists(true);
		
		// Fill mandatory fields and click "Save" button.
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		sugar().contacts.createDrawer.save();

		// TODO: VOOD-1424
		// Verify that the related contact is displayed on "CONTACTS" sub-panel.
		new VoodooControl("tr", "css", "[data-subpanel-link='contacts'] tbody tr:nth-of-type(1)").assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}