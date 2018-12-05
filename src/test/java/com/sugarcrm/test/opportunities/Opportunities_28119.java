package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28119 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();

		// Opportunities record exist with Revenue Line Item
		sugar().opportunities.create();
	}

	/**
	 * Verify the tool tip on Edit action drop down
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28119_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet hoverMessage = testData.get(testName).get(0);

		// Navigate to Opportunities -> View Opportunities which contains record in RLI subpanel
		sugar().opportunities.listView.clickRecord(1);

		// Hovering the mouse over the Edit Action drop down in the Revenue Line Items sub panel
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		rliSubpanel.expandSubpanel();
		rliSubpanel.getControl("expandActionRow01").hover();

		// Verify that On hovering the mouse, 'Actions' should be displayed 
		// TODO: VOOD-1292
		new VoodooControl("a", "css", "[data-original-title="+ hoverMessage.get("hoverMessage") +"]").assertVisible(true);

		// Collapsing subpanel since the default subpanel state is "Collapsed". 
		rliSubpanel.collapseSubpanel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
