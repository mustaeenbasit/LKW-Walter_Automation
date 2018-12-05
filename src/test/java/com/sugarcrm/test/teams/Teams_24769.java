package com.sugarcrm.test.teams;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_24769 extends SugarTest {
	DataSource ds;

	public void setup() throws Exception {
		sugar.login();
	}

	/**
	 *  Admin users can change default team
	 *  
	 * @throws Exception
	 */
	@Test
	public void Teams_24769_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		ds = testData.get(testName);
		
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("teamsManagement").click();
		VoodooUtils.focusFrame("bwc-frame");
		// TODO VOOD-1011
		new VoodooControl("input", "css", "#name_basic").set(sugar.users.getQAUser().get("lastName"));
		new VoodooControl("input", "css", "#search_form_submit").click();		
		new VoodooControl("a", "css", "form#MassUpdate tbody tr:nth-of-type(3) td:nth-of-type(4) a").click();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "id", "team_memberships_select_button").click();
		
		VoodooUtils.focusWindow(1);
		new VoodooControl("a", "xpath", "//table[@class='list view']//tr//td//a[contains(text(),'"+ds.get(0).get("name")+"')]").click();
		VoodooUtils.focusWindow(0);
		
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("tbody", "css", "table.list.view tbody").assertElementContains(ds.get(0).get("name"), true);
		
		VoodooUtils.focusDefault();

		// change default team
		sugar.navbar.navToProfile();
		sugar.users.detailView.edit();
		
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("input", "id", "Users0emailAddress0").set(sugar.users.getQAUser().get("emailAddress"));
		sugar.users.userPref.getControl("tab4").click();
		// TODO VOOD-1040
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("button", "css", "#EditView_team_name_table .id-ff-remove").click();
		new VoodooControl("input", "css", "input[title='Team Selected ']").set(ds.get(0).get("team1"));
		VoodooUtils.pause(500); // Pause for value to be fetched from server
		new VoodooControl("li", "css", "#EditView_team_name_table ul li:nth-of-type(1)").click();
		new VoodooControl("input", "css", "input[title='Select to make this team primary']").click();
		VoodooUtils.focusDefault();
		sugar.users.editView.save();
		VoodooUtils.pause(3000); // Pause is required here

		VoodooUtils.focusFrame("bwc-frame");
		String abc= new VoodooControl("a","id","tab2").getText();
		VoodooUtils.voodoo.log.info("abc " + abc);
		new VoodooControl("em", "xpath", "//div[@id='user_detailview_tabs']//ul[@class='yui-nav']//*[contains(text(),'Advanced')]").click();
		new VoodooControl("slot", "xpath", "//table[@class='detail view']//tr//td//slot[text()='Default Teams:']").waitForVisible();

		// assertEquals will fail as there exists a special non-breaking space chara in the HTML which breaks the assertEquals	
		new VoodooControl("slot", "css", "table.detail.view tr:nth-child(3) td:nth-child(2) slot").assertContains(ds.get(0).get("team1"), true); 
		
		VoodooUtils.focusDefault();
		// check default team on record create view
		sugar.accounts.navToListView();
		sugar.accounts.listView.create();
		sugar.accounts.recordView.showMore();
		// assertEquals will fail as there exists a special non-breaking space chara in the HTML which breaks the assertEquals	
		new VoodooControl("span", "css", ".fld_team_name.edit span.select2-chosen").assertContains(ds.get(0).get("team1"), true);	

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}