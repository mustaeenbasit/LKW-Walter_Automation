package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Cases_29014 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();

		// Login
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Created via UI to verify 'view count' on search and select drawer
		sugar().knowledgeBase.create();
	}

	/**
	 * Verify that Values should be displayed in Frequency & Author column of Search and Select Knowledge Base drawer
	 *
	 * @throws Exception
	 */
	@Test
	public void Cases_29014_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to Cases Module, click on a record at list view
		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);
		FieldSet customFS = testData.get(testName).get(0);

		// At Knowledge Base subpanel -> Click on Link Existing Record option under Action drop down
		StandardSubpanel kbSubpanel = sugar().cases.recordView.subpanels.get(sugar().knowledgeBase.moduleNamePlural);
		kbSubpanel.scrollIntoView();
		kbSubpanel.getControl("expandSubpanelActions").click(); 

		// TODO: RS-1255
		//  Go to Knowledge Base sub panel and click on "Link Existing Record" link
		new VoodooControl("a", "css", ".layout_KBContents .fld_select_button.panel-top-for-cases a").click();
		sugar().knowledgeBase.searchSelect.search(sugar().knowledgeBase.getDefaultData().get("name"));
		VoodooUtils.waitForReady();
		// TODO: VOOD-1162
		new VoodooControl("i", "css", ".layout_KBContents .dataTable.search-and-select .nosort.morecol button .fa.fa-cog").click();
		new VoodooControl("a", "css", ".layout_KBContents .dataTable.search-and-select .nosort.morecol ul [data-field-toggle='assigned_user_name']").click();
		VoodooUtils.waitForReady();

		// Verify that values should be displayed in Frequency & Author column as per the KB modules
		new VoodooControl("div", "css", ".layout_KBContents .list.fld_viewcount div").assertContains(customFS.get("frequency"), true);
		new VoodooControl("div", "css", ".layout_KBContents .list.fld_assigned_user_name div").assertContains(customFS.get("author"), true);

		// Close search and select section
		new VoodooControl("input", "css", ".selection-headerpane.fld_close [name='close']").click();

		VoodooUtils.voodoo.log.info(testName + "complete.");
	}

	public void cleanup() throws Exception {}
}