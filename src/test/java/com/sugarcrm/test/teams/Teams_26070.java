package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26070 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();		
		sugar.contacts.api.create();
	}

	/**
	 * Verify the delete function of team in mass update panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26070_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);
		sugar.contacts.navToListView();
		sugar.contacts.listView.checkRecord(1);
		sugar.contacts.listView.openActionDropdown();
		sugar.contacts.listView.massUpdate();
		
		// TODO VOOD-1003 
		new VoodooSelect("div","css", "div.filter-field").set(ds.get(0).get("field"));
		
		// TODO VOOD-518
		// add team
		new VoodooControl("button","css", "div.filter-value button.btn.first").click();
		new VoodooSelect("div","css", "div.filter-value div.control-group:nth-child(2) div.select2-container").set(ds.get(0).get("team1"));
		
		// remove team
		new VoodooControl("div","css", "div.filter-value div.control-group button[name='remove']").click();
		new VoodooControl("span","css", "div.filter-value div.control-group span.select2-chosen").assertEquals(ds.get(0).get("team1"),true);
		new VoodooControl("div","css", "div.filter-value div.control-group:nth-child(2)").assertExists(false);
		new VoodooControl("div","css", ".filter-value").assertElementContains(ds.get(0).get("team2"), false);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}