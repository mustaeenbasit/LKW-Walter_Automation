package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_17524 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Shouldn't trigger global search when user provides a zero length search string or a search string 
	 * consists of only whitespaces
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_17524_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customFS = testData.get(testName).get(0);
		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		VoodooControl headerpaneTitle = sugar().globalSearch.getControl("headerpaneTitle");
		
		// Press enter key without input anything in global search box
		// TODO: CB-252
		globalSearch.set("" + '\uE007');
		VoodooUtils.waitForReady();

		// Verify that it should redirect to Result page and and display default data
		headerpaneTitle.assertContains(customFS.get("headerpaneTitle"), true);
		sugar().globalSearch.getControl("toggleSidebar").assertVisible(true);
		new VoodooControl("span", "css", ".nav.search-results li:nth-child(1) .label-Employees").assertContains(customFS.get("employeesLabel"), true);
		
		// Type only space in global search box and press enter key
		// TODO: CB-252
		globalSearch.set(" " + '\uE007');
		VoodooUtils.waitForReady();
		
		// Verify that it should redirect to Result page and display message "No Result were found"
		// TODO: VOOD-1848
		headerpaneTitle.assertContains(customFS.get("headerpaneTitle"), true);
		new VoodooControl("li", "css", ".nav.search-results .no-results").assertContains(customFS.get("noResult"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}