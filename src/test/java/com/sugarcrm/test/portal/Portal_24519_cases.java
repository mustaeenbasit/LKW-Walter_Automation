package com.sugarcrm.test.portal;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.PortalTest;

public class Portal_24519_cases extends PortalTest {
	VoodooControl casesModuleCtrl, layoutPanelCtrl, fieldCtrl;
	FieldSet customField;

	public void setup() throws Exception {
		customField = testData.get(testName+"_fields").get(0);		
		AccountRecord myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().contacts.api.create();

		sugar().login();

		// Setup Portal Access
		sugar().admin.portalSetup.enablePortal();

		// TODO: VOOD-938
		// studio
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		casesModuleCtrl = new VoodooControl("a", "id", customField.get("module_name"));
		layoutPanelCtrl = new VoodooControl("td", "id", "layoutsBtn");
		fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");

		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		new VoodooControl("a", "id", customField.get("module_name")).click();
		VoodooUtils.waitForReady();
		fieldCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customField.get("module_field_name"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "calculated").click();		
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("textarea", "css", "#formulaInput").set(customField.get("formula"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady(30000);
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();

		// List view
		new VoodooControl("a", "id", customField.get("module_name")).click();
		VoodooUtils.waitForReady();
		layoutPanelCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnlistview tr:nth-child(1) td a").click(); 
		VoodooUtils.waitForReady();
		VoodooControl moveHere = new VoodooControl("td", "css", "#Default");
		new VoodooControl("li", "css", ".draggable[data-name='"+customField.get("module_field_name")+"_c").dragNDrop(moveHere);
		new VoodooControl("input", "css" ,"input[name='savebtn']").click();
		VoodooUtils.waitForReady(30000);
		sugar().admin.studio.clickStudio();

		VoodooUtils.focusDefault();

		// For protal layout 
		// Go to Admin -> Sugar Portal -> Layouts -> Bugs
		sugar().admin.navToAdminPanelLink("portalSettings");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1119 -Need Lib support for controls in Admin -> Sugar Portal
		// Add custom field to Portal Record view	
		new VoodooControl("a", "css", "#Layouts > table > tbody > tr:nth-child(2) > td > a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#Buttons table td:nth-child(2) > table tr:nth-child(2) > td > a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#viewBtnRecordView table tr:nth-child(2) a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customField.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.acceptDialog();
		VoodooUtils.waitForReady(30000);
		new VoodooControl("input", "css" ,"#footerHTML input[value='Sugar Portal Editor']").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		sugar().alerts.waitForLoadingExpiration();

		// Add Account to Contact
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();
		sugar().contacts.recordView.getEditField("relAccountName").set(myAccount.getRecordIdentifier());
		sugar().alerts.getAlert().confirmAlert();
		sugar().contacts.recordView.showMore();
		sugar().contacts.recordView.getEditField("portalName").set("portalUser");
		sugar().contacts.recordView.getEditField("password").set("portalUser");
		sugar().contacts.recordView.getEditField("confirmPassword").set("portalUser");
		sugar().contacts.recordView.getEditField("checkPortalActive").set("true");
		sugar().contacts.recordView.save();

		// Logout as Admin
		sugar().logout();
	}

	/**
	 * Calculated fields can be synced from server to portal
	 * @throws Exception
	 */
	@Test
	public void Portal_24519_cases_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Login into Portal
		FieldSet portalUser = new FieldSet();
		portalUser.put("userName", "portalUser");
		portalUser.put("password", "portalUser");
		portal().loginScreen.navigateToPortal();
		portal().login(portalUser);

		// Navigate to case module 
		portal().navbar.navToModule(portal().cases.moduleNamePlural);

		// Create a case record
		FieldSet customData = testData.get(testName).get(0);
		portal().cases.listView.create();
		sugar().cases.createDrawer.getEditField("name").set(customData.get("name"));
		sugar().cases.createDrawer.getEditField("description").set(customData.get("description"));
		sugar().cases.createDrawer.getEditField("status").set(customData.get("status"));
		sugar().cases.createDrawer.getEditField("priority").set(customData.get("priority"));
		portal().cases.createDrawer.save();

		// Click on the Case record from the list view
		// portal().cases.listView.clickRecord(1);
		new VoodooControl("a", "css", "div[data-voodoo-name*='list'] tbody tr:nth-of-type(1) span[class*='name'] a").click();

		// Verify that string name length
		new VoodooControl("div", "css", ".fld_"+customField.get("module_field_name")+"_c.detail div").assertContains("14", true);

		portal().logout();
		sugar().loginScreen.navigateToSugar();

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}