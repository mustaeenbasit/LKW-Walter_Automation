package com.sugarcrm.test.GlobalSearch;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class GlobalSearch_29500 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		FieldSet quoteFS = new FieldSet();
		quoteFS.put("quoteNumber", customData.get("quote_number"));
		quoteFS.put("quoteStage", customData.get("quote_stage"));
		sugar().quotes.api.create(quoteFS);
		
		sugar().login();
	}

	/**
	 * Verify that records from "Quotes" module can be found by Global Search
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_29500_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		VoodooControl globalSearch = sugar().navbar.getControl("globalSearch");
		globalSearch.click();

		// TODO: VOOD-1849, CB-252
		// search and hit enter
		globalSearch.set(sugar().quotes.getDefaultData().get("name")+'\uE007');

		// TODO: VOOD-1848
		// Verify quote record as primary(icon, subject) and secondary(description, number, stage) details
		new VoodooControl("span", "css", ".layout_default .search-result .fld_picture span").assertEquals(customData.get("quote_icon"), true);
		new VoodooControl("a", "css", ".layout_default .search-result h3 a").assertEquals(sugar().quotes.getDefaultData().get("name"), true);
		new VoodooControl("span", "css", ".layout_default .search-result .secondary span").assertEquals(customData.get("description_lbl"), true);
		new VoodooControl("span", "css", ".layout_default .search-result .secondary span:nth-of-type(2)").assertEquals(customData.get("description"), true);
		new VoodooControl("span", "css", ".layout_default .search-result .secondary span:nth-of-type(3)").assertEquals(customData.get("quote_lbl"), true);
		new VoodooControl("span", "css", ".layout_default .search-result .secondary span:nth-of-type(4)").assertEquals(customData.get("quote_number"), true);
		new VoodooControl("span", "css", ".layout_default .search-result .secondary span:nth-of-type(5)").assertEquals(customData.get("quote_stage_lbl"), true);
		new VoodooControl("span", "css", ".layout_default .search-result .secondary span:nth-of-type(6)").assertEquals(customData.get("quote_stage"), true);

		// Verify Preview button is disabled
		Assert.assertTrue("Preview button is enabled", new VoodooControl("a", "css", ".layout_default .search-result .search-list a").isDisabled());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}