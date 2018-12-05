package com.sugarcrm.test.reports;

import java.text.DecimalFormat;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.records.OpportunityRecord;
import com.sugarcrm.sugar.records.RevLineItemRecord;
import com.sugarcrm.test.SugarTest; 

public class Reports_26649 extends SugarTest {
	FieldSet customData, currencyData;
	DataSource ds;
	AccountRecord myAccount;
	RevLineItemRecord myRLI;
	OpportunityRecord myOpp;
	VoodooControl showPreferedCurrency;

	public void setup() throws Exception {				
		customData = testData.get(testName+"_1").get(0);
		ds = testData.get(testName);
		myAccount = (AccountRecord)sugar().accounts.api.create();
		sugar().login();

		// Create 3 Currencies
		for(int i = 0; i < ds.size(); i++) {
				currencyData = new FieldSet();
				currencyData.put("currencyName", testName+"_"+i);
				currencyData.put("conversionRate", ds.get(i).get("conversionRate"));
				currencyData.put("currencySymbol", ds.get(i).get("currencySymbol"));
				sugar().admin.setCurrency(currencyData);
		}
		
		// Navigate to user's Profile
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("tab4").click();
		
		// Verify 'Show Preferred Currency' is unchecked
		showPreferedCurrency = sugar().users.userPref.getControl("advanced_showpreferedCurrency");
		Assert.assertFalse("Show Prefered Currency is checked", showPreferedCurrency.isChecked());
				
		// Select Preferred currency as 'Yen' and save
		sugar().users.userPref.getControl("advanced_preferedCurrency").set(testName+"_2 : "+ds.get(2).get("currencySymbol"));
		sugar().users.userPref.getControl("save").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusDefault();		

		// Associate Account to opportunity.
		FieldSet fs = new FieldSet();
		fs.put("relAccountName", myAccount.getRecordIdentifier());
		myOpp = (OpportunityRecord) sugar().opportunities.create(fs);

		// Associate opportunity to RLI.
		FieldSet fs1 = new FieldSet();
		for(int i = 0; i < ds.size(); i++) {
			fs1.put("name", ds.get(i).get("rliName"));
			myRLI = (RevLineItemRecord) sugar().revLineItems.api.create(fs1);
			myRLI.navToRecord();
			sugar().revLineItems.recordView.edit();
			sugar().revLineItems.createDrawer.getEditField("relOpportunityName").set(myOpp.getRecordIdentifier());
			
			// TODO: VOOD-983 -Need lib support for currency dropdowns in RLI
			new VoodooControl("span", "css", "div[data-name='discount_price'] .currency.edit.fld_currency_id").click();
			new VoodooControl("li", "css", "#select2-drop > ul > li:nth-child("+(2+i)+")").waitForVisible();
			new VoodooControl("li", "css", "#select2-drop > ul > li:nth-child("+(2+i)+")").click();
			
			sugar().revLineItems.createDrawer.getEditField("unitPrice").set(ds.get(i).get("currencyAmount")) ;
			sugar().revLineItems.recordView.save();
			sugar().alerts.waitForLoadingExpiration();
		}
		VoodooUtils.focusDefault();
	}

	/**
	 * Verify currency value is displayed correctly in report according to Show Preferred Currency option
	 * @throws Exception
	 */
	@Test
	public void Reports_26649_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// TODO: VOOD-822
		VoodooControl createSummationReportCtrl = new VoodooControl("img", "css", "img[name='summationWithDetailsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");
		VoodooControl reportNameCtrl = new VoodooControl("input", "id", "save_report_as");
		VoodooControl saveAndRunCtrl = new VoodooControl("input", "css", "#report_details_div table:nth-child(1) tbody tr td #saveAndRunButton");
		VoodooControl nxtbtnCtrl = new VoodooControl("input", "css", "#chart_options_div table:nth-child(5) tbody tr td #nextButton");

