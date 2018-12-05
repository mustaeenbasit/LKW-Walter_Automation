package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_22899 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		customDS = testData.get(testName);

		// Create three Accounts record
		for (FieldSet fs : customDS) {
			FieldSet newFs = new FieldSet();
			newFs.put("name", fs.get("name"));
			sugar().accounts.api.create(newFs);
		}

		// Login as Admin
		sugar().login();
	}

	/**
	 * Mass Delete Account_Verify that selected account records to be mass updated are deleted from accounts list view.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22899_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Accounts listView
		sugar().accounts.navToListView();

		// Check the check-box in front of the account records to be selected in list view and click on Mass Update action
		sugar().accounts.listView.getControl("selectAllCheckbox").click();
		VoodooUtils.waitForReady();
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.massUpdate();

		// Fill all the fields in "MASS UPDATE" panel.
		sugar().accounts.massUpdate.getControl("massUpdateField02").set(customDS.get(0).get("type"));
		sugar().accounts.massUpdate.getControl("massUpdateValue02").set(customDS.get(0).get("industry"));
		VoodooUtils.waitForReady();

		// Select "Delete" button and confirm action on the popup menu.
		sugar().accounts.listView.openActionDropdown();
		sugar().accounts.listView.delete();
		sugar().accounts.listView.confirmDelete();

		// No matching account records are displayed in accounts list view.
		sugar().accounts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}