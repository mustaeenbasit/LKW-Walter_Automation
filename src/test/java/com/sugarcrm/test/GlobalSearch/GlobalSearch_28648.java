package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_28648 extends SugarTest {

	public void setup() throws Exception {
		DataSource customDS = testData.get(testName+"_contactData");
		sugar().contacts.api.create(customDS);
		sugar().login();
	}

	/**
	 * Verify that no html string appears in the searching query
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28648_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Enter any existing record name in Global Search textbox and press enter
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		globalSearchCtrl.click();
		FieldSet customFS = testData.get(testName).get(0);

		// TODO: CB-252
		// search and hit enter
		globalSearchCtrl.set(customFS.get("searchText")+'\uE007');
		VoodooUtils.waitForReady();

		// Verify that search list is displayed having header like for Eg. Search Results for "123" (21 of 21)
		VoodooControl headerTitleCtrl = sugar().globalSearch.getControl("headerpaneTitle");
		headerTitleCtrl.assertContains(customFS.get("verifyHeaderText1"), true);

		// TODO: VOOD-1849
		new VoodooControl("span", "css", ".headerpane [data-name='collection-count'] .count").assertContains(customFS.get("verifyHeaderText2"), true);

		// Verify, Make sure no html string is displayed. (For eg.Search Results for "123 &m=" )
		headerTitleCtrl.assertContains(customFS.get("notFoundText"), false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}