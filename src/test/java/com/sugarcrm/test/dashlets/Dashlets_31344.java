package com.sugarcrm.test.dashlets;

import java.text.NumberFormat;
import java.util.Locale;
import org.joda.time.DateTime;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Dashlets_31344 extends SugarTest {
	FieldSet customData = new FieldSet();
	String rliCloseDate = "";

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);

		// Getting today's date
		DateTime dateToday = DateTime.now();
		int currentMonth = dateToday.getMonthOfYear();
		int currentYear = dateToday.getYear();
		int rliMonth = 00;
		int rliYear = dateToday.getYear();

		// Calculating next quarter's month and year 
		if ((currentMonth + 3) > 12) {
			rliMonth = (currentMonth + 3) % 12;
			rliYear = currentYear + 1;
		}
		else 
			rliMonth = (currentMonth + 3);

		// Setting RLI close date to be '15'th day of month 3 months henceforth in format mm/dd/yyyy
		rliCloseDate = String.format("%02d", rliMonth) + "/" + "15/" + rliYear;

		FieldSet rliData = new FieldSet();
		rliData.put("date_closed", rliCloseDate);
		rliData.put("salesStage", customData.get("rliSalesStage"));

		// Creating an RLI with the Close Date and Sales Stage as stated above
		sugar().revLineItems.api.create(rliData);

		// Login as admin
		sugar().login();

		// Add a 'Top 10 Sales' Dashlet on My Dashboard
		sugar().home.dashboard.edit();
		// Clicking 'Add row' on Second column
		// TODO: VOOD-591 - Dashlets support
		new VoodooControl("a", "css", ".dashlets li:nth-child(2) [data-value='1']").click();
		sugar().home.dashboard.addDashlet(3,1);

		// Scrolling and clicking 'Top 10 Sales' link
		new VoodooControl("input", "css", ".span4.search").set(customData.get("reportType"));
		new VoodooControl("a", "css", ".list.fld_title .ellipsis_inline a").click();
		VoodooUtils.waitForReady();

		// Clicking on Save button
		new VoodooControl("a", "css", ".active .fld_save_button a").click();
		VoodooUtils.waitForReady();

		// Save 'My Dashboard' with the new dashlet
		new VoodooControl("a", "css", ".fld_save_button a").click();
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify the data is shown correctly in "Top 10 Sales" dashlet if you select Next Quarter
	 * 
	 * @throws Exception
	 */
	@Test
	public void Dashlets_31344_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Selecting the next quarter option on "Top 10 Sales" dashlet
		new VoodooSelect("span", "css", "li:nth-child(3) .edit.fld_filter_duration").set(customData.get("nextQuarter"));
		VoodooUtils.waitForReady();

		VoodooControl rliOnGraph = new VoodooControl("circle", "css", "li:nth-child(3) .nv-single-point circle");
		rliOnGraph.scrollIntoViewIfNeeded(false);

		// Hover on the RLI record on graph
		rliOnGraph.hover();
		VoodooUtils.waitForReady();

		// Verify the RLI for the next quarter created is displayed on "Top 10 Sales" dashlet
		VoodooControl hoverData = new VoodooControl("div", "class", "tooltip-inner");
		hoverData.assertContains("Created By: " + customData.get("createdBy"), true);

		// Converting the likely amount to US Currency format i.e. $XX,XXX.XX
		Locale locale = new Locale("en", "US");   
		hoverData.assertContains("Likely: " + NumberFormat.getCurrencyInstance(locale).format(Double.parseDouble(sugar().revLineItems.getDefaultData().get("likelyCase"))), true);
		hoverData.assertContains("Expected Close Date: " + rliCloseDate, true);
		hoverData.assertContains("Probability (%): " + customData.get("probabilityPercentage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}