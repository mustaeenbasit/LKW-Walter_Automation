package com.sugarcrm.test.subpanels;

import java.util.ArrayList;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Subpanels_30216 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Verify Expanded Records in the subpanel should not be collapsed when navigated back to same record view
	 * @throws Exception
	 */
	@Test
	public void Subpanels_30216_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");	

		DataSource modulesName = testData.get(testName);
		VoodooControl subpanelCollapsed;
		// Storing default data of modules
		ArrayList<FieldSet> modulesDefaultData = new ArrayList<FieldSet>();
		modulesDefaultData.add(sugar().calls.getDefaultData());
		modulesDefaultData.add(sugar().tasks.getDefaultData());
		modulesDefaultData.add(sugar().contacts.getDefaultData());
		modulesDefaultData.add(sugar().cases.getDefaultData());

		// Navigating to account record view
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Adding subpanels of account record view in Arraylist
		ArrayList<StandardSubpanel> subpanels = new ArrayList<StandardSubpanel>();
		for (int i = 0; i < modulesName.size(); i++)
			subpanels.add(sugar().accounts.recordView.subpanels.get(modulesName.get(i).get("subpanelModuleName")));

		// Verifying expanded behavior for subpanels(calls, meetings, tasks, notes, contacts, leads, cases)
		for (int j = 0; j < subpanels.size(); j++) {
			subpanels.get(j).scrollIntoViewIfNeeded(false);

			// Verifying subpanel is collapsed by default before adding record
			subpanelCollapsed = subpanels.get(j).getControl("subpanelStatus");
			subpanelCollapsed.assertAttribute(modulesName.get(0).get("attributeName"),modulesName.get(0).get("attributeValue"), true);

			// Adding one record
			subpanels.get(j).create(modulesDefaultData.get(j));
			sugar().alerts.getSuccess().closeAlert();

			// Verifying subpanel is expanded after adding record
			subpanels.get(j).scrollIntoViewIfNeeded(false);
			subpanelCollapsed.assertAttribute(modulesName.get(0).get("attributeName"),modulesName.get(0).get("attributeValue"), false);

			// Navigating to other module eg. contact
			sugar().contacts.navToListView();

			// Navigating to account record view again
			sugar().accounts.navToListView();
			sugar().accounts.listView.clickRecord(1);

			// Verifying subpanle still expanded after navigation
			subpanels.get(j).scrollIntoViewIfNeeded(false);
			subpanelCollapsed.assertAttribute(modulesName.get(0).get("attributeName"),modulesName.get(0).get("attributeValue"), false);
		}
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}