package com.sugarcrm.sugar.views;

public interface IListView {
	String getModuleTitle() throws Exception;
	void verifyModuleTitle(String toVerify) throws Exception;
	public void sortBy(String header, boolean ascending) throws Exception;
	public void verifyField(int row, String field, String value) throws Exception;
	public void toggleSelectAll() throws Exception;
	public void checkRecord(int rowNum) throws Exception;
	public void uncheckRecord(int rowNum) throws Exception;
	public void toggleRecordCheckbox(int rowNum) throws Exception;
	public void toggleFavorite(int rowNum) throws Exception;
	public void clickRecord(int rowNum) throws Exception;
	public void editRecord(int rowNum) throws Exception;
	public void deleteRecord(int rowNum) throws Exception;
}
