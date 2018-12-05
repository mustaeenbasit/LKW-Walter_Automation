package com.sugarcrm.test.portal;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.ContactRecord;

public class Portal_24519_contacts extends PortalTest {
	VoodooControl contactsModuleCtrl, fieldCtrl, layoutPanelCtrl, contactsSubPanelCtrl;
	FieldSet customField;
	ContactRecord myContact;
	
	public void setup() throws Exception {
		customField = testData.get(testName).get(0);
		AccountRecord myAccount = (AccountRecord)sugar().accounts.api.create();
		myContact = (ContactRecord)sugar().contacts.api.create();
		sugar().login();
		
		// Setup Portal Access
		sugar().admin.portalSetup.enablePortal();
					
		// TODO: VOOD-938 -Need library support for studio subpanel
		// studio
		contactsModuleCtrl = new VoodooControl("a", "id", customField.get("module_name"));
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		layoutPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");

		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		contactsSubPanelCtrl = new VoodooControl("a", "id", customField.get("module_name"));
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		fieldCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customField.get("module_field_name"));
		new VoodooControl("input", "id", "calculated").click();		
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("textarea", "css", "#formulaInput").set(customField.get("formula"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(30000);

		// List view
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		contactsSubPanelCtrl.click();
		VoodooUtils.waitForReady();
		layoutPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click(); 
		VoodooUtils.waitForReady();
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='"+customField.get("module_field_name")+"_c").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);
		
		// For protal layout 
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row .le_field.special:nth-of-type(2)");
		VoodooUtils.focusDefault();
		
		// Go to Admin -> Sugar Portal -> Layouts -> Bugs
		sugar().admin.navToAdminPanelLink("portalSettings");
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-1119 -Need Lib support for controls in Admin -> Sugar Portal
		// Add custom field to Portal Record view	
		new VoodooControl("a", "css", "#Layouts > table > tbody > tr:nth-child(2) > td > a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Buttons table td:nth-child(3) > table tr:nth-child(2) > td > a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnRecordView table tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customField.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		moveToNewFilter.waitForVisible(6000);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();

		// Add Account to Contact
		myContact.navToRecord();
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getEditField("portalName").set("portalUser");
		sugar().contacts.recordView.getEditField("password").set("portalUser");
		sugar().contacts.recordView.getEditField("confirmPassword").set("portalUser");
		sugar().contacts.recordView.getEditField("checkPortalActive").set("true");
		sugar().alerts.waitForLoadingExpiration();
		sugar().contacts.recordView.save();

		// Logout from Admin
		sugar().logout();
	}
	
	/**
	 * Calculated fields can be synced from server to portal
	 * @throws Exception
	 */
	@Test
	public void Portal_24519_contacts_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Log into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", "portalUser");
		portalUser.put("password", "portalUser");
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);
		sugar().navbar.navToProfile();
		
		// Verify last name length
		new VoodooControl("div ", "css", ".record [data-name='contact_field_c'] .ellipsis_inline").assertContains("4", true);
		
		portal().logout(); // Logout from portal
		sugar().loginScreen.navigateToSugar();
		
		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}