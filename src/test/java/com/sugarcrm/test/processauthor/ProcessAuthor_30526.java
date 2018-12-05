package com.sugarcrm.test.processauthor;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class ProcessAuthor_30526 extends SugarTest {

	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify the locked fields can be updated from PA engine on the approve or reject page
	 *
	 * @throws Exception
	 */
	@Test
	public void ProcessAuthor_30526_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet accountData = testData.get(testName).get(0);

		// Import Process Definition
		sugar().processDefinitions.importProcessDefinition("src/test/resources/data/" + testName + ".bpm");
		sugar().processDefinitions.navToListView();

		// Enable Process Definition
		sugar().processDefinitions.listView.openRowActionDropdown(1);
		sugar().processDefinitions.listView.getControl("enableAndDisable01").click();
		sugar().alerts.getWarning().confirmAlert();

		// Goto Accounts list view and edit a account record.
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("name").set(accountData.get("name"));
		sugar().accounts.recordView.showMore();
		// save the edited account record
		sugar().accounts.recordView.save();

		// Goto 'My Processes' view and open 'Show Process' action
		sugar().navbar.navToModule(sugar().processes.moduleNamePlural);

		// Approve the Process through Show Process
		// TODO: VOOD-1706
		sugar().processes.listView.openActionDropdown();
		new VoodooControl("a", "css", ".fld_edit_button a").click();

		// Edit the value of Account Name, Billing City and website
		sugar().processes.recordView.edit();
		sugar().accounts.recordView.getEditField("name").set(testName + " " + accountData.get("name"));
		sugar().accounts.recordView.getEditField("website").set(accountData.get("website"));
		sugar().accounts.recordView.showMore();
		sugar().accounts.recordView.getEditField("billingAddressCity").set(accountData.get("billingAddressCity"));

		// Click over 'Approve' button
		new VoodooControl("a", "css", ".fld_approve_button a").click();
		sugar().alerts.getWarning().confirmAlert();

		// Go to Accounts list view and open the record view of the account associated
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		// Verify that 'Account name', 'Billing City' and 'website' fields should be updated by the changed values and
		// 'Account name' and 'description' fields should show the value that changed by the 'Change Field' element part of the PD
		sugar().accounts.recordView.getDetailField("name").assertEquals(accountData.get("changed_name"), true);
		sugar().accounts.recordView.getDetailField("website").assertEquals(accountData.get("website"), true);
		sugar().accounts.recordView.getDetailField("billingAddressCity").assertEquals(accountData.get("billingAddressCity"), true);
		sugar().accounts.recordView.getDetailField("description").assertEquals(accountData.get("description"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
