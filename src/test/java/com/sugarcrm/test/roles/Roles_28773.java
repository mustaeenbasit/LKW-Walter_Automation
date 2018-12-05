package com.sugarcrm.test.roles;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooSelect;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28773 extends SugarTest {
	FieldSet roleAndDropDownData = new FieldSet();
	VoodooControl dropdownEditorCtrl, bugPriorityDomCtrl, saveBtnCtrl;

	public void setup() throws Exception {
		roleAndDropDownData = testData.get(testName).get(0);
		sugar().login();

		// Enable Bugs module
		sugar().admin.enableModuleDisplayViaJs(sugar().bugs);

		// Create a role myRole
		AdminModule.createRole(roleAndDropDownData);

		// Assign a user (QAUser) into the Role
		AdminModule.assignUserToRole(roleAndDropDownData);
	}

	/**
	 * Verify that new value is able to be added in the role's customized LOV
	 * 
	 * @throws Exception
	 */
	@Test
	public void Roles_28773_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-781
		// Define Controls
		// Using xPath to search the bug_priority_dom by its name through out the table (DDE can not be deleted and if any test case creates a custom DDE so this test case surely fails as CSS got changed.Hence using xPath)
		bugPriorityDomCtrl = new VoodooControl("a", "xpath", "//*[@id='dropdowns']/table/tbody/tr[contains(.,'"+roleAndDropDownData.get("bugPriorityDom")+"')]/td[contains(.,'"+roleAndDropDownData.get("bugPriorityDom")+"')]/a");
		saveBtnCtrl = new VoodooControl("input", "id", "saveBtn");
		VoodooControl dropItemNameCtrl = new VoodooControl("input", "id", "drop_name");
		VoodooControl dropDisplayValueCtrl = new VoodooControl("input", "id", "drop_value");
		VoodooControl addBtnCtrl = new VoodooControl("input", "id", "dropdownaddbtn");
		VoodooControl roleSelectCtrl = new VoodooControl("select", "css", "select[name='dropdown_role']");
		VoodooControl firstBugCtrl = new VoodooControl("input", "css", "ul#ul1 li#" + roleAndDropDownData.get("firstValue") + " td:nth-child(2) input[type='checkbox']");
		VoodooControl secondBugCtrl = new VoodooControl("input", "css", "ul#ul1 li#" + roleAndDropDownData.get("secondValue") + " td:nth-child(2) input[type='checkbox']");
		VoodooControl lowCtrl = new VoodooControl("input", "css", "ul#ul1 li#Low td:nth-child(2) input[type='checkbox']");
		VoodooControl mediumCtrl = new VoodooControl("input", "css", "ul#ul1 li#Medium td:nth-child(2) input[type='checkbox']");
		VoodooControl highCtrl = new VoodooControl("input", "css", "ul#ul1 li#High td:nth-child(2) input[type='checkbox']");
		VoodooControl urgentCtrl = new VoodooControl("input", "css", "ul#ul1 li#" + roleAndDropDownData.get("urgent") + " td:nth-child(2) input[type='checkbox']");
		dropdownEditorCtrl = sugar().admin.adminTools.getControl("dropdownEditor");

		// Admin goes to Drop Down Editor -> bug_priority_dom
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		dropdownEditorCtrl.click();
		VoodooUtils.waitForReady();
		bugPriorityDomCtrl.click();
		VoodooUtils.waitForReady();

		// Add one value, e.g. firstBug
		dropItemNameCtrl.set(roleAndDropDownData.get("firstValue"));
		dropDisplayValueCtrl.set(roleAndDropDownData.get("firstValue"));
		addBtnCtrl.click();
		// Wait for JS action to conclude properly. waitForReady() and waitForVisible() are not effective here.
		VoodooUtils.pause(2000);

		// Save it
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the "firstBug" is appearing in Default list
		bugPriorityDomCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "ul#ul1 li#" + roleAndDropDownData.get("firstValue")).assertExists(true);

		// Admin select "myRole", Drop Down Editor - De-select Urgent and "firstBug". Now this role has - High, Medium, Low. Save it
		roleSelectCtrl.set(roleAndDropDownData.get("roleName"));
		VoodooUtils.waitForReady();
		urgentCtrl.set(Boolean.toString(false));
		firstBugCtrl.set(Boolean.toString(false));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Admin -> bug_priority_dom, add another value, e.g. secondBug
		bugPriorityDomCtrl.click();
		VoodooUtils.waitForReady();
		dropItemNameCtrl.set(roleAndDropDownData.get("secondValue"));
		dropDisplayValueCtrl.set(roleAndDropDownData.get("secondValue"));
		addBtnCtrl.click();
		// Wait for JS action to conclude properly. waitForReady() and waitForVisible() are not effective here.
		VoodooUtils.pause(2000);

		// Save it
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Verify that the "secondBug" is appearing in Default list
		bugPriorityDomCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "ul#ul1 li#" + roleAndDropDownData.get("secondValue")).assertExists(true);

		// Admin -> bug_priority_dom -> myRole
		roleSelectCtrl.set(roleAndDropDownData.get("updatedRoleName"));
		VoodooUtils.waitForReady();

		// Verify that the "firstBug", "secondBug", Urgent - 3 values are Drop Down Editor - not selected. High, Medium, Low - 3 values are selected
		Assert.assertFalse(urgentCtrl.isChecked());
		Assert.assertFalse(firstBugCtrl.isChecked());
		Assert.assertFalse(secondBugCtrl.isChecked());
		Assert.assertTrue(mediumCtrl.isChecked());
		Assert.assertTrue(lowCtrl.isChecked());
		Assert.assertTrue(highCtrl.isChecked());
		VoodooUtils.focusDefault();

		// Logout from Admin and login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to create a new bug.  Click on the Bug Priority field
		sugar().bugs.navToListView();
		sugar().bugs.listView.create();
		sugar().bugs.createDrawer.showMore();
		VoodooControl bugPriorityFieldCtrl = sugar().bugs.createDrawer.getEditField("priority");
		bugPriorityFieldCtrl.click();
		// VOOD-1463
		VoodooSelect bugPriorityDropdownList = new VoodooSelect("ul", "css", "#select2-drop ul");
		VoodooControl bugPrioritySelectCtrl = new VoodooControl("li", "css", bugPriorityDropdownList.getHookString() + " li:nth-child(2)");

		// Verify that there are 3 values are available - High, Medium, Low
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("high"), true);
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("medium"), true);
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("low"), true);
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("firstValue"), false);
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("secondValue"), false);
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("urgent"), false);

		// To remove hover, need to select any Bug Priority
		bugPrioritySelectCtrl.click();

		// Cancel the Bugs create drawer
		sugar().bugs.createDrawer.cancel();

		// Logout from QAUser and login as Admin
		sugar().logout();
		sugar().login();

		// Admin -> bug_priority_dom -> myRole,  select "secondBug" again. Save it
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		dropdownEditorCtrl.click();
		VoodooUtils.waitForReady();
		bugPriorityDomCtrl.click();
		VoodooUtils.waitForReady();
		roleSelectCtrl.set(roleAndDropDownData.get("updatedRoleName"));
		VoodooUtils.waitForReady();
		secondBugCtrl.set(Boolean.toString(true));
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();

		// Admin -> bug_priority_dom -> myRole
		bugPriorityDomCtrl.click();
		VoodooUtils.waitForReady();
		roleSelectCtrl.set(roleAndDropDownData.get("updatedRoleName"));
		VoodooUtils.waitForReady();

		// Verify that there 4 values are selected in myRole Role - High, Medium, Low and secondBug.  2 values are DE-selected - firstBug, Urgent
		Assert.assertFalse(urgentCtrl.isChecked());
		Assert.assertFalse(firstBugCtrl.isChecked());
		Assert.assertTrue(secondBugCtrl.isChecked());
		Assert.assertTrue(mediumCtrl.isChecked());
		Assert.assertTrue(lowCtrl.isChecked());
		Assert.assertTrue(highCtrl.isChecked());
		VoodooUtils.focusDefault();

		// Logout from Admin and login as QAUser
		sugar().logout();
		sugar().login(sugar().users.getQAUser());

		// Go to create a new bug.  Click on the Bug Priority field
		sugar().bugs.navToListView();
		sugar().bugs.listView.create();
		sugar().bugs.createDrawer.showMore();
		bugPriorityFieldCtrl.click();

		// Verify that 4 values are listed in "Priority" drop down - High, Medium, Low and secondBug
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("high"), true);
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("medium"), true);
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("low"), true);
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("secondValue"), true);
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("firstValue"), false);
		bugPriorityDropdownList.assertContains(roleAndDropDownData.get("urgent"), false);

		// To remove hover, need to select any Bug Priority
		bugPrioritySelectCtrl.click();

		// Cancel the Bugs create drawer
		sugar().bugs.createDrawer.cancel();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}