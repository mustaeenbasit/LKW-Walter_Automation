package com.sugarcrm.test.GlobalSearch;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28652 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify empty search loads the search page. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28652_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Click in search box and press enter.
		// TODO: CB-252
		sugar().navbar.getControl("globalSearch").set("");
		sugar().navbar.getControl("globalSearch").append("" + '\uE007');
		VoodooUtils.waitForReady();

		// User is taken to /#search url and the search page loads, make sure user is NOT taken to the "Data Not Found" page
		Assert.assertTrue("User in not taken to the '/#search/?' url", VoodooUtils.getUrl().contains(customFS.get("url")));
		sugar().globalSearch.getControl("headerpaneTitle").assertContains(customFS.get("headerpaneTitle"), true);
		sugar().globalSearch.getControl("toggleSidebar").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}