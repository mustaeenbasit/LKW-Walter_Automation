package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28339 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that user can delete first RLI when create opportunity with multiple RLIs linked to it
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28339_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Opp module and create new Opp
		sugar().navbar.navToModule(sugar().opportunities.moduleNamePlural);
		sugar().opportunities.listView.create();

		// Set required Opp data
		sugar().opportunities.createDrawer.getEditField("name").
		set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").
		set(sugar().accounts.getDefaultData().get("name"));

		// Set required RLI data
		sugar().opportunities.createDrawer.getEditField("rli_name").
		set(sugar().opportunities.getDefaultData().get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").
		set(sugar().opportunities.getDefaultData().get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").
		set(sugar().opportunities.getDefaultData().get("rli_likely"));

		// Verify RLI1 delete (-) Button is disabled, (+) is enabled
		// TODO: VOOD-1357
		VoodooControl delRLI1Ctrl = new VoodooControl("a", "css", "tr:nth-child(1) .fieldset.edit .deleteBtn");
		VoodooControl addRLI1Ctrl = new VoodooControl("a", "css", "tr:nth-child(1) .fieldset.edit .addBtn");
		Assert.assertEquals(true, delRLI1Ctrl.isDisabled());
		Assert.assertEquals(false, addRLI1Ctrl.isDisabled());

		// Add another RLI2  by Clicking on "+"  Add button.
		addRLI1Ctrl.click();
		VoodooUtils.waitForReady();
		// Verify RLI1 and RLI2 delete (-) Buttons are now enabled
		// TODO: VOOD-1357
		VoodooControl delRLI2Ctrl = new VoodooControl("a", "css", "tr:nth-child(2) .fieldset.edit .deleteBtn");
		Assert.assertEquals(false, delRLI1Ctrl.isDisabled());
		Assert.assertEquals(false, delRLI2Ctrl.isDisabled());

		// Delete RLI1  by Clicking on "-"  delete button.
		delRLI1Ctrl.click();

		// Verify only 1 RLI remains with empty RLI fields
		delRLI2Ctrl.assertExists(false);
		sugar().opportunities.createDrawer.getEditField("rli_name").assertEquals("", true);
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").assertEquals("", true);
		sugar().opportunities.createDrawer.getEditField("rli_likely").assertEquals("", true);

		// Close create drawer
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}