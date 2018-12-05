package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;
import com.sugarcrm.test.SugarTest;

public class Studio_26945 extends SugarTest {
	VoodooControl contactsSubPanelCtrl, fieldCtrl, studioFooterCtrl, layoutSubPanelCtrl, recordViewSubPanelCtrl;
	FieldSet customData;
	AccountRecord myAccount;
	ContactRecord myContact;
	FieldSet fs;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);		
		sugar().login();
		
		contactsSubPanelCtrl = new VoodooControl("a", "id", "studiolink_Contacts");
		studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutSubPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
	}

	/**
	 * Verify calculated test area field in list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_26945_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-938
		// studio
		sugar().admin.adminTools.getControl("studio").click();
		contactsSubPanelCtrl.click();
		fieldCtrl.click();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("select", "css", "#type").set("TextArea");
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "id", "calculated").click();		
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("textarea", "css", "#formulaInput").set(customData.get("formula"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-999
		// List view
		studioFooterCtrl.click();
		contactsSubPanelCtrl.click();
		layoutSubPanelCtrl.click();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click(); 
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='"+customData.get("module_field_name")+"_c").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// create account and contact
		fs = new FieldSet();
		fs.put("description", customData.get("account_desc"));
		myAccount = (AccountRecord) sugar().accounts.api.create(fs);
		myContact = (ContactRecord) sugar().contacts.api.create();
		
		// Relation a contact to account
		fs.clear();
		fs = new FieldSet();
		fs.put("relAccountName", myAccount.getRecordIdentifier());		
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.setFields(fs);
		sugar().alerts.confirmAllAlerts();
		sugar().contacts.recordView.save();
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.pause(1000);
		VoodooUtils.focusDefault();		

		// Verify calculated test area field in list view
		sugar().contacts.navToListView();
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("div", "css" ,".fld_"+customData.get("module_field_name")+"_c.list.disabled").assertContains(customData.get("account_desc"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
