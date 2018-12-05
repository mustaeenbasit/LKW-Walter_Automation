package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class ProductCategoriesRecord extends StandardRecord {
	public ProductCategoriesRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().productCategories;
	}
} // Product Category Record