package com.sugarcrm.test.studio;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_28135 extends SugarTest {
	VoodooControl moduleCtrl;

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify that when comparison date has past current date, daysUntil() function should returns correct value.
	 * @throws Exception
	 */
	@Test
	public void Studio_28135_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fieldValueData = testData.get(testName).get(0);

		// Navigate to Admin > Studio
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// Go to Accounts > Fields and create a new Date type field called end_day_c 
		// TODO: VOOD-938
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		VoodooControl addFieldCtrl = new VoodooControl("input", "css", "#studiofields input[value='Add Field']");
		VoodooControl fieldNameCtrl = new VoodooControl("input", "id", "field_name_id");
		VoodooControl saveBtnCtrl = new VoodooControl("input", "css", "[name='fsavebtn']");
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "#type option[value='date']").click();
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(fieldValueData.get("dateFieldName"));

		// Save the Date type field
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Create a new Integer type field called Days Until with a calculation formula of "abs(subtract(daysUntil($end_day_c),daysUntil(today())))" 
		addFieldCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "css", "#type option[value='int']").click();
		VoodooUtils.waitForReady();
		fieldNameCtrl.set(fieldValueData.get("intFieldName"));
		new VoodooControl("input", "id", "calculated").click();
		new VoodooControl("input", "css", "#formulaRow input[name='editFormula']").click();
		new VoodooControl("textarea", "id", "formulaInput").set(fieldValueData.get("formula_field_description"));
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		VoodooUtils.waitForReady();

		// Save the field
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Place both fields on the Recordviews 
		sugar().admin.studio.clickStudio();
		moduleCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnrecordview").click();
		VoodooUtils.waitForReady();

		// Add the Date type and Integer field to the record view of account module
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row");
		VoodooControl moveToNewFilter1 = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row .le_field.special:nth-of-type(1)");
		VoodooControl moveToNewFilter2 = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(2) .le_row .le_field.special:nth-of-type(2)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		moveToLayoutPanelCtrl.waitForVisible();
		new VoodooControl("div", "css",  "div[data-name="+fieldValueData.get("dateFieldName")+"_c]").dragNDrop(moveToNewFilter1);
		new VoodooControl("div", "css",  "div[data-name="+fieldValueData.get("intFieldName")+"_c]").dragNDrop(moveToNewFilter2);

		// Save & Deploy
		new VoodooControl("input", "id", "publishBtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Find out the value of Todays Date
		Date  date = new Date();
		SimpleDateFormat  sdf = new SimpleDateFormat("MM/dd/yyyy");
		String todaysDate = sdf.format(date);
		Calendar  cal = Calendar.getInstance();
		cal.setTime(date);

		// Tomorrows date
		cal.add(Calendar.DATE, 1);
		date = cal.getTime();
		String tomorrowsDate = sdf.format(date);

		// Yesterdays date
		cal.add(Calendar.DATE, -2);
		date = cal.getTime();
		String yesterdaysDate = sdf.format(date);

		// Go to Account Record and Edit
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.showMore();

		// TODO: VOOD-1036
		// In the End Day field select different days to trigger the calculation for Days Until e.g Todays Date, Tomorrows Date, Yesterdays Date.
		VoodooControl endDayEditfieldCtrl =  new VoodooControl("input", "css", ".fld_end_day_c.edit input");
		VoodooControl daysUntilCtrl = new VoodooControl("input", "css", ".fld_days_until_c.disabled.edit input");

		// Set End Day as Todays Date
		endDayEditfieldCtrl.set(todaysDate);

		// Verify that the Days Until field should have the correct value, Todays Date = 0
		daysUntilCtrl.click(); // Need to click anywhere in record view to reflect the result/value in calculated field 'Days Until'
		daysUntilCtrl.assertEquals(fieldValueData.get("daysUntil_Today"), true);

		// Set End Day as Tomorrows Date
		endDayEditfieldCtrl.set(tomorrowsDate);

		// Verify that the Days Until field should have the correct value, Tomorrows date = 1 
		daysUntilCtrl.click(); // Need to click anywhere in record view to reflect the result/value in calculated field 'Days Until'
		daysUntilCtrl.assertEquals(fieldValueData.get("daysUntil_Tomorrow"), true);

		// Set End Day as Yesterdays Date
		endDayEditfieldCtrl.set(yesterdaysDate);

		// Verify that the Days Until field should have the correct value, Yesterdays date = -1
		daysUntilCtrl.click(); // Need to click anywhere in record view to reflect the result/value in calculated field 'Days Until'
		daysUntilCtrl.assertEquals(fieldValueData.get("daysUntil_Yesterday"), true);

		// Cancel the Account Record
		sugar().accounts.recordView.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}