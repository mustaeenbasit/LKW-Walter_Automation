package com.sugarcrm.test.grimoire;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;

public class StudioTests extends SugarTest {
	String studioModules[] = {
			"Accounts",
			"Bugs",
			"Calls",
			"Campaigns",
			"Cases",
			"Contacts",
			"Contracts",
			"Documents",
			"Employees",
			"Knowledge Base",
			"Leads",
			"Meetings",
			"Notes",
			"Opportunities",
			"Product Catalog",
			"Project Tasks",
			"Projects",
			"Quoted Line Items",
			"Quotes",
			"Revenue Line Items",
			"Targets",
			"Tasks",
			"Users"
	};

	public void setup() throws Exception {
		sugar().login();
	}

	@Test
	public void verifyElements() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElements() ...");
		
		// Verify all module links are present on Studio page
		sugar().admin.navToAdminPanelLink("studio");
		
		VoodooUtils.focusFrame("bwc-frame");
		
		// Fetch Module list + link from page
		String runStudioJS = "var moduleList = '';jQuery(\"a[id *= 'studiolink']\").each(function() { moduleList += jQuery(this).attr('id') + ','; });  return moduleList;";
		String studioJS = (String) VoodooUtils.executeJS(runStudioJS);
		VoodooUtils.voodoo.log.info("studioJS: " + studioJS);
		
		String studioM[] = studioJS.split(",");
		String linkID = "";
		
		ArrayList<String> studioList = new ArrayList<>();
		for(int i = 0; i < studioM.length; i++) {
			studioList.add(studioM[i]);
		}
		
		for(int i = 0; i < studioModules.length; i++) {
			switch (studioModules[i]) {
			case "Knowledge Base":
				linkID = "studiolink_KBContents";
				break;
			case "Product Catalog":
				linkID = "studiolink_ProductTemplates";
				break;
			case "Project Tasks":
				linkID = "studiolink_ProjectTask";
				break;
			case "Quoted Line Items":
				linkID = "studiolink_Products";
				break;
			case "Revenue Line Items":
				linkID = "studiolink_RevenueLineItems";
				break;
			case "Targets":
				linkID = "studiolink_Prospects";
				break;
			case "Projects":
				linkID = "studiolink_Project";
				break;
			default:
				linkID = "studiolink_" + studioModules[i];
			}
			assertTrue("linkID is missing", studioList.contains(linkID));
		}
				
		VoodooUtils.focusDefault();
		
