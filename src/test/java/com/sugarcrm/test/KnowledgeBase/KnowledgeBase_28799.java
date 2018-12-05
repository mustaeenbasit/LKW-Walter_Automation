package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28799 extends SugarTest {
	public void setup() throws Exception {
		// Create KB article with "Category field"
		sugar().knowledgeBase.api.create();

		// Login
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);

		// Create a Category
		// TODO: VOOD-1754
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "a[name='add_node_button']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(testName+'\uE007');		
		VoodooUtils.waitForReady();

		// Set category field
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.editRecord(1);
		// TODO: VOOD-1754
		new VoodooControl("b", "css", ".fld_category_name.edit .select-arrow b").click();
		new VoodooControl("a", "css", "a[data-action='jstree-select']").click();
		sugar().knowledgeBase.listView.saveRecord(1);
	}

	/**
	 * Knowledge Base: Verify user can properly edit records/articles in List View
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28799_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Verify default fields in article's list view
		sugar().knowledgeBase.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("name"))).assertVisible(true);
		// TODO: VOOD-1768
		new VoodooControl("th", "css", "[data-fieldname='language']").assertVisible(true);
		sugar().knowledgeBase.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("status"))).assertVisible(true);
		new VoodooControl("th", "css", "[data-fieldname='category_name']").assertVisible(true);
		new VoodooControl("th", "css", "[data-fieldname='viewcount']").assertVisible(true);
		sugar().knowledgeBase.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase("date_entered"))).assertVisible(true);

		// On ListView, Click gear icon
		sugar().knowledgeBase.listView.getControl("moreColumn").click();

		// Verify default fields are checked
		// TODO: VOOD-1517
		new VoodooControl("button", "css", "[data-field-toggle='name']").assertAttribute("class", "active", true);
		new VoodooControl("button", "css", "[data-field-toggle='language']").assertAttribute("class", "active", true);
		new VoodooControl("button", "css", "[data-field-toggle='status']").assertAttribute("class", "active", true);
		new VoodooControl("button", "css", "[data-field-toggle='category_name']").assertAttribute("class", "active", true);
		new VoodooControl("button", "css", "[data-field-toggle='viewcount']").assertAttribute("class", "active", true);
		new VoodooControl("button", "css", "[data-field-toggle='date_entered']").assertAttribute("class", "active", true);

		// Verify fields which are not checked
		new VoodooControl("button", "css", "[data-field-toggle='kbsapprover_name']").assertAttribute("class", "active", false);
		new VoodooControl("button", "css", "[data-field-toggle='assigned_user_name']").assertAttribute("class", "active", false);

		// Click the gear icon to close the drawer
		sugar().knowledgeBase.listView.getControl("moreColumn").click();

		// Inline edit languages
		sugar().knowledgeBase.listView.editRecord(1);

		// Verify language field is read-only
		sugar().knowledgeBase.listView.getEditField(1, "language").assertAttribute("class", "edit", false);

		// Inline edit fields
		FieldSet customData = testData.get(testName).get(0);
		sugar().knowledgeBase.listView.getEditField(1, "name").set(customData.get("name"));
		sugar().knowledgeBase.listView.getEditField(1, "status").set(customData.get("status"));
		// TODO: VOOD-806
		new VoodooControl("abbr", "css", ".clear-field").click();

		// Save
		sugar().knowledgeBase.listView.saveRecord(1);

		// Verify edits are saved 
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertContains(customData.get("name"), true);
		sugar().knowledgeBase.listView.getDetailField(1, "status").assertEquals(customData.get("status"), true);
		sugar().knowledgeBase.listView.getDetailField(1, "category").assertEquals("", true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}