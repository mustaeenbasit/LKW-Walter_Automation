package com.sugarcrm.test.opportunities;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Opportunities_17387 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify error validations - detail view create/edit
	 * 
	 * @throws Exception
	 */
	@Test
	public void Opportunities_17387_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Select Opportunities module to display list view
		sugar().opportunities.navToListView();
		
		// Click Create button
		sugar().opportunities.listView.create();
		
		// Leave all fields blank > Click Save
		sugar().opportunities.createDrawer.save();
		
		// TODO: VOOD-1755
		VoodooControl name = new VoodooControl("span", "css", ".fld_name [data-original-title='"+customData.get("errorMessage")+"']");
		VoodooControl accountName = new VoodooControl("span", "css", ".fld_account_name [data-original-title='"+customData.get("errorMessage")+"']");
		VoodooControl expectedClosedDate = new VoodooControl("span", "css", ".fld_date_closed [data-original-title='"+customData.get("errorMessage")+"']");
		VoodooControl likelyAmount = new VoodooControl("span", "css", ".fld_amount [data-original-title='"+customData.get("errorMessage")+"']");
		
		// Verify "Opportunity Name","Account Name","Expected Close date" and "Likely" fields outlined in red 
		name.assertAttribute("class", "error", true);
		accountName.assertAttribute("class", "error", true);
		expectedClosedDate.assertAttribute("class", "error", true);
		likelyAmount.assertAttribute("class", "error", true);
		
		// Verify that message "Error. This field is required" is displayed for "Opportunity Name","Account Name","Expected Close date" and "Likely" fields
		name.assertExists(true);
		accountName.assertExists(true);
		expectedClosedDate.assertExists(true);
		likelyAmount.assertExists(true);
		
		// Click on "Account Name" field
		VoodooSelect relatedAccount = (VoodooSelect)sugar().opportunities.createDrawer.getEditField("relAccountName");
		relatedAccount.click();
		
		// Verify that "Search and Select..." fields link are displayed
		relatedAccount.selectWidget.getControl("searchForMoreLink").assertContains(customData.get("searchAndSelect"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}