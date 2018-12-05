package com.sugarcrm.test.cases;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest;

public class Cases_23326 extends SugarTest {
	DataSource casesCreate;
	AccountRecord account2;

	public void setup() throws Exception {
		casesCreate = testData.get(testName);

		// Creating 2 Account Records
		AccountRecord account1 = (AccountRecord)sugar().accounts.api.create();
		FieldSet fs = new FieldSet();
		fs.put("name",testName);
		account2 = (AccountRecord)sugar().accounts.api.create(fs);

		// Creating 2 Case Records
		sugar().cases.api.create(casesCreate);

		sugar().login();
		sugar().navbar.navToModule(sugar().cases.moduleNamePlural);

		// Adding the Account and Priority value to the Case Records
		sugar().cases.listView.editRecord(1);
		sugar().cases.listView.getEditField(1, "relAccountName").set(account2.getRecordIdentifier());
		sugar().cases.listView.getEditField(1, "priority").set(casesCreate.get(0).get("priority"));
		sugar().cases.listView.saveRecord(1);
		VoodooUtils.waitForReady();
		sugar().cases.listView.editRecord(2);
		sugar().cases.listView.getEditField(2, "relAccountName").set(account1.getRecordIdentifier());
		sugar().cases.listView.getEditField(2, "priority").set(casesCreate.get(1).get("priority"));
		sugar().cases.listView.saveRecord(2);
		VoodooUtils.waitForReady();
		VoodooUtils.refresh(); // The records does not get sorted until refresh
		VoodooUtils.waitForReady();
	}

	/**
	 * Verify that cases can be merged in case list view.
	 * @throws Exception
	 */
	@Test
	public void Cases_23326_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.listView.getControl("selectAllCheckbox").click();
		sugar().cases.listView.openActionDropdown();

		// TODO: VOOD-721
		new VoodooControl("a", "css", ".list.fld_merge_button a").click();
		new VoodooControl("div", "css", ".merge-interface").assertVisible(true);

		VoodooControl mergeName = new VoodooControl("input", "css", ":not(.primary-edit-mode).col .merge-row input[data-field-name='name']");
		VoodooControl mergePriority = new VoodooControl("input", "css", ":not(.primary-edit-mode).col .merge-row input[data-field-name='priority']");
		VoodooControl mergeAccount =  new VoodooControl("input", "css", ":not(.primary-edit-mode).col .merge-row input[data-field-name='account_name']");
		VoodooControl mergeStatus =  new VoodooControl("input", "css", ":not(.primary-edit-mode).col .merge-row input[data-field-name='status']");
		VoodooControl mergeDescription =  new VoodooControl("input", "css", ":not(.primary-edit-mode).col .merge-row input[data-field-name='description']");

		Assert.assertFalse("The radioButton is checked !!", mergeName.isChecked());
		mergeName.click();
		Assert.assertFalse("The radioButton is checked !!", mergePriority.isChecked());
		mergePriority.click();
		Assert.assertFalse("The radioButton is checked !!", mergeAccount.isChecked());
		mergeAccount.click();
		Assert.assertFalse("The radioButton is checked !!", mergeStatus.isChecked());
		mergeStatus.click();
		Assert.assertFalse("The radioButton is checked !!", mergeDescription.isChecked());
		mergeDescription.click();

		// Save the changes done while merge
		new VoodooControl("a", "css", ".merge-duplicates-headerpane a[name='save_button']").click();
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();

		// Assert that the list view is displayed after the merge is complete
		sugar().cases.listView.assertVisible(true);

		// Assert that after merge, only one case record is displayed in the list view
		Assert.assertTrue("There are still 2 records existing.", sugar().cases.listView.countRows()==1);

		// Verifying the values of fields displayed in the list view after merge is complete
		sugar().cases.listView.verifyField(1, "name", casesCreate.get(1).get("name"));
		sugar().cases.listView.verifyField(1, "relAccountName", account2.getRecordIdentifier());
		sugar().cases.listView.verifyField(1, "priority", casesCreate.get(0).get("priority"));
		sugar().cases.listView.verifyField(1, "status", casesCreate.get(1).get("status"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
