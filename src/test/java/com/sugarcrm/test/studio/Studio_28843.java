package com.sugarcrm.test.studio;

import org.junit.Assert;
import org.junit.Test;
import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class Studio_28843 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Application should display only one validation message instead of multiple messages
	 * @throws Exception
	 */
	@Test
	public void Studio_28843_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		// Navigate to Admin
		sugar().navbar.navToAdminTools();

		// Navigate to Rename Modules, remove the default value for singular label & plural label for Accounts 
		sugar().admin.renameModule(sugar().accounts,"","");
		VoodooUtils.focusFrame("bwc-frame");

		// TODO: VOOD-1367
		// Validate the count of error messages displayed for Singular Label & Plural Label does not exceed more than once for each
		VoodooControl accErrMesgSingularCtrl = new VoodooControl("td", "css", ".module-accounts tbody tr");
		Assert.assertEquals(accErrMesgSingularCtrl.count() == Integer.parseInt(customData.get("errorMesgCount")),true);

		// Validate the error message text for Singular Label & Plural Label
		new VoodooControl("td", "css", ".module-accounts tbody tr").assertContains(customData.get("accountErrorMessageSingular"), true);
		new VoodooControl("td", "css", ".module-accounts tbody tr:nth-child(2)").assertContains(customData.get("accountErrorMessagePlural"),true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
