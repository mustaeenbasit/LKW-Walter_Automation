package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
@Features(revenueLineItem = false)
public class Calls_18517 extends SugarTest {
	FieldSet customData;
	VoodooControl opportunityPanel, fieldBtnCtrl, descriptionCtrl, calculatedCtrl, saveBtnCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar.opportunities.api.create();
		opportunityPanel = new VoodooControl("a", "id", "studiolink_Opportunities");
		fieldBtnCtrl = new VoodooControl("a", "id", "fieldsBtn");
		descriptionCtrl = new VoodooControl("a", "id", "description");
		calculatedCtrl = new VoodooControl("input", "id", "calculated");
		saveBtnCtrl = new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]");
		sugar.login();
	}

	/**
	 * Verify reflecting on calculated field while duplicating an existing call
	 * @throws Exception
	 */
	@Test
	public void Calls_18517_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Studio -> opportunity -> description -> calculated formula
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		opportunityPanel.click();
		VoodooUtils.waitForReady();
		fieldBtnCtrl.click();
		VoodooUtils.waitForReady();
		descriptionCtrl.click();
		VoodooUtils.waitForReady();
		calculatedCtrl.set(Boolean.toString(true));
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		new VoodooControl("textarea", "id", "formulaInput").set(customData.get("formula_name"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// 2 calls added to Opportunities
		sugar.opportunities.navToListView();
		sugar.opportunities.listView.clickRecord(1);
		StandardSubpanel callsSubpanel = sugar.opportunities.recordView.subpanels.get(sugar.calls.moduleNamePlural);
		callsSubpanel.addRecord();
		VoodooControl nameCtrl = sugar.calls.createDrawer.getEditField("name");
		nameCtrl.set(sugar.calls.getDefaultData().get("name"));
		sugar.calls.createDrawer.save();
		callsSubpanel.addRecord();
		nameCtrl.set(testName);
		sugar.calls.createDrawer.save();
		if (sugar.alerts.getSuccess().queryVisible()) // multiple success message handling
			sugar.alerts.closeAllSuccess();

		sugar.opportunities.recordView.showMore();
		VoodooUtils.refresh(); // refresh to reflect data in description field

		// Verify description field having calls record count (2)
		VoodooControl descriptionFieldCtrl = sugar.opportunities.recordView.getDetailField("description");
		descriptionFieldCtrl.assertEquals(customData.get("two"), true);

		// click on call
		callsSubpanel.scrollIntoViewIfNeeded(false);
		callsSubpanel.clickLink(testName, 1);
		sugar.calls.recordView.delete();
		sugar.alerts.getWarning().confirmAlert();

		// navigate to calls listview => parentName
		sugar.calls.navToListView();
		sugar.calls.listView.getDetailField(1, "relatedToParentName").click();
		VoodooUtils.waitForReady();

		// Verify description field having calls record count(1)
		descriptionFieldCtrl.assertEquals(customData.get("one"), true);
		callsSubpanel.scrollIntoViewIfNeeded(false);
		callsSubpanel.expandSubpanel();
		callsSubpanel.clickLink(sugar.calls.getDefaultData().get("name"), 1);

		// Copy existing call record
		sugar.calls.recordView.copy();
		sugar.calls.createDrawer.save();
		sugar.calls.recordView.getDetailField("relatedToParentName").click();
		VoodooUtils.waitForReady();

		// Verify description field having calls record count(2)
		descriptionFieldCtrl.assertEquals(customData.get("two"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}