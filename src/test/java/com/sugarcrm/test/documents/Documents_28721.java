package com.sugarcrm.test.documents;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.BWCSubpanel;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Documents_28721 extends SugarTest {
	public void setup() throws Exception {
		sugar().documents.api.create();
		sugar().login();
	}

	/**
	 * BWC: Verify that Sales stage field is displayed in the Opp subpanel of document record view when project from Opps+ RLIs 
	 * @throws Exception
	 */
	@Test
	public void Documents_28721_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource headerColumnDS = testData.get(testName);

		// Document detail view
		sugar().documents.navToListView();
		sugar().documents.listView.clickRecord(1);

		// Opportunity subpanel
		VoodooUtils.focusFrame("bwc-frame");
		BWCSubpanel oppSubpanel = sugar().documents.detailView.subpanels.get(sugar().opportunities.moduleNamePlural);

		// TODO: VOOD-1517
		VoodooControl header = new VoodooControl("tr", "css", oppSubpanel.getHookString()+" .list.view tr:nth-of-type(2)");

		// Verify "Sales Stage" & "Probability" fields are not displayed  but "Status" field is displayed, when OPP + RLI view settings
		header.assertContains(headerColumnDS.get(0).get("header_column_display_name"), true);
		header.assertContains(headerColumnDS.get(1).get("header_column_display_name"), false);
		header.assertContains(headerColumnDS.get(2).get("header_column_display_name"), false);
		VoodooUtils.focusDefault();

		// Verify "Sales Stage" & "Probability" fields are displayed but "Status" field is not displayed, when OPP view settings
		sugar().admin.api.switchToOpportunitiesView();
		VoodooUtils.refresh();
		VoodooUtils.focusFrame("bwc-frame");
		header.assertContains(headerColumnDS.get(2).get("header_column_display_name"), true);
		header.assertContains(headerColumnDS.get(1).get("header_column_display_name"), true);
		header.assertContains(headerColumnDS.get(0).get("header_column_display_name"), false);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}