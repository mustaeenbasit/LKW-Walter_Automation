package com.sugarcrm.test.tags;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_30007 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that Tags value are appearing after clicking on preview icon
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_30007_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to accounts module
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set(testName);
		sugar().accounts.createDrawer.showMore();

		// Add two tags in tag field
		FieldSet customDS = testData.get(testName).get(0);
		VoodooControl tagsCtrlRecordView = sugar().accounts.createDrawer.getEditField("tags");
		String firstTag = customDS.get("tagName1");
		String secondTag = customDS.get("tagName2");
		tagsCtrlRecordView.set(firstTag);
		tagsCtrlRecordView.set(secondTag);

		// Save createDrawer
		sugar().accounts.createDrawer.save();

		// Go to listView previewPane
		sugar().accounts.listView.previewRecord(1);

		// Verify the tags displayed in the Preview pane.
		VoodooControl previewTags = sugar().previewPane.getPreviewPaneField("tags");
		previewTags.assertContains(firstTag, true);
		previewTags.assertContains(secondTag, true);

		// Verify the tags in preview pane is a link 
		// TODO: VOOD-1843 - Improve ChildElement to detect the parent strategy
		// Once resolved below code lines should replaced by getChildElement
		VoodooControl tag1 = new VoodooControl("a", "css", ".preview-pane .detail.fld_tag .tag-wrapper a");
		new VoodooControl("a", "css", ".preview-pane .detail.fld_tag .tag-wrapper:nth-of-type(2) a").assertVisible(true);

		// Verify on clicking tag(link), accounts record get searched out in global search result 
		// click method already takes care of its visibility
		tag1.click();
		sugar().globalSearch.getRow(1).assertContains(testName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}