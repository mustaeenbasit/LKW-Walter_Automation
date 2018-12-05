package com.sugarcrm.test.grimoire;

import com.sugarcrm.test.PortalTest;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.LeadRecord;

public class PortalSignUpTest extends PortalTest {
	LeadRecord portalLead;

	public void setup() throws Exception {
		// Setup Portal Access
		sugar().login();
		sugar().admin.portalSetup.enablePortal();
		sugar().logout();
	}

	@Test
	public void execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Navigate to Portal
		portal.loginScreen.navigateToPortal();
		portal.loginScreen.startSignup();

		FieldSet leadData = new FieldSet();
		leadData.put("firstName", "Sugar");
		leadData.put("lastName", "Lead");
		leadData.put("email", "qa.sugar.qa.79@gmail.com");
		leadData.put("phoneWork", "408-123-4567");
		leadData.put("country", "USA");
		leadData.put("state", "California");
		leadData.put("company", "SugarCRM Inc.");
		leadData.put("title", "QA Engineer");
		portalLead = portal.signupScreen.signUp(leadData);

		// Navigate to Sugar to confirm Lead Record was created
		sugar().loginScreen.navigateToSugar();
		sugar().login();
		portalLead.navToRecord();
		sugar().logout();

		VoodooUtils.voodoo.log.info(testName + " completed...");
	}

	public void cleanup() throws Exception {}
}