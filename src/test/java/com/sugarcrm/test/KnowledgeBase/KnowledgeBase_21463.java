package com.sugarcrm.test.KnowledgeBase;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.PortalTest;

public class KnowledgeBase_21463 extends PortalTest {
	
	public void setup() throws Exception {
		// Creating Case record
		sugar().cases.api.create();
		
		// Logging in as admin
		sugar().login();
		
		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Relationship_Case_Create Article from case detail view
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21463_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		// Initializing test data
		FieldSet customData = testData.get(testName).get(0);
		
		// Navigate to Cases record view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		
		// In Knowledge Base subpanel, create a KB
		StandardSubpanel KBSubPanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);
		KBSubPanel.addRecord();
		
		// Verify that Name, Body and Related Case fields are correctly populated with current case's details
		String caseName = sugar().cases.getDefaultData().get("name");
		sugar().knowledgeBase.createDrawer.getEditField("name").assertEquals(caseName, true);
		VoodooUtils.focusFrame(0);
		VoodooControl KBbody = sugar().knowledgeBase.createDrawer.getEditField("body");
		KBbody.assertContains(customData.get("numberText"), true);
		KBbody.assertContains(customData.get("subjectText") + caseName, true);
		KBbody.assertContains(customData.get("descriptionText") + sugar().cases.getDefaultData().get("description"), true);
		KBbody.assertContains(customData.get("resolutionText") + sugar().cases.getDefaultData().get("resolution"), true);
		VoodooUtils.focusDefault();
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("relCase").assertEquals(caseName, true);
		
		// Click on Cancel button on KB create drawer
		sugar().knowledgeBase.createDrawer.cancel();
		
		// Verify KB record is not created i.e. KB subpanel contains no records
		Assert.assertTrue("KB record is created when it should not!", KBSubPanel.countRows() == 0);
		
		// Create a KB record this time
		KBSubPanel.addRecord();
		sugar().knowledgeBase.createDrawer.save();
		
		// Verify a KB record is created i.e. KB subpanel contains a record
		Assert.assertTrue("KB record is not displayed in subpanel when it should!", KBSubPanel.countRows() == 1);
		
		// Verify KB record is displayed as created in KB subpanel 
		KBSubPanel.getDetailField(1, "name").assertEquals(caseName, true);

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
} 