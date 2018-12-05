package com.sugarcrm.test.quotes;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/** 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Quotes_27931 extends SugarTest {
	public void setup() throws Exception {
		sugar.quotes.api.create();
		sugar.login();
	}

	/**
	 * Verify that default displayed columns on Quotes list view are correct 
	 * @throws Exception
	 */
	@Test
	public void Quotes_27931_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource headerColumnDS = testData.get(testName);

		sugar.quotes.navToListView();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1517, VOOD-1674
		VoodooControl headerRow = new VoodooControl("tr", "css", "#MassUpdate table.list.view tbody tr:nth-of-type(2)");
		for (int i = 0; i < headerColumnDS.size(); i++) {
			// Verify header columns on Quotes listview
			headerRow.assertContains(headerColumnDS.get(i).get("header_column_dsiplay_text"), true);
		}

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}