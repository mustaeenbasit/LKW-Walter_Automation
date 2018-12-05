package com.sugarcrm.test.studio;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Studio_25729_focusDefault  extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
	}

	/**
	  *  Verify that Browser back button functions correctly in Studio
      *  @throws Exception
      *  
      *  VOOD-2145 - VoodooUtils.getUrl() returns different URL for the same BWC page in different focus frames
      *  
      *  This method is for fetching URL with focus in focusDefault
      */
	@Test
	public void Studio_25729_focusDefault_execute() throws Exception {
	    VoodooUtils.voodoo.log.info("Running " + testName + "...");
	
		// TODO: VOOD-1503,VOOD-1504,VOOD-1505,VOOD-1511
		VoodooControl casesButtonCtrl = new VoodooControl("a", "id", "studiolink_Cases");
		VoodooControl contractsButtonCtrl = new VoodooControl("a", "id", "studiolink_Contracts");
		VoodooControl layoutsButtonCtrl = new VoodooControl("td", "id", "layoutsBtn");
		VoodooControl editViewButtonCtrl = new VoodooControl("td", "id", "viewBtneditview");
		VoodooControl detailViewButtonCtrl = new VoodooControl("td", "id", "viewBtndetailview");
		VoodooControl listViewButtonCtrl = new VoodooControl("td", "id", "viewBtnlistview");
		VoodooControl searchButtonCtrl = new VoodooControl("td", "id", "searchBtn");
		VoodooControl advancedSearchButtonCtrl = new VoodooControl("td", "id", "AdvancedSearchBtn");
		VoodooControl basicSearchButtonCtrl = new VoodooControl("td", "id", "BasicSearchBtn");
		VoodooControl fieldsButtonCtrl = new VoodooControl("td", "id", "fieldsBtn");
		VoodooControl labelsButtonCtrl = new VoodooControl("td", "id", "labelsBtn");
		VoodooControl subpanelsButtonCtrl = new VoodooControl("td", "id", "subpanelsBtn");
		VoodooControl contractsBreadCrumbCtrl = new VoodooControl("a", "xpath", "//*[@id='mbtabs']/div/div/div/div[1]/a[4]");
		VoodooControl relationshipsButtonCtrl = new VoodooControl("td", "id", "relationshipsBtn");
		String backPageURL="", currentPageURL="";
	
		// 1. Browser back from one module page to Studio Main page
		sugar().admin.navToAdminPanelLink("studio");
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl();
		
		VoodooUtils.focusFrame("bwc-frame");    
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from one module page to Studio Main page. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 2. Browser back from Labels view to one module page
		// We are already on Studio List page
		VoodooUtils.focusFrame("bwc-frame");
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl();   
		
		VoodooUtils.focusFrame("bwc-frame");
		labelsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from Labels view to one module page.. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 3. Browser back from Subpanels page to Labels view
		// Already on contracts Module Page
		VoodooUtils.focusFrame("bwc-frame");
		labelsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		VoodooUtils.focusFrame("bwc-frame");
		contractsBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		subpanelsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from Subpanels page to Labels view. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 4. Browser back from Relationships view to Fields view
		// Go back to contracts module page (from previous Labels view)
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickStudio();
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		fieldsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		VoodooUtils.focusFrame("bwc-frame");
		contractsBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		relationshipsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from Relationships view to Fields view. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 5. Browser back from Layouts view to Relationships view
		// Go back to contracts module page (from previous Fields view)
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickStudio();
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		relationshipsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		VoodooUtils.focusFrame("bwc-frame");
		contractsBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from Layouts view to Relationships view. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 6. Browser back from Edit View to Layouts page
		// Go back to contracts module page (from previous Relationships view)
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickStudio();
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		VoodooUtils.focusFrame("bwc-frame");
		editViewButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from Edit View to Layouts page. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 7. Browser back from Detail View to Edit View
		// Go back to contracts module page (from previous Layouts page)
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickStudio();
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		editViewButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		VoodooUtils.focusFrame("bwc-frame");
		contractsBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		detailViewButtonCtrl.click();   
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from Detail View to Edit View. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 8. Browser back from List View to Detail View
		// Go back to contracts module page (from previous Edit view)
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickStudio();
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		detailViewButtonCtrl.click();   
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		VoodooUtils.focusFrame("bwc-frame");
		contractsBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		listViewButtonCtrl.click();   
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from List View to Detail View. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// TODO: SC-4802 - Quick Create pops up unsaved alert - Once resolved below commented line will work
		// 9. Browser back from Quick Create to List View
		// Go back to contracts module page (from previous Detail view)
		VoodooUtils.focusFrame("bwc-frame");
		VoodooControl quickCreateButtonCtrl = new VoodooControl("td", "id", "viewBtnquickcreate");
		sugar().admin.studio.clickStudio();
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		listViewButtonCtrl.click();   
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		VoodooUtils.focusFrame("bwc-frame");
		contractsBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		quickCreateButtonCtrl.click();    
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.back();
		VoodooUtils.back();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from Quick create to List View. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 10. Browser back from Search page to Quick Create
		// Go back to contracts module page (from previous List view)
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickStudio();
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		quickCreateButtonCtrl.click();    
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		VoodooUtils.focusFrame("bwc-frame");
		contractsBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		searchButtonCtrl.click();   
		
		VoodooUtils.back();
		VoodooUtils.back();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from Search page to Quick create. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 11. Browser back from Basic Search to Search page
		// Go back to contracts module page (from previous Quick Create page)
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickStudio();
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		searchButtonCtrl.click();   
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		VoodooUtils.focusFrame("bwc-frame");
		contractsBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		searchButtonCtrl.click(); 
		VoodooUtils.waitForReady();
		basicSearchButtonCtrl.click();    
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from Basic Search to Search page. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 12. Browser back from Advanced Search to Basic Search
		// Go back to contracts module page (from previous Search page)
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickStudio();
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		searchButtonCtrl.click(); 
		VoodooUtils.waitForReady();
		basicSearchButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		VoodooUtils.focusFrame("bwc-frame");
		contractsBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		searchButtonCtrl.click(); 
		VoodooUtils.waitForReady();
		advancedSearchButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from Advance Search to Basic Search. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 13. Browser back from Sub panels page to Advanced Search
		// Go back to contracts module page (from previous Basic Search page)
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickStudio();
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		layoutsButtonCtrl.click();
		VoodooUtils.waitForReady();
		searchButtonCtrl.click(); 
		VoodooUtils.waitForReady();
		advancedSearchButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		VoodooUtils.focusFrame("bwc-frame");
		contractsBreadCrumbCtrl.click();
		VoodooUtils.waitForReady();
		subpanelsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		VoodooUtils.back();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from Sub panels page to Advanced Search. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		// 14. Browser back from one module page to another module page
		VoodooUtils.focusFrame("bwc-frame");
		sugar().admin.studio.clickStudio();
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		backPageURL = VoodooUtils.getUrl(); 
		
		// Select contracts Module
		VoodooUtils.focusFrame("bwc-frame");
		contractsButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.back(); // Back to List
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		VoodooUtils.focusFrame("bwc-frame");
		
		// Select Cases Module
		casesButtonCtrl.click();
		VoodooUtils.waitForReady();
		
		VoodooUtils.back(); // Back to List
		VoodooUtils.waitForReady();
		
		VoodooUtils.focusDefault();
		currentPageURL = VoodooUtils.getUrl();
		
		assertTrue("Error verifying: Browser back from one module page to another module page. Current: " + currentPageURL + " Back: " + backPageURL, currentPageURL.startsWith(backPageURL));
		
		VoodooUtils.focusDefault();
		
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}