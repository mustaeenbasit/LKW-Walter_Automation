package com.sugarcrm.test.tags;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

public class Tags_28531 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 * Verify Tags field is available on mobile layouts in Studio
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28531_execute() throws Exception {		
		VoodooUtils.voodoo.log.info("Running " + testName + "...");		

		// Add Modules Accounts, Calls, Cases, Contacts, Leads, Meetings, Notes, Opportunities, Quoted Line Items, Tasks
		ArrayList<Module>modules = new ArrayList<>();
		modules.add(sugar.accounts);
		modules.add(sugar.calls);
		modules.add(sugar.cases);
		modules.add(sugar.contacts);
		modules.add(sugar.leads);
		modules.add(sugar.meetings);
		modules.add(sugar.notes);
		modules.add(sugar.opportunities);
		modules.add(sugar.quotedLineItems);
		modules.add(sugar.tasks);

		// Goto Studio
		sugar.admin.navToAdminPanelLink("studio");
		
		for (Module module : modules) {
			VoodooUtils.focusFrame("bwc-frame");
			// Click Module
			new VoodooControl("a", "id", "studiolink_" + module.moduleNamePlural).click();
			VoodooUtils.waitForReady();
			
			// Click Mobile Layout
			new VoodooControl("td", "id", "wirelesslayoutsBtn").click();
			VoodooUtils.waitForReady();
			
			// Click Edit View
			new VoodooControl("td", "id", "viewBtnwirelesseditview").click();
			VoodooUtils.waitForReady();
			
			// Verify Tag field in available fields list
			new VoodooControl("div", "css", "div#availablefields div[data-name='tag']").assertExists(true);
			
			// Goto Mobile Layout via breadCrumb
			new VoodooControl("a", "xpath", "//a[@class='crumbLink'][contains(.,'Mobile Layouts')]").click();
			VoodooUtils.waitForReady();

			// Click Detail View
			new VoodooControl("td", "id", "viewBtnwirelessdetailview").click();
			VoodooUtils.waitForReady();

			// Verify Tag field in available fields list
			new VoodooControl("div", "css", "div#availablefields div[data-name='tag']").assertExists(true);

			// Goto Mobile Layout via breadCrumb
			new VoodooControl("a", "xpath", "//a[@class='crumbLink'][contains(.,'Mobile Layouts')]").click();
			VoodooUtils.waitForReady();
			
			// Click List view
			new VoodooControl("td", "id", "viewBtnwirelesslistview").click();
			VoodooUtils.waitForReady();

			// Verify Tag field in hidden fields list
			new VoodooControl("li", "css", ".draggable[data-name='tag']").assertExists(true);
			
			// Click Studio Link in Footer to go back to main Studio page
			new VoodooControl("input", "css", "div#footerHTML input[value='Studio']").click();
			VoodooUtils.waitForReady();
			
			VoodooUtils.focusDefault();
		}
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																						
}
