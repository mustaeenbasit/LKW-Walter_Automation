package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26068 extends SugarTest {	
	public void setup() throws Exception {
		sugar.accounts.api.create();
		sugar.login();		
	}

	/**
	 * Verify the add link function of team in mass update panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26068_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet teamData = testData.get(testName).get(0);

		// Go to modules that can mass update team info in mass update panel, such as accounts.
		sugar.accounts.navToListView();

		// Choose some records and select mass update action
		sugar.accounts.listView.checkRecord(1);
		sugar.accounts.listView.openActionDropdown();
		sugar.accounts.listView.massUpdate();

		// TODO: VOOD-1003 
		// Select Teams field for update
		new VoodooSelect("div","css", "div.filter-field").set(teamData.get("field"));

		// TODO: VOOD-518
		// Press add button next the team input box on the mass update panel
		new VoodooControl("div","css", "div.filter-value div.control-group:nth-child(2)").assertExists(false);
		new VoodooControl("button","css", "div.filter-value button.btn.first").click();

		// Verify the new team input box
		VoodooControl teamSelectCtrl = new VoodooControl("span","css", "div.filter-value div.control-group:nth-child(2) span.select2-chosen");
		teamSelectCtrl.assertEquals(teamData.get("assert"),true);
		new VoodooSelect("div","css", "div.filter-value div.control-group:nth-child(2) div.select2-container").set(teamData.get("team"));

		// Verify the new team
		teamSelectCtrl.assertEquals(teamData.get("team"),true);

		// Cancel the filter
		new VoodooControl("a", "css", ".btn-link.btn-invisible.cancel_button").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}