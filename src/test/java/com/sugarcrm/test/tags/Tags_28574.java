package com.sugarcrm.test.tags;
import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.modules.Module;
import com.sugarcrm.test.SugarTest;

public class Tags_28574 extends SugarTest {
	DataSource customDS;
	FieldSet fs = new FieldSet();
	ArrayList<Module> taggable = new ArrayList();
	
	public void setup() throws Exception {
		sugar().login();
		
		customDS = testData.get(testName);

		taggable.add(sugar().leads);
		taggable.add(sugar().cases);
		taggable.add(sugar().targetlists);
		taggable.add(sugar().targets);
		taggable.add(sugar().contacts);
		taggable.add(sugar().accounts);
		taggable.add(sugar().opportunities);
		taggable.add(sugar().notes);
		taggable.add(sugar().calls);
		taggable.add(sugar().meetings);
		taggable.add(sugar().leads);
		taggable.add(sugar().tasks);
		taggable.add(sugar().revLineItems);
		taggable.add(sugar().productCatalog);
		taggable.add(sugar().knowledgeBase);
		
		// Create Account, Tag, Opp and RLI
		sugar().accounts.api.create();
		
		fs.clear();
		fs.put("name", customDS.get(0).get("name"));
		sugar().tags.create(fs);
		
		sugar().opportunities.create();

		// Create RLI record with Tag
		fs.clear();
		fs.put("tags", customDS.get(0).get("name"));
		sugar().revLineItems.create(fs);		
	}

	/**
	 * Verify the displaying subpanels in Tag Record view
	 * 
	 * @throws Exception
	 */
	@Test
	public void Tags_28574_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		// Create a new Tag
		fs.clear();
		fs.put("name", customDS.get(1).get("name"));
		sugar().tags.create(fs);
		
		// Goto First Tag recordView
		sugar().tags.navToListView();
		sugar().tags.listView.clickRecord(2);

		// Verify all OOB taggable modules are present as subpanel
		for (Module module : taggable) {
			sugar().tags.recordView.subpanels.get(module.moduleNamePlural).assertExists(true);
		}
		
		// Verify RLI record is present
		sugar().tags.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural).expandSubpanel();
		sugar().tags.recordView.subpanels.get(sugar().revLineItems.moduleNamePlural).getDetailField(1, "name").assertContains(sugar().revLineItems.getDefaultData().get("name"), true);

		// Create a Custom Module
		FieldSet moduleData = testData.get(testName + "_module").get(0);				
		sugar().admin.navToAdminPanelLink("moduleBuilder");
		VoodooUtils.focusFrame("bwc-frame");
		
		//TODO: VOOD-933
		new VoodooControl("a", "css" ,"td#newPackage a").click();
		VoodooUtils.waitForReady();
		new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(moduleData.get("pack_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='key']").set(moduleData.get("key"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();		
		VoodooUtils.waitForAlertExpiration();
		
		new VoodooControl("a", "css" ,"table#new_module a").click();	
		VoodooUtils.waitForReady();
		
		new VoodooControl("input", "css" ,"table.mbTable input[name='name']").set(moduleData.get("mod_name"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='label']").set(moduleData.get("label"));
		new VoodooControl("input", "css" ,"table.mbTable input[name='label_singular']").set(moduleData.get("singular_label"));
		new VoodooControl("table", "css" ,"tr#factory_modules table#type_person").click();		
		new VoodooControl("input", "css" ,"table.mbTable input[name='savebtn']").click();
		VoodooUtils.waitForAlertExpiration();
		
		new VoodooControl("a", "css" ,"div.bodywrapper div a:nth-of-type(4)").click();
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").waitForVisible();
		new VoodooControl("input", "css" ,"table.mbTable input[name='deploybtn']").click();
		VoodooUtils.pause(2000); // Wait for deploy image to appear
		// TODO: VOOD-1010
		new VoodooControl("img", "css", ".bodywrapper img[align='absmiddle']").waitForInvisible(120000);
		VoodooUtils.waitForAlertExpiration();
			
		VoodooUtils.focusDefault();
		
		// Goto First Tag recordView
		sugar().tags.navToListView();
		sugar().tags.listView.clickRecord(2);

		// Verify that new Custom Module is present as a taggable subpanel
		new VoodooControl("div", "css", ".subpanels-layout.layout_Tags [data-subpanel-link='"+moduleData.get("key").toLowerCase()+"_"+moduleData.get("mod_name").toLowerCase()+"_link']").assertExists(true);
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}