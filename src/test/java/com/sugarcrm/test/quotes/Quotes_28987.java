package com.sugarcrm.test.quotes;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_28987 extends SugarTest {
	public void setup() throws Exception {
		DataSource quotesName = testData.get(testName);
		sugar.quotes.api.create(quotesName);
		sugar.login();
	}

	/**
	 * Verify that Select all check-box get unchecked on unchecking any child check-box
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_28987_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Quote module
		sugar.quotes.navToListView();

		// Select View Quotes from Quote module drop-down
		sugar.navbar.selectMenuItem(sugar.quotes, "viewQuotes");

		// Select all on this page from action drop-down. All check-box are selected
		sugar.quotes.listView.toggleSelectAll();

		// Un-check any of the quote record check-box
		sugar.quotes.listView.uncheckRecord(1);

		// Verify that 'Select all' on this page check-box should be unchecked
		VoodooUtils.focusFrame("bwc-frame");
		Assert.assertFalse("'Select all' check-box is still checked", sugar.quotes.listView.getControl("selectAllCheckbox").isChecked());
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}