package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_20057 extends SugarTest {
	DataSource customDS = new DataSource();
	UserRecord max;
	VoodooControl adminGlobalSearchCtrl, searchSettingSaveCtrl;

	public void setup() throws Exception {
		customDS = testData.get(testName+"_data");
		DataSource userData = testData.get(testName+"_user");
		sugar().contacts.api.create(customDS);
		sugar().leads.api.create(customDS);
		sugar().login();

		// TODO: VOOD-1200
		// Create Max user
		max = (UserRecord) sugar().users.create(userData.get(0));

		// Contacts records assign to QAuser
		FieldSet massUpdateData = new FieldSet();
		massUpdateData.put("Assigned to", sugar().users.qaUser.get("userName"));
		sugar().contacts.navToListView();
		sugar().contacts.listView.toggleSelectAll();
		sugar().contacts.massUpdate.performMassUpdate(massUpdateData);
		sugar().alerts.waitForLoadingExpiration();

		// Leads records assign to Max
		massUpdateData.put("Assigned to", max.getRecordIdentifier());
		sugar().leads.navToListView();
		sugar().leads.listView.toggleSelectAll();
		sugar().leads.massUpdate.performMassUpdate(massUpdateData);
		sugar().alerts.waitForLoadingExpiration();
	}

	/**
	 * Verify that disable module are not showing on global search result page
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20057_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		VoodooUtils.waitForReady();
		adminGlobalSearchCtrl = sugar().admin.adminTools.getControl("globalSearch");
		adminGlobalSearchCtrl.click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1024 
		// Disable Contact module from admin > search settings
		VoodooControl panalControl = new VoodooControl("tr", "css", "#disabled_div > div.yui-dt-bd");
		new VoodooControl("div", "css", "#enabled_div div.yui-dt-bd .yui-dt-data tr:nth-child(6)").dragNDrop(panalControl);
		searchSettingSaveCtrl = new VoodooControl("input", "css", ".button.primary[value='Save']");
		searchSettingSaveCtrl.click();
		VoodooUtils.waitForReady(60000);
		VoodooUtils.focusDefault();
		VoodooUtils.waitForReady();

		// Logout as Admin and Login as Max
		sugar().logout();
		sugar().login(max);

		// Set value in global search
		VoodooControl globalSearchCtrl = sugar().navbar.getControl("globalSearch");
		String searchText = customDS.get(0).get("lastName").substring(0, 4); // Use for Contacts and Leads both module
		FieldSet customFS = testData.get(testName).get(0);
		globalSearchCtrl.set(searchText+'\uE007');
		VoodooUtils.waitForReady();
		for (int i = 0, j = 0; i < (customDS.size()+customDS.size()); i++, j++) {
			// When loop completed five times then Logout as Max and Login as QAuser and verify same things for QAuser
			if(i == 5) {
				j = 0;
				// Logout as Max and Login as QAuser
				sugar().logout();
				sugar().login(sugar().users.getQAUser());
				globalSearchCtrl.set(searchText+'\uE007');
				VoodooUtils.waitForReady();
			}

			// Verify search results should show only enabled module results
			VoodooControl searchResultCtrl = sugar().globalSearch.getRow((j+1));
			searchResultCtrl.assertContains(customFS.get("moduleIconLe"), true);

			// Verify should not see disabled modules in the detail results page
			searchResultCtrl.assertContains(customFS.get("moduleIconCo"), false);
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}