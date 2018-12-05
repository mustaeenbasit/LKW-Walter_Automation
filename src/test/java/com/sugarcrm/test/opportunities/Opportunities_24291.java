package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_24291 extends SugarTest {
	
	public void setup() throws Exception {
		// Create 2 contacts and 1 opportunity record
		FieldSet fs = new FieldSet();
		fs.put("firstName", testName);
		sugar().contacts.api.create(fs);
		sugar().contacts.api.create();
		sugar().opportunities.api.create();
		
		sugar().login();
	}

	/**
	 * Select Contact_Verify that "Contact Search" function works successfully in the window which pops up after 
	 * clicking "Select" button in "Contacts" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_24291_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunity record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		
		// Select "Link existing record" button from dropdown in "Contacts" sub-panel
		StandardSubpanel contactsSubpanel = sugar.opportunities.recordView.subpanels.get(sugar.contacts.moduleNamePlural);
		contactsSubpanel.clickLinkExisting();
		
		// Fill the search fields in "Contact Search" sub-panel of the popup window
		sugar().contacts.searchSelect.search(sugar().contacts.getDefaultData().get("firstName"));
		
		// Verify matching contacts member(s) is displayed on the list of the pop-up box
		// TODO: VOOD-1487
		new VoodooControl("div", "css", ".layout_Contacts .list.fld_full_name").assertContains(sugar().contacts.getDefaultData().get("firstName"), true);
		sugar().contacts.searchSelect.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}