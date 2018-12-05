package com.sugarcrm.test.teams;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

public class Teams_26083 extends SugarTest {
	ArrayList<Module> modules = new ArrayList<Module>();

	public void setup() throws Exception {
		// Login as an Admin user
		sugar().login();

		// Add all disabled module into the array list 
		modules.add(sugar().quotedLineItems);
		modules.add(sugar().projects);
		modules.add(sugar().contracts);
		modules.add(sugar().bugs);
		modules.add(sugar().knowledgeBase);

		// Enable all disabled modules
		sugar().admin.enableModuleDisplayViaJs(modules);
	}

	/**
	 * Verify that for all modules - "Add Team" button,"Select team" drop down," Set as Primary Team" button and "Remove team" button are displayed in edit view page
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26083_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		DataSource ds = testData.get(testName);

		// TODO: VOOD-822
		VoodooControl rowColumsReportCtrl = new VoodooControl("img", "css", "img[name='rowsColsImg']");
		VoodooControl nextBtnCtrl = new VoodooControl("input", "id", "nextBtn");

		for (FieldSet fs : ds) {
			VoodooUtils.voodoo.log.info(fs.get("module") + " >>>>>>>");
			sugar().navbar.navToModule(fs.get("module"));

			// Click Module
			new VoodooControl("button", "css", "li[data-module='" + fs.get("module") + "'] button[data-toggle='dropdown']").click();
			VoodooUtils.pause(1000);

			// Click Create item
			new VoodooControl("a", "css", fs.get("createMenuItem")).click();
			VoodooUtils.waitForReady();

			if (fs.get("ifBWC").contentEquals("Y")) {
				VoodooUtils.focusFrame("bwc-frame");

				if (fs.get("module").contentEquals("Reports")) {
					rowColumsReportCtrl.waitForVisible();
					rowColumsReportCtrl.click();
					new VoodooControl("table", "id", "Accounts").click();		
					nextBtnCtrl.click();
					new VoodooControl("input", "id", "Accounts_name").click();
					nextBtnCtrl.click();
				}

				// Assert Add Team button
				new VoodooControl("button", "id", "teamAdd").assertExists(true);

				// Assert Select Team button
				new VoodooControl("button", "id", "teamSelect").assertExists(true);

				// Assert first Primary button
				new VoodooControl("input", "id", "primary_team_name_collection_0").assertExists(true);

				// Assert Remove Team button in first team
				new VoodooControl("button", "id", "remove_team_name_collection_0").assertExists(true);

				// Click Add button
				new VoodooControl("button", "id", "teamSelect").click();

				VoodooUtils.pause(1000); // One of the rarest places where nothing except hardcoded wait works

				VoodooUtils.focusWindow(1);
				new VoodooControl("input", "xpath", "//tr[contains(.,'qauser')]/td[1]/input").set("true");
				new VoodooControl("input", "id", "search_form_select").click();
				VoodooUtils.focusWindow(0);

				VoodooUtils.focusDefault();
				VoodooUtils.focusFrame("bwc-frame");

				// Assert Primary button in new team
				new VoodooControl("input", "id", "primary_team_name_collection_1").assertExists(true);

				// Assert Remove Team button in new team
				new VoodooControl("button", "id", "remove_team_name_collection_1").assertExists(true);

				// Cancel
				if (fs.get("module").contentEquals("Reports"))
					new VoodooControl("input", "css", "#report_details_div > table:nth-child(1) > tbody > tr > td > input:nth-child(5)").click();
				else if (fs.get("module").contentEquals("KBDocuments"))
					new VoodooControl("input", "id", "btn_cancel").click();
				else
					new VoodooControl("input", "id", "CANCEL_HEADER").click();

				VoodooUtils.focusDefault();
			}
			else {
				// First, make sure that show more is enabled
				VoodooControl showMoreCtrl = new VoodooControl("button", "css", "div.record-cell button[data-moreless='more']");
				VoodooControl addTeamBtnCtrl = new VoodooControl("button", "css", "span.fld_team_name.edit div.control-group:nth-of-type(1) button[name='add']");
				if (showMoreCtrl.queryVisible())
					showMoreCtrl.click();
				sugar().accounts.createDrawer.showMore();
				VoodooUtils.waitForReady();

				// Assert first Add Team button
				addTeamBtnCtrl.assertExists(true);

				// Assert first Primary button
				new VoodooControl("button", "css", "span.fld_team_name.edit div.control-group:nth-of-type(1) button[name='primary']").assertExists(true);

				// Click first Add button
				addTeamBtnCtrl.click();
				VoodooUtils.waitForReady();

				// Assert Dropdown
				new VoodooControl("a", "css", "span.fld_team_name.edit div.control-group:nth-of-type(2) a").assertContains("Select Team...", true);

				// Assert Primary button
				new VoodooControl("button", "css", "span.fld_team_name.edit div.control-group:nth-of-type(2) button[name='primary']").assertExists(true);

				// Assert Add Team button
				new VoodooControl("button", "css", "span.fld_team_name.edit div.control-group:nth-of-type(2) button[name='add']").assertExists(true);

				// Assert Remove Team button
				new VoodooControl("button", "css", "span.fld_team_name.edit div.control-group:nth-of-type(2) button[name='remove']").assertExists(true);

				// Click Cancel
				new VoodooControl("a", "css", "#drawers .fld_cancel_button a").click();
				VoodooUtils.waitForReady();
			}
		}

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}