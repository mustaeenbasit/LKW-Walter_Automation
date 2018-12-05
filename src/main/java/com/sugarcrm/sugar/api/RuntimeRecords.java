package com.sugarcrm.sugar.api;

public class RuntimeRecords {
	public String recordType;
	public String recordModule;
	public String recordName;
	public String recordValue;

	public RuntimeRecords(String v1, String v2,String v3, String v4 ) {
		recordType = v1;
		recordModule = v2;
		recordName = v3;
		recordValue = v4;
	}
}
