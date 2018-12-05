package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28272 extends SugarTest {
	FieldSet roleRecordData = new FieldSet();
	DataSource studioFieldsData = new DataSource();
	VoodooControl moduleCtrl;
	String firstField = "";
	String secondfield = "";

	public void setup() throws Exception {
		roleRecordData = testData.get("env_role_setup").get(0);
		studioFieldsData = testData.get(testName);

		// Login
		sugar().login();

		// Create a new Role 
		AdminModule.createRole(roleRecordData);

		// Assign role to "QAUser"
		AdminModule.assignUserToRole(roleRecordData);

		// Admin goes to Admin -> Studio
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");

		// Define controls for Studio
		// TODO: VOOD-542, VOOD-1504 and VOOD-1506
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl fieldsBtnCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "input[value='Add Field']");
		VoodooControl typeCtrl = new VoodooControl("select", "id", "type");
		VoodooControl saveBtnCtrl  = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");

		// Define field Name
		firstField = String.format("div[data-name=%s_c]",studioFieldsData.get(0).get("fieldName"));
		secondfield = String.format("div[data-name=%s_c]",studioFieldsData.get(1).get("fieldName"));

		// Accounts -> Fields -> Add Fields
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		fieldsBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Create a couple of fields, Such as drop down type, testdp; multupleSelect type, testmul
		for(int i = 0; i < studioFieldsData.size(); i++) {
			addFieldCtrl.click();
			VoodooUtils.waitForReady();
			typeCtrl.set(studioFieldsData.get(i).get("dataType"));
			VoodooUtils.waitForReady();
			fieldNameCtrl.set(studioFieldsData.get(i).get("fieldName"));
			saveBtnCtrl.click();
			VoodooUtils.waitForReady(60000);
		}
	}

	/**
	 * Verify that "Save Changes" correctly when switching Roles 
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_28272_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Admin goes to Studio -> Accounts -> Layouts -> Record View
		// TODO: VOOD-1506
		sugar().admin.studio.clickStudio();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		// Select custom Role
		// TODO: VOOD-1506
		VoodooControl selectRoleCtrl = new VoodooControl("select", "id", "roleList");
		selectRoleCtrl.set(roleRecordData.get("roleName"));
		VoodooUtils.waitForReady();

		// Add a new row with a couple of new fields, which are not in the Default layouts. Save & Deploy
		// Add one row
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)"));

		// Add Custom fields to layout
		new VoodooControl("div", "css", firstField).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel .le_row .le_field.special"));
		VoodooUtils.waitForReady(30000);
		new VoodooControl("div", "css", secondfield).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel .le_row .le_field.special:nth-of-type(2)"));

		// Save & Deploy
		VoodooControl saveAndDeployBtnCtrl = new VoodooControl("input", "id", "publishBtn");
		saveAndDeployBtnCtrl.click();   
		VoodooUtils.waitForReady();
		// TODO: VOOD-2004
		// TODO: Hard coded wait needed, as it takes time to generate history(time stamp) 
		VoodooUtils.pause(10000);

		// Remove one field, Save & Deploy. Now in View History there is 2nd version of layouts
		// TODO: VOOD-1506
		new VoodooControl("div", "css", firstField).dragNDrop(new VoodooControl("div", "css", "#availablefields .le_field.special"));

		// Save & Deploy
		saveAndDeployBtnCtrl.click();   
		VoodooUtils.waitForReady(60000);

		// Open History View and preview 2nd version of the view
		// TODO: VOOD-1506
		VoodooControl viewHistoryBtnCtrl = new VoodooControl("input", "id", "historyBtn");
		VoodooControl previewBtnCtrl = new VoodooControl("input", "css", "input[value='Preview']");
		VoodooControl previewPanelCtrl = new VoodooControl("div", "css", "#panels:nth-child(2) .le_panel");
		viewHistoryBtnCtrl.click();
		VoodooUtils.waitForReady(30000);

		// Verify that there is second version of layout
		// TODO: VOOD-1506
		new VoodooControl("td", "css", ".oddListRowS1").assertContains(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"), true);
		previewBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify the 2nd version layout having testmultiselect & drop down fields in layout for Role.
		previewPanelCtrl.assertContains(studioFieldsData.get(0).get("fieldName"), true);
		previewPanelCtrl.assertContains(studioFieldsData.get(1).get("fieldName"), true);

		// So on to build a History View list. As long as your current layout is different with Default and 2nd version of layouts
		// Close the View History pop up
		// TODO: VOOD-1506
		VoodooControl closeViewHistoryPopUpCtrl = new VoodooControl("a", "css", "#histWindow .container-close");
		VoodooControl closePreviewTabCtrl = new VoodooControl("a", "css", ".yui-nav li:nth-child(2) .sugar-tab-close");
		closeViewHistoryPopUpCtrl.click();
		closePreviewTabCtrl.click();

		// Save & Deploy again (to make current layout is different with Default)
		saveAndDeployBtnCtrl.click();   
		VoodooUtils.waitForReady(60000);

		// Open History View and preview new version of the view
		viewHistoryBtnCtrl.click();
		VoodooUtils.waitForReady(30000);

		// Verify that there is new version of layout
		previewBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify the new version layout, Only has testmultiselect field in layout for Role.
		previewPanelCtrl.assertContains(studioFieldsData.get(0).get("fieldName"), false);
		previewPanelCtrl.assertContains(studioFieldsData.get(1).get("fieldName"), true);

		// Click on "Restore"
		// TODO: VOOD-1506
		new VoodooControl("input", "css", "input[value='Restore']").click();
		VoodooUtils.waitForReady(30000);

		// Verify that the layout has last saved version
		VoodooControl mainLayoutCtrl = new VoodooControl("div", "id", "panels");
		mainLayoutCtrl.assertContains(studioFieldsData.get(0).get("fieldName"), false);
		mainLayoutCtrl.assertContains(studioFieldsData.get(1).get("fieldName"), true);

		// Close the View History pop up
		closeViewHistoryPopUpCtrl.click();
		VoodooUtils.waitForReady();

		// Switch to a different role, such as "Sales Administrator". Now "Save Changes" pop up. Click on "Save Changes"
		selectRoleCtrl.set(studioFieldsData.get(0).get("differentRole"));
		new VoodooControl("button", "css", "#confirmUnsaved .button-group button:nth-child(3)").click();
		VoodooUtils.waitForReady(30000);

		// Verify that the "Sales Administrator" appears, remains as Default layout
		VoodooControl toolboxLayoutCtrl = new VoodooControl("div", "id", "toolbox");
		toolboxLayoutCtrl.assertContains(studioFieldsData.get(0).get("fieldName"), true);
		toolboxLayoutCtrl.assertContains(studioFieldsData.get(1).get("fieldName"), true);
		mainLayoutCtrl.assertContains(studioFieldsData.get(0).get("fieldName"), false);
		mainLayoutCtrl.assertContains(studioFieldsData.get(1).get("fieldName"), false);

		// Click on Role drop down, Select the custom role
		selectRoleCtrl.set("* " + roleRecordData.get("roleName"));
		VoodooUtils.waitForReady();

		// Verify that the 2nd version of layout has been saved in the Role (for Custom role user only)
		mainLayoutCtrl.assertContains(studioFieldsData.get(0).get("fieldName"), false);
		mainLayoutCtrl.assertContains(studioFieldsData.get(1).get("fieldName"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}