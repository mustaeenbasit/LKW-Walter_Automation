package com.sugarcrm.test.contracts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Contracts_28091 extends SugarTest {
	VoodooControl moduleCtrl;

	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.contracts);
		sugar.contracts.create();
	}

	/**
	 * To verify Audit log for multi select field.
	 * @throws Exception
	 */
	@Test
	public void Contracts_28091_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fieldValueData = testData.get(testName).get(0);

		// Navigate to Admin > Studio
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Go to Contracts and create Multiselect field in Contracts 
		// TODO: VOOD-938
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Contracts");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields input[value='Add Field']").click();
		new VoodooControl("option", "css", "#type option[value='multienum']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(testName);

		// set audit flag.
		new VoodooControl("input", "css", "input[name='audited']").click();

		// Save the field
		new VoodooControl("input", "css", "[name='fsavebtn']").click();
		VoodooUtils.waitForReady();

		// Go to admin > Studio > Contracts > Layout > Edit View/Detail View
		for (int i = 0; i <= 1; i++) {
			sugar.admin.studio.clickStudio();
			moduleCtrl.click();
			VoodooUtils.waitForReady();
			new VoodooControl("td", "id", "layoutsBtn").click();
			VoodooUtils.waitForReady();
			if (i == 0)
				new VoodooControl("td", "id", "viewBtneditview").click();
			else
				new VoodooControl("td", "id", "viewBtndetailview").click();
			VoodooUtils.waitForReady();

			// Add the Multiselect field to the layout in Contracts.
			VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row");
			VoodooControl moveToNewFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row .le_field.special:nth-of-type(1)");
			new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
			moveToLayoutPanelCtrl.waitForVisible();
			new VoodooControl("div", "css", "div[data-name=contracts_28091_c]").dragNDrop(moveToNewFilter);

			// Save & Deploy
			new VoodooControl("input", "id", "publishBtn").click();
			VoodooUtils.waitForReady();
		}
		VoodooUtils.focusDefault();

		// Edit a contract 
		sugar.contracts.navToListView();
		sugar.contracts.listView.clickRecord(1);
		sugar.contracts.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// Modify custom created Multiselect field.
		// TODO: VOOD-1036
		VoodooControl analystCtrl = new VoodooControl("option", "css", "#contracts_28091_c option[value='Analyst']");
		analystCtrl.click();
		VoodooUtils.focusDefault();
		sugar.contracts.editView.save();

		// Edit the same Contract and modify this field again. 
		sugar.contracts.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1036
		analystCtrl.click(); // Need to click on previous selected value again to unselect this value
		new VoodooControl("option", "css", "#contracts_28091_c option[value='Investor']").click();
		VoodooUtils.focusDefault();
		sugar.contracts.editView.save();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-738
		// Click View to change log. 
		sugar.contracts.detailView.openPrimaryButtonDropdown();
		new VoodooControl("a", "id", "btn_view_change_log").click();
		VoodooUtils.focusWindow(1);

		// Verify that the change log shows before and after values and essentially works.
		new VoodooControl("h2", "css", ".moduleTitle h2").assertEquals(fieldValueData.get("changeLog"), true);
		new VoodooControl("td", "css", ".list.view tr:nth-child(2) td:nth-child(2)").assertContains(fieldValueData.get("fieldName"), true);
		new VoodooControl("div", "css", ".list.view tr:nth-child(2) td:nth-child(3) div").assertEquals(fieldValueData.get("oldValue"), true);
		new VoodooControl("div", "css", ".list.view tr:nth-child(2) td:nth-child(4) div").assertEquals(fieldValueData.get("newValue"), true);
		VoodooUtils.closeWindow();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}