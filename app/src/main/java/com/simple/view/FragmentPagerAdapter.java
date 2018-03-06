/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.simple.view;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentManager;

public class FragmentPagerAdapter extends
		android.support.v13.app.FragmentPagerAdapter {

	private ArrayList<Fragment> mFragmentList = null;

	public FragmentPagerAdapter(FragmentManager mFragmentManager,
			ArrayList<Fragment> fragmentList) {
		super(mFragmentManager);
		mFragmentList = fragmentList;
	}

	private int count = 0;
	public void setConut(int count){
		this.count = count;
	}
	
	@Override
	public int getCount() {
		return count == 0 ? mFragmentList.size() : count ;
	}

	@Override
	public Fragment getItem(int position) {

		Fragment fragment = null;
		if (position < mFragmentList.size()) {
			fragment = mFragmentList.get(position);
		} else {
			fragment = mFragmentList.get(0);
		}
		return fragment;

	}
}