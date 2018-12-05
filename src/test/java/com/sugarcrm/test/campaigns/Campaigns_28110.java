package com.sugarcrm.test.campaigns;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Campaigns_28110 extends SugarTest {
	VoodooControl layoutOptionCtrl, advancedSearchBtnCtrl;

	public void setup() throws Exception {
		sugar.campaigns.api.create();
		sugar.login();
	}

	/**
	 * Verify that Team field displays in list view
	 * @throws Exception
	 * */
	@Test
	public void Campaigns_28110_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet teamNameData = testData.get(testName).get(0);

		// Navigate to Campaigns module list view
		sugar.campaigns.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1072
		VoodooControl teamHeaderCtrl = new VoodooControl("a", "css", ".list.view tr:nth-child(2) th:nth-of-type(9) a");
		VoodooControl teamNameCtrl = new VoodooControl("a", "css", ".oddListRowS1 td:nth-of-type(12)");

		// Verify that the Team header is not appears in the list view
		teamHeaderCtrl.assertVisible(false);

		// Click on advance search
		sugar.campaigns.listView.getControl("advancedSearchLink").click();
		VoodooUtils.waitForReady();

		// Expand the layout option section
		// TODO: VOOD-975
		layoutOptionCtrl = new VoodooControl("a", "id", "tabFormAdvLink");
		layoutOptionCtrl.click();

		// Add Team to the list of displayed columns
		new VoodooControl("option", "css", "#hide_tabs_td option[value='TEAM_NAME']").click();
		new VoodooControl("img", "css", "#chooser_display_tabs_left_arrow img").click();

		// Click the search button
		advancedSearchBtnCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		advancedSearchBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the Team field is populated with the name of the Team that is related to the record in the list view
		teamHeaderCtrl.assertContains(sugar.teams.moduleNameSingular, true);
		teamNameCtrl.assertContains(teamNameData.get("global"), true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}