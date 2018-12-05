package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_17043 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();
		sugar().opportunities.create();
		sugar().revLineItems.create();
	}

	/**
	 * Currency type field display format
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_17043_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify amount and currency as one field in listview
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.verifyField(1, "oppAmount", ds.get(0).get("amountAndCurrency"));

		// Verify amount and currency as one field in record view
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.getDetailField("oppAmount").assertEquals(ds.get(0).get("amountAndCurrency"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}