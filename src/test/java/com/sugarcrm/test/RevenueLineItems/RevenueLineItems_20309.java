package com.sugarcrm.test.RevenueLineItems;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class RevenueLineItems_20309 extends SugarTest {
	FieldSet customFS, currencyPrefered;
	DataSource currencyData;
	OpportunityRecord myOpportunityRecord;
	String todaysDate;
	
	public void setup() throws Exception {
		// Get current date
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date dt = new Date();
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(dt);
		todaysDate = sdf.format(dt);
		
		customFS = testData.get(testName).get(0);
		currencyData = testData.get(testName+"_currency");
		myOpportunityRecord = (OpportunityRecord) sugar().opportunities.api.create();
		sugar().login();
		
		// Add two new Currencies
		sugar().admin.setCurrency(currencyData.get(0));
		sugar().admin.setCurrency(currencyData.get(1));
		
		// Set User's preferred currency is RMB and User's Show Preferred Currency option is checked.
		currencyPrefered = new FieldSet();
		currencyPrefered.put("advanced_preferedCurrency", testName + "_2 : " + currencyData.get(1).get("currencySymbol"));
		sugar().users.setPrefs(currencyPrefered);
	}

	/**
	 * Verify currency displayed in sub-panel
	 * 
	 * @throws Exception
	 */
	@Test
	public void RevenueLineItems_20309_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	
		
		// Create "Revenue Line Items" with required fields
		sugar().revLineItems.navToListView();
		sugar().revLineItems.listView.create();
		sugar().revLineItems.createDrawer.getEditField("name").set(testName);
		sugar().revLineItems.createDrawer.getEditField("date_closed").set(todaysDate);
		sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpportunityRecord.getRecordIdentifier());
		sugar().revLineItems.createDrawer.showMore();
		
		// Change the default values for worst/likely/best amount and save RLIs 
		sugar().revLineItems.createDrawer.getEditField("likelyCase").set(customFS.get("likelyCase"));
		
		// TODO: VOOD-983
		// Select a currency different from current one of the opportunity (such as selecting "Euro" currency) and then click Save		
		new VoodooSelect("span", "css", "div[data-name='likely_case'] .currency.edit.fld_currency_id").set(currencyData.get(0).get("ISOcode"));
		
		// Click on Save button
		sugar().revLineItems.createDrawer.save();
		
		// Go to opportunity recordView
		// Verify Rli sub-panel that likely will show both base currency (system currency, US Dollar) and transaction currency (record currency, Euro)
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel rliSubPanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);

		// TODO: VOOD-1424
		// Verify first currency (system currency, US Dollar)
		rliSubPanel.scrollIntoView();
		rliSubPanel.expandSubpanel();
		rliSubPanel.getDetailField(1, "likelyCase").assertContains("$"+customFS.get("likelyCase"), true);
		
		// Verify second currency (Created Euro)
		rliSubPanel.getDetailField(1, "likelyCase").assertContains(customFS.get("convertedCurrency"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}