package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28644 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify the tags with long name needs to display properly on the preview page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28644_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to contacts module
		sugar().contacts.navToListView();
		sugar().contacts.listView.create();
		sugar().contacts.createDrawer.getEditField("lastName").set(testName);
		sugar().contacts.createDrawer.showMore();

		// Create several tags in the Tags field with a long name.
		DataSource customDS = testData.get(testName);
		VoodooControl tagsCtrl = sugar().contacts.createDrawer.getEditField("tags");
		for(int i = 0; i < customDS.size(); i++) {
			tagsCtrl.set(customDS.get(i).get("tagName"));
		}

		// Save createDrawer
		sugar().contacts.createDrawer.save();

		// Go to listView previewPane
		sugar().contacts.listView.previewRecord(1);

		// Verify the tags displayed in the Preview pane.
		VoodooControl tags = sugar().previewPane.getPreviewPaneField("tags");
		for(int i = 0; i < customDS.size(); i++)
			tags.assertContains((customDS.get(i).get("tagName")), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}