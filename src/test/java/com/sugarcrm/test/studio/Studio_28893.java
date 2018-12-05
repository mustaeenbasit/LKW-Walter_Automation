package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_28893 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Admin->Studio- Dropdown Editor- verify that Deleted Item Name should be strike-through.
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_28893_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-542
		// Go to Admin->Studio->Drop-down Editor.
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickDropdownEditor();

		// TODO: VOOD-781
		// Create a new Drop down from the Editor
		new VoodooControl("input", "css", "[name='adddropdownbtn']").click();
		sugar().admin.studio.waitForAJAX();
		FieldSet customFS = testData.get(testName).get(0);
		new VoodooControl("input", "id", "dropdown_name").set(testName);
		new VoodooControl("input", "id", "drop_name").set(customFS.get("dropdown_item_name1"));
		new VoodooControl("input", "id", "drop_value").set(customFS.get("dropdown_display_label1"));
		new VoodooControl("input", "id", "dropdownaddbtn").click();
		VoodooUtils.waitForReady();

		// Then delete above created item by clicking into the remove (-) icon.
		new VoodooControl("img", "css", "ul.listContainer li.draggable table tr td a:nth-child(2) img").click();
		VoodooUtils.waitForReady();
		
		// Verify Deleted Item Name should be strike-through (Assert "deleted" class(this class use for strike-through) should be exist)
		new VoodooControl("li", "css", "ul.listContainer li").assertAttribute("class", "deleted", true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}