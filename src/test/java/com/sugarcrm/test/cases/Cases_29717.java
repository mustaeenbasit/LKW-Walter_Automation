package com.sugarcrm.test.cases;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Cases_29717 extends SugarTest {
	public void setup() throws Exception {
		sugar().cases.api.create();
		sugar().login();
	}

	/**
	 * Verify that case number & subject name is appearing in subject of email
	 * 
	 * @throws Exception
	 **/
	@Test
	public void Cases_29717_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().cases.navToListView();
		sugar().cases.listView.clickRecord(1);

		// TODO: VOOD-1582 - getText method needed, because case number increments in every run on same instance
		// Get current case number
		String caseNumber = sugar().cases.recordView.getDetailField("caseNumber").getText();
		String subjectName = String.format("[%s:%s] %s", sugar().cases.moduleNameSingular.toUpperCase(), caseNumber, sugar().cases.getDefaultData().get("name"));
		StandardSubpanel emailSub = sugar().cases.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSub.composeEmail();

		// Verify that case number & subject name is appearing in subject of email
		sugar().cases.recordView.composeEmail.getControl("subject").assertEquals(subjectName, true);
		
		// cancel method is not working on CI (Page was not ready within 120000s error). So adding only click code line to cancel drawer which doesnot impact on script
		//sugar().cases.recordView.composeEmail.cancel();
		sugar.cases.recordView.composeEmail.getControl("cancelButton").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
} 