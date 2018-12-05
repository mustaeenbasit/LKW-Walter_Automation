package com.sugarcrm.test.leads;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_26852 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Lead convert completes without warning message appears
	 * @throws Exception
	 */
	@Test
	public void Leads_26852_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Convert the Lead
		// TODO: VOOD-585 - Need to have method (library support) to define Convert function in Leads
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_lead_convert_button.detail a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css","div[data-module='Accounts'] .fld_name.edit input")
			.set(sugar().accounts.getDefaultData().get("name"));
		new VoodooControl("a", "css","div[data-module='Accounts'] .fld_associate_button.convert"
				+ "-panel-header a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();

		// Verify that convert process completes without warning message
		Assert.assertTrue("Warning message appears", sugar().alerts.getWarning().queryVisible() == false);
		VoodooUtils.waitForReady();

		// Verify that Converted flag appears in the record view
		new VoodooControl("span", "css", ".detail.fld_converted span").assertEquals("Converted", true);

		// Verify that in Leads record view, a session can preview the new Contact or Account
		new VoodooControl("a", "css", ".layout_Leads tr:nth-child(1) .preview-list-item").click();
		VoodooUtils.waitForReady();
		sugar().previewPane.setModule(sugar().contacts);
		sugar().previewPane.getPreviewPaneField("fullName").assertContains(sugar().leads
				.getDefaultData().get("fullName"), true);

		new VoodooControl("a", "css", ".layout_Leads tr:nth-child(2) .preview-list-item").click();
		VoodooUtils.waitForReady();
		sugar().previewPane.setModule(sugar().accounts);
		sugar().previewPane.getPreviewPaneField("name").assertContains(sugar().accounts
				.getDefaultData().get("name"), true);

		// Verify that Contact and Account are created in individual module
		sugar().contacts.navToListView();
		sugar().contacts.listView.verifyField(1, "fullName", sugar().leads.getDefaultData()
				.get("lastName"));
		sugar().accounts.navToListView();
		sugar().accounts.listView.verifyField(1, "name", sugar().accounts.getDefaultData()
				.get("name"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}