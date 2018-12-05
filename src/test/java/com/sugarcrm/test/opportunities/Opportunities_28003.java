package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_28003 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that all calculated fields are cleared when copy opportunity with linked RLI records
	 * @throws Exception
	 */
	@Test
	public void Opportunities_28003_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));

		// Add RLI records linked to this opportunity
		VoodooControl rliName = sugar().opportunities.createDrawer.getEditField("rli_name");
		VoodooControl rliExpectedCloseDate = sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date");
		VoodooControl rliLikely = sugar().opportunities.createDrawer.getEditField("rli_likely");	
		rliName.set(customFS.get("rli_name"));
		rliExpectedCloseDate.set(customFS.get("rli_expected_closed_date"));
		rliLikely.set(customFS.get("rli_likely"));

		// Save the Opportunity Record
		sugar().opportunities.createDrawer.save();

		// Open the Opportunity record from the list view
		sugar().opportunities.listView.clickRecord(1);

		// Select copy on action drop-down
		sugar().opportunities.recordView.copy();
		// No wait available in "copy" method.
		VoodooUtils.waitForReady();

		// Verify that all previously linked RLI records are unlinked 
		rliName.assertContains(customFS.get("rli_name"), false);
		rliExpectedCloseDate.assertContains(customFS.get("rli_expected_closed_date"), false);
		rliLikely.assertContains(customFS.get("rli_likely"), false);

		// Verify that all calculated fields are cleared in opportunity create drawer.
		sugar().opportunities.createDrawer.getEditField("oppAmount").assertEquals("", true);
		sugar().opportunities.createDrawer.getEditField("likelyCase").assertEquals("", true);
		sugar().opportunities.createDrawer.getEditField("bestCase").assertEquals("", true);
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}