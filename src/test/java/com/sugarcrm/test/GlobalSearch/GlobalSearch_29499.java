package com.sugarcrm.test.GlobalSearch;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class GlobalSearch_29499 extends SugarTest {
	public void setup() throws Exception {
		sugar().manufacturers.api.create();
		sugar().login();
	}

	/**
	 * Verify that records from "Manufacturers" module can be found by Global Search
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_29499_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		globalSearch.click();

		// TODO: VOOD-1849, CB-252
		// search and hit enter
		globalSearch.set(sugar().manufacturers.getDefaultData().get("name").substring(0, 2)+'\uE007');

		// TODO: VOOD-1848
		// Verify manufacture record as primary(icon, subject)
		new VoodooControl("span", "css", ".layout_default .search-result .fld_picture span").assertEquals(sugar().manufacturers.moduleNamePlural.substring(0, 2), true);
		new VoodooControl("a", "css", ".layout_default .search-result h3 a").assertEquals(sugar().manufacturers.getDefaultData().get("name"), true);

		// Verify Preview button is disabled
		Assert.assertTrue("Preview button is enabled", new VoodooControl("a", "css", ".layout_default .search-result .search-list a").isDisabled());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}