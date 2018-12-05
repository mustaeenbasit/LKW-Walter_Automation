package com.sugarcrm.test.accounts;

import java.util.ArrayList;
import java.util.Iterator;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.Record;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_22985 extends SugarTest {
	DataSource bugData = new DataSource();
	StandardSubpanel bugsSubpanelCtrl;

	public void setup() throws Exception {
		bugData = testData.get(testName);
		ArrayList<Record> bugRecords = new ArrayList<Record>();

		// Create an Account record
		sugar().accounts.api.create();

		// Login as a valid user
		sugar().login();

		// Enable Bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Enable Bugs sub-panel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().bugs);

		// Creating Bug records
		bugRecords = sugar().bugs.create(bugData);

		// Link the Bugs records with the created Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		bugsSubpanelCtrl = sugar().accounts.recordView.subpanels.get(sugar().bugs.moduleNamePlural);
		bugsSubpanelCtrl.linkExistingRecords(bugRecords);
	}

	/**
	 * Account Detail - Bugs sub-panel -Sort_Verify that bug records related to the accounts can be sorted  by column titles on "BUGS" sub-panel
	 * @throws Exception
	 */
	@Test
	public void Accounts_22985_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigates to the record view of the created Account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		VoodooUtils.waitForReady();

		// Add the Bugs Sub-panel headers
		ArrayList<String> columnsHeaderName = new ArrayList<String>();
		columnsHeaderName.add("headerName");
		columnsHeaderName.add("headerStatus");
		columnsHeaderName.add("headerType");
		columnsHeaderName.add("headerPriority");
		columnsHeaderName.add("headerAssignedusername");

		Iterator<String> keySetIterator = bugData.get(0).keySet().iterator();
		String keySet[] = new String[bugData.get(0).keySet().size()];
		int j = 0;
		while(keySetIterator.hasNext()) {
			keySet[j] = keySetIterator.next();
			j++;
		}
		for (int i = 0; i < columnsHeaderName.size(); i++) {
			// Sort by Columns (Descending)
			bugsSubpanelCtrl.sortBy(columnsHeaderName.get(i), false);
			VoodooUtils.waitForReady();
			bugsSubpanelCtrl.getDetailField(1, keySet[i]).assertEquals(bugData.get(1).get(keySet[i]), true);
			bugsSubpanelCtrl.getDetailField(2, keySet[i]).assertEquals(bugData.get(0).get(keySet[i]), true);
			
			// Sort by Columns (Ascending)
			bugsSubpanelCtrl.sortBy(columnsHeaderName.get(i), true);
			VoodooUtils.waitForReady();
			bugsSubpanelCtrl.getDetailField(1, keySet[i]).assertEquals(bugData.get(0).get(keySet[i]), true);
			bugsSubpanelCtrl.getDetailField(2, keySet[i]).assertEquals(bugData.get(1).get(keySet[i]), true);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}