package com.sugarcrm.test.targetlists;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class TargetLists_27258 extends SugarTest {
	TargetListRecord targetListData;

	public void setup() throws Exception {
		targetListData = (TargetListRecord)sugar().targetlists.api.create();
		sugar().login();
	}

	/**
	 * Verify that no target can be selected when select report based on unmatched 
	 * modules while adding record from reports in Target sub-panel of TargetList record view.
	 * @throws Exception
	 */
	@Test
	public void TargetLists_27258_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		targetListData.navToRecord();

		StandardSubpanel targetsSubpanel = sugar().targetlists.recordView.subpanels.get(sugar().
				targets.moduleNamePlural);
		targetsSubpanel.clickOnSelectFromReport();
		VoodooUtils.waitForReady();
		
		// Verifying that no reports are displayed on Search Select Drawer
		Assert.assertTrue("Reports in search select drawer are not equal to Zero when it should", 
				sugar().reports.searchSelect.countRows() == 0);
		
		// remove current "Target" module selected on filter
		// TODO: VOOD-1487
		new VoodooControl("i", "css", "[data-voodoo-name='filter-filter-dropdown'] div.choice-filter"
				+ "-clickable .fa-times-circle").click();

		// Search Other module: Opportunities
		new VoodooControl("input", "css", "div.filter-view.search.layout_Reports > div > input").set
			("Opportunities");
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", ".search-and-select tbody tr td:nth-of-type(1) input").click();	

		// Verify that a yellow warn message bar appears
		sugar().alerts.getWarning().waitForVisible();
		sugar().alerts.getWarning().assertContains(customData.get("message"), true);
		
		// Verify that no Target record appears in the Targets sub panel
		targetsSubpanel.expandSubpanel();
		Assert.assertTrue("Targets in targets subpanel are not equal to Zero when it should", 
				targetsSubpanel.countRows() == 0);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}