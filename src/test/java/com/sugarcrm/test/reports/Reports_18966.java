package com.sugarcrm.test.reports;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Reports_18966 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		sugar().login();
		ds = testData.get(testName);
	}

	/**
	 * Enterprise_Reporting_Custom_Query
	 * 
	 * @throws Exception
	 */
	@Test
	public void Reports_18966_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// VOOD-643
		// navigate to report module 
		sugar().navbar.navToModule(ds.get(0).get("module_plural_name"));
		new VoodooControl("li", "css", ".dropdown.active .fa-caret-down").click();
		// VOOD-1057 
		// Click "Manage Advanced Reports" link from navigation shortcuts.
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_ADVANCED_REPORTING']").click();
		new VoodooControl("li", "css", ".dropdown.active .fa-caret-down").click();
		// Assert Create Custom Query and View Custom Queries links are displayed in navigation shortcuts
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_CUSTOMQUERY']").assertExists(true);
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_CUSTOMQUERIES']").assertExists(true);
		// Click Create Custom Query link
		new VoodooControl("a", "css", "[data-navbar-menu-item='LNK_NEW_CUSTOMQUERY']").click();
 		VoodooUtils.focusFrame("bwc-frame");
 		new VoodooControl("input", "css", "[name = 'name']").set(ds.get(0).get("query_name"));
 		// Enter valid query in query edit view
 		new VoodooControl("textarea", "id", "custom_query").set(ds.get(0).get("query"));
 		// Click "Save" button.
 		new VoodooControl("input", "css", "#EditView > input.button").click();
 		// Assert query is created successfully.
 		new VoodooControl("slot", "css", "tr.oddListRowS1 > td:nth-child(3) > slot").assertContains("Valid", true);
 		VoodooUtils.focusDefault();
 		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}