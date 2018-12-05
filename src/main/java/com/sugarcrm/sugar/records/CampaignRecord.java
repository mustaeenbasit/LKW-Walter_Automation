package com.sugarcrm.sugar.records;

import com.sugarcrm.candybean.datasource.FieldSet;

public class CampaignRecord extends BWCRecord {
	public CampaignRecord(FieldSet data) throws Exception {
		super(data);
		module = sugar().campaigns;
	}
} // CampaignRecord
