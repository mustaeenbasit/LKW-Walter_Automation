package com.sugarcrm.test.leads;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_27903 extends SugarTest {
	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Verify that error message is replaced by success message
	 *
	 * @throws Exception
	 */
	@Test
	public void Leads_27903_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);

		// Go to leads Record view
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);

		// Click "Convert Lead" button in "Lead" detail view.
		sugar().leads.recordView.openPrimaryButtonDropdown();

		// TODO: VOOD-585
		new VoodooControl("a", "css", ".fld_lead_convert_button a").click();
		sugar().alerts.waitForLoadingExpiration();

		// Contact record is automatically linked, we need to click on reset
		new VoodooControl("a", "css", "div[data-module=Contacts] .fld_reset_button a").click();
		VoodooUtils.waitForReady();
		
		// Ensure all required fields are filled out for the Contact and then click Create Contact button
		new VoodooControl("span", "css", ".active [data-module='Contacts'] .fld_associate_button a").click();
		VoodooUtils.waitForReady();
		
		// Verify no error alert message appears and record added to it
		sugar().alerts.getError().assertVisible(false);
		new VoodooControl("div", "css", "div[data-module=Contacts] span.step-circle.complete").assertExists(true);

		// Click on create Account without required field
		VoodooControl accCreateButton = new VoodooControl("span", "css", ".active [data-module='Accounts'] .fld_associate_button a");
		accCreateButton.click();

		// Verify that pop-up stating "Error Please resolve any errors before proceeding" appears
		sugar().alerts.getError().assertContains(customData.get("errorMessage"), true);
		sugar().alerts.getError().closeAlert();

		// Fill the required fields and click on Create Account
		new VoodooControl("input", "css", "#collapseAccounts .fld_name input").set(sugar().accounts.getDefaultData().get("name"));
		accCreateButton.click();

		// Verify no error alert message appears and record added to it
		sugar().alerts.getError().assertVisible(false);
		new VoodooControl("div", "css", "div[data-module=Accounts] span.step-circle.complete").assertExists(true);

		// Click on create Opportunity without required field
		VoodooControl oppCreateButton = new VoodooControl("span", "css", ".active [data-module='Opportunities'] .fld_associate_button a");
		oppCreateButton.click();

		// Verify that pop-up stating "Error Please resolve any errors before proceeding" appears
		sugar().alerts.getError().assertContains(customData.get("errorMessage"), true);
		sugar().alerts.getError().closeAlert();

		// Fill the required fields and click on create Opportunity
		new VoodooControl("input", "css", "#collapseOpportunities .fld_name input").set(sugar().opportunities.getDefaultData().get("name"));
		oppCreateButton.click();

		// Verify no error alert message appears and record added to it
		sugar().alerts.getError().assertVisible(false);
		new VoodooControl("div", "css", "div[data-module=Opportunities] span.step-circle.complete").assertExists(true);
		new VoodooControl("a", "css", ".layout_Leads .convert-headerpane.fld_cancel_button a").click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}