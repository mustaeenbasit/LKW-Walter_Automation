package com.sugarcrm.test.contacts;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Contacts_23606 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().contacts.api.create();
		sugar().login();
	}

	/**
	 * Create direct reports : Verify that the new created contact is not displayed in current contact
	 * "Direct Reports" sub-panel without selecting current contact for "Report To" text field.
	 * @throws Exception
	 */
	@Test
	public void Contacts_23606_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.showMore();
		VoodooSelect reportsToSelect = (VoodooSelect) sugar().contacts.recordView.getEditField("reportsTo");
		
		// Click on "Search and Select..." in 'Reports To' field 
		reportsToSelect.clickSearchForMore();
		
		// In SSV drawer, click 'Create' and create a contact
		sugar().contacts.searchSelect.create();
		
		// TODO: VOOD-632 : Selectors fails if one drawer is created on top of other
		new VoodooControl("input", "css", ".active .fld_last_name input").set(testName);
		new VoodooControl("a", "css", ".active .create.fld_save_button a").click();
		VoodooUtils.waitForReady();
		sugar().contacts.recordView.save();
		
		StandardSubpanel directReportsSub = sugar().contacts.recordView.subpanels.get(sugar().contacts.moduleNamePlural);
		directReportsSub.expandSubpanel();
		
		// Verify no contact is displayed in "Direct Reports" sub-panel of "Contact Detail View" page.
		Assert.assertTrue("Contact displayed in subpanel when it should not.", directReportsSub.isEmpty());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}