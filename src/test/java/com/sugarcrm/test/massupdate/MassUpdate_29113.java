package com.sugarcrm.test.massupdate;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class MassUpdate_29113 extends SugarTest {
	VoodooControl moduleCtrl;
	FieldSet fieldData = new FieldSet();
	DataSource casesData = new DataSource();

	public void setup() throws Exception {
		fieldData = testData.get(testName).get(0);
		casesData = testData.get(testName + "_" + sugar.cases.moduleNamePlural);

		// Create some Cases, the values are not important
		sugar.cases.api.create(casesData);
		sugar.login();
	}

	/**
	 * Verify that Mass Update/Select All is working fine if Dependent formula is used for Related field
	 * 
	 * @throws Exception
	 */
	@Test
	public void MassUpdate_29113_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Studio Controls
		// TODO: VOOD-542
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Cases");
		VoodooControl studioFooterLnk = new VoodooControl("input", "css", "#footerHTML input[value='Studio']");

		// Navigate to Admin > Studio
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Go to Cases -> Fields -> Add Field (Resolution dependent with the following formula 'equal(related($accounts,"industry"),"test")')
		// TODO: VOOD-1504
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input[value='Add Field']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set(fieldData.get("fieldName"));
		new VoodooControl("input", "id", "dependent").click();
		new VoodooControl("input", "css", "#visFormulaRow td input[name='editFormula']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("textarea", "id", "formulaInput").set(fieldData.get("formula"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();

		// TODO: TR-10026 - Save Button is not functional while saving the formula for the calculated field.
		// Remove the following two lines(L#64 and L#65 after TR-10026 is fixed 
		new VoodooControl("a", "css", "#formulaBuilderWindow .container-close").click();
		VoodooUtils.waitForReady();

		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// Add created custom fields to Studio -> Cases -> Layouts -> Record View
		studioFooterLnk.click();
		VoodooUtils.waitForReady();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		// TODO: VOOD-1506
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", "#toolbox .le_row").dragNDrop(new VoodooControl("div", "css", "#panels div:nth-of-type(1).le_panel"));
		VoodooControl customFieldName = new VoodooControl("div", "css", "div[data-name='"+ fieldData.get("fieldName") + "_c']");
		customFieldName.scrollIntoViewIfNeeded(false);
		customFieldName.dragNDrop(new VoodooControl("div", "css", "#panels div:nth-of-type(1).le_panel .le_row div:nth-child(1).le_field.special"));
		new VoodooControl("input", "css", "#layoutEditorButtons #publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Go to the List view. Check a couple of Cases, and use the Mass Updater to update 'Status' to 'Assigned'
		sugar.cases.navToListView();
		sugar.cases.listView.toggleSelectAll();
		sugar.cases.listView.openActionDropdown();
		sugar.cases.listView.massUpdate();
		sugar.cases.massUpdate.getControl("massUpdateField02").set(fieldData.get("massUpdateField"));
		sugar.cases.massUpdate.getControl("massUpdateValue02").set(fieldData.get("massUpdateValue"));
		sugar.cases.massUpdate.update();

		// Verify that Mass Update should work fine
		for(int i = 0; i < casesData.size(); i++) {
			sugar.cases.listView.verifyField(i+1, "status", fieldData.get("massUpdateValue"));
		}

		// Verify that the Action menu should be disabled in case any row is not selected
		Assert.assertTrue("Expected action dropdown to be disabled.", sugar.cases.listView.getControl("actionDropdown").isDisabled());

		// Check "Select All" checkbox
		sugar.cases.listView.toggleSelectAll();

		// Verify that the Action menu should be enabled in case any row is selected
		Assert.assertFalse("Expected action dropdown to be enables.", sugar.cases.listView.getControl("actionDropdown").isDisabled());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}