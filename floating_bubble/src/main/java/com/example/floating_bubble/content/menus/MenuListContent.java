/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.floating_bubble.content.menus;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.example.floating_bubble.Content;
import com.example.floating_bubble.content.Navigator;
import com.example.floating_bubble.content.NavigatorContent;
import com.example.floating_bubble.content.toolbar.ToolbarNavigator;

/**
 * Implementation of {@link Content} that displays a {@link MenuItem} as a list.
 */
public class MenuListContent implements NavigatorContent {

    private static final String TAG = "MenuListNavigatorContent";

    private Menu mMenu;
    private MenuListView mMenuListView;
    private Navigator mNavigator;

    public MenuListContent(@NonNull Context context, @NonNull final Menu menu) {
        this(context, menu, null);
    }

    public MenuListContent(@NonNull Context context, @NonNull final Menu menu, @Nullable View emptyView) {
        mMenu = menu;
        mMenuListView = new MenuListView(context);
        mMenuListView.setMenu(menu);
        mMenuListView.setMenuItemSelectionListener(new MenuListView.MenuItemSelectionListener() {
            @Override
            public void onMenuItemSelected(@NonNull MenuItem menuItem) {
                menuItem.getMenuAction().execute(getView().getContext(), mNavigator);
            }
        });

        setEmptyView(emptyView);
    }

    public void setEmptyView(@Nullable View emptyView) {
        mMenuListView.setEmptyView(emptyView);
    }

    @NonNull
    @Override
    public View getView() {
        return mMenuListView;
    }

    @Override
    public void onShown(@NonNull Navigator navigator) {
        mNavigator = navigator;
        if (navigator instanceof ToolbarNavigator) {
            ((ToolbarNavigator) navigator).getToolbar().setTitle(mMenu.getTitle());
        }
    }

    @Override
    public void onHidden() {
        mNavigator = null;
    }

}
