package com.sugarcrm.test.users;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.TargetListRecord;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Users_30095 extends SugarTest{
	TargetListRecord myTargetList;
	UserRecord chrisUser;
	StandardSubpanel usersSubpanel;

	public void setup() throws Exception {
		sugar().login();
		
		myTargetList = (TargetListRecord) sugar().targetlists.create();
		chrisUser = (UserRecord) sugar().users.create();
	}

	/**
	 * Verify that Users are no longer able to save duplicate user names through Target List as there is no edit option available now
	 * 
	 * @throws Exception
	 */
	@Test
	public void Users_30095_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Goto TargetList
		myTargetList.navToRecord();

		// Add Chris to TargetList
		usersSubpanel = (StandardSubpanel) sugar().targetlists.recordView.subpanels.get(sugar().users.moduleNamePlural);
		usersSubpanel.linkExistingRecord(chrisUser);		
		usersSubpanel.expandSubpanel();

		// Verify that no edit option is available for Users Subpanel in Target List
		new VoodooControl("", "css", ".layout_Users .fieldset.actions.list.btn-group").assertExists(false);
				
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}