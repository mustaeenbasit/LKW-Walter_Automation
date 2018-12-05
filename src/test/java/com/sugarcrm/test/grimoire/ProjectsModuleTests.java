package com.sugarcrm.test.grimoire;

import com.sugarcrm.candybean.datasource.DataSource;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.test.SugarTest;
import com.sugarcrm.candybean.datasource.FieldSet;
import com.sugarcrm.sugar.VoodooControl;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.sugar.views.StandardSubpanel;

import java.text.SimpleDateFormat;
import static org.junit.Assert.assertTrue;

/**
 * Contains self tests for elements, menu dropdown, selection menu, action menu items,
 * toggle record, basic clear search form, countRows, hook values for listview edit & detail view.
 *
 * @author Snigdha Sivadas <ssivadas@sugarcrm.com>
 */
public class ProjectsModuleTests extends SugarTest {

    public void setup() throws Exception {
        sugar().projects.api.create();
        sugar().login();

        // Enable Projects module
        sugar().admin.enableModuleDisplayViaJs(sugar().projects);
    }

    @Test
    public void verifyElements() throws Exception {
        VoodooUtils.voodoo.log.info("Running verifyElements()...");
        sugar().projects.navToListView();
        VoodooUtils.focusFrame("bwc-frame");
        sugar().projects.listView.getControl("moduleTitle").assertVisible(true);
        sugar().projects.listView.getControl("nameBasic").assertVisible(true);
        sugar().projects.listView.getControl("myItemsCheckbox").assertVisible(true);
        sugar().projects.listView.getControl("myFavoritesCheckbox").assertVisible(true);
        sugar().projects.listView.getControl("searchButton").assertVisible(true);
        sugar().projects.listView.getControl("clearButton").assertVisible(true);
        sugar().projects.listView.getControl("advancedSearchLink").assertVisible(true);
        sugar().projects.listView.getControl("startButton").assertVisible(true);
        sugar().projects.listView.getControl("endButton").assertVisible(true);
        sugar().projects.listView.getControl("prevButton").assertVisible(true);
        sugar().projects.listView.getControl("nextButton").assertVisible(true);
        sugar().projects.listView.getControl("selectAllCheckbox").assertExists(true);
        sugar().projects.listView.getControl("selectDropdown").assertExists(true);
        sugar().projects.listView.getControl("actionDropdown").assertExists(true);
        sugar().projects.listView.getControl("massUpdateButton").assertExists(true);
        sugar().projects.listView.getControl("deleteButton").assertExists(true);
        sugar().projects.listView.getControl("exportButton").assertExists(true);
        sugar().projects.listView.getControl("selectThisPage").assertExists(true);
        sugar().projects.listView.getControl("selectAll").assertExists(true);
        sugar().projects.listView.getControl("deselectAll").assertExists(true);
        VoodooUtils.focusDefault();

        VoodooUtils.voodoo.log.info("Completed verifyElements()...");
    }

    @Test
    public void verifyMenuItems() throws Exception {
        VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");
        sugar().projects.navToListView();
        sugar().navbar.clickModuleDropdown(sugar().projects);

        // Verify menu items
        sugar().projects.menu.getControl("createProject").assertVisible(true);
        sugar().projects.menu.getControl("createProjectTemplate").assertVisible(true);
        sugar().projects.menu.getControl("viewProjects").assertVisible(true);
        sugar().projects.menu.getControl("viewProjectsTemplates").assertVisible(true);
        sugar().projects.menu.getControl("viewProjectsTasks").assertVisible(true);
        sugar().projects.menu.getControl("viewProjectsDashboard").assertVisible(true);

        sugar().navbar.clickModuleDropdown(sugar().projects); // to close dropdown

        VoodooUtils.voodoo.log.info("Completed verifyMenuItems()...");
    }

    @Test
    public void verifyModuleTitle() throws Exception {
        VoodooUtils.voodoo.log.info("Running verifyModuleTitle()...");

        sugar().projects.navToListView();
        String expected = sugar().projects.moduleNamePlural;
        String found = sugar().projects.listView.getModuleTitle();
        assertTrue("getModuleTitle did not return " + expected + "; " + found + " was found instead.", found.contains(expected));

        VoodooUtils.voodoo.log.info("Completed verifyModuleTitle()...");
    }

