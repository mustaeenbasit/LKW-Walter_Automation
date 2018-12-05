package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_30009 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify preview has the Tags field populated with the tags info correctly
	 * @throws Exception
	 */
	@Test
	public void Tags_30009_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource tagsDS = testData.get(testName);
		
		// Navigate to Accountss module
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		int size = tagsDS.size();
		
		// TODO: VOOD-1772
		// Create several tags in the Tags field
		for(int i = 0 ; i < size ; i++) {
			new VoodooControl("span", "css", ".fld_tag.edit div ul li input").set(tagsDS
					.get(i).get("tagName"));
			VoodooUtils.waitForReady();
			new VoodooControl("div", "css", ".select2-result-label").click();
		}

		// Save createDrawer
		sugar().accounts.createDrawer.save();

		// Go to above Account's Preview Pane
		sugar().accounts.navToListView();
		sugar().accounts.listView.previewRecord(1);

		// Verify Account preview has tag fields populated with the tags as saved with above
		for(int i = 0; i < size ; i++)
			new VoodooControl("span", "css", "div.preview-pane.active div:nth-child(9) div:nth-child(1)"
					+ " span:nth-child(" + (i+1) + ")").assertContains(tagsDS.get(i).get("tagName"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}