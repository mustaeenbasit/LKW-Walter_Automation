package com.sugarcrm.test.GlobalSearch;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class GlobalSearch_20099 extends SugarTest {
	DataSource customDS = new DataSource();

	public void setup() throws Exception {
		customDS = testData.get(testName);
		FieldSet customFS = new FieldSet();

		// Create a custom Account record
		customFS.put("name", customDS.get(0).get("lastName"));
		sugar().accounts.api.create(customFS);
		customFS.clear();

		// Create custom Contact, Lead and Target record
		sugar().contacts.api.create(customDS.get(1));
		sugar().leads.api.create(customDS.get(2));
		sugar().targets.api.create(customDS.get(3));
		sugar().login();

		// At least one account, lead, contact, target which have email address field filled
		// TODO: VOOD-444
		for (int i = 0; i < customDS.size(); i++) {
			switch (i) {
			// Add email address in Account record
			case 0:
				sugar().accounts.navToListView();
				sugar().accounts.listView.clickRecord(1);
				sugar().accounts.recordView.edit();
				sugar().accounts.recordView.showMore();
				sugar().accounts.recordView.getEditField("emailAddress").set(customDS.get(i).get("emailAddress"));
				sugar().accounts.recordView.save();
				break;
				// Add email address in Contact record
			case 1:
				sugar().contacts.navToListView();
				sugar().contacts.listView.clickRecord(1);
				sugar().contacts.recordView.edit();
				sugar().contacts.recordView.showMore();
				sugar().contacts.recordView.getEditField("emailAddress").set(customDS.get(i).get("emailAddress"));
				sugar().contacts.recordView.save();
				break;	
				// Add email address in Lead record	
			case 2:
				sugar().leads.navToListView();
				sugar().leads.listView.clickRecord(1);
				sugar().leads.recordView.edit();
				sugar().leads.recordView.showMore();
				sugar().leads.recordView.getEditField("emailAddress").set(customDS.get(i).get("emailAddress"));
				sugar().leads.recordView.save();
				break;
				// Add email address in Target record
			case 3:
				sugar().targets.navToListView();
				sugar().targets.listView.clickRecord(1);
				sugar().targets.recordView.edit();
				sugar().targets.recordView.showMore();
				sugar().targets.recordView.getEditField("emailAddress").set(customDS.get(i).get("emailAddress"));
				sugar().targets.recordView.save();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Verify email type filed support Full text search
	 * 
	 * @throws Exception
	 */
	@Test
	public void GlobalSearch_20099_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Define Controls
		VoodooControl globalSearchCtrl =  sugar().navbar.getControl("globalSearch");
		VoodooControl searchResultsCtrl = sugar().navbar.search.getControl("searchResults");
		VoodooControl cancelSearchCtrl = sugar().navbar.search.getControl("cancelSearch");

		for(int i = 0; i< customDS.size(); i++) {
			// Perform a search with email address on global search text box, (e.g. account, contact, lead, target's  email address)
			globalSearchCtrl.set(customDS.get(i).get("emailAddress"));
			VoodooUtils.waitForReady();

			// Verify that record is returned from search
			searchResultsCtrl.assertContains(customDS.get(i).get("lastName"), true);
			searchResultsCtrl.assertContains(customDS.get(i).get("emailAddress"), true);

			// Cancel the search
			cancelSearchCtrl.click();
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}