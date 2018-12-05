package com.sugarcrm.test.studio;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_24636 extends SugarTest {
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Filtering fields when select a function that using date as parameter
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_24636_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		// TODO: VOOD-938
		new VoodooControl("a", "id", "studiolink_Accounts").click();
		VoodooUtils.waitForReady();
		// click on Studio > Accounts > Fields   
		new VoodooControl("td", "id", "fieldsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "#studiofields > input:nth-child(1)").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "field_name_id").set("TestField");
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "calculated").click();
		new VoodooControl("input", "css", "#formulaRow td input[name='editFormula']").click();
		new VoodooControl("textarea", "css", "#formulaInput").set("dayofweek($d");
		// assert popup
		new VoodooControl("a", "css", "#fb_ac_wrapper > ul").assertVisible(true);
		new VoodooControl("a", "css", "#fb_ac_wrapper  ul  li:nth-child(1)").click();
		new VoodooControl("textarea", "css", "#formulaInput").set("dayofweek($date_entered)");
		new VoodooControl("input", "id", "fomulaSaveButton").click();
		// assert saved formula 
		new VoodooControl("input", "id", "formula_display").assertEquals("dayofweek($date_entered)", true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
