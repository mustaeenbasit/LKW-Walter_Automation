
package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25887 extends SugarTest {
	FieldSet customData = new FieldSet();

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		sugar().login();

		// Advanced Search layout of employees module has been modified once or more times
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-542 and VOOD-1509
		// Go to Studio > Employees
		new VoodooControl("a", "id", "studiolink_Employees").click();
		VoodooUtils.waitForReady();

		// layout subPanel
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();

		// Search view
		new VoodooControl("td", "id", "searchBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "AdvancedSearchBtn").click();
		VoodooUtils.waitForReady();

		// employee status and title fields are interchangeable, history count = 1 as Default Layout Subpanel
		VoodooControl empStatusCtrl = new VoodooControl("li", "css", "#Default ul li[data-name='employee_status']");
		VoodooControl titleCtrl = new VoodooControl("li", "css", "#Default ul li[data-name='title']");
		titleCtrl.dragNDrop(empStatusCtrl);
		VoodooUtils.waitForReady();

		// Verify the title position
		VoodooControl dragTitle = new VoodooControl("td", "css", "#Default ul li:nth-child(5) tr td");
		dragTitle.waitForVisible();
		dragTitle.assertEquals(customData.get("title"), true);
		VoodooControl saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		// TODO: Hard coded wait needed, as it takes time to generate history(Under investigation)  
		VoodooUtils.pause(7000);

		// department and phone fields are interchangeable, history count = 2 including Default Layout Subpanel
		VoodooControl departmentCtrl = new VoodooControl("li", "css", "#Default ul li[data-name='department']");
		VoodooControl phoneCtrl = new VoodooControl("li", "css", "#Default ul li[data-name='phone']");
		departmentCtrl.dragNDrop(phoneCtrl);
		VoodooUtils.waitForReady();
		saveBtnCtrl.click();
		// TODO: Hard coded wait needed, as it takes time to generate history(Under investigation) 
		VoodooUtils.pause(7000);

		// Verify the department position
		new VoodooControl("td", "css", "#Default ul li:nth-child(9) tr td").assertEquals(customData.get("department"), true);		
	}

	/**
	 * Restore a history advanced search layout of Employees module
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_25887_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// TODO: VOOD-542
		// Expected result: history layouts shown on history window
		new VoodooControl("td", "id", "historyBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#histWindow .tabform tr:nth-child(3)").assertContains(customData.get("default_layout"), true);

		// Restore with default layout
		new VoodooControl("a", "css", "#histWindow .tabform tr:nth-child(3) [value='Restore']").click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", "#histWindow a.container-close").click();
		VoodooUtils.waitForReady();

		// XPATH used here due to fields ordering issue
		// Expected result: default layout i.e employee status, title, phone,department fields are in original position
		new VoodooControl("li", "xpath", "//*[@id='Default']/ul/li[contains(.,'"+customData.get("emp_status")+"')]").assertExists(true);
		new VoodooControl("li", "xpath", "//*[@id='Default']/ul/li[contains(.,'"+customData.get("title")+"')]").assertExists(true);
		new VoodooControl("li", "xpath", "//*[@id='Default']/ul/li[contains(.,'"+customData.get("phone")+"')]").assertExists(true);
		new VoodooControl("li", "xpath", "//*[@id='Default']/ul/li[contains(.,'"+customData.get("department")+"')]").assertExists(true);
		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}