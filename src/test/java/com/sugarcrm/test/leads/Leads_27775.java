package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_27775 extends SugarTest {
	FieldSet customData = new FieldSet();
	VoodooControl accountsButtonCtrl;

	public void setup() throws Exception {	
		customData = testData.get(testName).get(0);
		sugar().contacts.api.create();
		sugar().leads.api.create();
		sugar().login();

		// studio> Accounts -> Field type (URL)	NOTE: No use of this field, but as defined in setup conditions over Testopia	
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1504
		accountsButtonCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsButtonCtrl.click();

		// Add URL type field
		new VoodooControl("td", "id", "fieldsBtn").click();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "id", "gencheck").set(Boolean.toString(true));
		new VoodooControl("select", "id", "fieldListOptions").set(customData.get("url_option"));		
		new VoodooControl("input", "css", "#fieldListHelper td:nth-of-type(2) input").click();

		// Save field
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		sugar().admin.studio.getControl("studioButton").click(); // some hack after save

		VoodooUtils.focusDefault();
	}

	/**
	 * Verify that lead converts correctly when creating the URL type field in Accounts module 
	 * @throws Exception
	 */
	@Test
	public void Leads_27775_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// By default => Unconverted
		// TODO: VOOD-585
		VoodooControl badgeCtrl = new VoodooControl("span", "css", ".detail.fld_converted span");
		badgeCtrl.assertEquals(customData.get("unconverted_lbl"), true);
		sugar().leads.recordView.openPrimaryButtonDropdown();

		new VoodooControl("a", "css", ".fld_lead_convert_button.detail a").click();
		VoodooUtils.waitForReady();
		
		// Associate Account
		new VoodooControl("input", "css","div[data-module='Accounts'] .fld_name.edit input").set
			(customData.get("acc_name"));
		new VoodooControl("a", "css","div[data-module='Accounts'] .fld_associate_button.convert"
				+ "-panel-header a").click();
		VoodooUtils.waitForReady();

		// Save & Convert
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();
		sugar().leads.recordView.waitForVisible(); // Need record view visibilty

		// Verify the Leads record is converted in record view
		badgeCtrl.assertEquals(customData.get("converted_lbl"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}