package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

/**
 * Contains self tests different Alert class methods in SugarCRM.
 * 
 * @author Ashish Jabble <ajabble@sugarcrm.com>
 */
public class AlertTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void warningAlertClickLink() throws Exception {
		VoodooUtils.voodoo.log.info("Running warningAlertClickLink()...");

		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();

		// Verify click on the desired link, by its index (1) and then no warning alert appears
		sugar().alerts.getWarning().clickLink(1);
		sugar().alerts.getWarning().assertVisible(false);
		sugar().calls.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("warningAlertClickLink() complete.");
	}

	@Test
	public void warningAlertCloseAlert() throws Exception {
		VoodooUtils.voodoo.log.info("Running warningAlertCloseAlert()...");

		// Verify warning close alert, then no warning appears
		sugar().navbar.navToModule(sugar().calendar.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		
		// TODO: VOOD-863
		new VoodooControl("div", "css", ".week div[time='08:00am']").click();
		VoodooUtils.focusDefault();
		sugar().alerts.getWarning().getControl("closeAlert").click();
		sugar().alerts.getWarning().assertVisible(false);
		sugar().calls.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("warningAlertCloseAlert() complete.");
	}

	@Test
	public void warningAlertCancelAlert() throws Exception {
		VoodooUtils.voodoo.log.info("Running warningAlertCancelAlert()...");

		sugar().calls.api.create();

		// Verify warning cancel alert button, then no warning appears
		sugar().calls.navToListView();
		sugar().calls.listView.deleteRecord(1);
		sugar().alerts.getWarning().getControl("cancelAlert").click(); 
		sugar().alerts.getWarning().assertVisible(false);

		VoodooUtils.voodoo.log.info("warningAlertCancelAlert() complete.");
	}

	@Test
	public void warningAlertConfirmAlert() throws Exception {
		VoodooUtils.voodoo.log.info("Running warningAlertConfirmAlert()...");

		sugar().calls.api.create();

		// Verify warning confirm alert button, then no warning appears
		sugar().calls.navToListView();
		sugar().calls.listView.deleteRecord(1);
		sugar().alerts.getWarning().getControl("confirmAlert").click();
		sugar().alerts.getWarning().assertVisible(false);

		VoodooUtils.voodoo.log.info("warningAlertConfirmAlert() complete.");
	}

	@Test
	public void errorAlertCloseAlert() throws Exception {
		VoodooUtils.voodoo.log.info("Running errorAlertCloseAlert()...");

		// Verify error close alert, then no error alert appears
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		sugar().calls.createDrawer.save();
		sugar().alerts.getError().getControl("closeAlert").click();
		sugar().alerts.getError().assertVisible(false);
		sugar().calls.createDrawer.cancel();

		VoodooUtils.voodoo.log.info("errorAlertCloseAlert() complete.");
	}

	@Test
	public void successAlertCloseAlert() throws Exception {
		VoodooUtils.voodoo.log.info("Running successAlertCloseAlert()...");

		// Verify success close alert, then no success alert appears
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.save();
		sugar().alerts.getSuccess().getControl("closeAlert").click();
		sugar().alerts.getSuccess().assertVisible(false);

		VoodooUtils.voodoo.log.info("successAlertCloseAlert() complete.");
	}

	@Test
	public void successAlertClickLink() throws Exception {
		VoodooUtils.voodoo.log.info("Running successAlertClickLink()...");

		// Verify success alert click link, clicking by its index(0) and then record-view appears
		sugar().calls.navToListView();
		sugar().calls.listView.create();
		sugar().calls.createDrawer.getEditField("name").set(testName);
		sugar().calls.createDrawer.save();
		sugar().alerts.getSuccess().clickLink(0);
		sugar().calls.recordView.assertVisible(true);

		VoodooUtils.voodoo.log.info("successAlertClickLink() complete.");
	}

	public void cleanup() throws Exception {}
}