		// Create Custom Report in RLI module, select the created custom field
		sugar().navbar.navToModule(customData.get("module_plural_name"));
		sugar().navbar.selectMenuItem(sugar().reports, "createReport");
		VoodooUtils.focusFrame("bwc-frame");
		sugar().alerts.waitForLoadingExpiration();
		createSummationReportCtrl.click();
		new VoodooControl("table", "id", "Revenue Line Items").click();
		nextBtnCtrl.click();
		new VoodooControl("tr", "id", "RevenueLineItems_date_closed:year").click();
		nextBtnCtrl.click();
		new VoodooControl("input", "id", "display_summaries_row_group_by_row_1_input").set(customData.get("fiscal_year"));//Fiscal Year: Expected Close Date
		nextBtnCtrl.click();		
		new VoodooControl("tr", "id", "RevenueLineItems_name").click();
		new VoodooControl("tr", "id", "RevenueLineItems_likely_case").click();
		new VoodooControl("tr", "id", "RevenueLineItems_discount_price").click();
		new VoodooControl("tr", "id", "RevenueLineItems_discount_usdollar").click();
		nextBtnCtrl.click();
		nxtbtnCtrl.click();
		reportNameCtrl.set(customData.get("report_name"));
		saveAndRunCtrl.click();
		sugar().alerts.waitForLoadingExpiration();

		// Verify currency amount according to  report before set profile currency show preferred
		// TODO: VOOD-822
		for(int i = 0; i < ds.size(); i++) {
			double priceInDoller = Double.parseDouble(ds.get(i).get("currencyAmount")) / Double.parseDouble(ds.get(i).get("conversionRate"));
			DecimalFormat formatter = new DecimalFormat("##,###.00");
			String unitPriceInDoller = String.format("%s%s", "$",formatter.format(priceInDoller));
			
			new VoodooControl("td","xpath","//*[@class='reportDataChildtablelistView']/tbody/tr[contains(.,'"+ds.get(i).get("rliName")+"')]/td[1]").assertContains(ds.get(i).get("rliName"), true);
			new VoodooControl("td","xpath","//*[@class='reportDataChildtablelistView']/tbody/tr[contains(.,'"+ds.get(i).get("rliName")+"')]/td[3]").assertContains(ds.get(i).get("currencyAmount"), true);
			new VoodooControl("td","xpath","//*[@class='reportDataChildtablelistView']/tbody/tr[contains(.,'"+ds.get(i).get("rliName")+"')]/td[4]").assertContains(unitPriceInDoller, true);
		}	
		VoodooUtils.focusDefault();

		// Go to My Profile and check Show Preferred Currency option
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.userPref.getControl("tab4").click();
		showPreferedCurrency.click();
		VoodooUtils.focusDefault();
		sugar().users.editView.save();
		sugar().alerts.waitForLoadingExpiration();
		
		// Return to report page
		sugar().navbar.navToModule(customData.get("module_plural_name"));
		sugar().alerts.waitForLoadingExpiration();
		sugar().navbar.clickRecentlyViewed(sugar().reports, customData.get("report_name"));
		VoodooUtils.focusFrame("bwc-frame");
		
		// Verify currency and converted amount accordingly on report
		// TODO: VOOD-822
		for(int i = 0; i < ds.size(); i++) {
			new VoodooControl("td","xpath","//*[@class='reportDataChildtablelistView']/tbody/tr[contains(.,'"+ds.get(i).get("rliName")+"')]/td[1]").assertContains(ds.get(i).get("rliName"), true);
			new VoodooControl("td","xpath","//*[@class='reportDataChildtablelistView']/tbody/tr[contains(.,'"+ds.get(i).get("rliName")+"')]/td[3]").assertContains(ds.get(i).get("AmountInPreferredCurrency"), true);
			new VoodooControl("td","xpath","//*[@class='reportDataChildtablelistView']/tbody/tr[contains(.,'"+ds.get(i).get("rliName")+"')]/td[4]").assertContains(ds.get(i).get("currency_in_yen"), true);
		}
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}