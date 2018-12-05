package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_27185 extends SugarTest {
	VoodooControl contactSubPanelCtrl, fieldCtrl, studioFooterCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl;
	FieldSet customData, fs;
	ContactRecord myContact;
	AccountRecord myAccount;
	
	public void setup() throws Exception {
		sugar().login();
		customData = testData.get(testName).get(0);
		
		fs = new FieldSet();
		fs.put("name", customData.get("name"));
		fs.put("description", customData.get("description"));
		myAccount = (AccountRecord) sugar().accounts.api.create(fs);
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Verify that calculated text area field is displayed properly in the list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_27185_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-938
		// studio
		contactSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		contactSubPanelCtrl.click();		
		fieldCtrl.click();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("select", "css", "#type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "id", "calculated").click();		
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("textarea", "css", "#formulaInput").set(customData.get("formula"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-999//	
		// List view
		studioFooterCtrl.click();
		contactSubPanelCtrl.click();
		layoutSubPanelCtrl.click();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click(); 
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='"+customData.get("module_field_name")+"_c").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-999//		
		studioFooterCtrl.click();
		contactSubPanelCtrl.click();
		layoutSubPanelCtrl.click();
		// Record view		
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		VoodooUtils.waitForReady();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Create contact
		FieldSet fs1 = new FieldSet();
		fs1.put("relAccountName", myAccount.getRecordIdentifier());
		myContact = (ContactRecord) sugar().contacts.api.create();
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.createDrawer.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.confirmAllAlerts();
		sugar().contacts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		sugar().contacts.navToListView();
		sugar().alerts.waitForLoadingExpiration();
		// Assert: list view appear account description field 
		sugar().contacts.listView.assertContains(customData.get("description"), true);
		
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().alerts.waitForLoadingExpiration();
		// Assert: edit view with disabled class
		new VoodooControl("td", "css" ,"div[data-name='myfield_c'] span:nth-child(1)").assertAttribute("class", "disabled");
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}