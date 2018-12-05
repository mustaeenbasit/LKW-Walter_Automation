package com.sugarcrm.test.cases;

import org.junit.Assert;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

/**
 * @author Dmitry Todarev <dtodarev@sugarcrm.com>
 */
public class Cases_23338 extends SugarTest {

	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Test Case 23338: Case List View_Verify "Mass Update" actions panel is disabled without selecting any case in list view.
	 */
	@Test
	public void Cases_23338_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Nav to the Cases list view
		sugar().cases.navToListView();
		
		// Verify that Mass update action dropdown is disabled
		VoodooControl listViewActionDropDown = sugar().cases.listView.getControl("actionDropdown");
		Assert.assertTrue("Action dropdown is enabled", listViewActionDropDown.isDisabled());
		
		// Check a case
		sugar().cases.listView.checkRecord(1);

		// Verify that Mass update action dropdown is enabled
		Assert.assertFalse("Action dropdown is not enabled", listViewActionDropDown.isDisabled());

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
