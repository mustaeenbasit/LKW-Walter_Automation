package com.sugarcrm.test.bugs;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;


public class Bugs_18594 extends SugarTest {
	DataSource bugs;
	
	public void setup() throws Exception {
		bugs = testData.get("Bugs_18594");
		
		sugar.login();
		sugar.admin.enableModuleDisplayViaJs(sugar.bugs);
		sugar.bugs.api.create(bugs);
	} 

	/**
	 * 
	 * Verify that all bugs are displayed without any error after clicking "Cancel" button on "MERGE RECORDS WITH:.." page.
	 * @throws Exception
	 */
	@Test
	public void Bugs_18594_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Bugs list view, click "select all" and "Merge" action.
		sugar.bugs.navToListView();
		sugar.bugs.listView.toggleSelectAll();
		sugar.bugs.listView.openActionDropdown();
		
		// TODO: JIRA story VOOD-657 about the ability to perform Merge action in list view.
		new VoodooControl("input","css","ul.dropdown-menu:nth-child(3) li:nth-child(2) a").click();
		new VoodooControl("a", "css", ".fld_cancel_button.merge-duplicates-headerpane a").scrollIntoViewIfNeeded(false);
		new VoodooControl("a", "css", ".fld_cancel_button.merge-duplicates-headerpane a").click();
		
		// Verify all bugs are displayed without any error after clicking "Cancel" button on "MERGE RECORDS WITH:.." page.
		sugar.bugs.navToListView();
		VoodooUtils.pause(1500);
		sugar.bugs.listView.verifyField(1, "name", bugs.get(1).get("name"));
		sugar.bugs.listView.verifyField(2, "name", bugs.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}