package com.sugarcrm.test.admin;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;

public class Admin_28187 extends SugarTest {
	FieldSet customData;
	OpportunityRecord myOpp;
	AccountRecord myAccount;
	VoodooControl resetButtonCtrl,resetClickCtrl,relationshipsCtrl,fieldsCtrl,labelsCtrl,layoutsCtrl,extensionsCtrl,opportunitiesCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myOpp = (OpportunityRecord)sugar().opportunities.api.create();
		myAccount=(AccountRecord)sugar().accounts.api.create();

		sugar().login();
		// TODO: VOOD-938
		opportunitiesCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
	}

	/**
	 * Precision Of Decimals should reflect The Field's Precision Setting
	 * 
	 * @throws Exception
	 */
	@Test
	public void Admin_28187_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio > Opportunities > fields
		// TODO: VOOD-938
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		opportunitiesCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();

		// Create new decimal type field
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "id", "type").set(customData.get("dataType"));
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("fieldName"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(30000);

		// Add created Decimal field to Record view
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		opportunitiesCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click(); 
		VoodooUtils.waitForReady();
		VoodooControl dropHere1=new VoodooControl("div", "css", "#panels > div.le_panel:nth-of-type(1) > div.le_row:nth-of-type(3)");
		new VoodooControl("div", "css", "[data-name='decimalfield_c']").dragNDrop(dropHere1);
		new VoodooControl("input", "id", "publishBtn").click(); 
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();

		// Go to Opportunities and put decimal value in the new field
		myOpp.navToRecord();
		sugar().opportunities.recordView.edit();
		// TODO: VOOD-1036 Need library support for Accounts/any sidecar module for newly created custom fields
		new VoodooControl("input", "css", "[name='decimalfield_c']").set(customData.get("data"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().opportunities.recordView.save();

		// Verify round-off value
		new VoodooControl("div", "css", ".fld_decimalfield_c.detail .ellipsis_inline").assertContains(customData.get("roundOffData"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}