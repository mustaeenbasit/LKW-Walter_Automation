package com.sugarcrm.test.accounts;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Accounts_22918 extends SugarTest {
	DataSource caseDS = new DataSource();

	public void setup() throws Exception {
		// Create multiple case records & an account record.
		caseDS = testData.get(testName);
		sugar().cases.api.create(caseDS);
		sugar().accounts.api.create();

		sugar().login();

		// TODO: VOOD-444
		// Link Account record with case records
		sugar().cases.navToListView();
		sugar().cases.listView.toggleSelectAll();
		sugar().cases.listView.openActionDropdown();
		sugar().cases.listView.massUpdate();
		FieldSet account = new FieldSet();
		account.put("Account Name",sugar().accounts.getDefaultData().get("name"));
		sugar().cases.massUpdate.performMassUpdate(account);
		VoodooUtils.waitForReady();
	}

	/**
	 * Account Detail - Cases sub-panel - Sort_Verify that case records related to the accounts can be sorted  by column titles on "CASES" sub-panel.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_22918_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel casesSubPanel = sugar().accounts.recordView.subpanels.get(sugar().cases.moduleNamePlural);
		casesSubPanel.scrollIntoView();
		casesSubPanel.expandSubpanel();

		// Click on "Name" header
		casesSubPanel.sortBy("headerName", false);
		VoodooUtils.waitForReady();
		// Verify records are in descending order
		for (int i=1; i<=caseDS.size(); i++) {
			// TODO: VOOD-1424 - Once resolved we will use verify() method
			casesSubPanel.getDetailField(i, "name").assertEquals(caseDS.get(i-1).get("name"), true);
		}

		casesSubPanel.sortBy("headerName", true);
		VoodooUtils.waitForReady();
		// Verify records in ascending order
		for (int i=1, j=2; i<=caseDS.size(); i++, j--) {
			// TODO: VOOD-1424 - Once resolved we will use verify() method
			casesSubPanel.getDetailField(i, "name").assertEquals(caseDS.get(j).get("name"), true);
		}

		// Click on "Status" header
		casesSubPanel.sortBy("headerStatus", false);
		VoodooUtils.waitForReady();
		// Verify records are in descending order
		for (int i=1; i<=caseDS.size(); i++) {
			// TODO: VOOD-1424 - Once resolved we will use verify() method
			casesSubPanel.getDetailField(i, "status").assertEquals(caseDS.get(i-1).get("status"), true);
		}

		casesSubPanel.sortBy("headerStatus", true);
		VoodooUtils.waitForReady();
		// Verify records are in ascending order
		for (int i=1, j=2; i<=caseDS.size(); i++, j--) {
			// TODO: VOOD-1424 - Once resolved we will use verify() method
			casesSubPanel.getDetailField(i, "status").assertEquals(caseDS.get(j).get("status"), true);
		}

		// TODO: VOOD-1850 - Once resolved we need to add "Priority" header title sort
		// TODO: VOOD-444 - Support creating relationships via API.

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}