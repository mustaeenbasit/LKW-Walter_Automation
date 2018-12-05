package com.sugarcrm.test.passwordmanagement;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.SugarTest;

public class PasswordManagement_20001 extends SugarTest {
	DataSource ds, ds1;
	UserRecord qaUser;
	
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	 *  Set"Regex Requirement"and" Regex Description",Verify user according with the rules set password and login successful
	 * 
	 * @throws Exception
	 */
	@Ignore("SI-66004")
	@Test
	public void PasswordManagement_20001_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		ds = testData.get(testName);
		ds1 = testData.get(testName + "_1");
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame"); 
		sugar().admin.adminTools.getControl("passwordManagement").click();
		
		// TODO VOOD-989
		new VoodooControl("span", "css", "#regex_config_display_lbl").click();
		new VoodooControl("input", "css", "#customregex").set(ds.get(0).get("customregex"));
		new VoodooControl("input", "css", "input[name='passwordsetting_regexcomment']").set(ds.get(0).get("regexcomment"));
		
		sugar().admin.passwordManagement.getControl("save").click();
		VoodooUtils.focusDefault(); 
		VoodooUtils.waitForAlertExpiration(); 	
		
		qaUser = new UserRecord(sugar().users.getQAUser());
		sugar().logout();
		
		// login as qaUser and change password
		sugar().login(qaUser);
		sugar().navbar.navToProfile();
		sugar().users.detailView.edit();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("passwordTab").click();
	
		// TODO VOOD-987
		new VoodooControl("input", "css", "#old_password").set(qaUser.get("password"));
		
		// only the password in last row match the requirement
		for(int i=0;i<ds1.size()-1;i++) {				
			sugar().users.editView.getEditField("newPassword").set(ds1.get(i).get("password"));
			// TODO VOOD-947
			new VoodooControl("td", "css", "div#generate_password table.x-sqs-list").assertElementContains(ds1.get(0).get("assert1"), true);			
			new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.bad[id='regex']").assertVisible(true);	
		}
				
		sugar().users.editView.getEditField("newPassword").set(ds1.get(ds1.size()-1).get("password"));
		new VoodooControl("div", "css",	"div#generate_password table.x-sqs-list div.good[id='regex']").assertVisible(true);
		sugar().users.editView.getEditField("confirmPassword").set(ds1.get(ds1.size()-1).get("password"));
		VoodooUtils.focusDefault();
		sugar().users.editView.save();

		VoodooUtils.focusFrame("bwc-frame");
		sugar().users.editView.getControl("confirmCreate").click(); // Password changed confirmation
		VoodooUtils.focusDefault();
		// login use new password
		qaUser.put("password", ds1.get(ds1.size()-1).get("password"));
		sugar().logout();
		sugar().login(qaUser);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}