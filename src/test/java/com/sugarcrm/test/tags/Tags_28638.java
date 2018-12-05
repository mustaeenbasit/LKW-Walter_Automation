package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28638 extends SugarTest {
	DataSource tagsData = new DataSource();

	public void setup() throws Exception {
		tagsData = testData.get(testName);
		sugar().accounts.api.create();
		sugar().contacts.api.create();
		
		// Creating tags test data
		sugar().tags.api.create(tagsData);

		// Logging in as admin
		sugar().login();

		// Navigate to Accounts Record and add tags
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		VoodooControl addTagCtrl = sugar().accounts.createDrawer.getEditField("tags");
		
		// Applying all the tags on the account record
		for (int i = 0; i < tagsData.size(); i++)
			addTagCtrl.set(tagsData.get(i).get("name"));

		// Click save 
		sugar().accounts.recordView.save();

		// Navigate to Contact Record and add tag
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);
		sugar().contacts.recordView.edit();

		// Add existing tag (Tag2) in contact Record
		// TODO: VOOD-1772 Add Tags field to all Sidecar modules.
		new VoodooControl("span", "css", ".fld_tag.edit div ul li input").set(tagsData.get(1).get("name"));
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", ".select2-result-label").click();

		// Click save 
		sugar.contacts.recordView.save();
	}

	/**
	 * Verify that when a Tag is edited in Tags Module it is updated system-wide
	 * @throws Exception
	 */
	@Test
	public void Tags_28638_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Tags module
		sugar().tags.navToListView();
		
		// Searching for tag 'Tag2' and selecting
		sugar().tags.listView.setSearchString(tagsData.get(1).get("name"));
		VoodooUtils.waitForReady();
		sugar().tags.listView.clickRecord(1);
		
		// Editing record and saving
		sugar().tags.recordView.edit();
		sugar().tags.recordView.getEditField("name").set(testName);
		sugar().tags.recordView.save();
		
		// Navigate to Accounts Record
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		
		VoodooControl accTagsField = sugar().accounts.recordView.getDetailField("tags");
		
		// Verifying that tag is displayed with edited name 
		accTagsField.assertContains(testName, true);
		
		// Verifying that tag with the old name is not displayed on record
		accTagsField.assertContains(tagsData.get(1).get("name"), false);
		
		// Navigate to Contacts Record
		sugar().contacts.navToListView();
		sugar().contacts.listView.clickRecord(1);

		VoodooControl contactTagsField = sugar().contacts.recordView.getDetailField("tags");
		
		// Verifying that tag is displayed with edited name 
		contactTagsField.assertContains(testName, true);
		
		// Verifying that tag with the old name is not displayed on record
		contactTagsField.assertContains(tagsData.get(1).get("name"), false);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}