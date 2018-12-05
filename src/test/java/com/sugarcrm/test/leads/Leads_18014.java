package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_18014 extends SugarTest {

	public void setup() throws Exception {
		// Creating a Lead record via API
		sugar().leads.api.create();

		// Login
		sugar().login();

		// Navigate to Lead list view
		sugar().leads.navToListView();
		sugar().leads.listView.openRowActionDropdown(1);

		// Converting the Lead
		// TODO: VOOD-498 - Need ListView functionality for all row actions
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-585 - Need to have method (library support) to define Convert function in Leads
		// Confirm with Accounts info
		new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input").set(testName);
		new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert-panel-header a").click();
		VoodooUtils.waitForReady();

		// Save and Convert the lead
		new VoodooControl("a", "css", ".convert-headerpane a[name='save_button']").click();
		VoodooUtils.waitForReady();
		sugar().leads.recordView.showMore();
	}

	/**
	 * Duplicating converted record having status is re-set to not New 
	 * @throws Exception
	 */
	@Test
	public void Leads_18014_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Changing the Default value of status to ASSIGNED for leads module
		// TODO: VOOD-542, VOOD-1504
		new VoodooControl("a", "id", "studiolink_Leads").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "status").click();
		VoodooUtils.waitForReady();
		FieldSet leadStatus = testData.get(testName).get(0);
		new VoodooControl("select", "id", "default[]").set(leadStatus.get("leadAssignedStatus"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name='fsavebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Log-out from Admin
		sugar().logout();

		// Login via qaUser
		sugar().login(sugar().users.getQAUser());
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showMore();

		// Assert that the status of the converted lead is CONVERTED.
		sugar().leads.recordView.getDetailField("status").assertEquals(leadStatus.get("leadConvertStatus"), true);

		// Copying the converted lead record
		sugar().leads.recordView.copy();
		VoodooUtils.waitForReady();
		sugar().leads.createDrawer.save();

		// Assert that the status of the copied lead is ASSIGNED.
		sugar().leads.recordView.getDetailField("status").assertEquals(leadStatus.get("leadAssignedStatus"), true);

		// Verifying that all values from original record are copied over
		DataSource customData = testData.get(testName+"_customData");
		FieldSet leadsDefaultData = sugar().leads.getDefaultData();

		// Verifying Account Name is copied correctly
		sugar().leads.recordView.getDetailField("accountName").assertEquals(testName,true);

		// Verifying rest all fields are copied correctly.
		for (int i = 0 ; i < customData.size() ; i++) {
			sugar().leads.recordView.getDetailField(customData.get(i).get("fieldName")).assertEquals(leadsDefaultData.get(customData.get(i).get("fieldName")), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}