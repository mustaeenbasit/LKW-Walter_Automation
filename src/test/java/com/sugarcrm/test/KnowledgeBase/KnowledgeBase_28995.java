package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28995 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
		
		// Enable KB module for sub-panel
		sugar().admin.enableSubpanelDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that Kb is created correctly in the sub panel of Cases
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28995_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigating to Cases list view and noting down value for case number
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		String caseNumber = sugar().cases.recordView.getDetailField("caseNumber").getChildElement("div", "css", ".ellipsis_inline").getAttribute("title");

		// Click on '+' icon in knowledgeBase sub panel
		StandardSubpanel knowledgeBaseSubpanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);
		knowledgeBaseSubpanel.scrollIntoViewIfNeeded(false);

		// TODO: TR-10085 - As '+' icon is not click able therefore using below work around
		knowledgeBaseSubpanel.getChildElement("a", "css", ".layout_KBContents [name='create_button']").click();

		// Asserting the name and body field in KnowledgeBase create Drawer 
		sugar().knowledgeBase.createDrawer.getEditField("name").set(sugar().cases.getDefaultData().get("name"));

		VoodooControl kbBodyField = sugar().knowledgeBase.createDrawer.getEditField("body");

		VoodooUtils.focusFrame(0);
		kbBodyField.hover();
		kbBodyField.assertContains(sugar().cases.getDefaultData().get("name"), true);
		kbBodyField.assertContains(caseNumber, true);
		kbBodyField.assertContains(sugar().cases.getDefaultData().get("description"), true);

		VoodooUtils.focusDefault();
		sugar().knowledgeBase.createDrawer.save();

		// Asserting the record in KnowledgeBase subpanel 
		knowledgeBaseSubpanel.scrollIntoViewIfNeeded(false);
		int subpanelRecordCount = knowledgeBaseSubpanel.countRows();
		Assert.assertTrue("In-Correct records are appearing in KB subpanel", subpanelRecordCount == 1 );
		knowledgeBaseSubpanel.getDetailField(1, "name").assertEquals(sugar().cases.getDefaultData().get("name"), true);
		knowledgeBaseSubpanel.scrollIntoViewIfNeeded(false);
		
		// TODO: As KB subpanel is located last, script is finding it difficult to scroll to the last vertical pixel and click
		//       the name. Find a way out and uncomment the below lines.
		// knowledgeBaseSubpanel.clickRecord(1);
		// Verify the value in Related Case field
		// TODO: VOOD-1749
		// sugar().knowledgeBase.recordView.showMore();
		// new VoodooControl("a", "css", ".fld_kbscase_name a").assertEquals(sugar().cases.getDefaultData().get("name"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}