package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29497 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that blank language set is not created in KB Configuration
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29497_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet error = testData.get(testName).get(0);

		// TODO: VOOD-1843 - Once resolved it should replaced by getChildElement
		VoodooControl language = new VoodooControl("div", "css", "div:nth-child(2)[data-name='languages_languages']");

		// Navigate to KB module list view -> Settings
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");

		// Try to create a new language set. Enter space in language code and language name. Click on Save.
		// TODO: VOOD-1762
		new VoodooControl("button", "css", "button[name='add']").click();
		new VoodooControl("input", "css", language.getHookString() + " [name='key_languages']").set(" ");
		new VoodooControl("input", "css", language.getHookString() + " [name='value_languages']").set(" ");
		new VoodooControl("a", "css", ".config-header-buttons.fld_save_button a").click();

		// Verify that the blank language set should not be created. A red error message bar saying "Error Please resolve any errors before proceeding."
		sugar().alerts.getError().assertEquals(error.get("errorMessage"), true);

		// TODO: VOOD-1762
		new VoodooControl("div", "css", language.getHookString() + " div div:nth-child(2) .languages").assertAttribute("class", error.get("errorClass"), true);
		new VoodooControl("div", "css", language.getHookString() + " div div .languages").assertAttribute("class", error.get("errorClass"), true);

		// Cancel the Setting drawer
		new VoodooControl("a", "css", ".config-header-buttons.fld_cancel_button a").click();
		VoodooUtils.waitForReady();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}