package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_30550 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();

		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that saving blank fields in KB Settings will display correct error messages
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_30550_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customData = testData.get(testName).get(0);
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "configure");
		
		// Adding Language in KB
		// TODO: VOOD-1762
		new VoodooControl("button", "css", ".edit.fld_languages button[name='add']").click();
		new VoodooControl("a", "css", ".layout_KBContents.drawer.active a[name='save_button']").click();
				
		// Verify tool tip appears on hover and its text 
		// TODO: VOOD-1292
		VoodooControl tooltipLanguageCode = new VoodooControl("span", "css", "[data-original-title='" + customData.get("languageCodeTooltipText") + "']");
		VoodooControl tooltipLanguageLabel = new VoodooControl("span", "css", "[data-original-title='" + customData.get("languageLabelToolTipText") + "']");
		VoodooControl tooltipValue = new VoodooControl("div", "css", ".tooltip-inner");
		
		// Verifying Error message for blank fields
		sugar().alerts.getError().assertVisible(true);
		sugar().alerts.getError().assertEquals(customData.get("errorMessage"), true);
		
		// Verifying Language Code text field's color becomes red.
		tooltipLanguageCode.assertCssAttribute(customData.get("tooltipAttribute"), customData.get("tooltipColorValue"), true);
     
		// Mouse hover to tooltip of Language Code field
		tooltipLanguageCode.hover();
		tooltipValue.assertVisible(true);
		
		// Verifying Tooltip text for Language Code field
		tooltipValue.assertEquals(customData.get("languageCodeTooltipText"), true);
		
		// Verifying Language Label text field's also becomes red 
		tooltipLanguageLabel.assertCssAttribute(customData.get("tooltipAttribute"), customData.get("tooltipColorValue"), true);
		
		// Mouse hover to tooltip of Language Label field
		tooltipLanguageLabel.hover();
		tooltipValue.assertVisible(true);
		
		// Verifying Tooltip text for Language Code field
		tooltipValue.assertEquals(customData.get("languageLabelToolTipText"), true);
		
		// TODO: VOOD-1762
		new VoodooControl("span", "css", ".config-header-buttons.fld_cancel_button").click();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}