package com.sugarcrm.test.emails;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Emails_28841 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Preview button should show message like "Legacy modules cannot be previewed
	 * 
	 * @throws Exception
	 */
	@Test
	public void Emails_28841_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet toolTipMsg = testData.get(testName).get(0);

		// Quick create - Compose Mail
		sugar().navbar.quickCreateAction(sugar().emails.moduleNamePlural);

		// TODO: VOOD-1423
		// Click on Address book icon in compose mail form
		new VoodooControl("i", "css", "[data-name='to_addresses'] i").click();		

		// Hover 'Preview' icon on the right of any User
		new VoodooControl("i", "css", ".flex-list-view-content .single .fa.fa-eye").hover();
		VoodooControl tooltipValue = new VoodooControl("div", "css", ".tooltip-inner");

		// Assert the visibility of the message after hover on 'Preview' icon
		tooltipValue.assertVisible(true);

		// Assert to validate the message displayed after hover on 'Preview' icon is as expected
		tooltipValue.assertEquals(toolTipMsg.get("toolTipMessage"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}		

	public void cleanup() throws Exception {}
}
