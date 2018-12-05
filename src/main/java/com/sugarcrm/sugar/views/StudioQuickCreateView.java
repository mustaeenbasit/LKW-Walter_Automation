package com.sugarcrm.sugar.views;

import com.sugarcrm.sugar.VoodooUtils;

public class StudioQuickCreateView extends View {
	protected static StudioQuickCreateView view;
	
	private StudioQuickCreateView() throws Exception {}
	
	public static StudioQuickCreateView getInstance() throws Exception {
		if (view == null)
			view = new StudioQuickCreateView();
		return view;
	}
}