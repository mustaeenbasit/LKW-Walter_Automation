package com.sugarcrm.test.quotes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Quotes_20082 extends SugarTest {
	VoodooControl quotesModuleCtrl;
	VoodooSelect operatorFieldCtrl;
	FieldSet customData = new FieldSet();
	DataSource fieldsData = new DataSource();
	HashMap<String, String> customFieldValues = new HashMap<>();

	public void setup() throws Exception {
		fieldsData = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();

		// TODO: VOOD-542
		quotesModuleCtrl = new VoodooControl("a", "id", "studiolink_Quotes");
		VoodooControl layoutCtrl =  new VoodooControl("td", "id", "layoutsBtn");

		// Nav to Admin -> Studio -> Quotes module
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		quotesModuleCtrl.click();
		VoodooUtils.waitForReady();

		// Add "Date Created" and "Date Modified" into advanced search layout
		// TODO: VOOD-1509
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "searchBtn").click();
		new VoodooControl("td", "id", "AdvancedSearchBtn").click();
		VoodooUtils.waitForReady();
		VoodooControl defaultFieldsColumn = new VoodooControl("li", "css", "[data-name='name']");
		new VoodooControl("li", "css", "li[data-name='date_entered']").dragNDrop(defaultFieldsColumn);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "li[data-name='date_modified']").dragNDrop(defaultFieldsColumn);
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * TODO: VOOD-1966
	 * Get Calendar Instance
	 */
	private Calendar getCalendarInstance(){
		Calendar calendar = Calendar.getInstance();
		return calendar;
	}

	/**
	 * TODO: VOOD-1966
	 * Get Filter key value by replacing space with "_"
	 */
	private String getFilterKey(String operatorstring){
		String newName = operatorstring.replaceAll("\\s", "_");
		return newName;
	}

	/**
	 * Method to verify datetime type filed range search feature
	 *
	 */
	private void verificationFilter() throws Exception {
		// Navigate to Quotes module's Advanced Search layout
		sugar().quotes.navToListView();
		VoodooUtils.focusFrame("bwc-frame");
		if (sugar().quotes.listView.getControl("advancedSearchLink").queryExists()) {
			sugar().quotes.listView.getControl("advancedSearchLink").click();
			VoodooUtils.waitForReady();
		}
		// TODO: VOOD-975
		VoodooControl clearButton = new VoodooControl("input", "id", "search_form_clear_advanced");
		VoodooControl searchButton = new VoodooControl("input", "id", "search_form_submit_advanced");

		// Do date range search using the "Date Created" and "Date Modified" fields
		for (int k = 0; k < 2; k++) {
			ArrayList<String> fieldToSearchApplied= new ArrayList<>();
			fieldToSearchApplied.add(fieldsData.get(0).get("entered"));
			fieldToSearchApplied.add(fieldsData.get(0).get("modified"));

			for (int j = 0; j < fieldsData.size(); j++) {
				String filtertype = getFilterKey(fieldsData.get(j).get("operator"));

				// Set different Operators
				// TODO: VOOD-975
				String chooseOperator = String.format("date_%s_advanced_range_choice",fieldToSearchApplied.get(k));
				new VoodooControl("select", "id", chooseOperator).set(fieldsData.get(j).get("operator"));

				// Set 'tadayDate' for operators 'Equals', 'Not On', 'After', 'Before'
				if (filtertype.matches("Equals|Not_On|After|Before")) {
					// TODO: VOOD-975
					String setFieldValue = String.format("range_date_%s_advanced",fieldToSearchApplied.get(k));
					new VoodooControl("input", "id", setFieldValue).set(customFieldValues.get("todayDate"));
				}
				// Set 'start date' and 'end date' for operator 'Is Between'
				if (filtertype.equals("Is_Between")) {
					// TODO: VOOD-975
					String startDate = String.format("start_range_date_%s_advanced",fieldToSearchApplied.get(k));
					String endDate = String.format("end_range_date_%s_advanced",fieldToSearchApplied.get(k));
					new VoodooControl("input", "id", startDate).set(customFieldValues.get("yesterdayDate"));
					new VoodooControl("input", "id", endDate).set(customFieldValues.get("tomorrowDate"));	
				}
				// Click Submit button
				searchButton.click();
				VoodooUtils.waitForReady();

				switch (filtertype) {
				// Verify expected result for operators- Equals, Last 7 Days, Next 7 Days, Last 30 Days, Next 30 Days
				//  ,This Month, This Year, Is Between
				case "Equals" :case "Last_7_Days" :case "Next_7_Days" :case "Last_30_Days" :case "Next_30_Days" :case "This_Month" :case "This_Year" :case "Is_Between" :
					VoodooUtils.focusDefault();
					sugar().quotes.listView.verifyField(1, "name", testName+"_0");
					sugar().quotes.listView.verifyField(2, "name", testName+"_1");
					sugar().quotes.listView.verifyField(3, "name", testName+"_2");
					VoodooUtils.focusFrame("bwc-frame");
					break;

					// Verify expected result for operators- Not On, After, Before, Last Month, Next Month, Last Year, Next Year
				case "Not_On" :case "After" :case "Before" :case "Last_Month" :case "Next_Month" :case "Last_Year" :case "Next_Year" :
					VoodooUtils.focusDefault();
					Assert.assertEquals("Listview is not empty", 0, sugar().quotes.listView.countRows());
					break;
				}
			}
			clearButton.click();
			searchButton.click();
		}
		// Cancel the Advanced filter
		sugar().quotes.listView.getControl("basicSearchLink").click();
		VoodooUtils.focusDefault();
	}


	/**
	 * Verify the datetime type filed range search feature can work fine for the advanced search layout
	 * 
	 * @throws Exception
	 */
	@Test
	public void Quotes_20082_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = getCalendarInstance();

		// Today date
		String todayDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");

		// Tomorrow date
		c = getCalendarInstance();
		c.add(Calendar.DATE, 1);
		Date td = c.getTime();
		String tomorrowDate = sdf.format(td);

		// Yesterday date
		c = getCalendarInstance();
		c.add(Calendar.DATE, -1);
		Date yd = c.getTime();
		String yesterdayDate = sdf.format(yd);

		// Add dates to 'customFieldValues' Array
		customFieldValues.put("tomorrowDate", tomorrowDate);
		customFieldValues.put("yesterdayDate", yesterdayDate);
		customFieldValues.put("todayDate", todayDate);

		// Create some new module records with custom different date time field values
		for (int i = 0; i < 3; i++) {
			sugar().navbar.selectMenuItem(sugar().quotes, "createQuote");
			VoodooUtils.waitForReady();
			VoodooUtils.focusFrame("bwc-frame");
			sugar().quotes.editView.getEditField("name").set(testName+"_"+i);
			sugar().quotes.editView.getEditField("date_quote_expected_closed").set(tomorrowDate);
			sugar().quotes.editView.getEditField("billingAccountName").set(sugar().accounts.defaultData.get("name"));
			VoodooUtils.focusDefault();
			sugar().quotes.editView.save();
		}

		// Verify filter
		verificationFilter();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}