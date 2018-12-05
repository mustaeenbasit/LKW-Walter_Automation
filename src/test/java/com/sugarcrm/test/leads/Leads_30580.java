package com.sugarcrm.test.leads;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Leads_30580 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// 1 Contact and Lead record created 
		FieldSet fs = new FieldSet();
		fs.put("firstName", customData.get("first_name"));
		fs.put("lastName", customData.get("last_name"));
		sugar().contacts.api.create(fs);
		sugar().leads.api.create(fs);
		sugar().login();

		// TODO: VOOD-444, VOOD-1005, VOOD-1282 - Once resolved record should be created via API
		setEmailAddress(sugar().contacts);
		setEmailAddress(sugar().leads);
	}

	/**
	 * Verify that duplicate contact can be detected during Lead Convert but can't be detected at find duplicate in converted Contact
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_30580_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Convert lead
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-695
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();
		
		// Ignore and create new
		new VoodooControl("a", "css", "#convert-accordion .dupecheck").click();
		new VoodooControl("a", "css", "div[data-module=Contacts] .fld_associate_button a").click();
		new VoodooControl("input", "css", "#collapseAccounts .fld_name.edit input").set(testName);
		new VoodooControl("a", "css", ".accordion-heading.enabled.active .convert-panel-header.fld_associate_button").click();
		new VoodooControl("a", "css", ".convert-headerpane.fld_save_button a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify Lead status has changed to "Converted". 
		sugar().leads.recordView.showMore();
		sugar().leads.recordView.getDetailField("status").assertEquals(customData.get("status"), true);

		// Go to the Contact record that was created during the lead conversion and then "Find Duplicates"
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-568, VOOD-578, VOOD-691, VOOD-738
		// Verify No Data has found.  No duplicate should be detected.
		new VoodooControl("a", "css", ".fld_find_duplicates a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", ".layout_Contacts div[data-voodoo-name=list-bottom] .block-footer").assertEquals(customData.get("no_data_available"), true);
		new VoodooControl("a", "css", ".find-duplicates-headerpane.fld_cancel_button a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	private void setEmailAddress(StandardModule module) throws Exception {
		module.navToListView();
		module.listView.clickRecord(1);
		module.recordView.edit();
		module.recordView.getEditField("emailAddress").set(customData.get("email_address"));
		module.recordView.save();
	}

	public void cleanup() throws Exception {}
}