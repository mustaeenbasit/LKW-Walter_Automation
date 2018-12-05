package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Studio_28214 extends SugarTest {
	FieldSet customData;
	VoodooControl saveButtonCtrl,resetButtonCtrl,resetClickCtrl,relationshipsCtrl,fieldsCtrl,labelsCtrl,layoutsCtrl,extensionsCtrl,opportunitiesCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		sugar().login();
		// TODO: VOOD-938
		opportunitiesCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		resetButtonCtrl = new VoodooControl("input", "id", "exportBtn");
		resetClickCtrl = new VoodooControl("button", "id", "execute_repair");
		relationshipsCtrl = new VoodooControl("input", "name", "relationships");
		fieldsCtrl = new VoodooControl("input", "name", "fields");
		layoutsCtrl = new VoodooControl("input", "name", "layouts");
		labelsCtrl = new VoodooControl("input", "name", "labels");
		extensionsCtrl = new VoodooControl("input", "name", "extensions");
	}

	/**
	 * Verify that Max related date formula.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_28214_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Admin > Studio > Opportunities > fields > date_close 
		// TODO: VOOD-938
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		opportunitiesCtrl.click();
		new VoodooControl("td", "id", "fieldsBtn").click();
		new VoodooControl("a", "id", "date_closed").click();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify Default formula
		new VoodooControl("textarea", "id", "formulaInput").assertContains(customData.get("defaultFormula"), true);

		// Save
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();

		// Verify that Max related date formula is saved.
		new VoodooControl("input", "id", "formula_display").assertContains(customData.get("defaultFormula"), true);
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}