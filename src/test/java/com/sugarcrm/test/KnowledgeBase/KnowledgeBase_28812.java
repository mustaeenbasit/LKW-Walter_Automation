package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28812 extends SugarTest {
	VoodooControl studioCtrl, kbCtrl, layoutsCtrl, listViewCtrl, saveBtnCtrl;

	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Navigate to admin
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		studioCtrl = sugar().admin.adminTools.getControl("studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Navigate to KB in studio
		// TODO: VOOD-1504 - Support Studio Module Fields View
		kbCtrl = new VoodooControl("a", "id", "studiolink_KBContents");
		kbCtrl.click();

		// List View
		// TODO: VOOD-1507
		layoutsCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutsCtrl.click();
		listViewCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		listViewCtrl.click(); 

		// Move tags field from "Hidden" to "Default"
		new VoodooControl("li", "css", ".draggable[data-name='tag']").dragNDrop(new VoodooControl(
				"li", "css", ".draggable[data-name='status']"));

		// Save & Deploy
		saveBtnCtrl = new VoodooControl("input", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 * Knowledge Base: Verify Tags can be used properly (in list view, record/edit view, and for filtering
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28812_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource tagAndKBnames = testData.get(testName);

		// TODO: VOOD-1753 - Need Lib support for Tags in KnowledgeBase module
		VoodooControl tagEditField = new VoodooControl("input", "css", ".fld_tag.edit .select2-input");
		VoodooSelect searchNselectTag = new VoodooSelect("span", "css", ".select2-match");
		VoodooControl kbName = sugar().knowledgeBase.createDrawer.getEditField("name");
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Create 2 KB records
		// Creating 1st KB record with 2 tags
		sugar().knowledgeBase.listView.create();
		kbName.set(tagAndKBnames.get(0).get("name"));
		sugar().knowledgeBase.createDrawer.showMore();
		// add tag1
		tagEditField.set(tagAndKBnames.get(0).get("name"));
		VoodooUtils.waitForReady();
		searchNselectTag.click();
		// add tag2
		tagEditField.set(tagAndKBnames.get(1).get("name"));
		VoodooUtils.waitForReady();
		searchNselectTag.click();
		sugar().knowledgeBase.createDrawer.save();

		// Creating 2nd KB record with only one tag
		sugar().knowledgeBase.listView.create();
		kbName.set(tagAndKBnames.get(2).get("name"));
		sugar().knowledgeBase.createDrawer.showMore();
		tagEditField.set(tagAndKBnames.get(2).get("name"));
		VoodooUtils.waitForReady();
		searchNselectTag.click();
		sugar().knowledgeBase.createDrawer.save();
		
		sugar().knowledgeBase.navToListView();

		// Opening the existing KB record in record view
		sugar().knowledgeBase.listView.clickRecord(2);

		// TODO: VOOD-1753 - Need Lib support for Tags in KnowledgeBase module
		// Asserting the tags in the record view
		VoodooControl kbDetailTag1 = new VoodooControl("a", "css", ".fld_tag.detail .tag-wrapper a.tag-name");
		VoodooControl kbDetailTag2 = new VoodooControl("a", "css", ".fld_tag.detail .tag-wrapper:nth-child(2) a.tag-name");

		// Assert the tags on the record view
		kbDetailTag1.assertEquals(tagAndKBnames.get(0).get("name"), true);
		kbDetailTag2.assertEquals(tagAndKBnames.get(1).get("name"), true);

		// Edit the record from the record view
		sugar().knowledgeBase.recordView.edit();

		// Remove a tag from the record view
		new VoodooControl("a", "css", ".fld_tag.edit li:nth-child(2) .select2-search-choice-close").click();

		// Add an Existing tag
		tagEditField.set(tagAndKBnames.get(2).get("name"));
		VoodooUtils.waitForReady();
		searchNselectTag.click();
		sugar().knowledgeBase.recordView.save();

		// Assert the tags after editing on the record view
		kbDetailTag1.assertEquals(tagAndKBnames.get(0).get("name"), true);
		kbDetailTag2.assertEquals(tagAndKBnames.get(2).get("name"), true);

		// Inline edit a record and add/delete tags
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// Sorting because of Product issue : TR-10163 - KB list view : Recently modified record does not appears on the top
		sugar().knowledgeBase.listView.sortBy("headerName", false);
		sugar().knowledgeBase.listView.editRecord(1);

		// Add a tag from list view
		tagEditField.set(tagAndKBnames.get(1).get("name"));
		VoodooUtils.waitForReady();
		searchNselectTag.click();

		// Remove an existing tag from the record in the list view
		new VoodooControl("a", "css", ".layout_KBContents .flex-list-view .single .fld_tag.edit li .select2-search-choice-close").click();
		sugar().knowledgeBase.listView.saveRecord(1);
		VoodooControl tagValueListView = new VoodooControl("a", "css", ".layout_KBContents .flex-list-view .single .fld_tag.list a");
		tagValueListView.assertEquals(tagAndKBnames.get(1).get("name"), true);
		tagValueListView.assertContains(tagAndKBnames.get(0).get("name"), false);

		// Create a filter.
		sugar().knowledgeBase.listView.openFilterDropdown();
		sugar().knowledgeBase.listView.selectFilterCreateNew();

		// Setting-Up filter in list view of KB module
		// TODO: VOOD-1785 - Add/Update Tag-Strategy-Hook Values for Mass Update, Filters in KB, Tags
		new VoodooSelect("span", "css", ".detail.fld_filter_row_name").set(tagAndKBnames.get(0).get("rowName"));
		new VoodooSelect("a", "css", ".detail.fld_filter_row_operator .select2-choice").set(tagAndKBnames.get(0).get("rowOperator"));
		new VoodooControl("input", "css", ".detail.fld_tag .select2-input").set(tagAndKBnames.get(1).get("name"));
		searchNselectTag.click();
		VoodooUtils.waitForReady();
		Assert.assertTrue("Row count is greater than 1, when it should not", sugar().knowledgeBase.listView.countRows()==1);
		tagValueListView.assertEquals(tagAndKBnames.get(1).get("name"), true);	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}