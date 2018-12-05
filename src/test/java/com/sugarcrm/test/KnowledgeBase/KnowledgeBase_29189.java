package com.sugarcrm.test.KnowledgeBase;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class KnowledgeBase_29189 extends SugarTest {

	public void setup() throws Exception {
		sugar().login();

		// Enable Knowledge Base module
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}

	/**
	 * Verify that user can create new templates within Search and Select drawer, and they are correctly applied to KB Article
	 * 
	 * @throws Exception
	 */
	@Test
	public void KnowledgeBase_29189_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		FieldSet customFS = testData.get(testName).get(0);

		// Go to Knowledge Base -> Create Article -> Type in article name, e.g. "KB1" (and NOTHING/no text in the body)
		sugar().knowledgeBase.navToListView();
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(customFS.get("name1"));

		// TODO: VOOD-695 & VOOD-1756
		// Click on "Templates" button -> On Search and Select Templates page, click on Create button. -> Enter KB template name e.g. "KB Temp1"
		VoodooControl templateBtn = new VoodooControl("a", "css", "[data-type='template-button'] .btn");
		templateBtn.click();
		VoodooUtils.waitForReady();
		new VoodooControl("a", "css", ".selection-headerpane.fld_create_button a.btn-primary").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css", "[data-voodoo-name='KBContentTemplates'] .fld_name.edit input").set(customFS.get("templateName"));
		VoodooUtils.focusFrame("mce_9_ifr");
		new VoodooControl("body", "css", "#tinymce").set(customFS.get("body1"));
		VoodooUtils.focusDefault();
		new VoodooControl("a", "css", "[data-voodoo-name='KBContentTemplates'] [name='save_button']").click();
		VoodooUtils.waitForReady();

		// Verify, user is redirected back to the KB Article with the template body filled in the article body ("this is the template body")
		VoodooUtils.focusFrame("mce_0_ifr");
		sugar().knowledgeBase.createDrawer.getEditField("body").assertContains(customFS.get("body1"), true);
		VoodooUtils.focusDefault();

		// Click to save the KB Article
		sugar().knowledgeBase.createDrawer.save();

		// Verify that the User redirected to KB listview, saved article is listed (KB1)
		sugar().knowledgeBase.listView.verifyField(1, "name", customFS.get("name1"));

		// Go to Create KB Article -> Type in article name, e.g. "KB2" -> Add some text to the body, e.g. "blah blah blah"
		sugar().knowledgeBase.listView.create();
		sugar().knowledgeBase.createDrawer.getEditField("name").set(customFS.get("name2"));
		VoodooUtils.focusFrame("mce_10_ifr");
		sugar().knowledgeBase.createDrawer.getEditField("body").set(customFS.get("body2"));
		VoodooUtils.focusDefault();

		// TODO: VOOD-1756
		// Click on "Templates" button -> In Search and Select Knowledge Base Templates page, select the template you just created, "KB Temp1"
		templateBtn.click();
		new VoodooControl("input", "css", "[name='KBContentTemplates_select']").click();

		// User is directed back to their KB article (KB2), and popup/alert appears "The template will overwrite all contents. Are you sure you want to use this template?"
		sugar().alerts.getWarning().assertContains(customFS.get("warningMsg"), true);

		// Click Confirm on the popup/alert
		sugar().alerts.getWarning().confirmAlert();
		VoodooUtils.waitForReady();

		// Verify, After clicking Confirm, the article body is populated with the text in the template body ("this is the template body" replaces the "blah blah blah" text)
		VoodooUtils.focusFrame("mce_10_ifr");
		sugar().knowledgeBase.createDrawer.getEditField("body").assertContains(customFS.get("body1"), true);
		VoodooUtils.focusDefault();

		// Click to save the KB article
		sugar().knowledgeBase.createDrawer.save();

		// Verify that the User redirected to KB listview, saved article is listed (KB2)
		sugar().knowledgeBase.listView.verifyField(1, "name", customFS.get("name2"));
		sugar().knowledgeBase.listView.verifyField(2, "name", customFS.get("name1"));

		// Go to View Templates (in Knowledge Base's megamenu)
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "viewTemplates");

		// TODO: VOOD-1756
		// Verify that the template you just created is listed (KB Temp1)
		new VoodooControl("a", "css", ".list.fld_name div a").assertContains(customFS.get("templateName"), true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}