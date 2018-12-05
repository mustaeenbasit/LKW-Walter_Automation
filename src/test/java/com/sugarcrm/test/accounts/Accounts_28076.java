package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Accounts_28076 extends SugarTest {
	FieldSet customData;
	String dataNameDraggableField;
	VoodooControl saveBtnCtrl,resetButtonCtrl,resetClickCtrl,relationshipsCtrl,fieldsCtrl,labelsCtrl,layoutsCtrl,extensionsCtrl,accountsCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		sugar().login();
		accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
	}

	/**
	 * Verify List View is shown correctly when Multiselect field is added.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_28076_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-938
		// Go to Admin > Studio > Accounts > Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		accountsCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();

		// Add field and save
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		new VoodooControl("select", "id", "type").set(customData.get("dataType"));
		sugar().admin.studio.waitForAJAX(80000);
		new VoodooControl("input", "id", "field_name_id").set(customData.get("fieldName"));
		sugar().alerts.waitForLoadingExpiration();
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// Add MultiSelect field to Record view
		sugar().admin.studio.clickStudio();
		accountsCtrl.click();
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		new VoodooControl("td", "id", "viewBtnrecordview").click(); 
		VoodooUtils.waitForReady();
		// Add one row
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		// Add Custom field
		dataNameDraggableField = String.format("div[data-name=%s_c]",customData.get("fieldName"));
		new VoodooControl("div", "css", dataNameDraggableField).dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"));
		new VoodooControl("input", "id", "publishBtn").click();   
		VoodooUtils.waitForReady();

		// Add created MultiSelect field to List view
		sugar().admin.studio.clickStudio();
		accountsCtrl.click();
		layoutCtrl.click();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooControl dropHere1 =  new VoodooControl("td", "id", "Default");
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "[data-name='multiselectfield_c']").dragNDrop(dropHere1);
		saveBtnCtrl=new VoodooControl("input", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.focusDefault();

		// Go to accounts list View
		sugar().accounts.navToListView();

		// Verify Accounts List view is shown correctly with MultiSelect field.
		// TODO: VOOD-1036 Need library support for Accounts/any sidecar module for newly created custom fields
		new VoodooControl("th", "css", "[data-fieldname='multiselectfield_c']").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}