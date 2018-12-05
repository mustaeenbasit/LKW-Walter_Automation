package com.sugarcrm.test.calls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Calls_20083 extends SugarTest {
	VoodooControl callsModuleCtrl;
	VoodooSelect operatorFieldCtrl;
	FieldSet customData = new FieldSet();
	DataSource fieldsData = new DataSource();
	HashMap<String, String> customFieldValues = new HashMap<>();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		fieldsData = testData.get(testName+"_fields");
		sugar().login();

		// TODO: VOOD-542
		callsModuleCtrl = new VoodooControl("a", "id", "studiolink_Calls");
		VoodooControl fieldLayoutCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "[name='addfieldbtn']");
		VoodooControl dataTypeDropdownCtrl = new VoodooControl("select", "css", "select#type");
		VoodooControl nameFieldCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl enableRangeCtrl = new VoodooControl("input", "css", "input[name='enable_range_search']");
		VoodooControl saveButtonCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
		VoodooControl layoutCtrl =  new VoodooControl("td", "id", "layoutsBtn");

		// Navigate to Admin > studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Go to Calls and add a date time type custom field as required field and check "Enable Range Search" checkbox
		callsModuleCtrl.click();
		VoodooUtils.waitForReady();
		fieldLayoutCtrl.click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		dataTypeDropdownCtrl.set(customData.get("dataType"));
		VoodooUtils.waitForReady();
		nameFieldCtrl.set(customData.get("fieldName"));
		enableRangeCtrl.set(Boolean.toString(true));
		new VoodooControl("input", "css", "input[name='required']").set("true");
		saveButtonCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate back to Layout
		new VoodooControl("a", "css", "#mbtabs .bodywrapper a:nth-child(4)").click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();

		// Adding the above created custom datetime field to search layout
		// TODO: VOOD-1509
		new VoodooControl("td", "id", "searchBtn").click();
		new VoodooControl("td", "id", "FilterSearchBtn").click();
		VoodooUtils.waitForReady();
		VoodooControl defaultFieldsColumn = new VoodooControl("li", "css", "[data-name='date_start']");
		String customDateTime = String.format("li[data-name=%s_c]",customData.get("fieldName")); 
		new VoodooControl("li", "css", customDateTime).dragNDrop(defaultFieldsColumn);
		new VoodooControl("td", "id", "savebtn").click();
		VoodooUtils.waitForReady();

		// Add field to Record View
		// TODO: VOOD-1506
		new VoodooControl("a", "css", "#mbtabs .bodywrapper a:nth-child(5)").click();
		VoodooControl recordViewSubPanelCtrl = new VoodooControl("td", "id", "viewBtnrecordview");
		recordViewSubPanelCtrl.click();	
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row");
		VoodooControl moveToFilter =	new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		String dateTimeTypeField = String.format("div[data-name=%s_c]",customData.get("fieldName"));
		new VoodooControl("div", "css", dateTimeTypeField).dragNDrop(moveToFilter);
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		new VoodooControl("input", "id", "publishBtn").click();
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
	 * TODO: VOOD-1966
	 * Return true if current date is last day of month
	 */
	private boolean isLastDayOfMonth(){
		boolean flag = false;
		if (getCalendarInstance().getActualMaximum(Calendar.DAY_OF_MONTH) == getCalendarInstance().get(Calendar.DAY_OF_MONTH))
			flag = true;
		return flag;
	}

	/**
	 * TODO: VOOD-1966
	 * Return true if current date is first day of month
	 */
	private boolean isFirstDayOfMonth(){
		boolean flag = false;
		if (getCalendarInstance().get(Calendar.DAY_OF_MONTH) == 1)
			flag = true;
		return flag;
	}

	/**
	 * TODO: VOOD-1966
	 * Return true if current date is last day of year
	 */
	private boolean isLastDayOfYear(){
		boolean flag = false;
		if (isLastDayOfMonth()){
			if (getCalendarInstance().getActualMaximum(Calendar.MONTH) == getCalendarInstance().get(Calendar.MONTH))
				flag = true;
		}
		return flag;
	}

	/**
	 * TODO: VOOD-1966
	 * Return true if current date is first day of year
	 */
	private boolean isFirstDayOfYear(){
		boolean flag = false;
		if (isFirstDayOfMonth()){
			if (getCalendarInstance().get(Calendar.MONTH) == 0)
				flag = true;
		}
		return flag;
	}

	/**
	 * Method to verify filter for custom field with different operators
	 *
	 */
	private void verificationFilter() throws Exception {
		// To handle corner case check today
		boolean lastDayOfMonth = isLastDayOfMonth();
		boolean firstDayOfMonth = isFirstDayOfMonth();
		boolean firstDayOfYear = isFirstDayOfYear();
		boolean lastDayOfYear = isLastDayOfYear();

		// TODO: VOOD-1036
		VoodooSelect filterFieldCtrl = new VoodooSelect("div", "css", "div.filter-definition-container div:nth-child(1) div div[data-filter='field']");
		operatorFieldCtrl = new VoodooSelect("div", "css",  "div.filter-definition-container div:nth-child(1) div div[data-filter='operator']");

		// Create filters in Calls module with conditions and observe the results
		sugar().calls.listView.getControl("filterDropdown").click();
		sugar().calls.listView.getControl("filterCreateNew").click();
		filterFieldCtrl.set(customData.get("fieldLabel"));

		for (int j = 0; j < fieldsData.size(); j++) {
			// Set different Operators
			operatorFieldCtrl.set(fieldsData.get(j).get("operator"));
			VoodooUtils.waitForReady();
			String filtertype = getFilterKey(fieldsData.get(j).get("operator"));

			// Set 'start date' and 'end date' for operator 'is between'
			if (filtertype.equals("in_between")) {
				new VoodooControl("input", "css", ".fld_custom_field_c_max div input").set(customFieldValues.get("tomorrowDate"));
				new VoodooControl("input", "css", ".fld_custom_field_c_min div input").set(customFieldValues.get("yesterdayDate"));
				VoodooUtils.waitForReady();
			}

			// Set 'tomorrowDate' for operators 'is equal to', 'before', 'after'
			if (filtertype.matches("is_equal_to|before|after")) {  
				System.out.println("getFilterKey 1 " + getFilterKey(fieldsData.get(j).get("operator")));
				new VoodooControl("input", "css", ".fld_custom_field_c div input").set(customFieldValues.get("tomorrowDate"));
				VoodooUtils.waitForReady();
			}

			switch (filtertype) {
				// Verify expected result for operators 'is equal to' and 'tomorrow'
				case "is_equal_to" :case "tomorrow" :
					sugar().calls.listView.verifyField(1, "name", testName+"_tomorrowDate");
					break;
	
				// Verify expected result for operators 'before' and 'last 30 days'
				case "before": case "last_30_days":
					sugar().calls.listView.verifyField(1, "name", testName+"_todayDate");
					sugar().calls.listView.verifyField(2, "name", testName+"_yesterdayDate");
					break;
	
				// Verify expected result for operators 'after', 'last month', 'next month', 'last year' and 'next year'
				case "after": case "last_month": case "next_month": case "last_year":  case "next_year":
					if (filtertype.equals("last_year") && firstDayOfYear)
						sugar().calls.listView.verifyField(1, "name", testName + "_yesterdayDate");
					else if (filtertype.equals("next_year") && lastDayOfYear)
						sugar().calls.listView.verifyField(1, "name", testName + "_tomorrowDate");
					else if (filtertype.equals("last_month") && firstDayOfMonth)
						sugar().calls.listView.verifyField(1, "name", testName + "_yesterdayDate");
					else if (filtertype.equals("next_month") && lastDayOfMonth)
						sugar().calls.listView.verifyField(1, "name", testName + "_tomorrowDate");
					else
						sugar().calls.listView.assertIsEmpty();
					break;
	
				// Verify expected result for operator 'yesterday'
				case "yesterday":
					sugar().calls.listView.verifyField(1, "name", testName+"_yesterdayDate");
					break;
	
				// Verify expected result for operator 'today'
				case "today":
					sugar().calls.listView.verifyField(1, "name", testName+"_todayDate");
					break;
	
				// Verify expected result for operators 'next 7 days' and 'next 30 days'
				case "next_7_days": case "next_30_days":
					sugar().calls.listView.verifyField(1, "name", testName+"_todayDate");
					sugar().calls.listView.verifyField(2, "name", testName+"_tomorrowDate");
					break;
	
				// Verify expected result for operators 'this month', 'this year' and 'is between'
				case "this_month":case "this_year":case "is_between":
					if ((filtertype.equals("this_month") && lastDayOfMonth)||(filtertype.equals("this_year") && lastDayOfYear)){
						sugar().calls.listView.verifyField(1, "name", testName+"_todayDate");
						sugar().calls.listView.verifyField(2, "name", testName+"_yesterdayDate");
					}
					else if((filtertype.equals("this_year") && firstDayOfYear)||(filtertype.equals("this_month") && firstDayOfMonth)){
						sugar().calls.listView.verifyField(1, "name", testName+"_todayDate");
						sugar().calls.listView.verifyField(2, "name", testName+"_tomorrowDate");
					}
					else{
						sugar().calls.listView.verifyField(1, "name", testName+"_todayDate");
						sugar().calls.listView.verifyField(2, "name", testName+"_yesterdayDate");
						sugar().calls.listView.verifyField(3, "name", testName+"_tomorrowDate");
					}
					break;
			}
		}
	}


	/**
	 * Verify the custom datetime type filed range search feature can work fine for the advanced search layout
	 *
	 * @throws Exception
	 */
	@Test
	public void Calls_20083_execute() throws Exception {
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
		sugar().calls.navToListView();
		Iterator<Map.Entry<String, String>> entries = customFieldValues.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next();
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			sugar().calls.listView.create();
			sugar().calls.createDrawer.getEditField("name").set(testName+"_"+ entry.getKey());
			// TODO: VOOD-1036
			new VoodooControl("input", "css", ".fld_custom_field_c input[data-type='date']").set(customFieldValues.get(entry.getKey()));
			sugar().calls.createDrawer.save();
		}
		
		// Verify filter
		verificationFilter();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}