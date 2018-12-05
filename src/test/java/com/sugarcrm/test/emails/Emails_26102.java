package com.sugarcrm.test.emails;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Emails_26102 extends SugarTest {
	FieldSet emailSetupData;
	String team1, team2, team3, team4, team5, team6;
	
	public void setup() throws Exception {
		// SMTP settings
		emailSetupData = testData.get(testName+"_smtp_settings").get(0);
		sugar().login();
		sugar().admin.setEmailServer(emailSetupData);
	}

	/**
	 * Verify that group email address is available in the From address
	 * @throws Exception
	 */
	@Test
	public void Emails_26102_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		// Emails module
		sugar().navbar.navToModule("Emails");
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		// Click on Compose button
		new VoodooControl("button", "id", "composeButton").click();
		sugar().alerts.waitForLoadingExpiration();
		VoodooUtils.pause(6000); // Required for completely load compose email form
		
		// TODO: VOOD-843 -Lib support to handle new email composer UI
		// click on Option button
		new VoodooControl("button", "css", "#composeHeaderTable1 > tbody > tr:nth-child(1) > th > table > tbody > tr > td:nth-child(1) > button:nth-child(4)").click();
		
		// click on Choose Team button
		new VoodooControl("button", "css", "#composeOptionsForm1_team_name_table > tbody > tr:nth-child(1) > td:nth-child(1) > span > button.button.firstChild").click();
		VoodooUtils.focusWindow(1); // focus on Team List window
		new VoodooControl("input", "id", "massall").click();
		
		// UnSelect Global Team(already exist)
		new VoodooControl("input", "css", "#MassUpdate > table.list.view > tbody > tr:nth-child(3) > td:nth-child(1) > input").click();
		new VoodooControl("input", "id", "search_form_select").click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		
		team1 = new VoodooControl("input", "css", "#lineFields_composeOptionsForm1_team_name_0 input").getAttribute("value");
		team2 = new VoodooControl("input", "css", "#lineFields_composeOptionsForm1_team_name_1 input").getAttribute("value");
		team3 = new VoodooControl("input", "css", "#lineFields_composeOptionsForm1_team_name_2 input").getAttribute("value");
		team4 = new VoodooControl("input", "css", "#lineFields_composeOptionsForm1_team_name_3 input").getAttribute("value");
		team5 = new VoodooControl("input", "css", "#lineFields_composeOptionsForm1_team_name_4 input").getAttribute("value");
		team6 = new VoodooControl("input", "css", "#lineFields_composeOptionsForm1_team_name_5 input").getAttribute("value");
		
		// After selection of multiple Teams, verify them (Global in 1st position and Administrator & qauser in any order)
		assertTrue("Error in Team 1 - not equal to Global", team1.contains("Global"));
		
		assertTrue("Error in Team 2/3/4/5/6 - neither contains qauser", team2.contains("qauser") || 
				team3.contains("qauser") ||
				team4.contains("qauser") ||
				team5.contains("qauser") ||
				team6.contains("qauser") );
		
		assertTrue("Error in Team 2/3/4/5/6 - neither contains Administrator", team2.contains("Administrator") || 
				team3.contains("Administrator") ||
				team4.contains("Administrator") ||
				team5.contains("Administrator") ||
				team6.contains("Administrator") );

		VoodooUtils.focusDefault();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}
		
	public void cleanup() throws Exception {}
}