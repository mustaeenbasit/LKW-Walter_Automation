package com.sugarcrm.test.leads;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooUtils;

public class Leads_18019 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().leads.api.create();
	}

	/**
	 * Clicking a Lead name from the Leads module's List View takes you to full form edit.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Leads_18019_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().leads.navToListView();
		// TODO : VOOD-627. Please update the following steps once it is fixed.
		// Click on first name link
		new VoodooControl("a", "css", ".fieldset.list.fld_full_name a").click();
		
		// Verify that record view is opened
		sugar().leads.recordView.getDetailField("fullName").assertVisible(true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
