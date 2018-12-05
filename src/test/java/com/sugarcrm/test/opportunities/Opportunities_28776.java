package com.sugarcrm.test.opportunities;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28776 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Opportunity module behaves correctly after resetting opportunity module in studio
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28776_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();

		// TODO: VOOD-1445
		// Declaring Controls for Likely,Best,Worst & Expected Close Date 
		VoodooControl likelyCase = new VoodooControl("span", "css", ".fld_amount.edit");
		VoodooControl bestCase = new VoodooControl("span", "css", ".fld_best_case.edit");
		VoodooControl worstCase = new VoodooControl("span", "css", ".fld_worst_case.edit");
		VoodooControl expClosedDate = new VoodooControl("span", "css", ".fld_date_closed.edit");

		// Verifying Likely,Worst,Expected Closed Date & BestCase are Un-Editable/Disabled field.
		assertTrue("Likely field is not disabled",likelyCase.isDisabled());
		assertTrue("Worst field is not disabled",worstCase.isDisabled());
		assertTrue("Expected Close Date field is not disabled",expClosedDate.isDisabled());
		assertTrue("Best field is not disabled",bestCase.isDisabled());

		// TODO: VOOD-1359
		// Verifying SalesStage & Probability field are not visible on create Drawer of Opportunities page.
		new VoodooControl("span", "css", "[data-voodoo-name='create'] [data-voodoo-name='sales_stage']").assertVisible(false);
		new VoodooControl("span", "css", "[data-voodoo-name='create'] [data-voodoo-name='probability']").assertVisible(false);

		// TODO: VOOD-542
		// Reset Opportunities Module 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "id", "studiolink_Opportunities").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "exportBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("button", "id", "execute_repair").click();
		sugar().admin.studio.waitForAJAX(120000);
		VoodooUtils.focusDefault();

		// Navigating back to Opportunities Create Drawer Page
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();

		// Verifying Likely,Best,Worst & Expected Close Date remains Un-Editable 
		assertTrue("Likely field is not disabled",likelyCase.isDisabled());
		assertTrue("Best field is not disabled",bestCase.isDisabled());
		assertTrue("Worst field is not disabled",worstCase.isDisabled());
		assertTrue("Expected Close Date field is not disabled",expClosedDate.isDisabled());

		// TODO: VOOD-1359
		// Verifying SalesStage & Probability field are not visible on create Drawer of Opportunities page 
		new VoodooControl("span", "css", "[data-voodoo-name='create'] [data-voodoo-name='sales_stage']").assertVisible(false);
		new VoodooControl("span", "css", "[data-voodoo-name='create'] [data-voodoo-name='probability']").assertVisible(false);
		sugar().opportunities.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}