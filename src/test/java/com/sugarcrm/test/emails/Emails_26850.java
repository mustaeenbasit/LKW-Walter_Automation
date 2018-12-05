package com.sugarcrm.test.emails;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.AccountRecord;
import com.sugarcrm.test.SugarTest; 

public class Emails_26850 extends SugarTest {
	FieldSet customData;
	VoodooControl settingsButton, signatureName, sigTextCode, htmlSource, insertBtnCtrl;
	VoodooControl bodyCtrl, linkTextSet, saveBtnCtrl, deleteBtnCtrl;
	AccountRecord myAccount;

	public void setup() throws Exception {
		customData = testData.get(testName).get(0);
		myAccount = (AccountRecord) sugar.accounts.api.create();
		sugar.login();		
	}

	/**
	 * Verify that Signatures obeys to the User_id
	 * @throws Exception
	 */
	@Test
	public void Emails_26850_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "..."); 

		// TODO: VOOD-672 -Need Lib support for Email settings
		settingsButton = new VoodooControl("button", "id", "settingsButton");
		signatureName = new VoodooControl("input", "css", "input[id='name']");
		sigTextCode = new VoodooControl("a", "css", "#sigText_code");
		htmlSource = new VoodooControl("textarea", "css", "#htmlSource");
		insertBtnCtrl = new VoodooControl("input", "css", ".mceActionPanel #insert");
		bodyCtrl = new VoodooControl("body", "css", "body");
		linkTextSet = new VoodooControl("a", "css", "#href");
		saveBtnCtrl = new VoodooControl("input", "css", "input[title='Save']");
		deleteBtnCtrl = new VoodooControl("input", "css", "#delete_sig input");

		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		settingsButton.click();

		// TODO: VOOD-672 -Need Lib support for Email settings
		new VoodooControl("a", "css", "input[value='Create']").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.focusWindow(1);		
		signatureName.set(customData.get("Dataset_5")); // add signature name
		// add text into HTMLeditor
		sigTextCode.click();
		VoodooUtils.waitForAlertExpiration();
		VoodooUtils.focusWindow(2);
		htmlSource.set(customData.get("Dataset_5"));
		insertBtnCtrl.click();
		VoodooUtils.focusWindow(1);
		bodyCtrl.click();		
		// For save signature
		saveBtnCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");
		// Assert value
		new VoodooControl("select", "name", "signature_id").assertContains(customData.get("Dataset_5"), true);

		// logout from admin and then login with qauser
		VoodooUtils.focusDefault();
		sugar.logout();
		sugar.login(sugar.users.getQAUser());		

		// Create signature after loged-in with qauser
		sugar.navbar.navToModule("Emails");
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");
		settingsButton.click();

		// TODO: VOOD-672 -Need Lib support for Email settings
		new VoodooControl("a", "css", "input[value='Create']").click();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusWindow(1);		
		signatureName.set(customData.get("Dataset_6")); // add signature name
		// add text into HTMLeditor
		sigTextCode.click();
		VoodooUtils.focusWindow(2);
		htmlSource.set(customData.get("Dataset_6"));
		insertBtnCtrl.click();
		VoodooUtils.focusWindow(1);
		bodyCtrl.click();		
		// For save signature
		saveBtnCtrl.click();
		VoodooUtils.focusWindow(0);
		VoodooUtils.focusFrame("bwc-frame");		
		// Assert value
		new VoodooControl("select", "name", "signature_id").assertContains(customData.get("Dataset_6"), true);
		VoodooUtils.focusDefault();

		myAccount.navToRecord();
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("a", "name", "email_compose_button").click();
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("a", "name", "signature_button").click();
		sugar.alerts.waitForLoadingExpiration();
		new VoodooControl("div", "css", "div.layout_UserSignatures table tr td:nth-child(2) > span > div").assertContains(customData.get("Dataset_6"), true);
		new VoodooControl("div", "css", "div.layout_UserSignatures table tr td:nth-child(2) > span > div").assertContains(customData.get("Dataset_5"), false);
		sugar.alerts.confirmAllAlerts();
		sugar.alerts.waitForLoadingExpiration();

		VoodooUtils.voodoo.log.info(testName + "complete."); 
	}

	public void cleanup() throws Exception {}
}