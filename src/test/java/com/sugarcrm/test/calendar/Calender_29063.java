package com.sugarcrm.test.calendar;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calender_29063 extends SugarTest {
	FieldSet customData;
	VoodooControl languageList;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		
		// Login sugar with French language
		// TODO: VOOD-999
		languageList = new VoodooControl("a", "id", "languageList");
		languageList.click();
		new VoodooControl("a", "css", "#languageList a[data-lang-key='fr_FR']").click();
		sugar.alerts.waitForLoadingExpiration();
		sugar.login();
	}

	/**
	 * Verify that on selecting French/ Spanish/ Greek / Finnish/ Herbrew languages, "Jan 2015" should not be skipped 
	 * from calendar for "valid until" field, under Quotes Module.
	 * 
	 * @throws Exception
	 */
	@Ignore("VOOD-1446")
	@Test
	public void Calender_29063_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Quotes module
		sugar.navbar.selectMenuItem(sugar.quotes, "createQuote");
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-863
		VoodooControl calnav = new VoodooControl("a", "css", ".calnav");
		VoodooControl selectMonth = new VoodooControl("select", "id", "date_quote_expected_closed_trigger_div_nav_month");
		
		// Create a new Quotes and input the field "Valid Until" using the popup calendar to fill in the value
		new VoodooControl("img", "id", "date_quote_expected_closed_trigger").click();
		calnav.click();
		selectMonth.assertContains(customData.get("setMonth"), true); // Verify that month "Jan" exist
		selectMonth.set(customData.get("setMonth"));
		
		// Set year
		new VoodooControl("input", "id", "date_quote_expected_closed_trigger_div_nav_year").set(customData.get("year"));
		new VoodooControl("button", "id", "date_quote_expected_closed_trigger_div_nav_submit").click();
		VoodooUtils.waitForReady();
		
		// Verify that popup window contain the Jan 2015
		new VoodooControl("a", "css", ".calnav").assertContains(customData.get("month")+" "+customData.get("year"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}