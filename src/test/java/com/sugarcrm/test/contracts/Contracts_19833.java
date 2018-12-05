package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_19833 extends SugarTest {

	public void setup() throws Exception {
		sugar.contracts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
		sugar.admin.enableSubpanelDisplayViaJs(sugar.contracts);
	}

	/**
	 * Select Contact_Verify that Contact can be created when selecting contact for Contract.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Contracts_19828_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to contracts module
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1713
		// Click "Select" button in "Contacts" sub-panel.
		new VoodooControl("a", "id", "contracts_contacts_select_button").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusWindow(1);

		// Enter data in the mandatory fields in "Create Contact" sub-panel, such as "Last Name".
		new VoodooControl("input", "css", "#addformlink input").click();
		new VoodooControl("input", "css", "[name='last_name']").set(sugar.contacts.getDefaultData().get("lastName"));
		new VoodooControl("input", "css", "[name='first_name']").set(sugar.contacts.getDefaultData().get("firstName"));
		new VoodooControl("input", "id", "Contacts_popupcreate_save_button").click(); // Click on Save btn

		// Select created Contact record
		new VoodooControl("a", "css", ".oddListRowS1 td:nth-child(1) a").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");

		// Verify that The newly created record is listed in "Contact" list view.
		new VoodooControl("a", "css", "#whole_subpanel_contacts tr:nth-child(3) td:nth-child(1) a").assertEquals(sugar.contacts
				.getDefaultData().get("firstName") + " " + sugar.contacts.getDefaultData().get("lastName"), true);
		VoodooUtils.focusDefault();

		// Verify created contact record appears in the list view
		sugar.contacts.navToListView();
		sugar.contacts.listView.verifyField(1, "fullName", sugar.contacts.getDefaultData().get("firstName") + " "
				+ sugar.contacts.getDefaultData().get("lastName"));

		// Go to the record view
		sugar.contacts.listView.clickRecord(1);

		// Filter subpanels to display only Contracts subpanel
		sugar.contacts.recordView.setRelatedSubpanelFilter(sugar.contracts.moduleNamePlural);

		// Expand Contracts subpanel
		sugar.contacts.recordView.subpanels.get(sugar.contracts.moduleNamePlural).expandSubpanel();

		// Verify that the contract record is linked to the created contact.
		FieldSet fs = new FieldSet();
		fs.put("name", sugar.contracts.getDefaultData().get("name"));
		sugar.contacts.recordView.subpanels.get(sugar.contracts.moduleNamePlural).verify(1, fs, true);

		// Reset filter to display all subpanels on Contacts record view
		sugar.contacts.recordView.setRelatedSubpanelFilter("All");

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}