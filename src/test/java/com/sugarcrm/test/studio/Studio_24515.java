package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_24515 extends SugarTest {
	VoodooControl accountsSubPanelCtrl, fieldCtrl;
	FieldSet customData = new FieldSet();
	AccountRecord myAccount1, myAccount2;

	public void setup() throws Exception {
		sugar().login();
		
		customData = testData.get(testName).get(0);
		
		myAccount1 = (AccountRecord) sugar().accounts.api.create();
		myAccount2 = (AccountRecord) sugar().accounts.api.create();
		
		accountsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl studioFooterCtrl = sugar().admin.studio.getControl("studioButton");
		VoodooControl layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		
		// TODO: VOOD-542
		// Add field
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		accountsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("field_name"));
		sugar().admin.studio.waitForAJAX();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(30000);

		// Layout	
		// TODO: VOOD-999
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		accountsSubPanelCtrl.click();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542
		// Record View
		recordViewSubPanelCtrl.click();	
		VoodooUtils.waitForReady();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady(30000);
		
		// TODO: VOOD-542
		// List View
		studioFooterCtrl.click();
		VoodooUtils.waitForReady();
		accountsSubPanelCtrl.click();
		layoutSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click(); 
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		
		// TODO: VOOD-984
		new VoodooControl("li", "css", ".draggable[data-name='"+customData.get("field_name")+"_c").waitForElement();
		new VoodooControl("li", "css", ".draggable[data-name='"+customData.get("field_name")+"_c").dragNDrop(moveHere);	
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
	}

	/**
	 * Mass update should be available after calculated field feature is disabled
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24515_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().accounts.navToListView();
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.listView.toggleSelectAll();
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.listView.openActionDropdown();		
		
		// TODO: VOOD-938
		new VoodooControl("a", "css", ".btn-group.open .dropdown-menu li:nth-of-type(2)").click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooSelect("span", "css", ".select2-container.mu_attribute span.select2-chosen").set(customData.get("field_lable"));
		new VoodooControl("input", "css", ".datepicker").set(customData.get("date_custom"));
		new VoodooControl("a", "css", ".fld_update_button.massupdate .btn-primary").click();
		sugar().alerts.confirmAllAlerts();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();

		// Verify that field show on mass update panel and Mass update execute correctly
		myAccount1.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		sugar().accounts.recordView.showMore();
		new VoodooControl("div", "css", "span.fld_"+customData.get("field_name")+"_c.detail div").assertContains(customData.get("date_custom"), true);
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("button", "css", "div.headerpane h1 div div button.btn.btn-invisible.next-row").click(); // click on next record
		sugar().accounts.recordView.showMore();
		new VoodooControl("div", "css", "span.fld_"+customData.get("field_name")+"_c.detail div").assertContains(customData.get("date_custom"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}