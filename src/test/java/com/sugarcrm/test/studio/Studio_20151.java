package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_20151 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that for OOB modules, all fields are being shown correctly on fields page and Column sorting also works correctly
	 * @throws Exception
	 */
	@Test
	public void Studio_20151_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		DataSource ds = testData.get(testName);
		
		// TODO: VOOD-1504
		VoodooControl fieldCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl studioFooterCtrl = new VoodooControl("input", "css" ,"#footerHTML input[value='Studio']");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");

		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		
		// First verify fields
		String myModule;
		for(int myLine = 0; myLine < ds.size(); myLine++) {
			myModule = ds.get(myLine).get("module");
			
			new VoodooControl("a", "id", myModule).click();
			VoodooUtils.waitForReady();
			
			// Select Fields button
			fieldCtrl.click();
			VoodooUtils.waitForReady();

			while (myLine < ds.size() && ds.get(myLine).get("module").toString().contentEquals(myModule)) {
				// Verify Table row with the combination of Name + Display Label + Type exists
				new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'"+ds.get(myLine).get("name")+"')][contains(td[2],'"+ds.get(myLine).get("displayLabel")+"')][contains(td[3],'"+ds.get(myLine).get("type")+"')]").assertExists(true);
				new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'"+ds.get(myLine).get("name")+"')][contains(td[2],'"+ds.get(myLine).get("displayLabel")+"')][contains(td[3],'"+ds.get(myLine).get("type")+"')]").hover();
				myLine++;
			}
			
			myLine--; // so that it gets incremented by the outer for loop correctly
			studioFooterCtrl.click();			
			VoodooUtils.waitForReady();
		}
		
		// Next Verify Column Sorting with Accounts module
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();

		// Select Fields button
		fieldCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-938
		// Sort on Name
		new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(1)").click();
		if (new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(1) a[title='Click to sort descending']").queryExists()) {
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'account_type')][contains(td[2],'Type')][contains(td[3],'DropDown')]").assertExists(true);
			new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(1) a[title='Click to sort descending']").click();
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'website')][contains(td[2],'Website')][contains(td[3],'URL')]").assertExists(true);
		}
		if (new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(1) a[title='Click to sort ascending']").queryExists()) {
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'website')][contains(td[2],'Website')][contains(td[3],'URL')]").assertExists(true);
			new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(1) a[title='Click to sort ascending']").click();
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'account_type')][contains(td[2],'Type')][contains(td[3],'DropDown')]").assertExists(true);
		}

		// Sort on Display label
		new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(2)").click();
		if (new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(2) a[title='Click to sort descending']").queryExists()) {
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'website')][contains(td[2],'Website')][contains(td[3],'URL')]").assertExists(true);
			new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(2) a[title='Click to sort descending']").click();
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'phone_alternate')][contains(td[2],'Alternate Phone')][contains(td[3],'Phone')]").assertExists(true);
		}
		if (new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(2) a[title='Click to sort ascending']").queryExists()) {
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'phone_alternate')][contains(td[2],'Alternate Phone')][contains(td[3],'Phone')]").assertExists(true);
			new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(2) a[title='Click to sort ascending']").click();
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'website')][contains(td[2],'Website')][contains(td[3],'URL')]").assertExists(true);
		}

		// Sort on Type
		new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(3)").click();
		if (new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(3) a[title='Click to sort descending']").queryExists()) {
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'website')][contains(td[2],'Website')][contains(td[3],'URL')]").assertExists(true);
			new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(3) a[title='Click to sort descending']").click();
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'deleted')][contains(td[2],'Deleted')][contains(td[3],'Checkbox')]").assertExists(true);
		}
		if (new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(3) a[title='Click to sort ascending']").queryExists()) {
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'deleted')][contains(td[2],'Deleted')][contains(td[3],'Checkbox')]").assertExists(true);
			new VoodooControl("a", "css", "div#field_table thead tr:nth-of-type(1) th:nth-of-type(3) a[title='Click to sort ascending']").click();
			new VoodooControl("td", "xpath", "//*[@id='field_table']/div[3]/table/tbody[2]/tr[contains(td[1],'website')][contains(td[2],'Website')][contains(td[3],'URL')]").assertExists(true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}