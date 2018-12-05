package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_27736 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that opportunity record shows errors only after all RLI  errors are fixed 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27736_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		VoodooControl saveBtn = sugar().opportunities.createDrawer.getControl("saveButton");
		saveBtn.click();

		// Verify Save without filling RLI required fields
		VoodooControl rliName = sugar().opportunities.createDrawer.getEditField("rli_name");
		VoodooControl expectedCloseDate = sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date");
		VoodooControl likely = sugar().opportunities.createDrawer.getEditField("rli_likely");
		rliName.assertAttribute("class", "required");
		rliName.assertAttribute("class", "required");
		rliName.assertAttribute("class", "required");

		// Now, RLI fields are filling
		rliName.set(sugar().opportunities.getDefaultData().get("rli_name"));
		expectedCloseDate.set(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"));
		likely.set(sugar().opportunities.getDefaultData().get("rli_likely"));
		saveBtn.click(); 

		// Verify Save without filling Opp required fields
		sugar().alerts.getError().assertVisible(true);
		sugar().alerts.getError().closeAlert();
		sugar().opportunities.createDrawer.getEditField("name").assertAttribute("class", "required");
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}