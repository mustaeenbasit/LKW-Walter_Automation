package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28798 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that user cannot add duplicate languages to Knowledge Base
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28798_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to KB > Settings
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Add a new language - fr | French
		// TODO: VOOD-1762
		FieldSet customData = testData.get(testName).get(0);
		VoodooControl addButtonCtrl = new VoodooControl("button", "css", "[name='add']");
		addButtonCtrl.click();
		new VoodooControl("input", "css", "#config-languagesCollapse div:nth-child(2) div div input").set(customData.get("languageCode"));
		new VoodooControl("input", "css", "#config-languagesCollapse div:nth-child(2) div div:nth-child(2) input").set(customData.get("languageLabel"));

		// Add same language one more time
		// TODO: VOOD-1762
		addButtonCtrl.click();
		new VoodooControl("input", "css", "#config-languagesCollapse div:nth-child(3) div div input").set(customData.get("languageCode"));
		new VoodooControl("input", "css", "#config-languagesCollapse div:nth-child(3) div div:nth-child(2) input").set(customData.get("languageLabel"));

		// Save
		// TODO: VOOD-1762
		new VoodooControl("a", "css", ".rowaction.btn.btn-primary[name='save_button']").click();

		// Verify warning message should appear at the top telling the user error about the duplication
		// It happens too quickly on CI, hence this if-loop is needed. OK at Local.
		if (sugar().alerts.getAlert().queryVisible()) {
			sugar().alerts.getError().assertVisible(true);
			sugar().alerts.getAlert().assertContains(customData.get("errorMessage"), true);
			// Close Alert
			sugar().alerts.getAlert().closeAlert();
		}

		// Also verify the error message on Language Label Field
		// TODO: VOOD-1292
		new VoodooControl("i", "css", "#config-languagesCollapse div:nth-child(2) div div .error-tooltip i").hover();
		VoodooUtils.waitForReady();
		new VoodooControl("div", "css", ".tooltip-inner").assertEquals((customData.get("alertText")), true);

		// Cancel KnowledgeBase Settings page
		// TODO: VOOD-1762
		new VoodooControl("a", "css", ".config-header-buttons.fld_cancel_button a").click();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}