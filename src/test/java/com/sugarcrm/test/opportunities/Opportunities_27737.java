package com.sugarcrm.test.opportunities;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_27737 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that new RLI row appears after click "+" sign when creating opportunity
	 * @throws Exception
	 */
	@Test
	public void Opportunities_27737_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		
		// TODO: VOOD-1357 and VOOD-1359
		// Verify RLI subpanel below your normal Opps create RLI subpanel first line "-" sign (Delete button) should be disabled (unclickable)
		sugar().opportunities.createDrawer.getEditField("rli_name").assertVisible(true);
		Assert.assertTrue("Delete button is not clickable at the moment", new VoodooControl("a", "css", ".fieldset.edit .deleteBtn").isDisabled());
		
		// RLI fields filled with data
		sugar().opportunities.createDrawer.getEditField("rli_name").set(sugar().opportunities.defaultData.get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(sugar().opportunities.defaultData.get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(sugar().opportunities.defaultData.get("rli_likely"));

		// Add new RLI row
		new VoodooControl("a", "css", ".fieldset.edit .addBtn").click();
		VoodooUtils.waitForReady();
		
		// Verify new RLI row appears and "-" delete button is enabled 
		new VoodooControl("input", "css", "[data-voodoo-name='subpanel-for-opportunities-create'] tr:nth-of-type(2) .fld_name.edit input").assertExists(true);
		Assert.assertFalse("Delete button is clickable", new VoodooControl("a", "css", "[data-voodoo-name='subpanel-for-opportunities-create'] tr:nth-of-type(2) .deleteBtn").isDisabled());
		sugar().opportunities.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}