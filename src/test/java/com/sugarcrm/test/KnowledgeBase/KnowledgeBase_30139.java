package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30139 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();

		// Enable knowledge Base Module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * KB: Verify 'Template Name' should be displayed as column name in search and select Templates layout.
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30139_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		// Navigate to Knowledge Base -> Create Template
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createTemplate");
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.save();
		sugar().alerts.getSuccess().closeAlert();
		
		// Click on 'Create Article' in KB
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		
		// TODO: VOOD-1756
		// Clicking on template button in Kb create drawer
		new VoodooControl("a", "css", "[data-type='template-button'] .btn").click();
		
		VoodooUtils.waitForReady();
		
		// Verifying 'Template Name' is displaying as column name in search and select Templates layout.
		new VoodooControl("th", "css", ".search-and-select [data-fieldname='name']").assertEquals(customData.get("name"), true);
		
		// Cancel the search and select drawer of KB template
		new VoodooControl("a", "css", "span[data-voodoo-name='close'] a").click();
		
		// Cancel the create drawer of kb
		sugar().knowledgeBase.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}