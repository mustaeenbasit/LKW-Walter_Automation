package com.sugarcrm.test.RevenueLineItems;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18378 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
		sugar().opportunities.create();
	}	

	/**
	 * ENT/ULT: Verify that there are "Date Created" and "Date Modified" fields in the RLI record view
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18378_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Navigate to RevenueLineItems List view and Click Create button
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();

		// Fill out all required fields
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().revLineItems.getDefaultData().get("relOpportunityName"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));

		// Click "Show More" link to see all available fields
		sugar().revLineItems.createDrawer.showMore();

		// "Date Created" and "Date Modified" fields are present at the bottom of the new record and blank by default
		// TODO: VOOD-597
		VoodooControl dateCreated = new VoodooControl("span", "css", ".fld_date_entered_by");
		dateCreated.assertVisible(true);
		VoodooControl dateModified = new VoodooControl("span", "css", ".fld_date_modified_by");
		dateModified.assertVisible(true);
		dateCreated.assertContains(customData.get("noData"), true);
		dateModified.assertContains(customData.get("noData"), true);
		String createdTime = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy hh:mm");

		// Save and navigate to the same record to update
		sugar().revLineItems.createDrawer.save();
		sugar().revLineItems.listView.clickRecord(1);
		sugar().revLineItems.recordView.edit();

		// "Date Created" and "Date Modified" are populated with the correct information
		dateCreated.assertContains(createdTime, true);
		dateModified.assertContains(createdTime, true);

		// Verify that both fields are "Read only"
		dateCreated.assertAttribute("class", "detail", true);
		dateModified.assertAttribute("class", "detail", true);

		// Wait for at least 1 minute is required to mark the difference between "Date Created" and "Date Modified" fields
		VoodooUtils.pause(60000);

		// Update any field and save
		sugar().revLineItems.recordView.getEditField("salesStage").set(customData.get("salesStage"));
		sugar().revLineItems.recordView.save();

		// Verify "Date created " remains the same and "Date Modified" is updated.
		dateCreated.assertContains(createdTime, true);
		dateModified.assertContains(createdTime, false);
		Assert.assertTrue("Date Modified field is not updated", !dateModified.getText().equals(createdTime));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}