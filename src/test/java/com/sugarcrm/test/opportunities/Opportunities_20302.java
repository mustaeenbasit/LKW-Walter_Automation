package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_20302 extends SugarTest {
	FieldSet currencyData = new FieldSet();
	FieldSet currencyMngt;
	
	public void setup() throws Exception {
		currencyMngt = testData.get(testName+"_currency").get(0);
		sugar().accounts.api.create();
		sugar().login();
		
		// Create Currency custom currency 'Euro'
		currencyData.put("currencyName", testName);
		currencyData.put("conversionRate", currencyMngt.get("rate"));
		currencyData.put("currencySymbol", currencyMngt.get("symbol"));
		sugar().admin.setCurrency(currencyData);
	}

	/**
	 *  Verify that currency drop down is able to open and select when fill in the value first
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_20302_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Opportunities module
		sugar().opportunities.navToListView();
		
		// TODO: VOOD-983
		VoodooSelect likelyCase = new VoodooSelect("span", "css", ".fld_amount .fld_currency_id");
		VoodooSelect bestCase = new VoodooSelect("span", "css", ".fld_best_case .fld_currency_id");
		VoodooSelect worstCase = new VoodooSelect("span", "css", ".fld_worst_case .fld_currency_id");
				
		// Create a record by filling in value in Likely, then click on currency drop down besides of Likely to switch currency
		sugar().opportunities.listView.create();
		sugar().opportunities.createDrawer.getEditField("likelyCase").set(sugar().opportunities.getDefaultData().get("likelyCase"));
		likelyCase.set(currencyMngt.get("symbol"));
		
		// Verify that it allows to select currency
		likelyCase.assertContains(currencyMngt.get("symbol"), true);
		
		// Fill value in Best field, then click on currency drop down besides of Best field to switch currency
		sugar().opportunities.createDrawer.getEditField("bestCase").set(currencyMngt.get("bestCase"));
		bestCase.set(currencyMngt.get("symbol"));
		
		// Verify that it allows to select currency
		bestCase.assertContains(currencyMngt.get("symbol"), true);
		
		// Fill value in Worst field, then click on currency drop down besides of Worst field to switch currency
		sugar().opportunities.createDrawer.getEditField("worstCase").set(currencyMngt.get("worstCase"));
		worstCase.set(currencyMngt.get("symbol"));
		
		// Verify that it allows to select currency
		worstCase.assertContains(currencyMngt.get("symbol"), true);
		
		// Fill in other required fields and save
		sugar().opportunities.createDrawer.getEditField("name").set(sugar().opportunities.getDefaultData().get("name"));
		sugar().opportunities.createDrawer.getEditField("relAccountName").set(sugar().opportunities.getDefaultData().get("relAccountName"));
		sugar().opportunities.createDrawer.getEditField("date_closed").set(sugar().opportunities.getDefaultData().get("date_closed"));
		sugar().opportunities.createDrawer.save();
		
		// Verify that the record is saved successfully
		sugar().opportunities.listView.verifyField(1, "name", sugar().opportunities.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}