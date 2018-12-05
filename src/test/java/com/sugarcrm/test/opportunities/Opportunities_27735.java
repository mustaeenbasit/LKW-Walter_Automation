package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_27735 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that RLI record shows errors when try to save Opportunity with RLI with empty required fields
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27735_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.save();

		// Verify RLI row shows errors for the required fields.
		sugar().opportunities.createDrawer.getEditField("rli_name").assertAttribute("class", "required");
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").assertAttribute("class", "required");
		sugar().opportunities.createDrawer.getEditField("rli_likely").assertAttribute("class", "required");
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}