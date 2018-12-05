package com.sugarcrm.test.grimoire;

import org.junit.Test;
import com.sugarcrm.sugar.VoodooUtils;
import com.sugarcrm.test.SugarTest;

public class ProductCatalogModuleTest extends SugarTest {
	public void setup() throws Exception {
		sugar().login();
		sugar().productCatalog.create();
		sugar().productCatalog.navToListView();
	}

	@Test
	public void verifyInlineRecordFieldsInListView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyInlineRecordFieldsInListView()...");

		sugar().productCatalog.listView.getDetailField(1, "name").assertVisible(true);
		sugar().productCatalog.listView.getDetailField(1, "status").assertVisible(true);
		sugar().productCatalog.listView.getDetailField(1, "costPrice").assertVisible(true);
		sugar().productCatalog.listView.getDetailField(1, "unitPrice").assertVisible(true);
		sugar().productCatalog.listView.getDetailField(1, "listPrice").assertVisible(true);
		sugar().productCatalog.listView.getDetailField(1, "stockQuantity").assertVisible(true);
		sugar().productCatalog.listView.getDetailField(1, "productCategory").assertVisible(true);

		sugar().productCatalog.listView.editRecord(1);

		sugar().productCatalog.listView.getEditField(1, "name").assertVisible(true);
		sugar().productCatalog.listView.getEditField(1, "status").assertVisible(true);
		sugar().productCatalog.listView.getEditField(1, "costPrice").assertVisible(true);
		sugar().productCatalog.listView.getEditField(1, "unitPrice").assertVisible(true);
		sugar().productCatalog.listView.getEditField(1, "listPrice").assertVisible(true);
		sugar().productCatalog.listView.getEditField(1, "stockQuantity").assertVisible(true);
		sugar().productCatalog.listView.getEditField(1, "productCategory").assertVisible(true);
		sugar().productCatalog.listView.cancelRecord(1);

		VoodooUtils.voodoo.log.info("verifyInlineRecordFieldsInListView() complete.");
	}

	@Test
	public void verifyEditAndDetailFieldsInRecordView() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyEditAndDetailFieldsInRecordView()...");

		sugar().productCatalog.listView.clickRecord(1);

		sugar().productCatalog.recordView.getDetailField("name").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("status").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("webSite").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("date_available").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("texClass").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("costPrice").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("unitPrice").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("listPrice").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("date_cost_price").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("productCategory").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("description").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("stockQuantity").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("manufacturerName").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("mftPartNumber").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("vendorPartNumber").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("weight").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("type").assertVisible(true);	
		sugar().productCatalog.recordView.getDetailField("costUSD").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("discountPriceUSD").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("listUSD").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("defaultPricingFormula").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("supportName").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("supportDesc").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("supportContact").assertVisible(true);
		sugar().productCatalog.recordView.getDetailField("supportTerm").assertVisible(true);

		sugar().productCatalog.recordView.edit();

		sugar().productCatalog.recordView.getEditField("name").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("status").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("webSite").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("date_available").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("texClass").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("costPrice").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("unitPrice").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("listPrice").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("date_cost_price").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("productCategory").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("description").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("stockQuantity").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("manufacturerName").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("mftPartNumber").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("vendorPartNumber").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("weight").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("defaultPricingFormula").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("supportName").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("supportDesc").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("supportContact").assertVisible(true);
		sugar().productCatalog.recordView.getEditField("supportTerm").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyEditAndDetailFieldsInRecordView() complete.");
	}

	@Test
	public void verifyPreviewPaneFields() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyPreviewPaneFields()...");

		// preview record
		sugar().productCatalog.listView.previewRecord(1);

		sugar().previewPane.getPreviewPaneField("name").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("status").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("webSite").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("date_available").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("texClass").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("costPrice").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("unitPrice").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("listPrice").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("date_cost_price").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("description").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("stockQuantity").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("costUSD").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("discountPriceUSD").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("listUSD").assertVisible(true);
		sugar().previewPane.getPreviewPaneField("defaultPricingFormula").assertVisible(true);

		VoodooUtils.voodoo.log.info("verifyPreviewPaneFields() complete.");
	}

	@Test
	public void verifyListHeadersWithSortIcon() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyListHeadersWithSortIcon()...");

		// Verify all sort headers in listview
		for(String header : sugar().productCatalog.listView.getHeaders()){
			sugar().productCatalog.listView.getControl(VoodooUtils.prependCamelCase("header", VoodooUtils.camelCase(header))).assertVisible(true);
		}

		VoodooUtils.voodoo.log.info("verifyListHeadersWithSortIcon() test complete.");
	}

	@Test
	public void verifyMenuItems() throws Exception {
		VoodooUtils.voodoo.log.info("Running verifyMenuItems()...");

		sugar().navbar.clickModuleDropdown(sugar().productCatalog);

		sugar().productCatalog.menu.getControl("createProduct").assertVisible(true);
		sugar().productCatalog.menu.getControl("viewProduct").assertVisible(true);
		sugar().productCatalog.menu.getControl("viewManufacturers").assertVisible(true);
		sugar().productCatalog.menu.getControl("viewProductCategories").assertVisible(true);
		sugar().productCatalog.menu.getControl("viewProductTypes").assertVisible(true);
		sugar().productCatalog.menu.getControl("importProductCatalog").assertVisible(true);

		sugar().navbar.clickModuleDropdown(sugar().productCatalog); // to close dropdown

		VoodooUtils.voodoo.log.info("verifyMenuItems() complete.");
	}

	public void cleanup() throws Exception {}
}