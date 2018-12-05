package com.sugarcrm.test.meetings;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Meetings_19743 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);		
		sugar().meetings.api.create(ds);
		sugar().meetings.navToListView();
	}

	/**
	 * Verify that searching meetings with special characters works correctly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Meetings_19743_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		for(int i=0;i<ds.size();i++){
			sugar().meetings.listView.setSearchString(ds.get(0).get("name"));
			// TODO VOOD-760
			new VoodooControl("a", "css", "table.list.view tbody tr.oddListRowS1 td:nth-of-type(6) a").assertEquals(ds.get(0).get("name"), true);
			new VoodooControl("a", "css", "table.list.view tbody tr.evenListRowS1").assertExists(false);
		}
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
