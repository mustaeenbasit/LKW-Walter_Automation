package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_27187 extends SugarTest {
	FieldSet myData = new FieldSet();
	LeadRecord leadRecord;
	
	public void setup() throws Exception {
		leadRecord = (LeadRecord) sugar().leads.api.create();
		sugar().accounts.api.create();
		myData = testData.get(testName).get(0);
		sugar().login();
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-1504: Support Studio Module Fields View
		VoodooControl opportunityModule = new VoodooControl("a", "id", "studiolink_Opportunities");
		opportunityModule.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		
		// Create a custom field as follows in Opportunities Module with 'DataType: Date', 'Name: DateTest' and 'Calculated Value (Formula): today()'
		new VoodooControl("input", "css", "input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(myData.get("field_name"));
		new VoodooControl("input", "id", "calculated").click();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		new VoodooControl("textarea", "id", "formulaInput").set(myData.get("formula"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#footerHTML input[value='Studio']").click();
		VoodooUtils.waitForReady();
		opportunityModule.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		
		// Add custom fields to Record view
		// TODO: VOOD-1506: Support Studio Module RecordView Layouts View
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s]",myData.get("display_name"));
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify calculated fields in lead conversion.
	 * @throws Exception
	 */
	@Test
	public void Leads_27187_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		leadRecord.navToRecord();
		sugar().leads.recordView.openPrimaryButtonDropdown();
		
		// Click "Convert Lead" button in "Lead" detail view.
		// TODO: VOOD-585
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		VoodooUtils.waitForReady();
		
		// Fill in Account name and click Associate Account
		new VoodooControl("input", "css", "#collapseAccounts .fld_name input").set(sugar().accounts.getDefaultData().get("name"));
		new VoodooControl("a", "css", ".active [data-module='Accounts'] .fld_associate_button a").click();
		
		// Fill in Opportunity name and click Associate Opportunity
		new VoodooControl("input", "css", "#collapseOpportunities .fld_name input").set(myData.get("opportunityName"));
		new VoodooControl("a", "css", ".active [data-module='Opportunities'] .fld_associate_button a").click();

		// Click Save and Convert.
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();
		
		// Verify the Lead is converted properly without errors.
		new VoodooControl("span", "css", ".detail.fld_converted span").assertContains(myData.get("convertedBadge"), true);
		
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		String customFieldValue = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		
		// Verify value of the custom field in opportunity record
		// TODO: VOOD-1036: Need library support for Accounts/any sidecar module for newly created custom fields
		new VoodooControl("span", "css", "[data-voodoo-name='datetest_c']").assertContains(customFieldValue, true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}