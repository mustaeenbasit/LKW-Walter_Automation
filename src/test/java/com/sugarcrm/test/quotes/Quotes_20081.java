package com.sugarcrm.test.quotes;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Ashish Raina <araina@sugarcrm.com>
 */
public class Quotes_20081 extends SugarTest {
	String todaysDate, fieldRestName;
	public void setup() throws Exception {

		//Create 3 quotes records with 2 different dates in "Expected Close" field (defined in csv)
		fieldRestName = testData.get(testName).get(0).get("dateTimeSugarField");
		SimpleDateFormat sdFmt = new SimpleDateFormat("MM/dd/yyyy");
		todaysDate = sdFmt.format(new Date());
		//Create even records with todays 'Expected Close Date', odd records with default date
		FieldSet quotesName = new FieldSet();
		for(int i = 3; i > 0; i--) {
			quotesName.put("name", testName+"_"+i);
			if (i%2==0)
				quotesName.put(fieldRestName,todaysDate);
			sugar.quotes.api.create(quotesName);
			quotesName.clear();
		}
		sugar.login();
	}

	/**
	 * Verify the datetime type filed range search feature can add to basic search layout
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_20081_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// TODO: VOOD-938
		// Goto Admin > studio > Calls > layout > Search > Basic Search
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("studio").click();
		new VoodooControl("a", "id", "studiolink_Quotes").click();
		new VoodooControl("td", "id", "layoutsBtn").click();
		new VoodooControl("td", "id", "searchBtn").click();
		new VoodooControl("td", "id", "BasicSearchBtn").click();
		
		// Drag and drop Expected Close field from hidden to default and save 
		VoodooControl defaultControl = new VoodooControl("li", "css", "#Default .noBullet");
		VoodooControl dateCreatedControl = new VoodooControl("li", "css", "[data-name='date_quote_expected_closed']");
		dateCreatedControl.dragNDrop(defaultControl);
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Navigate to quotes list view  and filter given dateTime with todays date and click search
		sugar.quotes.navToListView();
		VoodooUtils.waitForReady();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", ".edit.view.search.basic #range_date_quote_expected_closed_basic").set(todaysDate);
		VoodooUtils.focusDefault();
		sugar.quotes.listView.submitSearchForm();
		VoodooUtils.waitForReady();
		
		// Assert search result, row count and then reset search
		sugar.quotes.listView.verifyField(1, "name", (testName+"_2"));
		Assert.assertTrue("Assert quotes listView count Rows equals 1 FAILED", sugar.quotes.listView.countRows() == 1);
		VoodooUtils.focusDefault();
		sugar.quotes.listView.clearSearchForm();
		sugar.quotes.listView.submitSearchForm();
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}