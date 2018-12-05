package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;

@Features(revenueLineItem = false)
public class Leads_30187 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		
		// Login as admin
		sugar().login();
	}

	/**
	 * Verify that changes made in Account, Contact and Opportunity panel is validated by application while converting lead
	 * @throws Exception
	 */
	@Test
	public void Leads_30187_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// Convert the Lead
		// TODO: VOOD-585 - Need to have method (library support) to define Convert function in Leads
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.openPrimaryButtonDropdown();
		new VoodooControl("a", "css", ".fld_lead_convert_button.detail a").click();
		VoodooUtils.waitForReady();
		
		// Clicking Contacts panel 'Reset' button
		new VoodooControl("a", "css", "[data-module='Contacts'] .fld_reset_button a").click();
		VoodooUtils.waitForReady();
		
		VoodooControl contactLastName = new VoodooControl("input", "css", "[name='last_name']");
		
		// Editing the Last name in Contacts
		contactLastName.set(testName);
		
		VoodooControl createContactBtn = new VoodooControl("a", "css", "[data-module='Contacts'] [name='associate_button']");
		
		// Clicking 'Create Contact' button
		createContactBtn.click();
		VoodooUtils.waitForReady();
		
		VoodooControl accountName = new VoodooControl("input", "css", "[name='name']");
		
		// Editing the name in Accounts
		accountName.set(testName);
				
		VoodooControl accountCreateBtn = new VoodooControl("a", "css", "[data-module='Accounts'] [name='associate_button']");
		
		// Clicking 'Create Account' button
		accountCreateBtn.click();
		VoodooUtils.waitForReady();

		VoodooControl oppNameInput = new VoodooControl("input", "css", "[aria-label='Opportunity Name']");
		VoodooControl oppCloseDate = new VoodooControl("input", "css", "[name='date_closed']");
		VoodooControl oppLikelyAmount = new VoodooControl("input", "css", "[name='amount']");
		
		// Putting in the 'Name' in Opportunity
		oppNameInput.set(testName);
		
		// Putting in the 'Close Date' in Opportunity
		oppCloseDate.click();
		new VoodooControl("td", "css", ".day.active").click();
		
		String likelyAmt = sugar().opportunities.defaultData.get("likelyCase");
		
		// Putting in the 'Likely Amount' in Opportunity
		oppLikelyAmount.set(likelyAmt);
		
		VoodooControl createOppBtn = new VoodooControl("a", "css", "[data-module='Opportunities'] [name='associate_button']");
		
		// Clicking 'Create Opportunity' button
		createOppBtn.click();
		VoodooUtils.waitForReady();
		
		// Reopen Contact panel
		new VoodooControl("a", "css", "[data-module='Contacts'] .fld_reset_button a").click();
		VoodooUtils.waitForReady();
		
		// Remove required data i.e contact's 'last name' from the Contact panel
		contactLastName.set("");
		
		// Clicking 'Create Contact' button
		createContactBtn.click();
		VoodooUtils.waitForReady();
		
		// Verify that error message: "Error Please resolve any errors before proceeding." is displayed
		sugar().alerts.getError().assertContains(customData.get("errorMessage"), true);
		sugar().alerts.getAlert().closeAlert();
		
		// Changing the Contact record entries from the entries used before
		new VoodooControl("span", "css", ".fld_salutation span").click();
		new VoodooControl("div", "css", ".select2-results li:nth-child(5) div").click();      
		new VoodooControl("input", "css", "[name='first_name']").set(testName);
		String contactLastNameValue = sugar().contacts.defaultData.get("lastName");
		contactLastName.set(contactLastNameValue);
		
		// Clicking 'Create Contact' button
		createContactBtn.click();
		VoodooUtils.waitForReady();
		
		String newContactName = customData.get("changedSalutation") + " " + testName + " " + contactLastNameValue;
		
		// Verify that the changed Contact name is displayed as panel title
		new VoodooControl("span", "css", "[data-module='Contacts'] .title").assertContains(newContactName , true);
		
		// Reopen Accounts panel
		new VoodooControl("a", "css", "[data-module='Accounts'] .fld_reset_button a").click();
		
		// Remove required data i.e account's 'name' from the Account panel
		accountName.set("");
		
		// Clicking 'Create Account' button
		accountCreateBtn.click();
		VoodooUtils.waitForReady();
		
		// Verify that error message: "Error Please resolve any errors before proceeding." is displayed
		sugar().alerts.getError().assertContains(customData.get("errorMessage"), true);
		sugar().alerts.getAlert().closeAlert();
		
		String newAccountName = sugar().accounts.moduleNameSingular + " " + testName;
		
		// Changing the Account record entries from the entries used before
		accountName.set(newAccountName);
				
		// Clicking 'Create Account' button
		accountCreateBtn.click();
		VoodooUtils.waitForReady();
		
		// Verify that the changed Account name is displayed as panel title
		new VoodooControl("span", "css", "[data-module='Accounts'] .title").assertContains(newAccountName , true);
		
		// Remove required data i.e opportunity's 'name', 'close date' and 'likely' amount from the Opportunity panel
		oppNameInput.set("");
		oppCloseDate.set("");
		oppLikelyAmount.set("");
		
		// Clicking 'Create Opportunity' button
		createOppBtn.click();
		
		// Verify that error message: "Error Please resolve any errors before proceeding." is displayed
		sugar().alerts.getError().assertContains(customData.get("errorMessage"), true);
		sugar().alerts.getAlert().closeAlert();
		
		String newOppName = sugar().opportunities.moduleNameSingular + " " + testName;
		
		// Re-inputting the required Opportunity entries
		oppNameInput.set(newOppName);
		oppCloseDate.set(VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy"));
		oppCloseDate.click();
		oppLikelyAmount.set(likelyAmt);
		
		// Clicking 'Create Opportunity' button
		createOppBtn.click();
		VoodooUtils.waitForReady();
		
		// Clicking the 'Save and Convert' button
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		sugar().accounts.navToListView();
		
		// Verify that an account with the changed name (as created above) is displayed
		sugar().accounts.listView.verifyField(1, "name", newAccountName);
		
		sugar().contacts.navToListView();

		// Verify that an account with the changed name (as created above) is displayed
		sugar().contacts.listView.verifyField(1, "fullName", newContactName);

		sugar().opportunities.navToListView();

		// Verify that an account with the changed name (as created above) is displayed
		sugar().opportunities.listView.verifyField(1, "name", newOppName);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}