package com.sugarcrm.test.opportunities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Opportunities_20063 extends SugarTest {
	DataSource ds;
	String currDate;

	public void setup() throws Exception {
		ds = testData.get(testName);
		sugar().accounts.api.create();
		sugar().opportunities.api.create();
		sugar().login();

		// get current date
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		currDate = sdf.format(dt);
		FieldSet firstRecord = new FieldSet();
		firstRecord.put("name", testName);
		firstRecord.put("rli_expected_closed_date", currDate);

		// TODO: VOOD-444 (When this VOOD is fixed, opp record can be created through api)
		// opportunities record created through UI to make a relationship between account , rli and opportunity 
		sugar().opportunities.create(firstRecord);
	}

	/**
	 * Verify the date type filed range search feature can work fine in filter
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_20063_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().opportunities.navToListView();
		// Create a filter.
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterCreateNew();

		// TODO: VOOD-1766
		// Add "Expected Close Date" to the filter setup
		new VoodooSelect("span", "css", ".search-filter .filter-definition-container  span.select2-chosen").set(ds.get(17).get("operatorName"));
		new VoodooControl("span", "css", "div.filter-definition-container .select2-default span.select2-arrow").click();

		// Verify all the available The operators
		for(int i = 0; i < ds.size()-1; i++)
		{
			new VoodooControl("div", "xpath", "//*[@id='select2-drop']/ul/li["+(i+1)+"]/div").assertContains(ds.get(i).get("operatorName").trim(), true);
		}

		// select "is equal to" operator 
		new VoodooControl("li", "xpath", "//*[@id='select2-drop']/ul/li[1]").click();
		sugar().alerts.waitForLoadingExpiration();

		// set current date as expected close date
		new VoodooControl("input", "css", ".detail.fld_date_closed input").set(currDate);
		// set custom filter name
		new VoodooControl("div", "css", "[data-voodoo-name='filter-actions'] input").set(testName);
		sugar().alerts.waitForLoadingExpiration();

		// save custom filter
		new VoodooControl("a", "css", ".btn.btn-primary.save_button").click();
		sugar().alerts.waitForLoadingExpiration();

		// count no of rows after applying custom filter 
		int rowCount = sugar().opportunities.listView.countRows();
		// Verify range search using custome date field is working
		Assert.assertTrue("Filter not applid correctly. Expected 1 record, found " + rowCount, rowCount == 1);
		sugar().opportunities.listView.verifyField(1, "name", testName);

		// reset the filter to All 
		sugar().opportunities.listView.openFilterDropdown();
		sugar().opportunities.listView.selectFilterAll();
		sugar().alerts.waitForLoadingExpiration();

		// count no of rows after removing custom filter 
		rowCount = sugar().opportunities.listView.countRows();
		Assert.assertTrue("Filter not applid correctly. Expected 2 record, found " + rowCount, rowCount == 2);
		// verify all the records after removing custom filter 
		sugar().opportunities.listView.verifyField(1, "name", testName);
		sugar().opportunities.listView.verifyField(2, "name", sugar().opportunities.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}