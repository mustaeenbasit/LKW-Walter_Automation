package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Accounts_26562 extends SugarTest {
	FieldSet customData;
	VoodooControl accountsSubpanel;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().login();
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();

		// TODO: VOOD-517 & VOOD-938
		// Encrypt field
		accountsSubpanel = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsSubpanel.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "td#fieldsBtn a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("select", "id", "type").set(customData.get("data_type"));
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("module_field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// Record view
		new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']").click();
		accountsSubpanel.click();
		new VoodooControl("a", "css", "td#layoutsBtn a").click();
		new VoodooControl("a", "css", "td#viewBtnrecordview a").click();
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row"); 
		VoodooControl moveToNewFilter = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"); 
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dataNameDraggableFieldToRecordSubpanel = String.format("div[data-name=%s_c]",customData.get("module_field_name")); 
		new VoodooControl("div", "css", dataNameDraggableFieldToRecordSubpanel).dragNDrop(moveToNewFilter);
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify Encrypt filed show unencrypted value 
	 * To ensure that User see Unencrypted value, however value should be stored with encrypted values in DB i.e. part of DB testing
	 * @throws Exception
	 */
	@Test
	public void Accounts_26562_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		new VoodooControl("input", "css", ".fld_"+customData.get("module_field_name")+"_c.edit input").set(customData.get("value"));
		sugar().accounts.recordView.save();
		sugar().alerts.waitForLoadingExpiration();

		// Expect to see the unencrypted value is shown in the list view.
		new VoodooControl("div", "css", ".fld_"+customData.get("module_field_name")+"_c.detail div").assertContains(customData.get("value"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}