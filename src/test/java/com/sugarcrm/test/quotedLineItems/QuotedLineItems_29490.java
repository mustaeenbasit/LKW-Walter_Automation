package com.sugarcrm.test.quotedLineItems;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class QuotedLineItems_29490 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().login();

		// Enable QLI module and sub-panels
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
		sugar().admin.enableSubpanelDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * Verify that "Contact Name" field is not blank when view in QLI subpanel of contact record view 
	 * 
	 * @throws Exception
	 */
	@Test
	public void QuotedLineItems_29490_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a QLI record and select a particular Contact in the 'Contact Name' field.
		FieldSet fs = new FieldSet();
		fs.put("relContactName", sugar().contacts.getDefaultData().get("lastName"));
		sugar().quotedLineItems.create(fs);

		// Go to Contacts and click to open record view of the Contact selected in above.
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		// TODO: VOOD-1993
		// Move to QLI sub-panel.
		sugar().contacts.recordView.setRelatedSubpanelFilter("Quoted Line Items"); 

		// Verify that the Contact Name field in QLI sub-panel is filled with contact selected in above created QLI
		new VoodooControl("span", "css", ".layout_Products .list.fld_contact_name").assertContains(sugar().contacts.getDefaultData().get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}