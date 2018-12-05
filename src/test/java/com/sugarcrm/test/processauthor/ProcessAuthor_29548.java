package com.sugarcrm.test.processauthor;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest;

public class ProcessAuthor_29548 extends SugarTest {
	public void setup() throws Exception {
		sugar().opportunities.api.create();
		sugar().login();
	}

	/**
	 * Verify Call / Meeting is displayed in the Calendar after add a related record
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_29548_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		RevLineItemRecord myRecord;

		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + "_7700.bpm");
		sugar().processDefinitions.navToListView();

		// Enable Process Definition
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Create RLI record
		sugar().revLineItems.navToListView();
		myRecord = (RevLineItemRecord) sugar().revLineItems.create();
		
		// Navigate to calendar module
		sugar().calendar.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-863
		// Lib support for Calendar module
		// Navigate to date 07/28/2015 in calendar 
		new VoodooControl("span", "css", ".dateTime").click();
		new VoodooControl("a", "css", ".calheader .calnav").click();
		new VoodooControl("select", "css", "#goto_date_trigger_div_nav_month").click();
		new VoodooControl("option", "css", "#goto_date_trigger_div_nav_month [value='6']").click();
		new VoodooControl("input", "css", "#goto_date_trigger_div_nav_year").set(customData.get("dateTime").substring(6, 10));
		new VoodooControl("button", "css", "#goto_date_trigger_div_nav_submit").click();
		new VoodooControl("td", "css", "#goto_date_trigger_div_t tr:nth-child(5) td:nth-child(5)").click();
		VoodooUtils.waitForReady();
		
		// Verifying call is displaying in the correct time cell.
		new VoodooControl("div", "css", "[datetime='" + customData.get("dateTime") + "'] [module_name='Calls']").assertEquals(myRecord.getRecordIdentifier(), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}