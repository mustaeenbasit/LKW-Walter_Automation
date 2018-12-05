package com.sugarcrm.test.forecasts;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.SugarUrl;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.records.UserRecord;
import com.sugarcrm.test.Features;
import com.sugarcrm.test.SugarTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

@Features(revenueLineItem = false)
public class Forecasts_27817 extends SugarTest {
	Configuration config;
	String baseUrl;
	DataSource users;
	FieldSet firstUser, secondUser, thirdUser;
	UserRecord myFirstUser, mySecondUser, myThirdUser;
	VoodooControl commitButton;

	public void setup() throws Exception {
		// TODO The VoodooControl references in Forecasts will be replaced by
		// VOOD-725 Forecasts Lib
		config = VoodooUtils.getGrimoireConfig();
		baseUrl = new SugarUrl().getBaseUrl();
		users = testData.get("Forecasts_27817");
		firstUser = users.get(0);
		secondUser = users.get(1);
		thirdUser = users.get(2);

		commitButton = new VoodooControl("a", "css", "a[name='commit_button'].btn.btn-primary");

		sugar.login();
		
		// Create 3 users and set up 1 as Manager
		// TODO VOOD-444 - API Creating relationships
		myFirstUser = (UserRecord) sugar.users.create(firstUser);
		
		secondUser.put("reportsTo", firstUser.get("firstName"));
		thirdUser.put("reportsTo", firstUser.get("firstName"));
		
		mySecondUser = (UserRecord) sugar.users.create(secondUser);
		myThirdUser = (UserRecord) sugar.users.create(thirdUser);

		// Assign Sales Administrator role to Sally
		sugar.navbar.navToAdminTools();
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		sugar.admin.adminTools.getControl("rolesManagement").click();
		
		// Click on 'Sales Administrator'
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "xpath", "//*[@id='MassUpdate']/table/tbody/tr[contains(.,'Sales Administrator')]/td[3]/b/a").click();

