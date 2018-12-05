package com.sugarcrm.test.teams;

import org.junit.Test;
import com.sugarcrm.candybean.datasource.DataSource;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class Teams_26079 extends SugarTest {
	DataSource documentsData, teamsData;
	VoodooControl studioCtrl,moduleCtrl, layoutCtrl, searchBtnCtrl,
	advanceSearchCtrl, saveBtnCtrl, basicSearch;

	public void setup() throws Exception {

		documentsData = testData.get(testName + "_documents");
		teamsData = testData.get(testName);
		sugar.login();

		// create document records 
		sugar.documents.api.create(documentsData);

		//  Admin -> Studio
		sugar.navbar.navToAdminTools();
		sugar.alerts.waitForLoadingExpiration();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO VOOD-517 Create Studio Module (BWC) - will provide the
		// references to replace these explicit VoodooControls
		studioCtrl = new VoodooControl("a", "id", "studio");
		studioCtrl.click();
		VoodooUtils.waitForReady();

		// Admin -> Studio -> Documents
		moduleCtrl = new VoodooControl("a", "id", "studiolink_Documents");
		moduleCtrl.click();
		VoodooUtils.waitForReady();

		// Select Documents -> layouts -> search -> advanced search 
		layoutCtrl = new VoodooControl("td", "id", "layoutsBtn");
		layoutCtrl.click();
		VoodooUtils.waitForReady();

		searchBtnCtrl = new VoodooControl("td", "id", "searchBtn");
		searchBtnCtrl.click();
		VoodooUtils.waitForReady();

		advanceSearchCtrl = new VoodooControl("td", "id", "AdvancedSearchBtn");
		advanceSearchCtrl.click();
		sugar.admin.studio.waitForAJAX(30000);

		VoodooUtils.waitForReady();
		
		//  Drag teams field from Hidden panel to Default panel 
		VoodooControl itemToDrag = new VoodooControl("li", "css", "#Hidden [data-name='team_name']");
		VoodooControl dropCtrl = new VoodooControl("td", "id", "Default");
		itemToDrag.dragNDrop(dropCtrl);

		// Save & Deploy
		saveBtnCtrl = new VoodooControl("td", "id", "savebtn");
		saveBtnCtrl.click();
		VoodooUtils.waitForReady();
		VoodooUtils.focusDefault();
	}

	/**
	 *   Combine team id search condition with another search condition (at least)
	 * 
	 * @throws Exception
	 */
	@Test
	public void Teams_26079_execute() throws Exception {
		VoodooUtils.voodoo.log.info("Running " + testName + "...");

		sugar.documents.navToListView();
		sugar.alerts.waitForLoadingExpiration();

		VoodooUtils.focusFrame("bwc-frame");
		// search the record to update team 
		basicSearch = new VoodooControl("input", "id", "search_form_submit");
		VoodooControl nameBasic = new VoodooControl("input", "id", "document_name_basic");

		String strToSearch = documentsData.get(1).get("documentName").substring(0,documentsData.get(1).get("documentName").length()-1);
		nameBasic.set(strToSearch);
		basicSearch.click();
		VoodooUtils.focusDefault();

		sugar.documents.listView.checkRecord(1);
		sugar.documents.listView.checkRecord(2);
		sugar.documents.listView.openActionDropdown();

		// mass update documents records teams 
		sugar.documents.listView.massUpdate();
		VoodooUtils.focusFrame("bwc-frame");

		// TODO VOOD-768, VOOD-1160		
		new VoodooControl("input", "id", "MassUpdate_team_name_collection_0").set(teamsData.get(1).get("teamName"));
		VoodooUtils.waitForReady();
		new VoodooControl("li", "css", "div#MassUpdate_MassUpdate_team_name_collection_0_results ul li:nth-of-type(1)").click();
		new VoodooControl("input", "id", "update_button").click();
		VoodooUtils.acceptDialog();

		// clear the search
		nameBasic.set("");
		basicSearch.click();

		// TODO: VOOD-975
		//  Go to advance search 
		new VoodooControl("a", "id", "advanced_search_link").click();
		VoodooUtils.waitForReady();

		// TODO: VOOD-1162
		VoodooControl teamCtrl= new VoodooControl("input", "id", "search_form_team_name_advanced_collection_0");
		VoodooControl selectCtrl= new VoodooControl("li", "css", "#search_form_search_form_team_name_advanced_collection_0_results li:nth-child(1)");
		VoodooControl searchCtrl = new VoodooControl("input", "id", "search_form_submit_advanced");
		VoodooControl searchConditionCtrl = new VoodooControl("input", "css", "#team_name_advanced_type[value='all']");
		VoodooControl documentCategoryCtrl = new VoodooControl("option", "css", "#category_id_advanced [value='Marketing']");
		VoodooControl listItemRowCtrl = new VoodooControl("table", "css", "#MassUpdate > table");

		for (int i = 0; i < documentsData.size()-1; i++) {
			// click document category
			documentCategoryCtrl.click();

			// input teams id (eg. qauser,Global)
			teamCtrl.set(teamsData.get(i).get("teamName"));

			// search and select team id
			selectCtrl.click();
			VoodooUtils.pause(200); //small pause required to click checkbox

			// Select a radio button below the team id (i.e. Any)
			searchConditionCtrl.click();

			// search record
			searchCtrl.click();
			VoodooUtils.waitForReady();

			// Verify that record is in the documents list for searched condition
			if(i == 0){
				listItemRowCtrl.assertContains(documentsData.get(0).get("documentName"), true);
				listItemRowCtrl.assertContains(documentsData.get(1).get("documentName"), true);
				listItemRowCtrl.assertContains(documentsData.get(2).get("documentName"), true);
			}else{
				listItemRowCtrl.assertContains(documentsData.get(0).get("documentName"), true);
				listItemRowCtrl.assertContains(documentsData.get(1).get("documentName"), true);
			}
			documentCategoryCtrl.click();
		}
		VoodooUtils.focusDefault();
		VoodooUtils.voodoo.log.info(testName + " complete.");
	}

	public void cleanup() throws Exception {}
}