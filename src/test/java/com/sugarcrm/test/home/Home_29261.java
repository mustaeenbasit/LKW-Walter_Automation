package com.sugarcrm.test.home;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Home_29261 extends SugarTest {
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * Verify Feedback form still shows user success after receiving an error
	 * 
	 * @throws Exception
	 */
	@Test
	public void Home_29261_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet feedbackData = testData.get(testName).get(0);

		// Click the Feedback button in the footer
		// TODO: VOOD-1361
		new VoodooControl("span", "css", "[data-action='feedback'] .action-label").click();

		// Click Send button on the Feedback form
		VoodooControl sendFeedback = new VoodooControl("button", "css", "[data-action='submit']");
		sendFeedback.click();

		// Verify Red error tells user to fill both fields - "Error Feedback not sent, please fill in both fields"
		sugar.alerts.getError().assertContains(feedbackData.get("errorMessage"), true);
		sugar.alerts.getError().closeAlert();

		// Click any number of stars, and type some text in the field on the Feedback form -> Click Send
		VoodooControl feedbackDescriptionCtrl = new VoodooControl("textarea", "css", ".fld_feedback_text textarea");
		new VoodooControl("i", "css", ".field-rating [data-value='3']").click();
		feedbackDescriptionCtrl.set(feedbackData.get("comment"));
		sendFeedback.click();

		// Verify the errors and form hide from the page, and green alert appears at the top of the page "Success Feedback Sent"
		sugar.alerts.getSuccess().assertContains(feedbackData.get("successMessage"), true);
		sugar.alerts.getError().assertExists(false);
		feedbackDescriptionCtrl.assertExists(false);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}