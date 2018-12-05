package com.sugarcrm.test.calls;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Shagun Kaushik <skaushik@sugarcrm.com>
 */
public class Calls_29955 extends SugarTest {

	public void setup() throws Exception {	
		sugar().login();
	}

	/**
	 * Verify that valid error message is getting displayed while saving the Record after entering
	 * invalid values in Date and time fields.
	 * @throws Exception
	 */
	@Test
	public void Calls_29955_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		
		// Navigate to Calls module
		sugar().navbar.selectMenuItem(sugar().calls, "create"+sugar().calls.moduleNameSingular);
		
		// Filling all mandatory fields i.e. subject here
		sugar().calls.createDrawer.getEditField("name").set(sugar().calls.getDefaultData().get("name"));
		
		// Enter 12312312 (Numbers) in the End date
		sugar().calls.createDrawer.getEditField("date_end_date").set(customData.get("dateInput"));
		
		// Clicking Save to get error
		sugar().calls.createDrawer.save();
		
		// Verify that an error message in red is displayed upon clicking Save
		sugar().alerts.getError().assertContains(customData.get("alertError"), true);
		sugar().alerts.getError().closeAlert();
		
		// Verify that the 'End date' fields are displayed in red (due to error)
		// TODO: VOOD-1755 
		new VoodooControl("span", "css", ".fld_date_end.error").assertVisible(true);
		
		// Verify that upon hovering 'End date' i icon, error is displayed on tooltip
		// TODO: VOOD-1292
		VoodooControl iIcon = new VoodooControl("span", "css", ".fld_date_end.error .error-tooltip");
		iIcon.hover();
		iIcon.assertAttribute("data-original-title", customData.get("hoverError"));
		sugar().calls.createDrawer.cancel();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
