package com.sugarcrm.test.RevenueLineItems;

import java.util.Calendar;
import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_17803  extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify date picker is displayed properly within the Expected Close Date text field in Revenue Line Items module.
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_17803_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		Calendar calendar = Calendar.getInstance();
		int expectedCloseDate = calendar.get(Calendar.DAY_OF_MONTH); 

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// open date picker on expected close date field.
		sugar().revLineItems.recordView.getEditField("date_closed").click();

		// Make sure calendar is displayed.
		VoodooControl dropdownCtrl = new VoodooControl("div", "css", "div.main-pane div.dropdown-menu");
		dropdownCtrl.assertVisible(true);
		dropdownCtrl.assertAttribute("class", "datepicker");

		// Verify date picker is displayed within the "Expected Close Date" field.
		new VoodooControl("td", "css", ".main-pane .datepicker-days .day.active").assertContains(String.valueOf(expectedCloseDate), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}