		// Click Select User button
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "a#acl_roles_users_select_button").click();
		// Select Sally
		VoodooUtils.focusWindow(1);
 		new VoodooControl("a", "xpath", "//td/a[contains(text(),'"+firstUser.get("fullName")+"')]").click();
 		VoodooUtils.focusWindow(0);

		VoodooUtils.focusDefault();

		// Enable default Forecast settings
		sugar.navbar.navToModule("Forecasts"); 
		new VoodooControl("a", "css", ".rowaction.btn.btn-primary").waitForVisible(30000);
		new VoodooControl("a", "css", ".rowaction.btn.btn-primary").click();
		VoodooUtils.waitForAlertExpiration(); // Required
		sugar.alerts.waitForLoadingExpiration(30000);

		VoodooUtils.focusDefault();

		sugar.logout();
	}

	/**
	 * Verify that quota for both sales rep and manager is repopulating after conversion from 
	 * Opps to Opps + RLIs and back
	 * getAlert()
	 * @throws Exception
	 */
	@Test
	public void Forecasts_27817_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// After user creation, now add quota figures to users
		// Cannot add this to csv file as this will affect user creation
		firstUser.put("quota", "102.00");
		secondUser.put("quota", "202.00");
		thirdUser.put("quota", "302.00");
		String total_quota = "606.00";

		// Step 1 - The first user is a Manager so assign Quota options should be visible
		sugar.login(firstUser);
		
		// TODO: VOOD-929 applicable for all below controls
		sugar.navbar.navToModule("Forecasts");
		new VoodooControl("a", "css", ".btn.dropdown-toggle.btn-primary")
				.click();
		new VoodooControl("a", "css", ".fld_assign_quota").assertVisible(true);

		// Set quota for firstUser
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+firstUser.get("fullName")+"')]/td[2]/span/div/div").click();
		new VoodooControl("input", "xpath", "//table/tbody/tr[contains(.,'"+firstUser.get("fullName")+"')]/td[2]/span/div/input").set(firstUser.get("quota"));
		
		// Set quota for secondUser
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+secondUser.get("fullName")+"')]/td[2]/span/div/div").click();
		new VoodooControl("input", "xpath", "//table/tbody/tr[contains(.,'"+secondUser.get("fullName")+"')]/td[2]/span/div/input").set(secondUser.get("quota"));

		// Set quota for thirdUser
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+thirdUser.get("fullName")+"')]/td[2]/span/div/div").click();
		new VoodooControl("input", "xpath", "//table/tbody/tr[contains(.,'"+thirdUser.get("fullName")+"')]/td[2]/span/div/input").set(thirdUser.get("quota"));

		// As soon as any quota figure is changed, commit button becomes enabled
		new VoodooControl("h2", "id", "quota").click(); // To remove focus from quota 
		
		// Just to confirm VOOD-1379. To check if the class attribute exists for commitButton. 
		assertTrue("Commit Button does not have class attribute", commitButton.hasAttribute("class"));

		commitButton.waitForAttributeNotToContain("class", "disabled");
		commitButton.click();
		commitButton.waitForAttributeToContain("class", "disabled");
		
		sugar.alerts.getSuccess().waitForVisible();
		sugar.alerts.getSuccess().closeAlert();

		sugar.logout(); // firstUser
		
		// Step 3 - Switch to Opp+RLI
		sugar.login();
		sugar.admin.api.switchToRevenueLineItemsView();
		sugar.logout();

		sugar.login(firstUser);

		// Goto Forecasts 
		sugar.navbar.navToModule("Forecasts");

		// TODO: VOOD-929 applicable for all below controls
		// Verify that quota figures remain intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(total_quota, true);
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+firstUser.get("fullName")+"')]/td[2]/span/div/div").assertContains(firstUser.get("quota"), true);
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+secondUser.get("fullName")+"')]/td[2]/span/div/div").assertContains(secondUser.get("quota"), true);
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+thirdUser.get("fullName")+"')]/td[2]/span/div/div").assertContains(thirdUser.get("quota"), true);
		
		// Goto firstUser worksheet
		new VoodooControl("a", "css", "#forecastsTree > div > a").click();
		new VoodooControl("li", "xpath", "//div[@id='people']/ul/li/ul/li[contains(.,'"+firstUser.get("fullName")+"')]").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Verify that quota for firstUser is intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(firstUser.get("quota"), true);
		
		// Goto secondUser worksheet
		new VoodooControl("a", "css", "#forecastsTree > div > a").click();
		new VoodooControl("li", "xpath", "//div[@id='people']/ul/li/ul/li[contains(.,'"+secondUser.get("fullName")+"')]").click();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that quota for secondUser is intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(secondUser.get("quota"), true);
		
		// Goto thirdUser worksheet
		new VoodooControl("a", "css", "#forecastsTree > div > a").click();
		new VoodooControl("li", "xpath", "//div[@id='people']/ul/li/ul/li[contains(.,'"+thirdUser.get("fullName")+"')]").click();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that quota for thirdUser is intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(thirdUser.get("quota"), true);
		
		sugar.logout(); // firstUser

		// Step 7 - Switch Opp mode
		sugar.login();
		sugar.admin.api.switchToOpportunitiesView();
		sugar.logout();

		sugar.login(firstUser);

		// Goto Forecasts 
		sugar.navbar.navToModule("Forecasts");

		// TODO: VOOD-929 applicable for all below controls
		// Verify that quota figures remain intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(total_quota, true);
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+firstUser.get("fullName")+"')]/td[2]/span/div/div").assertContains(firstUser.get("quota"), true);
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+secondUser.get("fullName")+"')]/td[2]/span/div/div").assertContains(secondUser.get("quota"), true);
		new VoodooControl("div", "xpath", "//table/tbody/tr[contains(.,'"+thirdUser.get("fullName")+"')]/td[2]/span/div/div").assertContains(thirdUser.get("quota"), true);
		
		// Goto firstUser worksheet
		new VoodooControl("a", "css", "#forecastsTree > div > a").click();
		new VoodooControl("li", "xpath", "//div[@id='people']/ul/li/ul/li[contains(.,'"+firstUser.get("fullName")+"')]").click();
		sugar.alerts.waitForLoadingExpiration();
		
		// Verify that quota for firstUser is intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(firstUser.get("quota"), true);
		
		// Goto secondUser worksheet
		new VoodooControl("a", "css", "#forecastsTree > div > a").click();
		new VoodooControl("li", "xpath", "//div[@id='people']/ul/li/ul/li[contains(.,'"+secondUser.get("fullName")+"')]").click();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that quota for secondUser is intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(secondUser.get("quota"), true);
		
		// Goto thirdUser worksheet
		new VoodooControl("a", "css", "#forecastsTree > div > a").click();
		new VoodooControl("li", "xpath", "//div[@id='people']/ul/li/ul/li[contains(.,'"+thirdUser.get("fullName")+"')]").click();
		sugar.alerts.waitForLoadingExpiration();

		// Verify that quota for thirdUser is intact
		new VoodooControl("h1", "css", "span.fld_quota.info div h2").assertContains(thirdUser.get("quota"), true);
		
		sugar.logout(); // firstUser		
		sugar.login();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}
