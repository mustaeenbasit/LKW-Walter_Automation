package com.sugarcrm.test.RevenueLineItems;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_18221 extends SugarTest {
	DataSource myDataSource; 

	public void setup() throws Exception {
		sugar().opportunities.api.create();
		myDataSource = testData.get(testName);
		sugar().login();
	}

	/**
	 *  Verify that changing the sales stage automatically updates probability 
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_18221_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		for(FieldSet myTestData  : myDataSource) { 
			// Set Sales Stage
			sugar().revLineItems.createDrawer.getEditField("salesStage").set(myTestData.get("salesStage"));

			// Verify Probability
			sugar().revLineItems.createDrawer.getEditField("probability").assertContains(myTestData.get("probability"), true);
		}

		// Fill out the required fields and click 'Save' and click on the created RLI record to View. 
		sugar().revLineItems.createDrawer.getEditField("salesStage").set(myDataSource.get(0).get("salesStage"));
		sugar().revLineItems.createDrawer.getEditField("name").set(sugar().revLineItems.getDefaultData().get("name"));
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(sugar().revLineItems.getDefaultData().get("relOpportunityName"));
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(sugar().revLineItems.getDefaultData().get("date_closed"));
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(sugar().revLineItems.getDefaultData().get("likelyCase"));
		sugar().revLineItems.createDrawer.save();
		sugar().revLineItems.listView.clickRecord(1);

		// Verify that new RLI is created
		sugar().revLineItems.recordView.getDetailField("name").assertContains(sugar().revLineItems.getDefaultData().get("name"), true);

		// The sales stage defaults to "Prospecting" and probability defaults to "10%".
		sugar().revLineItems.recordView.getDetailField("salesStage").assertContains(myDataSource.get(0).get("salesStage"), true);
		sugar().revLineItems.recordView.getDetailField("probability").assertContains(myDataSource.get(0).get("probability"), true);
		sugar().revLineItems.recordView.edit();

		// Change sales stage in the record view 
		sugar().revLineItems.recordView.getEditField("salesStage").set(myDataSource.get(1).get("salesStage"));

		// Verify the probability is updated automatically in the record view
		sugar().revLineItems.recordView.getEditField("probability").assertContains(myDataSource.get(1).get("probability"), true);
		sugar().revLineItems.recordView.save();

		// Go to the RLI module list view 
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.editRecord(1);

		//  Update sales stage in the list view 
		sugar().revLineItems.listView.getEditField(1, "salesStage").set(myDataSource.get(2).get("salesStage"));
		sugar().revLineItems.listView.saveRecord(1);

		// Verify the probability is updated automatically in the list view
		sugar().revLineItems.listView.getDetailField(1, "salesStage").assertContains(myDataSource.get(2).get("salesStage"), true);
		sugar().revLineItems.listView.getDetailField(1, "probability").assertContains(myDataSource.get(2).get("probability"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}