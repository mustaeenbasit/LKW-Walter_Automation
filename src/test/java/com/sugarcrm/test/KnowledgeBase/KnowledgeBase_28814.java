package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * @author Basant Chandak <bchandak@sugarcrm.com>
 */
public class KnowledgeBase_28814 extends SugarTest {
	VoodooControl authorHeader;

	public void setup() throws Exception {
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Knowledge Base: Verify that can reassign KB articles 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_28814_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a Category
		// TODO: VOOD-1754
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewCategories");
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "a[name='add_node_button']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "input.jstree-rename-input:not([style *='display: none'])").set(testName+'\uE007');		
		VoodooUtils.waitForReady();

		// Declaring controls for category & Tags
		// TODO: VOOD-1753
		VoodooControl categoryName = new VoodooControl("div", "css", "[name='category_name']");
		VoodooControl dropdownTag = new VoodooControl("a", "css", ".dropdown-menu .list [data-action='jstree-select']");
		VoodooControl selectTag = new VoodooControl("ul", "css", ".fld_tag .select2-choices");
		VoodooControl tagCtrl = new VoodooControl("input", "css", ".select2-dropdown-open input");
		VoodooControl tagResult = new VoodooControl("span", "css", ".select2-result-label span");

		// Create KB Articles having Tag & Category in it.
		sugar().knowledgeBase.navToListView();
		for(int i = 0 ; i <= 3 ; i++){
			sugar().knowledgeBase.listView.create();
			sugar().knowledgeBase.createDrawer.getEditField("name").set(testName + "_" +i);
			sugar().knowledgeBase.createDrawer.showMore();
			categoryName.click();
			dropdownTag.click();
			selectTag.click();
			tagCtrl.set(testName);
			tagResult.click();
			sugar().knowledgeBase.createDrawer.save();
		}

		// Navigating to UserManagement and clicking Re-assign Records.
		sugar().users.navToListView();
		sugar().navbar.selectMenuItem(sugar().users, "reassignRecords");
		String qauser = sugar().users.getQAUser().get("userName");

		// TODO: VOOD-1023
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl fromUserCtrl = new VoodooControl("select", "id", "fromuser");
		fromUserCtrl.click();
		fromUserCtrl.set("Administrator");
		VoodooControl toUserCtrl = new VoodooControl("select", "id", "touser");
		toUserCtrl.click();
		toUserCtrl.set(qauser);
		new VoodooControl("input", "css", "#modulemultiselect option:nth-child(14)").click();
		new VoodooControl("input", "css", ".button[name='steponesubmit']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[name='KBContents_workflow']").click();
		new VoodooControl("input", "css", ".button[type='submit']").click();
		new VoodooControl("input", "css", "#contentTable .button").click();
		VoodooUtils.focusDefault();

		// Clicking the Gear icon present on the RHS of Listview
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.toggleSidebar();
		sugar().knowledgeBase.listView.getControl("moreColumn").click();

		// Bring the Author Header field on the Knowledge ListView
		// TODO: VOOD-1761
		authorHeader = new VoodooControl("li", "css", "[data-field-toggle='assigned_user_name']");
		authorHeader.click();

		// Verify that KnowledgeBase articles has author as QAuser
		for(int i = 0; i <= 3; i++)
			new VoodooControl("td", "css", ".flex-list-view-content .single:nth-child("+(i+1)+") .fld_assigned_user_name").assertEquals(qauser, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}