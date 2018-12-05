package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_21058 extends SugarTest {
	DataSource callsDS = new DataSource();

	public void setup() throws Exception {
		callsDS = testData.get(testName);
		FieldSet systemSettings = testData.get(testName+"_1").get(0);
		sugar().calls.api.create(callsDS);
		sugar().login();

		// change display number on list view
		sugar().admin.setSystemSettings(systemSettings);
	}

	/**
	 * Verify that corresponding records are displayed in call list view when clicking the pagination control link in call list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Calls_21058_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to calls listView
		sugar().calls.navToListView();

		// Verify on very first time only 2 records be on listview
		sugar().calls.listView.verifyField(1, "name", callsDS.get(5).get("name"));
		sugar().calls.listView.verifyField(2, "name", callsDS.get(4).get("name"));
		sugar().calls.listView.getControl("checkbox03").assertVisible(false);

		// Verify after click on more  button only 4 records be on listview
		sugar().calls.listView.getControl("showMore").assertExists(true);
		sugar().calls.listView.showMore();
		sugar().calls.listView.verifyField(1, "name", callsDS.get(5).get("name"));
		sugar().calls.listView.verifyField(2, "name", callsDS.get(4).get("name"));
		sugar().calls.listView.verifyField(3, "name", callsDS.get(3).get("name"));
		sugar().calls.listView.verifyField(4, "name", callsDS.get(2).get("name"));
		sugar().calls.listView.getControl("checkbox05").assertVisible(false);

		// Verify after click on more  button only 6 records be on listview, more button should be hide
		sugar().calls.listView.showMore();
		sugar().calls.listView.verifyField(1, "name", callsDS.get(5).get("name"));
		sugar().calls.listView.verifyField(2, "name", callsDS.get(4).get("name"));
		sugar().calls.listView.verifyField(3, "name", callsDS.get(3).get("name"));
		sugar().calls.listView.verifyField(4, "name", callsDS.get(2).get("name"));
		sugar().calls.listView.verifyField(5, "name", callsDS.get(1).get("name"));
		sugar().calls.listView.verifyField(6, "name", callsDS.get(0).get("name"));
		sugar().calls.listView.getControl("checkbox06").assertVisible(true);
		sugar().calls.listView.getControl("showMore").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}