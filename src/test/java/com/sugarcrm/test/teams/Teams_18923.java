package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_18923 extends SugarTest {
	DataSource ds;
	
	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 * New action dropdown list in team detail view page
	 * @throws Exception
	 */
	@Test
	public void Teams_18923_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
				
		DataSource ds = testData.get(testName);
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("teamsManagement").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "css", ".list.view tbody .oddListRowS1 input").waitForVisible();
		// TODO VOOD-1011
		new VoodooControl("input", "css", "#name_basic").set(sugar.users.getQAUser().get("lastName"));
		new VoodooControl("input", "css", "#search_form_submit").click();	
		new VoodooControl("a", "css", "form#MassUpdate tbody tr:nth-of-type(3) td:nth-of-type(4) a").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", ".list.view .oddListRowS1 a").waitForVisible();
		new VoodooControl("span", "css", "ul#team_action_menu span.ab").click();
		new VoodooControl("span", "css", ".subnav.ddopen").assertElementContains(ds.get(0).get("delete"), true);
		new VoodooControl("span", "css", ".subnav.ddopen").assertElementContains(ds.get(0).get("copy"), true);
		new VoodooControl("li", "css", "li.sugar_action_button").assertElementContains(ds.get(0).get("edit"), true);
		new VoodooControl("li", "css", "li.sugar_action_button").click();
		new VoodooControl("input", "css", "input[name='name']").assertEquals(sugar.users.getQAUser().get("lastName"), true);
		new VoodooControl("textarea", "css", "textarea[name='description']").assertVisible(true);		
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}