		// nav to sidecar module
		sugar().studio.clickStudioModule(sugar().accounts.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().studio.studioModule.getControl("labels").assertVisible(true);
		sugar().studio.studioModule.getControl("fields").assertVisible(true);
		sugar().studio.studioModule.getControl("relationships").assertVisible(true);
		sugar().studio.studioModule.getControl("layouts").assertVisible(true);
		sugar().studio.studioModule.getControl("subpanels").assertVisible(true);
		sugar().studio.studioModule.getControl("mobileLayouts").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// nav to bwc module
		sugar().studio.footer.clickStudio();
		sugar().studio.clickStudioModule(sugar().quotes.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().studio.studioModule.getControl("labels").assertVisible(true);
		sugar().studio.studioModule.getControl("fields").assertVisible(true);
		sugar().studio.studioModule.getControl("relationships").assertVisible(true);
		sugar().studio.studioModule.getControl("layouts").assertVisible(true);
		sugar().studio.studioModule.getControl("subpanels").assertVisible(true);
		sugar().studio.studioModule.getControl("mobileLayouts").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// nav to users bwc module
		sugar().studio.breadCrumb.clickStudioInBreadCrumb();
		sugar().studio.clickStudioModule(sugar().users.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		sugar().studio.studioModule.getControl("labels").assertVisible(true);
		sugar().studio.studioModule.getControl("fields").assertVisible(true);
		sugar().studio.studioModule.getControl("relationships").assertVisible(true);
		sugar().studio.studioModule.getControl("layouts").assertVisible(true);
		sugar().studio.studioModule.getControl("subpanels").assertVisible(true);
		sugar().studio.studioModule.getControl("mobileLayouts").assertVisible(true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info("verifyElements() complete.");
	}
	
	@Test
	public void verifyNavigation() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyNavigation...");
		
		// Navigate to Leads and verify Leads page is open
		sugar().studio.navToStudioModule(sugar().leads);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Leads')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// Navigate to Studio Page via Footer
		sugar().studio.footer.clickStudio();
		
		// Navigate to Knowledge Base and verify Knowledge Base page is open
		sugar().studio.clickStudioModule(sugar().knowledgeBase.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Knowledge Base')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// Navigate to Studio Page via BreadCrmb
		sugar().studio.breadCrumb.clickStudioInBreadCrumb();
		
		// Navigate to Product Catalog and verify Product Catalog page is open
		sugar().studio.clickStudioModule(sugar().productCatalog.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Product Catalog')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// Navigate to Product Catalog Layouts
		sugar().studio.clickItemOnPage(sugar().studio.studioModule.getControl("layouts"));
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Layouts')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// Navigate back to Product Catalog via BreadCrmb
		sugar().studio.breadCrumb.clickModuleInBreadCrumb(sugar().productCatalog);
		
		// Navigate to Studio Page via BreadCrmb
		sugar().studio.breadCrumb.clickStudioInBreadCrumb();
		
		// Navigate to Project Tasks and verify Project Tasks page is open
		sugar().studio.clickStudioModule("Project Tasks");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Project Tasks')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// Navigate to Studio Page via Footer
		sugar().studio.footer.clickStudio();
		
		// Navigate to Quoted Line Items and verify Quoted Line Items page is open
		sugar().studio.clickStudioModule(sugar().quotedLineItems.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Quoted Line Items')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// Navigate to Studio Page via BreadCrmb
		sugar().studio.breadCrumb.clickStudioInBreadCrumb();
		
		// Navigate to Revenue Line Items and verify Revenue Line Items page is open
		sugar().studio.clickStudioModule(sugar().revLineItems.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Revenue Line Items')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// Navigate to Studio Page via Footer
		sugar().studio.footer.clickStudio();
		
		// Navigate to Targets and verify Targets page is open
		sugar().studio.clickStudioModule(sugar().targets.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Targets')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// Navigate to Studio Page via BreadCrmb
		sugar().studio.breadCrumb.clickStudioInBreadCrumb();
		
		// Navigate to Projects and verify Projects page is open
		sugar().studio.clickStudioModule("Projects");
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Projects')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info("verifyNavigation() complete.");
	}

	// Below three tests will be added during fixing of recordView/listView VOODs
	@Test
	public void verifyClickAndSetAndCheckbox() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyClickAndSetAndCheckbox...");
		
		VoodooUtils.voodoo.log.info("verifyClickAndSetAndCheckbox() complete.");
	}

	@Test
	public void verifyMoveDragNDrop() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMoveDragNDrop...");
		
		VoodooUtils.voodoo.log.info("verifyMoveDragNDrop() complete.");
	}

	@Test
	public void verifyMoveDragNDropViaJS() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMoveDragNDropViaJS...");
		
		VoodooUtils.voodoo.log.info("verifyMoveDragNDropViaJS() complete.");
	}

	@Test
	public void verifyClickModuleInBreadCrumb() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyClickModuleInBreadCrumb...");
		
		// Navigate to Opportunities and verify Opportunities page is open
		sugar().studio.navToStudioModule(sugar().opportunities);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Opportunities')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// Navigate to Opportunities Layouts
		sugar().studio.clickItemOnPage(sugar().studio.studioModule.getControl("layouts"));
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Layouts')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		// Navigate back to Opportunities via BreadCrmb
		sugar().studio.breadCrumb.clickModuleInBreadCrumb(sugar().opportunities);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Opportunities')]").assertVisible(true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info("verifyClickModuleInBreadCrumb() complete.");
	}

	@Test
	public void verifyClickStudioInBreadCrumb() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyClickStudioInBreadCrumb...");
		
		// Navigate to Calls and verify Calls page is open
		sugar().studio.navToStudioModule(sugar().calls);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Calls')]").assertVisible(true);
		
		// Navigate to Studio Page via BreadCrmb
		sugar().studio.breadCrumb.clickStudioInBreadCrumb();
		
		// Navigate to Meetings and verify Meetings page is open
		sugar().studio.clickStudioModule(sugar().meetings.moduleNamePlural);
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("span", "xpath", "//span[@class='crumbLink'][contains(.,'Meetings')]").assertVisible(true);		
		
		VoodooUtils.voodoo.log.info("verifyClickStudioInBreadCrumb() complete.");
	}

	@Test
	public void verifyElementsInFooter() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyElementsInFooter...");
		
		sugar().admin.navToAdminPanelLink("studio");
		
		VoodooUtils.focusFrame("bwc-frame");
		sugar().studio.footer.getControl("homeButton").assertVisible(true);
		sugar().studio.footer.getControl("studioButton").assertVisible(true);
		sugar().studio.footer.getControl("moduleBuilderButton").assertVisible(true);
		sugar().studio.footer.getControl("portalEditorButton").assertVisible(true);
		sugar().studio.footer.getControl("dropdownEditorButton").assertVisible(true);		
		
		VoodooUtils.voodoo.log.info("verifyElementsInFooter() complete.");
	}

	@Test
	public void verifyClickFooterItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyClickFooterItems...");
		
		sugar().admin.navToAdminPanelLink("studio");		
		VoodooUtils.focusFrame("bwc-frame");
		
		sugar().studio.footer.clickHome();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#mbtabs > ul > li > a").assertContains("Home", true);		
		
		sugar().studio.footer.clickStudio();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#mbtabs > ul > li > a").assertContains("Studio", true);		
		
		sugar().studio.footer.clickModuleBuilder();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#mbtabs > ul > li > a").assertContains("Package List", true);		
		
		sugar().studio.footer.clickPortalEditor();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#mbtabs > ul > li > a").assertContains("Sugar Portal Editor", true);		
		
		sugar().studio.footer.clickDropdownEditor();
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("a", "css", "#mbtabs > ul > li > a").assertContains("Dropdown Editor", true);		
		
		VoodooUtils.voodoo.log.info("verifyClickFooterItems() complete.");
	}

	@Test
	public void verifyReset() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyReset...");
		
		// Also test chaining of methods
		sugar().studio
				.navToStudioModule(sugar.accounts)
				.resetModule();
		
		VoodooUtils.focusFrame("bwc-frame");
		new VoodooControl("div", "id", "export").assertVisible(true);
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info("verifyReset() complete.");
	}

	public void cleanup() throws Exception {}
}