package com.sugarcrm.test.accounts;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Accounts_18723 extends SugarTest {
	VoodooControl accountsStudioCtrl, layoutCtrl, listViewCtrl, saveAndDeploy;
	
	public void setup() throws Exception {
		sugar().accounts.api.create();
		sugar().login();
	}
	/**
	 * Verify cannot edit the date created, date modified, modified by user and created 
	 * by user fields on record views
	 * @throws Exception
	 */
	@Test
	public void Accounts_18723_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO:VOOD-1507 [Support Studio Module ListView Layouts View]
		accountsStudioCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		layoutCtrl=new VoodooControl("td", "id", "layoutsBtn");
		listViewCtrl=new VoodooControl("td", "id", "viewBtnlistview");
		saveAndDeploy = new VoodooControl("input", "css", ".list-editor #savebtn");
		
		// Studio >> Accounts >> Layouts >> List View   
		sugar().admin.navToAdminPanelLink("studio");
		VoodooUtils.focusFrame("bwc-frame");
		accountsStudioCtrl.click();
		VoodooUtils.waitForReady();
		layoutCtrl.click();
		VoodooUtils.waitForReady();
		listViewCtrl.click();
		VoodooUtils.waitForReady();

		// Adding "Created By" field from Hidden to Default List
		VoodooControl nameCtrl = new VoodooControl("li", "css", "[data-name='name']");
		VoodooControl createdByCtrl = new VoodooControl("li", "css", "[data-name='created_by_name']");
		createdByCtrl.dragNDrop(nameCtrl);

		// Adding "Modified By" field from Hidden to Default List
		VoodooControl modifiedByCtrl = new VoodooControl("li", "css", "[data-name='modified_by_name']");
		modifiedByCtrl.dragNDrop(nameCtrl);
		saveAndDeploy.click();
		VoodooUtils.waitForReady(30000);
		VoodooUtils.focusDefault();
		
		// Navigate to Account record
		sugar().accounts.navToListView();
		sugar().accounts.listView.openRowActionDropdown(1);
		sugar().accounts.listView.getControl("edit01").click();
		
		// TODO:VOOD-597 
		new VoodooControl("span", "css", "span[data-voodoo-name='date_entered']")
			.assertAttribute("class", "edit", false);
		new VoodooControl("span", "css", "span[data-voodoo-name='created_by_name']")
			.assertAttribute("class", "edit", false);
		new VoodooControl("span", "css", "span[data-voodoo-name='date_modified']")
			.assertAttribute("class", "edit", false);
		new VoodooControl("span", "css", "span[data-voodoo-name='modified_by_name']")
			.assertAttribute("class", "edit", false);

		// Cancel Edit view page
		sugar().accounts.listView.cancelRecord(1);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}