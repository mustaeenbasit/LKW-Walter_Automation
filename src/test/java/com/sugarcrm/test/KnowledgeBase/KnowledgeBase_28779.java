package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_28779 extends SugarTest {
	FieldSet customData = new FieldSet();
	
	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().knowledgeBase.api.create();
		sugar().login();
		
		// Enable KB module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
		
		// Create a Category
		// TODO: VOOD-1754
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "a[name='add_node_button']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(customData.get("category")+'\uE007');		
		VoodooUtils.waitForReady();
				
		// Go to knowledgeBase Record view and add Category and External Article
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.showMore();
		sugar().knowledgeBase.recordView.edit();
		sugar().knowledgeBase.createDrawer.getEditField("isExternal").set(customData.get("isExternal"));
		sugar().knowledgeBase.createDrawer.getEditField("status").set(customData.get("status"));
		
		// TODO: VOOD-695 & VOOD-1754
		new VoodooControl("div", "css", ".fld_category_name.edit div").click();
		VoodooUtils.waitForReady();
		new VoodooSelect("li", "css", ".fld_category_name.edit .dropdown-menu .tree ul li").click();
		sugar().knowledgeBase.recordView.save();
		
		sugar().logout();
		
		// Login as qauser
		sugar().login(sugar().users.getQAUser());
	}

	/**
	 * Verify that KB has copy drop down action and works correctly
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28779_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Go to knowledgeBase record view and click copy
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.clickRecord(1);
		sugar().knowledgeBase.recordView.copy();
		
		// Change the Name of the KB in order to distinguish the KB with original one
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.save();
		VoodooUtils.waitForReady();
		sugar().knowledgeBase.recordView.showMore();
		
		// Verify that the Status should be Draft even the original KB has non-Draft status
		sugar().knowledgeBase.recordView.getDetailField("status").assertEquals(sugar().knowledgeBase.getDefaultData().get("status"), true);
		
		// Verify that the Name field has the string which is entered
		sugar().knowledgeBase.recordView.getDetailField("name").assertEquals(testName, true);
		
		// Verify that the values from of fields are copied over besides of Status
		sugar().knowledgeBase.recordView.getDetailField("body").assertEquals(sugar().knowledgeBase.getDefaultData().get("body"), true);
		sugar().knowledgeBase.recordView.getDetailField("language").assertEquals(customData.get("language"), true);
		sugar().knowledgeBase.recordView.getDetailField("relAssignedTo").assertEquals(customData.get("relAssignedTo"), true);
		sugar().knowledgeBase.recordView.getDetailField("relTeam").assertEquals(customData.get("relTeam"), true);
		// TODO: VOOD-1754
		new VoodooControl("a", "css", ".detail.fld_category_name div a").assertContains(customData.get("category"), true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}