package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_27134 extends SugarTest {
	OpportunityRecord myOpp;
	VoodooControl oppCtrl, subpanelsCtrl, rliSubpanelCtrl, saveDeployCtrl;

	public void setup() throws Exception {
		// TODO: VOOD-1511: Support Studio Module Subpanels Layout View
		oppCtrl = new VoodooControl("a", "id", "studiolink_Opportunities");
		rliSubpanelCtrl = new VoodooControl("td", "css", "#Buttons tr:nth-child(1) td:nth-child(5)");

		sugar().accounts.api.create();
		sugar().login();
		// Create an opportunity with RLI
		myOpp = (OpportunityRecord) sugar().opportunities.create();
	}

	/**
	 * Verify Generate Quote action button still exist after Studio Subpanel Modification
	 *
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_27134_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		myOpp.navToRecord();
		StandardSubpanel rliSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		VoodooControl quoteButton = rliSubpanel.getControl("quoteButton");
		VoodooControl deleteButton = rliSubpanel.getControl("deleteButton");
		VoodooControl actionDropdown = rliSubpanel.getControl("actionDropdown");
		rliSubpanel.scrollIntoView();
		rliSubpanel.toggleSubpanel();
		// In Revenue Line Items Subpanel and select the checkbox of one record
		rliSubpanel.checkRecord(1);
		// Select the Action Menu dropdown to see the options you can take on the selected records
		rliSubpanel.openActionDropdown();
		// Should see two options, Generate Quote and Delete.
		quoteButton.assertVisible(true);
		deleteButton.assertVisible(true);
		actionDropdown.click();

		// Navigate to Admin > Studio > Opportunities > Subpanels > Revenue Line Items
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		sugar().admin.studio.waitForAJAX();

		oppCtrl.click();
		sugar().admin.studio.waitForAJAX();
		subpanelsCtrl = new VoodooControl("td", "id", "subpanelsBtn");
		subpanelsCtrl.click();
		VoodooUtils.waitForReady();
		rliSubpanelCtrl.click();
		VoodooUtils.waitForReady();
		VoodooControl defaultPanelCtrl = new VoodooControl("td", "id", "Default");
		saveDeployCtrl = new VoodooControl("input", "id", "savebtn");
		VoodooControl dateHiddenFieldCtrl = new VoodooControl("li", "css", "#Hidden .draggable[data-name='date_entered']");

		// Move  "Date Created" field from the Hidden pane to the Default pane
		dateHiddenFieldCtrl.dragNDrop(defaultPanelCtrl);
		saveDeployCtrl.click();
		VoodooUtils.focusDefault();

		// Navigate back to the Opportunity, Revenue Line Items Subpanel.
		myOpp.navToRecord();
		rliSubpanel.scrollIntoView();
		rliSubpanel.expandSubpanel();
		rliSubpanel.checkRecord(1);
		rliSubpanel.openActionDropdown();

		// Verify the same Two action buttons as before: Generate Quote and Delete
		quoteButton.assertVisible(true);
		deleteButton.assertVisible(true);
		actionDropdown.click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {
	}
}