    @Test
    public void verifySelectionMenuItems() throws Exception {
        VoodooUtils.voodoo.log.info("Running verifySelectionMenuItems()...");

        sugar().projects.navToListView();
        VoodooUtils.focusFrame("bwc-frame");
        sugar().projects.listView.getControl("selectAllCheckbox").assertVisible(true);
        VoodooUtils.focusDefault();
        sugar().projects.listView.openSelectDropdown();
        VoodooUtils.focusFrame("bwc-frame");
        sugar().projects.listView.getControl("selectThisPage").assertVisible(true);
        sugar().projects.listView.getControl("selectAll").assertVisible(true);
        sugar().projects.listView.getControl("deselectAll").assertVisible(true);
        sugar().projects.listView.getControl("selectDropdown").click();
        VoodooUtils.focusDefault();

        VoodooUtils.voodoo.log.info("Completed verifySelectionMenuItems()...");
    }

    @Test
    public void verifyActionMenuItems() throws Exception {
        VoodooUtils.voodoo.log.info("Running verifyActionMenuItems()...");

        sugar().projects.navToListView();
        sugar().projects.listView.checkRecord(1);
        sugar().projects.listView.openActionDropdown();
        VoodooUtils.focusFrame("bwc-frame");
        sugar().projects.listView.getControl("deleteButton").assertVisible(true);
        sugar().projects.listView.getControl("massUpdateButton").assertVisible(true);
        sugar().projects.listView.getControl("exportButton").assertVisible(true);
        sugar().projects.listView.getControl("actionDropdown").click();
        VoodooUtils.focusDefault();

        VoodooUtils.voodoo.log.info("Completed verifyActionMenuItems()...");
    }

    @Test
    public void verifyListviewField() throws Exception {
        VoodooUtils.voodoo.log.info("Running verifyListviewField()...");
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        FieldSet fieldSet = sugar().projects.getDefaultData();
        sugar().projects.navToListView();
        sugar().projects.listView.verifyField(1, "name", fieldSet.get("name"));
        sugar().projects.listView.verifyField(1, "date_estimated_start", formatter.format(formatter.parse(fieldSet.get("date_estimated_start"))));
        sugar().projects.listView.verifyField(1, "date_estimated_end", formatter.format(formatter.parse(fieldSet.get("date_estimated_end"))));
        sugar().projects.listView.verifyField(1, "status", fieldSet.get("status"));

        VoodooUtils.voodoo.log.info("Completed verifyListviewField()...");
    }

    @Test
    public void verifyCheckUncheckRecord() throws Exception {
        VoodooUtils.voodoo.log.info("Running verifyCheckUncheckRecord()...");

        sugar().projects.navToListView();
        VoodooControl checkbox = sugar().projects.listView.getControl("checkbox01");
        sugar().projects.listView.checkRecord(1);
        VoodooUtils.focusFrame("bwc-frame");
        checkbox.assertChecked(true);
        VoodooUtils.focusDefault();

        sugar().projects.listView.uncheckRecord(1);
        VoodooUtils.focusFrame("bwc-frame");
        checkbox.assertChecked(false);
        VoodooUtils.focusDefault();

        VoodooUtils.voodoo.log.info("Completed verifyCheckUncheckRecord()");
    }

    @Test
    public void verifyBasicAndClearSearch() throws Exception {
        VoodooUtils.voodoo.log.info("Running verifyBasicAndClearSearch()...");

        // Add 2 records
        DataSource ds = new DataSource();
        FieldSet name1 = new FieldSet();
        name1.put("name", "Clearsearch_1");
        FieldSet name2 = new FieldSet();
        name2.put("name", "Clearsearch_2");
        ds.add(name1);
        ds.add(name2);
        sugar().projects.api.create(ds);
        VoodooUtils.refresh(); // to populate data

        sugar().projects.navToListView();
        sugar().projects.listView.basicSearch("Blank");
        int row = sugar().projects.listView.countRows();
        Assert.assertTrue("Number of rows did not equal zero.", row == 0);
        VoodooUtils.focusDefault();

        // Clear the search and submit the search form
        sugar().projects.listView.clearSearchForm();
        sugar().projects.listView.submitSearchForm();
        VoodooUtils.focusDefault();
        row = sugar().projects.listView.countRows();
        Assert.assertTrue("After clear Number of rows did not equal to three.", row == 3);
        VoodooUtils.focusDefault();

        // Search for the keyword
        sugar().projects.listView.basicSearch(sugar().projects.getDefaultData().get("name"));
        row = sugar().projects.listView.countRows();
        Assert.assertTrue("Number of rows did not equal one.", row == 1);

        VoodooUtils.voodoo.log.info("Completed verifyBasicAndClearSearch()...");
    }

