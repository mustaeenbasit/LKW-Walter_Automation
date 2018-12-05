package com.sugarcrm.test.GlobalSearch;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Raina <araina@sugarcrm.com>
 */
public class GlobalSearch_28687 extends SugarTest {
	public void setup() throws Exception {
		sugar().documents.api.create();
		sugar().login();
	}

	/**
	 * Verify that records from "Documents" module can be found by Global Search
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_28687_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		globalSearch.click();

		// TODO: VOOD-1849, CB-252 - Once resolved we need to remove hit key
		// search and hit enter
		globalSearch.set(sugar().documents.getDefaultData().get("documentName")+'\uE007');

		// TODO: VOOD-1848
		// Verify document record details
		new VoodooControl("span", "css", ".layout_default .search-result .fld_picture span").assertEquals(sugar().documents.moduleNameSingular.substring(0, 2), true);
		new VoodooControl("a", "css", ".layout_default .search-result h3 a").assertEquals(sugar().documents.getDefaultData().get("documentName"), true);

		// Verify Preview button is disabled
		Assert.assertTrue("Preview button is enabled", new VoodooControl("a", "css", ".layout_default .search-result .search-list a").isDisabled());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}