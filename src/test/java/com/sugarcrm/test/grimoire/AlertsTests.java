package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests different Alerts class methods in SugarCRM.
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class AlertsTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void getAlert() throws Exception {
		VoodooUtils.voodoo.log.info("Running getAlert()...");

		// Verify alert and can be of any type
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.save();
		sugar().alerts.getAlert().assertVisible(true);
		sugar().alerts.getAlert().assertContains("Error Please resolve any errors before proceeding.", true);
		sugar().alerts.getAlert().closeAlert();
		sugar().alerts.getAlert().assertVisible(false);

		VoodooUtils.voodoo.log.info("getAlert() complete.");
	}

	@Test
	public void getProcess() throws Exception {
		VoodooUtils.voodoo.log.info("Running getProcess()...");

		// Verify Loading.. message
		sugar().accounts.navToListView();
		VoodooUtils.refresh();
		sugar().alerts.getProcess().assertVisible(true);

		VoodooUtils.voodoo.log.info("getProcess() complete.");
	}

	@Test
	public void getInfo() throws Exception {
		VoodooUtils.voodoo.log.info("Running getInfo()...");

		sugar().opportunities.api.create();

		// RLI record via UI
		sugar().revLineItems.create();

		// OPP record view -> RLI subpanel -> Check record -> Generate Quote -> Edit RLI record -> Save
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel revLineItemsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		revLineItemsSubpanel.toggleSubpanel();
		revLineItemsSubpanel.checkRecord(1);
		revLineItemsSubpanel.openActionDropdown();
		revLineItemsSubpanel.generateQuote();
		sugar().quotes.editView.cancel();
		VoodooUtils.waitForReady();
		revLineItemsSubpanel.scrollIntoView();
		revLineItemsSubpanel.editRecord(1);
		revLineItemsSubpanel.getEditField(1, "name").set(testName);
		revLineItemsSubpanel.getControl("saveActionRow01").click();

		// Verify getInfo (Blue message)		
		sugar().alerts.getInfo().assertVisible(true);
		sugar().alerts.closeAllAlerts();

		VoodooUtils.voodoo.log.info("getInfo() complete.");
	}

	@Test
	public void getErrorAlert() throws Exception {
		VoodooUtils.voodoo.log.info("Running getErrorAlert()...");

		// Verify Error alert
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.save();
		sugar().alerts.getError().assertVisible(true);
		sugar().alerts.getError().assertContains("Error Please resolve any errors before proceeding.", true);
		sugar().alerts.getError().closeAlert();
		sugar().alerts.getError().assertVisible(false);

		VoodooUtils.voodoo.log.info("getErrorAlert() complete.");
	}

	@Test
	public void getSuccessAlert() throws Exception {
		VoodooUtils.voodoo.log.info("Running getSuccessAlert()...");

		// Verify Success alert
		sugar().accounts.navToListView();
		sugar().accounts.listView.create();
		sugar().accounts.createDrawer.getEditField("name").set("Ashish Jabble");
		sugar().accounts.createDrawer.save();
		sugar().alerts.getSuccess().assertVisible(true);
		sugar().alerts.getSuccess().assertContains("Success You successfully created the account Ashish Jabble", true);
		sugar().alerts.getSuccess().closeAlert();
		sugar().alerts.getSuccess().assertVisible(false);

		VoodooUtils.voodoo.log.info("getSuccessAlert() complete.");
	}

	@Test
	public void getWarningAlert() throws Exception {
		VoodooUtils.voodoo.log.info("Running getWarningAlert()...");

		sugar().accounts.api.create();
		// Verify Warning alert
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel emailSubpanel = sugar().accounts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubpanel.scrollIntoViewIfNeeded(false);
		emailSubpanel.composeEmail();
		sugar().alerts.getWarning().assertVisible(true);
		sugar().alerts.getWarning().assertContains("Warning The default system SMTP server is not configured", true);
		sugar().alerts.getWarning().closeAlert();
		sugar().alerts.getWarning().assertVisible(false);
		sugar().accounts.recordView.composeEmail.cancel();

		VoodooUtils.voodoo.log.info("getWarningAlert() complete.");
	}

	@Test
	public void closeAllSuccess() throws Exception {
		VoodooUtils.voodoo.log.info("Running closeAllSuccess()...");

		// Verify and close all Success alerts
		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.recordView.getEditField("website").set("http://jabbleashish.blogspot.in");
		sugar().accounts.recordView.save();
		sugar().alerts.closeAllSuccess();
		sugar().alerts.getSuccess().assertVisible(false);

		VoodooUtils.voodoo.log.info("closeAllSuccess() complete.");
	}

	@Test
	public void closeAllWarning() throws Exception {
		VoodooUtils.voodoo.log.info("Running closeAllWarning()...");

		// Verify and close all Warning alerts
		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		StandardSubpanel emailSubpanel = sugar().accounts.recordView.subpanels.get(sugar().emails.moduleNamePlural);
		emailSubpanel.scrollIntoViewIfNeeded(false);
		emailSubpanel.composeEmail();
		sugar().alerts.closeAllWarning();
		sugar().alerts.getWarning().assertVisible(false);
		sugar().accounts.recordView.composeEmail.cancel();

		VoodooUtils.voodoo.log.info("closeAllWarning() complete.");
	}

	@Test
	public void closeAllError() throws Exception {
		VoodooUtils.voodoo.log.info("Running closeAllError()...");

		// Verify and close all Error alerts
		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.clickRecord(1);
		sugar().accounts.recordView.edit();
		sugar().accounts.createDrawer.getEditField("name").set("");
		sugar().accounts.recordView.save();
		sugar().alerts.closeAllError();
		sugar().alerts.getError().assertVisible(false);
		sugar().accounts.recordView.cancel();

		VoodooUtils.voodoo.log.info("closeAllError() complete.");
	}

	@Test
	public void closeAllAlerts() throws Exception {
		VoodooUtils.voodoo.log.info("Running closeAllAlerts()...");

		sugar().opportunities.api.create();

		// RLI record via UI
		sugar().revLineItems.create();

		// OPP record view -> RLI subpanel -> Check record -> Generate Quote -> Edit RLI record -> Save
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.clickRecord(1);
		StandardSubpanel revLineItemsSubpanel = sugar().opportunities.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural);
		revLineItemsSubpanel.toggleSubpanel();
		revLineItemsSubpanel.checkRecord(1);
		revLineItemsSubpanel.openActionDropdown();
		VoodooUtils.waitForReady();
		revLineItemsSubpanel.generateQuote();
		sugar().quotes.editView.cancel();
		VoodooUtils.waitForReady();
		revLineItemsSubpanel.scrollIntoView();
		revLineItemsSubpanel.editRecord(1);
		revLineItemsSubpanel.getEditField(1, "name").set(testName);
		revLineItemsSubpanel.getControl("saveActionRow01").click();

		// Verify closeAllAlerts (No Blue, Error, Green, Yellow messages more)
		sugar().alerts.closeAllAlerts();
		sugar().alerts.getAlert().assertVisible(false);
		sugar().alerts.getSuccess().assertVisible(false);
		sugar().alerts.getError().assertVisible(false);
		sugar().alerts.getWarning().assertVisible(false);
		sugar().alerts.getInfo().assertVisible(false);

		VoodooUtils.voodoo.log.info("closeAllAlerts() complete.");
	}

	@Test
	public void cancelAllWarning() throws Exception {
		VoodooUtils.voodoo.log.info("Running cancelAllWarning()...");

		// Verify and cancel all Warning alerts
		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.deleteRecord(1);
		sugar().alerts.cancelAllWarning();
		sugar().alerts.getWarning().assertVisible(false);
		sugar().accounts.listView.verifyField(1, "name", sugar().accounts.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info("cancelAllWarning() complete.");
	}

	@Test
	public void cancelAllAlert() throws Exception {
		VoodooUtils.voodoo.log.info("Running cancelAllAlert()...");

		// Verify and cancel all alerts
		sugar().opportunities.api.create();
		sugar().opportunities.navToListView();
		sugar().opportunities.listView.deleteRecord(1);
		sugar().alerts.cancelAllAlerts();
		sugar().alerts.getSuccess().assertVisible(false);
		sugar().alerts.getError().assertVisible(false);
		sugar().alerts.getWarning().assertVisible(false);
		sugar().alerts.getInfo().assertVisible(false);
		sugar().opportunities.listView.verifyField(1, "name", sugar().opportunities.getDefaultData().get("name"));

		VoodooUtils.voodoo.log.info("cancelAllAlert() complete.");
	}

	@Test
	public void confirmAllWarning() throws Exception {
		VoodooUtils.voodoo.log.info("Running confirmAllWarning()...");

		// Verify and confirm all Warning alerts
		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.deleteRecord(1);
		sugar().alerts.confirmAllWarning();
		sugar().alerts.getWarning().assertVisible(false);
		sugar().accounts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info("confirmAllWarning() complete.");
	}

	@Test
	public void confirmAllAlerts() throws Exception {
		VoodooUtils.voodoo.log.info("Running confirmAllAlerts()...");

		// Verify and confirm all alerts
		sugar().accounts.api.create();
		sugar().accounts.navToListView();
		sugar().accounts.listView.deleteRecord(1);
		sugar().alerts.confirmAllAlerts();
		sugar().alerts.getSuccess().assertVisible(false);
		sugar().alerts.getError().assertVisible(false);
		sugar().alerts.getWarning().assertVisible(false);
		sugar().alerts.getInfo().assertVisible(false);
		sugar().accounts.listView.assertIsEmpty();

		VoodooUtils.voodoo.log.info("confirmAllAlerts() complete.");
	}

	public void cleanup() throws Exception {}
}