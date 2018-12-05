package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest; 

public class Leads_17973 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
		sugar().leads.api.create();
	}

	/**
	 * TC 17973: No Create New in Filter when select All in Related in Leads' sub panel
	 * @throws Exception
	 */
	@Test
	public void Leads_17973_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		sugar().leads.recordView.showDataView();

		// TODO: VOOD-598.  Once it is fixed, please update the following steps
		// Select All in Related
		new VoodooControl("i", "css", "span.select2-chosen i.fa-caret-down").click();
		new VoodooControl("li", "css", "ul.select2-results li:first-child").click();

		// Verify that Filter is not a drop down
		new VoodooControl("div", "css", "div.select2-drop-active.select2-drop-above").assertVisible(false);
		// Verify no Create
		new VoodooControl("div", "css", "div.filter-view.search.layout_Leads").assertContains("Create", false);
		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}