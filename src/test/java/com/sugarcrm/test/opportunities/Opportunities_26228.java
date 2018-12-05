package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.candybean.datasource.FieldSet;

public class Opportunities_26228 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		// Create Opp with RLI
		sugar().opportunities.create();
	}

	/**
	 * TC:26228: ENT/ULT: Verify that copy of opportunity record works correctly
	 *  
	 */
	@Test
	public void Opportunities_26228_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet myTestData = testData.get(testName).get(0);
		sugar().opportunities.listView.clickRecord(1);

		// Verify that worst, likely, and best amounts plus expected close date are calculated from linked RLI record
		sugar().opportunities.recordView.getDetailField("worstCase").assertEquals(myTestData.get("rliAmount"), true);
		sugar().opportunities.recordView.getDetailField("likelyCase").assertEquals(myTestData.get("rliAmount"), true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertEquals(myTestData.get("rliAmount"), true);
		sugar().opportunities.recordView.getDetailField("date_closed").assertEquals(myTestData.get("rliCloseDate"), true);

		// Copy Opportunity record created during setup
		sugar().opportunities.recordView.copy();
		VoodooUtils.waitForReady();

		// Verify that the Opportunity created by copy has all calculated fields blanks
		sugar().opportunities.createDrawer.getEditField("worstCase").assertEquals("", true);
		sugar().opportunities.createDrawer.getEditField("likelyCase").assertEquals("", true);
		sugar().opportunities.createDrawer.getEditField("bestCase").assertEquals("", true);

		// Provide a unique name for new opportunity record and save 
		sugar().opportunities.createDrawer.getEditField("name").set(myTestData.get("newOppName"));
		// Provide required rli fields 
		FieldSet fs = sugar().opportunities.getDefaultData();
		sugar().opportunities.createDrawer.getEditField("rli_name").set(fs.get("rli_name"));
		sugar().opportunities.createDrawer.getEditField("rli_expected_closed_date").set(fs.get("rli_expected_closed_date"));
		sugar().opportunities.createDrawer.getEditField("rli_likely").set(fs.get("rli_likely"));
		sugar().opportunities.createDrawer.save();

		// Verify record is successfully saved
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.verifyField(1, "name", myTestData.get("newOppName"));

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}