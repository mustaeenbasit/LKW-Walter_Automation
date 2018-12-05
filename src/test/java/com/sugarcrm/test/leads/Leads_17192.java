package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Leads_17192 extends SugarTest {

	public void setup() throws Exception {
		sugar().leads.api.create();
		sugar().login();
	}

	/**
	 * Convert a Leads record from the Leads module's List View.
	 * @throws Exception
	 */
	@Test
	public void Leads_17192_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		
		// TODO: VOOD-585
		VoodooControl convertOption = new VoodooControl("a", "css", ".fld_lead_convert_button.list a");
		VoodooControl pageHeader = new VoodooControl("div", "css", "div[data-voodoo-name="
				+ "'convert-headerpane'] .fld_title div");
		VoodooControl contactName = new VoodooControl("span", "css", "#convert-accordion div"
				+ "[data-module='Contacts'] .title");
		VoodooControl accNameTextBox = new VoodooControl("input", "css", "div[data-module='Accounts']"
				+ " .fld_name.edit input");
		VoodooControl createAccountBtn = new VoodooControl("a", "css", "div[data-module='Accounts'] "
				+ ".fld_associate_button.convert-panel-header a");
		VoodooControl oppNameTextBox = new VoodooControl("input", "css", "div[data-module="
				+ "'Opportunities'] .fld_name.edit input");
		VoodooControl createOppBtn = new VoodooControl("a", "css","div[data-module='Opportunities']"
				+ " .fld_associate_button.convert-panel-header a");
		
		// Part 1: Converting Lead and clicking Cancel button
		// Navigating to Leads 
		sugar().leads.navToListView();
		sugar().leads.listView.openRowActionDropdown(1);
		convertOption.click();
		VoodooUtils.waitForReady();

		// Verify that Leads Convert page is displayed
		pageHeader.assertEquals(customData.get("leadConvertTitle") + sugar().leads.getDefaultData()
				.get("firstName") +" " + sugar().leads.getDefaultData().get("lastName"), true);

		// Verify that the Contact name is same as the full name of Lead record
		contactName.assertContains(sugar().leads.defaultData.get("fullName"), true);

		// Associate Account
		accNameTextBox.set(customData.get("accountName"));
		createAccountBtn.click();

		// Associate Oppurtunity
		oppNameTextBox.set(customData.get("opportunityName"));
		createOppBtn.click();

		// Click Cancel
		new VoodooControl("a", "css", "div[data-voodoo-name='convert-headerpane'] a[name='cancel_"
				+ "button']").click();
		VoodooUtils.waitForReady();

		// Verify that Account is not created
		sugar().accounts.navToListView();
		sugar().accounts.listView.assertIsEmpty();

		// Verify that Contact is not created
		sugar().contacts.navToListView();
		sugar().contacts.listView.assertIsEmpty();

		// Verify that Opportunity is not created
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.assertIsEmpty();

		// Part 2: Converting Lead and clicking Save button
		// Navigating to Leads
		sugar().leads.navToListView();
		sugar().leads.listView.openRowActionDropdown(1);
		convertOption.click();
		VoodooUtils.waitForReady();

		// Verify that Leads Convert page is displayed
		pageHeader.assertEquals(customData.get("leadConvertTitle") + sugar().leads.getDefaultData()
				.get("firstName") +" " + sugar().leads.getDefaultData().get("lastName"), true);

		// Verify that the Contact name is same as the full name of Lead record
		contactName.assertContains(sugar().leads.defaultData.get("fullName"), true);

		// Associate Account
		accNameTextBox.set(customData.get("accountName"));
		createAccountBtn.click();

		// Associate Oppurtunity
		oppNameTextBox.set(customData.get("opportunityName"));
		createOppBtn.click();

		// Save the conversion
		new VoodooControl("a", "css", ".fld_save_button.convert-headerpane a").click();
		VoodooUtils.waitForReady();

		// Verify the Leads record is converted in record view
		new VoodooControl("span", "css", ".detail.fld_converted span").assertEquals(customData.get
				("status"), true);

		// Verify the Leads record is converted in list view
		sugar().leads.navToListView();
		sugar().leads.listView.verifyField(1, "status", customData.get("status"));

		// Verify that Account is created
		sugar().accounts.navToListView();
		sugar().accounts.listView.getDetailField(1, "name").assertContains(customData.get
				("accountName"), true);

		// Verify that Contact is created
		sugar().contacts.navToListView();
		sugar().contacts.listView.getDetailField(1, "fullName").assertContains(sugar().leads.
				defaultData.get("fullName"), true);

		// Verify that Opportunity is created
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.getDetailField(1, "name").assertContains(customData.get
				("opportunityName"), true);

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}