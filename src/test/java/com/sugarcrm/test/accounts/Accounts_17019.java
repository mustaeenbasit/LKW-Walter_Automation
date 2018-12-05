package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

/**
 * @author Vaibhav Singhal <vsinghal@sugarcrm.com>
 */
public class Accounts_17019 extends SugarTest {
	FieldSet customData;
	String dataNameDraggableField;
	AccountRecord myAccount;
	VoodooControl accountsCtrl;
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		sugar().login();

		// TODO: VOOD-938
		// Go to Admin > Studio > Accounts > Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountsCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		
		// Add field and save
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		new VoodooControl("select", "id", "type").set(customData.get("dataType"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("fieldName"));
		new VoodooControl("input", "css", "input[name='required']").set(Boolean.toString(true));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();
		
		// Add created URL field to Record view
		sugar().admin.studio.clickStudio();
		accountsCtrl.click();
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		new VoodooControl("td", "id", "viewBtnrecordview").click(); 
		VoodooUtils.waitForReady();
		
		// Add one row
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		
		// Add Custom text field to Record View
		dataNameDraggableField = String.format("div[data-name=%s_c]",customData.get("fieldName"));
		new VoodooControl("div", "css", dataNameDraggableField).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"));
		new VoodooControl("input", "id", "publishBtn").click();   
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Validation check for textfield type field on create form.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_17019_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(customData.get("name"));
		sugar().accounts.createDrawer.save();
		
		// TODO: VOOD-1036
		VoodooControl customTextFld = new VoodooControl("input", "css", "input[name='"+customData.get("fieldName")+"_c']");
		// Verify that error is thrown to input required custom field
		customTextFld.assertAttribute("class", "required", true);
		sugar().alerts.getError().assertContains(customData.get("error_message"), true);
		sugar().alerts.getAlert().closeAlert();
		//Verify that the string in custom field does not exceed the max length
		customTextFld.set(customData.get("custom_val"));
		customTextFld.assertAttribute("maxLength", customData.get("maxLength"), true);
		sugar().accounts.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}