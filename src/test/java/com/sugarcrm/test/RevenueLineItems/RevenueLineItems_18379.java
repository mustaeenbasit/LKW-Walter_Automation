package com.sugarcrm.test.RevenueLineItems;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18379 extends SugarTest {
	String currentCreatedDateAndTime, currentModifiedDateAndTime;
	VoodooControl dateCreatedFieldCtrl, dateModifiedFieldCtrl;
	DataSource dateAndTimeFormat;
	FieldSet dateAndTimeSetting;

	public void setup() throws Exception {
		dateAndTimeFormat = testData.get(testName);
		dateAndTimeSetting = new FieldSet();
		sugar().accounts.api.create();
		sugar().login();

		// Opportunity is created linked to the account( Also creates a RLI record related to the Opportunity record)
		sugar().opportunities.create();

		// Navigate to record view of RLI record created
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.showMore();

		// Get the created and modified date and time of the RLI record created
		// TODO: VOOD-597
		dateCreatedFieldCtrl = new VoodooControl("span", "css", ".fld_date_entered_by");
		dateModifiedFieldCtrl = new VoodooControl("span", "css", ".fld_date_modified_by");
		currentCreatedDateAndTime = dateCreatedFieldCtrl.getText();
		currentModifiedDateAndTime = dateModifiedFieldCtrl.getText();
	}

	/**
	 * ENT/ULT: Verify that "Date Created" and "Date Modified" fields are adhere user's date and time format
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18379_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to user preferences -> advance tab and change default date and time format to any other format separate from the default
		dateAndTimeSetting.put("advanced_dateFormat", dateAndTimeFormat.get(0).get("advanced_dateFormat"));
		dateAndTimeSetting.put("advanced_timeFormat", dateAndTimeFormat.get(0).get("advanced_timeFormat"));
		sugar().users.setPrefs(dateAndTimeSetting);
		dateAndTimeSetting.clear();

		// Go to Revenue Line Items module and open RLI record created in the setup in the record view
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.showMore();

		// Calculate the date and time in the current format specified in users preferences
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mmaa");
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		// For Date Created
		Date dateCreated = simpleDateFormat.parse(currentCreatedDateAndTime.substring(0, 18));
		String updatedCreatedDateAndTime = outputDateFormat.format(dateCreated);

		// For Date Modified
		Date dateModified = simpleDateFormat.parse(currentModifiedDateAndTime.substring(0, 18));
		String updatedModifiedDateAndTime = outputDateFormat.format(dateModified);

		// Verify that the date and time in the "Date Created" and "Date Modified" fields are in the format specified in user preferences
		dateCreatedFieldCtrl.assertContains(updatedCreatedDateAndTime, true);
		dateModifiedFieldCtrl.assertContains(updatedModifiedDateAndTime, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}