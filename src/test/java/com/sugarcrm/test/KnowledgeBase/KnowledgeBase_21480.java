package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_21480 extends SugarTest {
	public void setup() throws Exception {
		sugar().knowledgeBase.api.create();
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify adding custom date field to Knowledge Base list view does not breaks list view
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_21480_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to  KB module, associate the record with Tag : FAQs
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.edit();
		FieldSet customData = testData.get(testName).get(0);
		sugar().knowledgeBase.recordView.getEditField("tags").set(customData.get("tags"));
		sugar().knowledgeBase.recordView.save();

		// Navigate to Studio > KB > Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();

		// TODO: VOOD-542, VOOD-1504
		new VoodooControl("a", "id", "studiolink_KBContents").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();

		// Create a custom Date field called "Article Date" under the KB module
		// Click on "Add Field" button
		new VoodooControl("input", "css", "input[value='Add Field']").click();
		VoodooUtils.waitForReady();

		// Select "Date" type field
		new VoodooControl("option", "css", "#type option[value='date']").click();
		VoodooUtils.waitForReady();

		// Set the name of the field : "Article Date"
		new VoodooControl("input", "id", "field_name_id").set(customData.get("field_name"));
		VoodooUtils.waitForReady();

		// click on Save button
		new VoodooControl("input", "css", "[name='fsavebtn']").click();

		// TODO: VOOD-542 and VOOD-1507
		// Add the custom field : "Article Date" in list view of KB
		// Studio > KB > Layouts > List View
		new VoodooControl("a", "css", ".bodywrapper div a").click();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();
		VoodooUtils.waitForReady();

		// Move the custom field : "Article Date" and "Tags" from hidden panel to default panel
		VoodooControl defaultPanel = new VoodooControl("td", "id", "Default");
		new VoodooControl("li", "css", "#Hidden [data-name='articledate_c']").dragNDrop(defaultPanel);
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "#Hidden [data-name='tag']").dragNDrop(defaultPanel);
		VoodooUtils.waitForReady();
		new VoodooControl("input", "id", "savebtn").click();
		VoodooUtils.focusDefault();

		// Navigate to KB listView
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.editRecord(1);

		// TODO: VOOD-1036 - Need library support for Accounts/any sidecar module for newly created custom fields
		// Set the current date in custom field : "Article Date"
		String todaysDate = VoodooUtils.getCurrentTimeStamp("MM/dd/yyyy");
		new VoodooControl("input", "css", ".fld_articledate_c .datepicker").set(todaysDate);
		sugar().knowledgeBase.listView.saveRecord(1);

		// Verify the List view works with new added column.
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(sugar().knowledgeBase.defaultData.get("name"), true);
		
		// TODO: VOOD-1489 - Need Library Support for All fields moved from Hidden to Default for All Modules
		new VoodooControl("td", "css", ".single [data-type='tag']").assertEquals(customData.get("tags"), true);

		// TODO: VOOD-1036 - Need library support for Accounts/any sidecar module for newly created custom fields
		// Verifying newly created "Article Date" field 
		new VoodooControl("div", "css" ,".fld_"+customData.get("field_name")+"_c.list div").assertEquals((todaysDate), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
