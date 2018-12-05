package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28817 extends SugarTest {
	public void setup() throws Exception {

		// Create KB record with status = published
		FieldSet kbFS = new FieldSet();
		kbFS.put("status", "Published");
		kbFS.put("name", testName);
		sugar().knowledgeBase.api.create(kbFS);

		// Log-In as an admin
		sugar().login();

		// Enable the KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create 5 Revisions of KB record
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		for (int i = 0; i < 5; i++) {
			sugar().knowledgeBase.listView.openRowActionDropdown(1);
			// TODO: VOOD-568 - library support for all of the actions in the RowActionDropdown
			new VoodooControl("a", "css", ".list.fld_create_revision_button a").click();
			VoodooUtils.waitForReady();
			sugar().knowledgeBase.createDrawer.save();
		}
	}

	/**
	 * Knowledge Base: Make sure that user can create many revisions - and they display correctly in subpanel RS-508
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28817_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet kbFS = testData.get(testName).get(0);

		// Test 1 -Check subpanel Header total shows "(5)", no other link is shown.
		// TODO: VOOD-1988 
		sugar().knowledgeBase.listView.clickRecord(1);
		new VoodooControl("span", "css", "[data-voodoo-name='panel-top-for-revisions'] [data-voodoo-name='collection-count'] span").assertEquals(kbFS.get("Header5"), true);
		new VoodooControl("a", "css", "[data-voodoo-name='panel-top-for-revisions'] [data-voodoo-name='collection-count'] span a").assertExists(false);

		// Test 2 - 5 expanded sub panel records- check Header total shows "(5)", link is not shown
		new VoodooControl("h4", "css", "[data-voodoo-name='panel-top-for-revisions'] h4").click();
		VoodooUtils.waitForReady();
		new VoodooControl("span", "css", "[data-voodoo-name='panel-top-for-revisions'] [data-voodoo-name='collection-count'] span").assertEquals(kbFS.get("Header5"), true);
		new VoodooControl("a", "css", "[data-voodoo-name='panel-top-for-revisions'] [data-voodoo-name='collection-count'] span a").assertExists(false);

		// Check total records count is 5
		int subpanelRowsCount = new VoodooControl("a", "css", "[data-voodoo-name='subpanel-for-revisions'] tr.single").count();
		Assert.assertEquals("Number of rows in subpanel dows not equal 5", Integer.parseInt(kbFS.get("int5")), subpanelRowsCount);

		// Test 3 - Add 6 more Revisions (total=11)
		for (int i = 0; i < 6; i++) {
			sugar().knowledgeBase.recordView.openPrimaryButtonDropdown();
			new VoodooControl("a", "css", ".detail .fld_create_revision_button a").click();
			sugar().knowledgeBase.createDrawer.save();
		}

		// Check Header total shows "(5 of 6+)"
		new VoodooControl("span", "css", ".fld_collection-count span").assertEquals(kbFS.get("Header5of6+"), true);

		// Test 4 - Click on link 6+ in "(5 of 6+)"
		new VoodooControl("a", "css", ".fld_collection-count a").click();
		VoodooUtils.waitForReady();

		// Check Header total shows "(5 of 11)", link is no longer shown
		new VoodooControl("span", "css", "[data-voodoo-name='panel-top-for-revisions'] [data-voodoo-name='collection-count'] span").assertEquals(kbFS.get("Header5of11"), true);
		new VoodooControl("a", "css", "[data-voodoo-name='panel-top-for-revisions'] [data-voodoo-name='collection-count'] span a").assertExists(false);

		// Check total records count is still 5
		subpanelRowsCount = new VoodooControl("a", "css", "[data-voodoo-name='subpanel-for-revisions'] tr.single").count();
		Assert.assertEquals("Number of rows in subpanel dows not equal 5",  Integer.parseInt(kbFS.get("int5")), subpanelRowsCount);

		// Test 5 - click Show More
		new VoodooControl("button", "css", "[data-action='show-more']").click();
		VoodooUtils.waitForReady();

		// Assert 10 records are shown
		subpanelRowsCount = new VoodooControl("a", "css", "[data-voodoo-name='subpanel-for-revisions'] tr.single").count();
		Assert.assertEquals("Number of rows in subpanel dows not equal 10",  Integer.parseInt(kbFS.get("int10")), subpanelRowsCount);

		// Assert sub panel header total shows (10 of 11)
		new VoodooControl("span", "css", "[data-voodoo-name='panel-top-for-revisions'] [data-voodoo-name='collection-count'] span").assertEquals(kbFS.get("Header10of11"), true);

		// Test 6 - click Show More again
		new VoodooControl("button", "css", "[data-action='show-more']").click();
		VoodooUtils.waitForReady();

		// Assert 11 records are shown
		subpanelRowsCount = new VoodooControl("a", "css", "[data-voodoo-name='subpanel-for-revisions'] tr.single").count();
		Assert.assertEquals("Number of rows in subpanel dows not equal 11",  Integer.parseInt(kbFS.get("int11")), subpanelRowsCount);

		// Assert sub panel header total shows (11 of 11), show more is not shown
		new VoodooControl("span", "css", "[data-voodoo-name='panel-top-for-revisions'] [data-voodoo-name='collection-count'] span").assertEquals(kbFS.get("Header11"), true);
		new VoodooControl("button", "css", "[data-action='show-more']").assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}