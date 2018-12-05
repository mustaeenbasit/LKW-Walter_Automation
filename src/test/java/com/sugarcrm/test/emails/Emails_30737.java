package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_30737 extends SugarTest {

	public void setup() throws Exception {
		// Create a record in Targets Module
		sugar().targets.api.create();
		sugar().login();
		FieldSet customData  = testData.get("env_email_setup").get(0);
		// Edit the record in Target Module by adding an emailAddress 
		sugar().targets.navToListView();
		sugar().targets.listView.editRecord(1);
		new VoodooControl("input", "css", ".flex-list-view.left-actions [name='email']").set(customData.get("userName"));
		sugar.targets.listView.saveRecord(1);
	}

	/**
	 * Verify that when select Targets module in Email address won't generate error
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_30737_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Quick create - Compose Mail
		sugar().navbar.quickCreateAction(sugar().emails.moduleNamePlural);
		VoodooUtils.waitForReady();

		// TODO: VOOD-1423, VOOD-444
		// Click on the address icon
		new VoodooControl("i", "css", "[data-name='to_addresses'] i").click();
		VoodooUtils.waitForReady();

		// Find the Module drop down list
		new VoodooControl("span", "css", ".layout_Emails .select2-choice-type").click();

		// Find only the Targets module
		new VoodooControl("li", "css", ".select2-results li:nth-child(5)").click();
		VoodooUtils.waitForReady();
		//click on check box after selecting only the Targets
		new VoodooControl("input", "css", ".layout_Emails .checkall input").click();

		new VoodooControl("a", "css", ".btn.btn-primary[name='done_button']").click();
		VoodooUtils.waitForReady();

		// Address field in Compose mail
		new VoodooControl("div", "css", ".fld_to_addresses.edit").assertEquals(sugar().targets.getDefaultData().get("fullName") , true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}		

	public void cleanup() throws Exception {}

}
