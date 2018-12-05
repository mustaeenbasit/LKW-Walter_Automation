package com.sugarcrm.test.cases;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

/**
 * @author Dmitry Todarev <dtodarev@sugarcrm.com>
 */
public class Cases_23332 extends SugarTest {
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 * Create Case_Verify that case is not created when using cancel function.
	 */
	@Test
	public void Cases_23332_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Set fields in Cases, not Save but click on Cancel button
		sugar().cases.navToListView();
		sugar().cases.listView.create();
		sugar().cases.createDrawer.showMore();
		sugar().cases.createDrawer.setFields(sugar().cases.getDefaultData());
		sugar().cases.createDrawer.cancel();

		// Verify that case is not created
		sugar().cases.listView.setSearchString(sugar().cases.getDefaultData().get("name"));
		sugar().alerts.waitForLoadingExpiration();
		sugar().cases.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
