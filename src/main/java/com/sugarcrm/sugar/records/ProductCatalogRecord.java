package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class ProductCatalogRecord extends StandardRecord {
	public ProductCatalogRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().productCatalog;
	}
} // Product Catalog Record
