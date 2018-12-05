package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Opportunities_29697 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify Mass update panel is NOT displayed in the RLI subpanel of Opportunity record view.
	 * @throws Exception
	 */
	@Test
	public void Opportunities_29697_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet rliSubpanelData = testData.get(testName).get(0);

		// Navigate to Opportunities record view
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);

		// Go to RLI sub-panel (First Expand, then Collapse, then Expand again)
		rliSubpanel.expandSubpanel();
		rliSubpanel.collapseSubpanel();
		rliSubpanel.expandSubpanel();

		// Verify that the 'Mass update' panel should not be displayed in the RLI sub-panel [Bug in ENTv7.6.1.0#1130]
		// TODO: VOOD-1843 - Once resolved below line should be replaced by "getChildElement"
		new VoodooControl("div", "css", rliSubpanel.getHookString()+" div[data-voodoo-name=massupdate]").assertVisible(false);

		// Here verifying that 'Mass Update' title and 'Update' button text is not displayed under RLI sub-panel
		rliSubpanel.assertContains(rliSubpanelData.get("massUpdate"), false);
		rliSubpanel.assertContains(rliSubpanelData.get("updateBtn"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}