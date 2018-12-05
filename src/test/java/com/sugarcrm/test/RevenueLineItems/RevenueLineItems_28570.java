package com.sugarcrm.test.RevenueLineItems;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_28570 extends SugarTest {
	FieldSet forecastInfo;

	public void setup() throws Exception {
		forecastInfo =testData.get(testName).get(0);
		sugar().accounts.api.create();
		sugar().opportunities.api.create();

		// Including two Rli's in DataSource
		DataSource rliName = new DataSource();
		FieldSet rliNamefs = new FieldSet();
		rliNamefs.put("name", forecastInfo.get("firstRliName"));
		rliName.add(rliNamefs);
		rliNamefs.clear();
		rliNamefs.put("name", forecastInfo.get("secondRliName"));
		rliName.add(rliNamefs);
		rliNamefs.clear();
		sugar().revLineItems.api.create(rliName);
		sugar().login();
	}

	/**
	 * Verify that warning message to update forecast appears when included RLI is deleted from RLI record view
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_28570_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Configuring the Forecasts setting
		sugar().navbar.navToModule(sugar().forecasts.moduleNamePlural);
		sugar().forecasts.setup.saveSettings();
		
		// Go to admin page(//Hack: Direct navigation to RLI module is not working so using this to change the focus)
		// TODO: VOOD-1434
		sugar().navbar.navToAdminTools();

		// Linking Opportunity with Account Record 
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		sugar().opportunities.recordView.edit();
		sugar().opportunities.recordView.getEditField("relAccountName").set(sugar().accounts.getDefaultData().get("name"));
		sugar().opportunities.recordView.save();

		for(int i = 1;i <=2; i++) {
			sugar().revLineItems.navToListView();
			sugar().revLineItems.listView.clickRecord(1);
			sugar().revLineItems.recordView.edit();
			sugar().revLineItems.recordView.getEditField("relOpportunityName").set(sugar().opportunities.getDefaultData().get("name"));
			if(i==1) {
				sugar().revLineItems.recordView.getEditField("forecast").set(forecastInfo.get("forecastValueInclude"));
				sugar().revLineItems.recordView.save();
				sugar().revLineItems.recordView.delete();

				// Verifying Warning appears related to update Forecast Settings
				sugar().alerts.getWarning().confirmAlert();
				sugar().alerts.getAlert().assertContains(forecastInfo.get("warningMessage"), true);
				sugar().alerts.getAlert().closeAlert();
			} else {
				sugar().revLineItems.recordView.save();
				sugar().revLineItems.recordView.delete();
				sugar().alerts.getWarning().confirmAlert();

				// Verifying No Warning appears related to update Forecast settings
				sugar().alerts.getWarning().assertVisible(false);
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}