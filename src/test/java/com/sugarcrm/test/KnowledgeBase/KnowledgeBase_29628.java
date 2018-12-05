package com.sugarcrm.test.KnowledgeBase;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29628 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that user with List permission set to "None" sees proper things on KB list views
	 *   
	 * @throws Exception
	 */
	@Ignore("SC-5466 - 'Access Denied' warning message should be displayed when list view is displayed after creating.")
	@Test
	public void KnowledgeBase_29628_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-856
		// Create Role with  Field Permissions not_set_with_access_permission_enable
		FieldSet rolePermissions = testData.get(testName+"_role").get(0);
		AdminModule.createRole(rolePermissions);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("td", "css", "#ACLEditView_Access_KBContents_list div:nth-of-type(2)").click();
		new VoodooControl("select", "css", "td#ACLEditView_Access_KBContents_list div select").set(rolePermissions.get("moduleAccess"));
		VoodooUtils.waitForReady();

		// Click Save button to save the roleNone
		new VoodooControl("input", "id", "ACLROLE_SAVE_BUTTON").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();

		// Assign roleNone to qaUser(Sally)
		AdminModule.assignUserToRole(sugar().users.getQAUser());

		// Logout from Admin and Login as qaUser(Sally)
		sugar().logout();
		sugar().login(sugar().users.qaUser);

		// Navigate to Knowledge Base module (article list view)
		sugar().knowledgeBase.navToListView();
		FieldSet customFS = testData.get(testName).get(0);

		// Verify that Access Denied Contact your Support Administrator to get access to this view for KBContents module
		sugar().alerts.getWarning().assertContains(customFS.get("accessDeniedMsg"), true);
		sugar().alerts.closeAllWarning();

		// Add "Most Useful Published Knowledge Base Articles" dashlet to a Dashboard
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.showMore();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(testName);
		sugar().knowledgeBase.createDrawer.getEditField("category").click();

		// TODO: VOOD-1754
		// Click to Create new KB article
		new VoodooControl("div", "css", ".parenttree.open .dropdown-menu li [data-action='create-new']").click();

		// Try to create new Category on the KB article Create form you just opened on Step 5 (click to focus in
		// the category field -> then click "Create a Category" link -> type a Category name and press Enter on keyboard)
		// TODO: CB-252
		new VoodooControl("input", "css", ".parenttree.open .dropdown-menu li [data-place='bottom-create'] input").set(testName+'\uE007');
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".jstree-focused li a").click();
		VoodooUtils.waitForReady();

		// Fill all required fields, and click "Save" to save the new KB article
		sugar().knowledgeBase.createDrawer.save();

		// Verify that After clicking save, Sally is directed to KB list view with yellow warning that says ""Access Denied Contact your Support Administrator to get access to this view for KBContents module." 
		sugar().alerts.getWarning().assertContains(customFS.get("accessDeniedMsg"), true);

		// Go to KB Activity Stream -> click to open the newly created record -> Add "Other Languages" dashlet to RHS dashboard
		sugar().alerts.getSuccess().waitForVisible();
		sugar().alerts.getSuccess().getChildElement("a", "css", ".alert.alert-success.alert-block.closeable a").click();

		// Select My Dashboard from the right hand side
		VoodooControl dashboardTitle = sugar().accounts.dashboard.getControl("dashboardTitle");
		if(!dashboardTitle.queryContains(customFS.get("dashboardTitle"), true))
			sugar().dashboard.chooseDashboard(customFS.get("dashboardTitle"));

		// TODO: VOOD-591
		// Verify that the Sally should NOT be able to see Other Languages listed in the dashlet, the dashlet should show "No data available"
		new VoodooControl("div", "css", "[data-voodoo-name='kbs-dashlet-localizations'] div").assertContains(customFS.get("dashletMsg"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}