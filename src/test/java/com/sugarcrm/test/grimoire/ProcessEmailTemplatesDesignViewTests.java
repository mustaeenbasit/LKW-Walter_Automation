package com.sugarcrm.test.grimoire;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProcessEmailTemplatesDesignViewTests extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyElementsHookValue() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElementsHookValue()...");

		sugar().processEmailTemplates.create();
		sugar().processEmailTemplates.listView.openRowActionDropdown(1);
		sugar().processEmailTemplates.listView.getControl("design01").click();
		sugar().processEmailTemplates.designView.getControl("targetModule").assertEquals(sugar().leads.moduleNamePlural, true);
		sugar().processEmailTemplates.designView.getControl("designName").assertVisible(true);
		sugar().processEmailTemplates.designView.getControl("designDescription").assertVisible(true);
		sugar().processEmailTemplates.designView.getControl("subject").assertVisible(true);
		sugar().processEmailTemplates.designView.getControl("save").assertVisible(true);
		sugar().processEmailTemplates.designView.getControl("cancel").assertVisible(true);
		sugar().processEmailTemplates.designView.getControl("moduleTitle").assertVisible(true);
		sugar().processEmailTemplates.designView.getControl("subjectSelector").assertVisible(true);
		sugar().processEmailTemplates.designView.getControl("primaryDropdown").assertVisible(true);
		sugar().processEmailTemplates.designView.getControl("primaryDropdown").click();
		sugar().processEmailTemplates.designView.getControl("saveAndExit").assertVisible(true);
		sugar().processEmailTemplates.designView.getControl("fieldsSelector").assertVisible(true);
		VoodooUtils.focusFrame(0);
		sugar().processEmailTemplates.designView.getControl("contentBody").assertVisible(true);
		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info("verifyElementsHookValue() complete.");
	}

	@Test
	public void verifySaveProcessEmailTemplatesDesignView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySaveProcessEmailTemplatesDesignView()...");

		sugar().processEmailTemplates.create();
		sugar().processEmailTemplates.listView.openRowActionDropdown(1);
		VoodooControl designCtrl  = sugar().processEmailTemplates.listView.getControl("design01");
		designCtrl.click();
		VoodooUtils.waitForReady();
		sugar().processEmailTemplates.designView.getControl("targetModule").assertEquals(sugar().leads.moduleNamePlural, true);
		sugar().processEmailTemplates.designView.getControl("designName").set("demo design");
		sugar().processEmailTemplates.designView.getControl("designDescription").set("demo design description");
		sugar().processEmailTemplates.designView.getControl("subject").set("demo design subject");
		sugar().processEmailTemplates.saveDesign();
		sugar().processEmailTemplates.navToListView();
		sugar().processEmailTemplates.listView.openRowActionDropdown(1);
		designCtrl.click();
		VoodooUtils.waitForReady();
		sugar().processEmailTemplates.designView.getControl("targetModule").assertEquals(sugar().leads.moduleNamePlural, true);
		sugar().processEmailTemplates.designView.getControl("designName").assertEquals("demo design", true);
		sugar().processEmailTemplates.designView.getControl("subject").assertEquals("demo design subject", true);
		// TODO: MACAROON-1334 -- Process Email Templates Compose view doesn't expose field contents in the DOM.
		sugar().processEmailTemplates.designView.getControl("designDescription").assertAttribute("value", "demo design description", true);

		VoodooUtils.voodoo.log.info("verifySaveProcessEmailTemplatesDesignView() complete.");
	}

	@Test
	public void verifySaveAndExitProcessEmailTemplatesDesignView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifySaveAndExitProcessEmailTemplatesDesignView()...");

		sugar().processEmailTemplates.create();
		sugar().processEmailTemplates.listView.openRowActionDropdown(1);
		VoodooControl designCtrl  = sugar().processEmailTemplates.listView.getControl("design01");
		designCtrl.click();
		VoodooUtils.waitForReady();
		sugar().processEmailTemplates.designView.getControl("targetModule").assertEquals(sugar().leads.moduleNamePlural, true);
		sugar().processEmailTemplates.designView.getControl("designName").set("demo design");
		sugar().processEmailTemplates.designView.getControl("designDescription").set("demo design description");
		sugar().processEmailTemplates.designView.getControl("subject").set("demo design subject");
		sugar().processEmailTemplates.saveAndExitDesign();
		sugar().processEmailTemplates.navToListView();
		sugar().processEmailTemplates.listView.openRowActionDropdown(1);
		designCtrl.click();
		VoodooUtils.waitForReady();
		sugar().processEmailTemplates.designView.getControl("targetModule").assertEquals(sugar().leads.moduleNamePlural, true);
		sugar().processEmailTemplates.designView.getControl("designName").assertEquals("demo design", true);
		sugar().processEmailTemplates.designView.getControl("subject").assertEquals("demo design subject", true);
		// TODO: MACAROON-1334 -- Process Email Templates Compose view doesn't expose field contents in the DOM.
		sugar().processEmailTemplates.designView.getControl("designDescription").assertAttribute("value", "demo design description", true);

		VoodooUtils.voodoo.log.info("verifySaveAndExitProcessEmailTemplatesDesignView() complete.");
	}

	@Test
	public void verifyCancelProcessEmailTemplatesDesignView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyCancelProcessEmailTemplatesDesignView()...");

		sugar().processEmailTemplates.create();
		sugar().processEmailTemplates.navToListView();
		sugar().processEmailTemplates.listView.openRowActionDropdown(1);
		sugar().processEmailTemplates.listView.getControl("design01").click();
		VoodooUtils.waitForReady();
		sugar().processEmailTemplates.designView.getControl("targetModule").assertEquals(sugar().leads.moduleNamePlural, true);
		sugar().processEmailTemplates.designView.getControl("designName").set("demo design");
		sugar().processEmailTemplates.designView.getControl("designDescription").set("demo design description");
		sugar().processEmailTemplates.designView.getControl("subject").set("demo design subject");
		sugar().processEmailTemplates.cancelDesign();
		sugar().processEmailTemplates.listView.getDetailField(1, "name").assertEquals("Process Email Template 1", true);

		VoodooUtils.voodoo.log.info("verifyCancelProcessEmailTemplatesDesignView() complete.");
	}

	public void cleanup() throws Exception {}
}