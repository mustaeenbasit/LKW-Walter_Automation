package com.sugarcrm.test.opportunities;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_20030 extends SugarTest {
	FieldSet currencySetupData;
	DataSource currencyRecord;
	
	public void setup() throws Exception {
		currencySetupData = testData.get(testName+"_currency").get(0);
		currencyRecord = testData.get(testName);
		sugar().accounts.api.create();
		sugar().login();
		// create new currency i.e. EURO
		sugar().admin.setCurrency(currencySetupData);
	}

	/**
	 *  Amount fields are calculated correct when switching Currency when create a new Opp
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_20030_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// navigate to opportunities listview
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.create();

		// In Likely, Best and Worst fields, fill in values, such as Likely=100, Best=200, Worst=0.
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().opportunities.getDefaultData().get("relAccountName"));
		sugar().opportunities.createDrawer.getEditField("date_closed").set(sugar().opportunities.getDefaultData().get("date_closed"));
		VoodooControl bestCaseCtrl = sugar().opportunities.createDrawer.getEditField("bestCase");
		VoodooControl worstCaseCtrl = sugar().opportunities.createDrawer.getEditField("worstCase");
		VoodooControl likelyCaseCtrl = sugar().opportunities.createDrawer.getEditField("likelyCase");
		bestCaseCtrl.set(currencyRecord.get(0).get("bestCase"));
		worstCaseCtrl.set(currencyRecord.get(0).get("worstCase"));
		likelyCaseCtrl.set(currencyRecord.get(0).get("likelyCase"));
		// TODO: VOOD-1402
		// Click on currency field to open drop down field, switch to EUR from USD.
		new VoodooSelect("span", "css", ".fld_amount.edit .select2-chosen").set(currencySetupData.get("currencySymbol") +" ("+ currencySetupData.get("ISOcode") + ")" );

		// Verify that Likely=90, Best=180, Worst=0. 
		likelyCaseCtrl.assertContains(currencyRecord.get(1).get("likelyCase"), true);
		bestCaseCtrl.assertContains(currencyRecord.get(1).get("bestCase"), true);
		worstCaseCtrl.assertContains(currencyRecord.get(1).get("worstCase"), true);
		// save the record
		sugar().opportunities.createDrawer.save();
		sugar().opportunities.listView.clickRecord(1);
		
		// Verify that Likely=90, Best=180, Worst=0. 
		sugar().opportunities.recordView.getDetailField("likelyCase").assertContains(currencyRecord.get(1).get("likelyCase"), true);
		sugar().opportunities.recordView.getDetailField("bestCase").assertContains(currencyRecord.get(1).get("bestCase"), true);
		sugar().opportunities.recordView.getDetailField("worstCase").assertContains(currencyRecord.get(1).get("worstCase"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}