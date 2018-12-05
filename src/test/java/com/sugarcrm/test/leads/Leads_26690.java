package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;
import com.sugarcrm.test.SugarTest;

public class Leads_26690 extends SugarTest {
	LeadRecord leadRecord;
	VoodooControl leadsModuleNameCtrl, accountModuleCtrl, contactModuleCtrl, oppModuleCtrl, layoutBtnCtrl,
		recordViewBtnCtrl, firstPanelCtrl,secondPanelCtrl, saveBtnCtrl, panelViewCtrl ; 

	public void setup() throws Exception {
		leadRecord = (LeadRecord)sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Set to tabs in the modules invloved in lead convert flow.
	 * @throws Exception
	 */
	@Test
	public void Leads_26690_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet leadFieldSet = testData.get(testName).get(0);
		
		// Navigate to studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Define Studio controls
		// TODO: VOOD-517 , VOOD-542, VOOD-1504, VOOD-1506 
		leadsModuleNameCtrl = new VoodooControl("a", "id", "studiolink_Leads");
		layoutBtnCtrl = new VoodooControl("a", "css", "#layoutsBtn tr:nth-child(2) a");
		recordViewBtnCtrl = new VoodooControl("a", "css", "#viewBtnrecordview  tr:nth-child(2) a");
		firstPanelCtrl = new VoodooControl("select", "css", "div.le_panel:nth-of-type(1) span > select");
		secondPanelCtrl = new VoodooControl("select", "css", "div.le_panel:nth-of-type(2) span > select");
		saveBtnCtrl = new VoodooControl("input", "id", "saveBtn");
		panelViewCtrl = new VoodooControl("a", "css", "#mbtabs  div:nth-child(1) a:nth-child(3)");
		accountModuleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		contactModuleCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		oppModuleCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");

		// Change all panel to tab in admin->Studio->Leads->Layouts->Recordview.
		leadsModuleNameCtrl.click();
		VoodooUtils.waitForReady();
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		recordViewBtnCtrl.click();
		VoodooUtils.waitForReady();
		firstPanelCtrl.set(leadFieldSet.get("tab"));
		secondPanelCtrl.set(leadFieldSet.get("tab"));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		panelViewCtrl.click();
		VoodooUtils.waitForReady();

		// Change all panel to tab in admin->Studio->Contacts>Layouts->Recordview
		contactModuleCtrl.click();
		VoodooUtils.waitForReady();
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		recordViewBtnCtrl.click();
		VoodooUtils.waitForReady();
		firstPanelCtrl.set(leadFieldSet.get("tab"));
		secondPanelCtrl.set(leadFieldSet.get("tab"));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		panelViewCtrl.click();
		VoodooUtils.waitForReady();

		// change all panel to tab in admin->Studio->Accounts>Layouts->Recordview
		accountModuleCtrl.click();
		VoodooUtils.waitForReady();
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		recordViewBtnCtrl.click();
		VoodooUtils.waitForReady();
		firstPanelCtrl.set(leadFieldSet.get("tab"));
		secondPanelCtrl.set(leadFieldSet.get("tab"));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		panelViewCtrl.click();
		VoodooUtils.waitForReady();

		// change all panel to tab in admin->Studio->Opportunity>Layouts->Recordview
		oppModuleCtrl.click();
		VoodooUtils.waitForReady();
		layoutBtnCtrl.click();
		VoodooUtils.waitForReady();
		recordViewBtnCtrl.click();
		VoodooUtils.waitForReady();
		firstPanelCtrl.set(leadFieldSet.get("tab"));
		secondPanelCtrl.set(leadFieldSet.get("tab"));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		panelViewCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Navigates to the List view of Leads
		sugar().leads.navToListView();
		sugar().leads.listView.openRowActionDropdown(1);

		// TODO When VOOD-585, Lib Support for Lead Convert, is implemented the
		// VoodooControls can be replaced
		new VoodooControl("a", "css", ".fld_lead_convert_button.list a").click();
		VoodooUtils.waitForReady();
		
		// Associate Accounts to lead.
		new VoodooControl("input", "css", "div[data-module='Accounts'] .fld_name.edit input").
			set(leadFieldSet.get("accountName"));
		new VoodooControl("a", "css", "div[data-module='Accounts'] .fld_associate_button.convert"
				+ "-panel-header a").click();
		VoodooUtils.waitForReady();
		
		// Associate Opportunity to lead.
		new VoodooControl("input", "css","div[data-module='Opportunities'] .fld_name.edit input")
			.set(leadFieldSet.get("opportunityName"));
		new VoodooControl("a", "css","div[data-module='Opportunities'] .fld_associate_button.convert"
				+ "-panel-header a").click();
		VoodooUtils.waitForReady();

		// Save the conversion
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		// Verify the Leads record is converted in record view
		new VoodooControl("span", "css", ".detail.fld_converted span").assertEquals("Converted", true);
		
		// Verify the Leads record is converted in list view
		sugar().leads.navToListView();
		new VoodooControl("span", "css", ".fld_status.list").assertEquals("Converted", true);

		// Verify the record in the account listview 
		sugar().accounts.navToListView();
		sugar().accounts.listView.verifyField(1, "name",leadFieldSet.get("accountName"));

		// Verify the record in the opportunities listview
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.verifyField(1,"name",leadFieldSet.get("opportunityName") );

		// Verify the record in the contacts listview
		sugar().contacts.navToListView();
		sugar().contacts.listView.assertContains(leadRecord.get("fullName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}