    @Test
    public void countRows() throws Exception {
        VoodooUtils.voodoo.log.info("Running countRows()...");

        sugar().projects.navToListView();
        // Verify 1 record count
        int row = sugar().projects.listView.countRows();
        Assert.assertTrue("Number of rows did not equal one.", row == 1);

        // Verify 3 record count
        DataSource ds = new DataSource();
        FieldSet name1 = new FieldSet();
        name1.put("name", "Project_1");
        FieldSet name2 = new FieldSet();
        name2.put("name", "Project_2");
        ds.add(name1);
        ds.add(name2);
        sugar().projects.api.create(ds);
        VoodooUtils.refresh(); // to populate data

        row = sugar().projects.listView.countRows();
        Assert.assertTrue("Number of rows did not equal three.", row == 3);
        VoodooUtils.focusDefault();

        // Verify 2 records after deleting 1 record
        sugar().projects.listView.deleteRecord(1);
        sugar().projects.listView.confirmDelete();
        VoodooUtils.focusDefault();
        row = sugar().projects.listView.countRows();
        Assert.assertTrue("Number of rows did not equal two.", row == 2);

        // Verify no records after deleting all records
        VoodooUtils.focusDefault();
        sugar().projects.listView.toggleSelectAll();
        VoodooUtils.focusDefault();
        sugar().projects.listView.delete();
        sugar().projects.listView.confirmDelete();
        VoodooUtils.focusDefault();
        row = sugar().projects.listView.countRows();
        Assert.assertTrue("Number of rows did not equal zero.", row == 0);

        VoodooUtils.voodoo.log.info("Completed countRows()...");
    }

    @Test
    public void verifyEditAndDetailFieldsInRecordView() throws Exception {
        VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

        sugar().projects.navToListView();
        sugar().projects.listView.clickRecord(1);
        sugar().projects.detailView.edit();
        VoodooUtils.focusFrame("bwc-frame");
        sugar().projects.editView.getEditField("name").assertVisible(true);
        sugar().projects.editView.getEditField("description").assertVisible(true);
        sugar().projects.editView.getEditField("date_estimated_start").assertVisible(true);
        sugar().projects.editView.getEditField("date_estimated_end").assertVisible(true);
        sugar().projects.editView.getEditField("status").assertVisible(true);
        sugar().projects.editView.getEditField("priority").assertVisible(true);
        sugar().projects.editView.getEditField("assignedTo").assertVisible(true);
        sugar().projects.editView.getEditField("teams").assertVisible(true);
        VoodooUtils.focusDefault();
        sugar().projects.editView.cancel();
        VoodooUtils.focusFrame("bwc-frame");
        sugar().projects.detailView.getDetailField("name").assertExists(true);
        sugar().projects.detailView.getDetailField("description").assertExists(true);
        sugar().projects.detailView.getDetailField("date_estimated_start").assertExists(true);
        sugar().projects.detailView.getDetailField("date_estimated_end").assertExists(true);
        sugar().projects.detailView.getDetailField("status").assertExists(true);
        sugar().projects.detailView.getDetailField("priority").assertExists(true);
        sugar().projects.detailView.getDetailField("assignedTo").assertExists(true);
        sugar().projects.detailView.getDetailField("teams").assertExists(true);
        VoodooUtils.focusDefault();

        VoodooUtils.voodoo.log.info("Completed verifyEditAndDetailFieldsInRecordView()...");
    }

    @Test
    public void verifySortBySubpanel() throws Exception {
        VoodooUtils.voodoo.log.info("Running verifySortBySubpanel()...");
        
        // Enable Projects Submodule
        sugar().admin.enableSubpanelDisplayViaJs(sugar().projects);

        sugar().accounts.create();
        sugar().accounts.listView.clickRecord(1);

        StandardSubpanel projectsSub = sugar().accounts.recordView.subpanels.get(sugar().projects.moduleNamePlural);
        projectsSub.expandSubpanel();

        VoodooUtils.waitForReady();
        Assert.assertTrue("Get Header ",!projectsSub.getHeaders().isEmpty());
        VoodooUtils.voodoo.log.info("debug" + projectsSub.getHeaders());

        projectsSub.sortBy("headerName", false);
        projectsSub.sortBy("headerEstimatedstartdate", false);
        projectsSub.sortBy("headerEstimatedenddate", false);

        projectsSub.removeHeader("name");
        Assert.assertTrue("Get Header after remove ", !projectsSub.getHeaders().contains("name"));

        VoodooUtils.voodoo.log.info("Completed verifySortBySubpanel()...");
    }

    @Ignore("VOOD-1674 Need library support for column headers control in BWCListview")
    public void verifySortBy() throws Exception {
        VoodooUtils.voodoo.log.severe("Projects/BWCListview sortBy() not implemented yet!");
    }

    @Ignore("VOOD-1674 Need library support for column headers control in BWCListview")
    public void verifyHeader() throws Exception {
        VoodooUtils.voodoo.log.info("Projects/BWCListview getHeaders() not implemented yet!");
    }

    public void cleanup() throws Exception {}
}


