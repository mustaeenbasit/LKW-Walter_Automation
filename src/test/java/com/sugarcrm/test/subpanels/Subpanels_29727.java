package com.sugarcrm.test.subpanels;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class Subpanels_29727 extends SugarTest {
	FieldSet emailSetup = new FieldSet();

	public void setup() throws Exception {
		sugar().leads.api.create();
		emailSetup = testData.get("env_email_setup").get(0);
		sugar().login();
		sugar().admin.setEmailServer(emailSetup);
	}

	/**
	 * Verify that Email should not be linked with email sub-panel on clicking cancel button.
	 * @throws Exception
	 */
	@Test
	public void Subpanels_29727_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Leads listview with emails subpanel
		sugar().leads.navToListView();
		sugar().leads.listView.clickRecord(1);
		StandardSubpanel emailSubpanel = sugar().leads.recordView.subpanels.get(sugar().emails.moduleNamePlural);

		// Verify email subpanel is collapsed
		// TODO: VOOD-1843 Once resolved below code should replaced by getChildElement
		new VoodooControl("li", "css", emailSubpanel.getHookString()+" ul.nav.results li").assertAttribute("class", "closed", true);
		// TODO: VOOD-1411 - composeEmail method not working (Page was not ready 120000)
		//emailSubpanel.composeEmail();
		VoodooControl composeMail = emailSubpanel.getControl("composeEmail");
		composeMail.click();
		VoodooUtils.waitForReady();
		sugar().leads.recordView.composeEmail.cancel();

		// Verify Email Record should not be linked with Email sub panel on clicking "Cancel" button.
		emailSubpanel.expandSubpanel();
		Assert.assertTrue("Emails are linked in email subpanel", emailSubpanel.isEmpty());

		// Send mail with required fields
		composeMail.click();
		VoodooUtils.waitForReady();
		VoodooControl emailToSearchResultCtrl = new VoodooControl("div", "css", ".select2-result-label");
		VoodooControl toAddress = sugar().leads.recordView.composeEmail.getControl("toAddress");
		toAddress.set(emailSetup.get("userName"));
		emailToSearchResultCtrl.click();

		DataSource mailInfoDS = testData.get(testName);
		VoodooControl subject = sugar().leads.recordView.composeEmail.getControl("subject");
		subject.set(mailInfoDS.get(0).get("subject"));
		sugar().leads.recordView.composeEmail.getControl("sendButton").click();

		// TODO: VOOD-843 comments "Body message is not working on multiple navigation compose mail", Once resolved set body message and remove confirmAllWarnings
		sugar().alerts.confirmAllWarning();

		// Verify Email Record should be linked with Email sub panel on clicking "Send" button.
		// TODO: VOOD-1077, VOOD-1380
		VoodooControl name = new VoodooControl("a", "css", ".layout_Emails tr.single .fld_name div a");
		VoodooControl status = new VoodooControl("div", "css", ".layout_Emails tr.single .fld_status div");
		name.assertEquals(mailInfoDS.get(0).get("subject"), true);
		status.assertEquals(mailInfoDS.get(0).get("status"), true);

		// Draft an email with required fields
		composeMail.click();
		VoodooUtils.waitForReady();
		toAddress.set(emailSetup.get("userName"));
		emailToSearchResultCtrl.click();
		subject.set(mailInfoDS.get(1).get("subject"));

		sugar().leads.recordView.composeEmail.getControl("actionDropdown").click();
		sugar().leads.recordView.composeEmail.getControl("saveDraft").click();
		VoodooUtils.waitForReady();

		// Verify Email should be linked with Email sub-panel. 
		name.assertEquals(mailInfoDS.get(1).get("subject"), true);
		status.assertEquals(mailInfoDS.get(1).get("status"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}