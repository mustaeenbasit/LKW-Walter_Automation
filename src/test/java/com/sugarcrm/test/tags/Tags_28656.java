package com.sugarcrm.test.tags;

import org.junit.Test;
import org.openqa.selenium.Keys;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Tags_28656 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify that user is not allowed to add blank tags
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28656_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet tagsFS = testData.get(testName).get(0);
		
		// Navigate to Accountss module
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
				
		// Verify that pressing just Enter at Tag field does not input anything
		// TODO: VOOD-1437 - Need lib support to send "Key" to a control
		new VoodooControl("input", "css", ".fld_tag.edit input.select2-input").set(""+'\uE007');
		new VoodooControl("li", "css", ".select2-results:not([role]) li.select2-no-results").assertVisible(true);
		new VoodooControl("li", "css", ".select2-results:not([role]) li.select2-no-results").assertEquals("Please enter 1 or more character", true);
		new VoodooControl("body", "css", "body").click();

		// Verify that "No matches found" appears upon inputting spaces
		sugar().accounts.createDrawer.getEditField("tags").set(tagsFS.get("tagName"));		
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", ".select2-results:not([role]) li.select2-no-results").assertVisible(true);
		new VoodooControl("li", "css", ".select2-results:not([role]) li.select2-no-results").assertEquals("No matches found", true);
		new VoodooControl("body", "css", "body").click();
		
		// Input name
		sugar().accounts.createDrawer.getEditField("name").set(testName);

		// Save createDrawer
		sugar().accounts.createDrawer.save();

		// Go to above Account's recordView
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);

		// Verify Account record has no tag
		sugar().accounts.recordView.getDetailField("tags").assertEquals("", true);		
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}