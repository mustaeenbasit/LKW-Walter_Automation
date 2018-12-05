package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28090 extends SugarTest {
	FieldSet customFieldAndOppData;
	AccountRecord myAccount;
	VoodooControl moduleCtrl;

	public void setup() throws Exception {
		customFieldAndOppData = testData.get(testName).get(0);
		myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the validation on required MultiSelect field on initial save
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28090_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		VoodooControl fieldsCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[value='Add Field']");
		VoodooControl multiSelectFieldCtrl = new VoodooControl("option", "css", "#type option[value='multienum']");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl requiredFieldCtrl = new VoodooControl("input", "css", "input[name='required']");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css", "input[name='fsavebtn']");
		VoodooControl layoutBtnCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl recordViewCtrl = new VoodooControl("td", "id", "viewBtnrecordview");

		// Go to Opportunities create a new field (Type: multiselect, Default value : blank, Required : Checked)
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		fieldsCtrl.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		multiSelectFieldCtrl.click();
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(customFieldAndOppData.get("fieldName"));
		requiredFieldCtrl.click();

		// Save the MultiSelect field
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Go to Studio -> Opportunity -> Layout -> RecordView
		sugar().admin.studio.clickStudio();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		recordViewCtrl.click();
		VoodooUtils.waitForReady();

		// Add MultiSelect field to the record view
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css",  "div[data-name="+customFieldAndOppData.get("fieldName")+"_c]").dragNDrop(moveToNewFilter);

		// Save & Deploy
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		//  Create a new Opportunity and leave required multiSelect empty
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(testName);
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().opportunities.createDrawer.getEditField("rli_name").set(sugar().opportunities.getDefaultData().get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().opportunities.getDefaultData().get("rli_likely"));

		// Click on save button
		sugar().opportunities.createDrawer.getControl("saveButton").click();

		// Verify that the validation message should be displayed on required field "Error Please resolve any errors before proceeding."
		sugar().alerts.getError().assertContains(customFieldAndOppData.get("errorMessage"), true);
		sugar().alerts.getError().closeAlert();

		// No method to check red color of the field so assert 'error' text in class to verify that the field highlighted in red color
		// TODO: VOOD-1036
		VoodooSelect testMultiSelectFieldCtrl = new VoodooSelect("span", "css", ".fld_"+customFieldAndOppData.get("fieldName")+"_c.edit");
		testMultiSelectFieldCtrl.assertAttribute("class", "error", true);

		// Now select any value from the required MultiSelect field and click on Save
		testMultiSelectFieldCtrl.set(customFieldAndOppData.get("customer"));
		sugar().opportunities.createDrawer.save();

		// Verify that in Opportunities list view only one record(named = Opportunities_28090) exist
		sugar().opportunities.listView.verifyField(1, "name", testName);
		sugar().opportunities.listView.getControl("checkbox02").assertVisible(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}