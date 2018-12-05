package com.sugarcrm.test.KnowledgeBase;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

public class KnowledgeBase_21438 extends SugarTest {
	DataSource ds = new DataSource();

	public void setup() throws Exception {
		ds = testData.get(testName);

		// create article with body containing string "aaa bbb ccc"
		sugar().knowledgeBase.api.create(ds);
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Article_Search_Condition_With_Space
	 *
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21438_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet fs = testData.get(testName + "_filter").get(0);

		// Click 'Knowledge Base' tab on navigation bar
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);

		// click filter create button
		sugar().calls.listView.getControl("filterDropdown").click();
		sugar().knowledgeBase.listView.getControl("filterCreateNew").click();
		// TODO: VOOD-1879
		// Select 'Body' for fields dropdown, enter 'aaa bbb' string in Containing these words field.
		new VoodooSelect("span", "css", ".fld_filter_row_name.detail").set(fs.get("filter_row_name"));
		new VoodooSelect("span", "css", ".fld_filter_row_operator.detail").set(fs.get("filter_row_operator"));
		VoodooControl fieldValue = new VoodooControl("input", "css", ".detail.fld_kbdocument_body [name='kbdocument_body']");
		fieldValue.set(ds.get(0).get("body"));
		VoodooUtils.waitForReady();

		// verify that only Article containing string "aaa bbb" is displayed
		sugar().knowledgeBase.listView.verifyField(1, "name", ds.get(0).get("name"));

		// cancel the filter
		sugar().knowledgeBase.listView.filterCreate.cancel();
		VoodooUtils.waitForReady();

		// verify that all records comes back after canceling the filter
		sugar().knowledgeBase.listView.verifyField(1, "name", ds.get(1).get("name"));
		sugar().knowledgeBase.listView.verifyField(2, "name", ds.get(0).get("name"));

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}