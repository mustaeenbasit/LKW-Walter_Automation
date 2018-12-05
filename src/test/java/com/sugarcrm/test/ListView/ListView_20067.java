package com.sugarcrm.test.ListView;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ListView_20067 extends SugarTest {
	DataSource quotesData;
	VoodooControl openItemsCtrl;

	public void setup() throws Exception{
		quotesData = testData.get(testName);
		
		// Ensure correct order of creation
		// TODO: VOOD-2139 - Need to ensure all moduleCSV files (both Sidecar and BWC) have date and time fields for both dateCreation and dateModified values
		for (int i = 0; i < quotesData.size(); i++) {
			sugar().quotes.api.create(quotesData.get(i));
			VoodooUtils.pause(1000); // Insert deliberate delay
		}
		sugar.login();		
	}

	/**
	 * Search out open items in Quotes module
	 * @throws Exception
	 */
	@Test
	public void ListView_20067_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.quotes.navToListView();

		// Verify that all the records are coming in the ListView
		int size = quotesData.size();
		for(int i=0; i<size ; i++){
			sugar.quotes.listView.verifyField(i+1, "quoteStage", quotesData.get(size-(i+1)).get("quoteStage"));
		}

		// Selecting on Open Items Check box and Clicking Search Button
		// TODO: VOOD-975
		VoodooUtils.focusFrame("bwc-frame");
		openItemsCtrl= new VoodooControl("input", "id", "open_only_basic");
		openItemsCtrl.click();
		VoodooUtils.focusDefault();
		sugar.quotes.listView.submitSearchForm();

		// Verifying that only two rows are returned
		int row =sugar.quotes.listView.countRows();
		Assert.assertTrue("Number of records in listview not equals to 2.", row == 2);

		// Verify that only Open Items are listed in listview  
		VoodooUtils.focusDefault();
		sugar.quotes.listView.verifyField(1, "quoteStage", quotesData.get(4).get("quoteStage"));
		sugar.quotes.listView.verifyField(2, "quoteStage", quotesData.get(3).get("quoteStage"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
