package com.sugarcrm.test.roles;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.AdminModule;
import com.sugarcrm.test.SugarTest;

public class Roles_28294 extends SugarTest {
	FieldSet customData;
	VoodooControl accountsCtrl;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();
	}

	/**
	 * Verify that switching record view setting correctly between core and mobile
	 *  
	 * @throws Exception
	 */
	@Test
	public void Roles_28294_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a new Role.
		AdminModule.createRole(customData);
		
		// Assign QAuser to the Role
		AdminModule.assignUserToRole(customData);
		VoodooUtils.waitForReady();
		
		// TODO: VOOD-938
		// Studio->Accounts->Layouts ->Record View 
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();
		accountsCtrl = new VoodooControl("a", "id", "studiolink_Accounts");
		accountsCtrl.click();
		VoodooControl layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		new VoodooControl("td", "id", "viewBtnrecordview").click(); 
		VoodooUtils.waitForReady();
		
		// Select qauser Role
		VoodooControl roleList = new VoodooControl("select", "id", "roleList");
		roleList.set(customData.get("roleName"));
		VoodooUtils.waitForReady();
		VoodooUtils.waitForReady();
		
		// Modify one row in Record View
		VoodooControl moveToLayoutPanelCtrl = new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1)");
		new VoodooControl("div", "css", "#toolbox .le_row.special").dragNDrop(moveToLayoutPanelCtrl);
		new VoodooControl("div", "css", "div[data-name='facebook']").dragNDrop(new VoodooControl("div", "css", "#panels .le_panel:nth-of-type(1) .le_row .le_field.special:nth-of-type(1)"));
		new VoodooControl("input", "id", "publishBtn").click();   
		VoodooUtils.waitForReady();
		
		// Studio-> Accounts -> Mobile Layouts ->Mobile EditView 
		sugar().admin.studio.clickStudio();
		accountsCtrl.click();
		new VoodooControl("td", "id", "wirelesslayoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnwirelesseditview").click();
		VoodooUtils.waitForReady();
		new VoodooControl("option", "xpath", "//*[@id='roleList']/option[contains(.,'Default')]").hasAttribute("selected");
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}