package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28792 extends SugarTest {
	public void setup() throws Exception {
		// Login as a valid user
		sugar().login();

		// Enabling QuotedLineItems Module through Enable & Disable Modules
		sugar().admin.enableModuleDisplayViaJs(sugar().quotedLineItems);
	}

	/**
	 * Verify user cannot see the dependency child field if he does not have access to the dependent parent
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_28792_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData =testData.get(testName).get(0);
		FieldSet roleData = testData.get("env_role_setup").get(0);

		// Studio Controls
		// TODO: VOOD-542
		VoodooControl qliCtrl = new VoodooControl("a", "id", "studiolink_Products");
		VoodooControl studioFooterLnk = new VoodooControl("input", "css", "#footerHTML input[value='Studio']");

		// Navigate to Admin > Studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// Go to Accounts -> Fields -> Add Field
		// TODO: VOOD-1504
		qliCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		VoodooControl addFieldBtnCtrl = new VoodooControl("input", "css", "input[value='Add Field']");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl dataTypeCtrl = new VoodooControl("select", "id", "type");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");

		// Create Custom check box field
		addFieldBtnCtrl.click();
		VoodooUtils.waitForReady();
		dataTypeCtrl.set(customData.get("firstFieldDataType"));
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(customData.get("firstFieldName"));
		VoodooUtils.waitForReady();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(60000);

		// Create Custom currency field -> Check dependent and set the Visible if formula as $firstCustomField created 
		addFieldBtnCtrl.click();
		VoodooUtils.waitForReady();
		dataTypeCtrl.set(customData.get("secondFieldDataType"));
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(customData.get("secondFieldName"));
		new VoodooControl("input", "id", "dependent").click();
		new VoodooControl("input", "css", "#visFormulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("textarea", "id", "formulaInput").set(customData.get("formula"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady(30000);

		// Add these created 2 custom fields in the Record View Layout
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		qliCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1506
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		// Drag and drop "firsttestfield" & "secondtestfield" field in the Layout
		// Adding New Row & New Filter in Record view layout of Qli
		// TODO: VOOD-1506
		VoodooControl insertNewRow = new VoodooControl("div", "css", "#toolbox .le_row");
		VoodooControl toPanel = new VoodooControl("div", "css", "#panels div:nth-of-type(1).le_panel");
		VoodooControl firstFieldNameCtrl = new VoodooControl("div", "css", "#toolbox div[data-name='" + customData.get("firstFieldName") + "_c']");
		VoodooControl secondFieldNameCtrl = new VoodooControl("div", "css", "#toolbox div[data-name='" + customData.get("secondFieldName") + "_c']");
		VoodooControl filler1 = toPanel.getChildElement("div", "css", ".le_row div:nth-child(1).le_field.special");
		VoodooControl filler2 = new VoodooControl("div", "css", "#panels div:nth-of-type(1).le_panel .le_row div:nth-child(2).le_field.special");
		insertNewRow.dragNDrop(toPanel);
		VoodooUtils.waitForReady();
		firstFieldNameCtrl.scrollIntoViewIfNeeded(false);
		firstFieldNameCtrl.dragNDrop(filler1);
		VoodooUtils.waitForReady();
		secondFieldNameCtrl.scrollIntoViewIfNeeded(false);
		secondFieldNameCtrl.dragNDrop(filler2);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#layoutEditorButtons #publishBtn").click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();

		// Create a role myRole
		AdminModule.createRole(roleData);
		VoodooUtils.waitForReady(); // Wait needed
		VoodooUtils.focusFrame("bwc-frame");

		// Set the custom field firsttestfield as "None" for 'Quoted Line Items' module
		// TODO: VOOD-580, VOOD-856
		new VoodooControl("a", "css", "#contentTable .edit.view tbody tr:nth-child(19) td a").click();
		VoodooUtils.waitForReady();
		VoodooControl fieldCtrl = new VoodooControl("div", "id", "firsttestfield_clink");
		VoodooControl permissionsCtrl = new VoodooControl("select", "id", "flc_guidfirsttestfield_c");
		fieldCtrl.click();
		if(!permissionsCtrl.queryVisible())
			fieldCtrl.click();
		permissionsCtrl.set(roleData.get("roleNone"));

		// Save role
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign QAuser to the Role and logout
		VoodooUtils.focusDefault();
		AdminModule.assignUserToRole(roleData);
		VoodooUtils.waitForReady();

		// Create a quoted line item. Check the custom check box field and set the custom currency field to 9
		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.create();
		sugar().quotedLineItems.createDrawer.showMore();
		sugar().quotedLineItems.createDrawer.getEditField("name").set(testName);
		// TODO: VOOD-1036
		new VoodooControl("input", "css", ".fld_" + customData.get("firstFieldName") + "_c.edit input").set(Boolean.toString(true));
		new VoodooControl("input", "css", "input[name='" + customData.get("secondFieldName") + "_c']").set(customData.get("currencyData"));
		sugar().quotedLineItems.createDrawer.save();

		// Logout from admin and Login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Navigates to the created quoted line item record view
		sugar().quotedLineItems.navToListView();
		sugar().quotedLineItems.listView.clickRecord(1);

		// Verify that "No Access" is displayed under custom checkbox field i.e. firsttestfield_c
		// TODO: VOOD-1445, VOOD-1036
		new VoodooControl("span", "css", ".noaccess.fld_" + customData.get("firstFieldName") + "_c span").assertEquals(customData.get("noAccess"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}