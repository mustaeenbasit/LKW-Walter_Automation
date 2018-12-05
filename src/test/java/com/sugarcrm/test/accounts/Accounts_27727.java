package com.sugarcrm.test.accounts;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

public class Accounts_27727 extends SugarTest {
	AccountRecord myAccount;
	String newDate;
	
	public void setup() throws Exception {
		// Store current date
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		newDate = sdf.format(dt);
		
		//Create account
		myAccount = (AccountRecord) sugar().accounts.api.create();
		sugar().login();
	}

	/**
	 *  Verify value of Date Created and Assigned User are displayed in Related cloumns in Notes subpanel
	 * 
	 * @throws Exception
	 */
	@Test
	public void Accounts_27727_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		myAccount.navToRecord();
		sugar().alerts.waitForLoadingExpiration();
		StandardSubpanel noteSub = sugar().accounts.recordView.subpanels.get(sugar().notes.moduleNamePlural);
		FieldSet fs = new FieldSet();
		fs.put("subject", "testNew");
		noteSub.create(fs);
		
		// Verify that the value of Date Created and Assigned User are displayed in Related cloumns in Notes subpanel
		sugar().accounts.recordView.getDetailField("relAssignedTo").assertContains("Administrator", true);
		noteSub.assertContains("Administrator", true);		
		sugar().accounts.recordView.showMore();
		
		// TODO: VOOD-597 -need lib support for date created and date updated fields
		VoodooUtils.voodoo.log.info(new VoodooControl("div", "css", "#content span.fld_date_entered.detail > div").getText() + " current date.");
		new VoodooControl("div", "css", "#content span.fld_date_entered.detail > div").assertContains(newDate, true);
		sugar().accounts.recordView.showLess();
		noteSub.assertContains(newDate, true);		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
