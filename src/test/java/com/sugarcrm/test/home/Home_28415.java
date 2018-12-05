package com.sugarcrm.test.home;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Home_28415 extends SugarTest {
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Error is given if you miss filling info in the Feedback
	 * 
	 * @throws Exception
	 */
	@Test
	public void Home_28415_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet fs = testData.get(testName).get(0);
		
		// Click the Feedback in the footer
		VoodooControl feedback = new VoodooControl("span", "css", "[data-action='feedback'] .action-label");
		feedback.click();
		
		for (int i = 0; i < 3; i++) {
			if (i == 1) {
				// Fill comment/feedback
				new VoodooControl("textarea", "css", ".fld_feedback_text textarea").set(fs.get("comment"));
			}
			if (i == 2) {
				// Refresh page to get the feedback popup in fresh state and rating (star) 
				VoodooUtils.refresh();
				VoodooUtils.waitForReady();
				feedback.click();
				new VoodooControl("i", "css", ".field-rating [data-value='3']").click();
			}
			
			// Click send button
			VoodooControl sendFeedback = new VoodooControl("button", "css", "[data-action='submit']");
			sendFeedback.click();
			
			// Verify that error message- "Error Feedback not sent, please fill in both fields" displayed
			sugar.alerts.getError().assertContains(fs.get("error"), true);
			sugar.alerts.getError().closeAlert();
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}