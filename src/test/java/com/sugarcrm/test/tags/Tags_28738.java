package com.sugarcrm.test.tags;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.StandardModule;
import com.sugarcrm.test.SugarTest;

public class Tags_28738 extends SugarTest {
	public void setup() throws Exception {
		// Create Accounts, Contacts and Calls record
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		sugar().calls.api.create();

		// Login as Admin user
		sugar().login();

		// Edit Accounts, Contacts and Calls record and add Tag field
		addTagsField(sugar().accounts);
		addTagsField(sugar().contacts);
		addTagsField(sugar().calls);
	}

	// Edit Accounts, Contacts and Calls record and add Tag field
	private void addTagsField(StandardModule sugarModule) throws Exception {
		sugarModule.navToListView();
		sugarModule.listView.clickRecord(1);
		sugarModule.recordView.edit();
		sugarModule.recordView.showMore();
		sugarModule.recordView.getEditField("tags").set(testName);
		sugarModule.recordView.save();
	}

	/**
	 * Verify Tag is Link-able to global search results page Immediately after Save
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28738_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to any Accounts record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.showMore();

		// Click on the tag
		// TODO: VOOD-1349
		VoodooControl tagsLinkCtrl = sugar().accounts.recordView.getDetailField("tags");
		new VoodooControl("a", "css", tagsLinkCtrl.getHookString() + " .tag-name").click();
		VoodooUtils.waitForReady();
		FieldSet contactsData = sugar().contacts.getDefaultData();

		// Verify that all records with that tag should appear on global search page
		sugar().globalSearch.getControl("headerpaneTitle").assertContains(testName, true);
		sugar().globalSearch.getRow(1).assertContains(sugar().calls.getDefaultData().get("name"), true);
		sugar().globalSearch.getRow(2).assertContains(contactsData.get("firstName") + " " + contactsData.get("lastName"), true);
		sugar().globalSearch.getRow(3).assertContains(sugar().accounts.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}