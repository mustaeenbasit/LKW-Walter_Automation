package com.sugarcrm.test.studio;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25748 extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().admin.enableModuleDisplayViaJs(sugar().knowledgeBase);
	}
	
	/**
	 *  Verify that a custom field can be added to the KB module Listview
	 * 
	 * @throws Exception
	 */
	@Test
	public void Studio_25748_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");
		
		FieldSet customData = testData.get(testName).get(0);
		
		// TODO: VOOD-542
		// Go to Studio-> Calls-> Fields
		sugar().navbar.navToAdminTools();
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.adminTools.getControl("studio").click();
		VoodooControl knowledgeBaseVoodCtrl = new VoodooControl("a", "id", "studiolink_KBContents");
		knowledgeBaseVoodCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "fieldsBtn").click();
		
		// Add custom field
		new VoodooControl("input", "css", "#studiofields input[name=addfieldbtn]").click();
		new VoodooControl("input", "id", "field_name_id").set(customData.get("field_name"));
		new VoodooControl("input", "css", "#popup_form_id input[name=fsavebtn]").click();
		VoodooUtils.waitForReady();

		// Add custom field to layout > record view in knowledge Base module 
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		knowledgeBaseVoodCtrl.click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "layoutsBtn").click();
		VoodooUtils.waitForReady();
		new VoodooControl("td", "id", "viewBtnlistview").click();	
		VoodooControl fieldToMove = new VoodooControl("div", "css", "[data-name='demofield_c']"); 
		VoodooControl moveToNewPanel =	new VoodooControl("td", "id", "Default");
		fieldToMove.dragNDrop(moveToNewPanel);
		new VoodooControl("css", "id", "savebtn").click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
		
		// Create Article
		String documentName = customData.get("document_name");
		sugar().navbar.selectMenuItem(sugar().knowledgeBase, "createArticle");
		sugar().knowledgeBase.createDrawer.getEditField("name").set(documentName);
		VoodooUtils.focusFrame(0);
		sugar().knowledgeBase.createDrawer.getEditField("body").set(documentName);
		VoodooUtils.focusDefault();
		sugar().knowledgeBase.createDrawer.save();
		
		// Go to knowledge Base and verify custom field & its data
		// TODO: VOOD-1036
		sugar().navbar.navToModule(sugar().knowledgeBase.moduleNamePlural);
		new VoodooControl("span", "css", "[data-fieldname='demofield_c'] .ui-draggable span").assertContains(customData.get("field_name"), true);
		sugar().knowledgeBase.listView.getDetailField(1, "name").assertEquals(documentName, true);

		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}