package com.skyinbrowser.Floating;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.floating_bubble.Content;
import com.example.floating_bubble.HoverMenu;
import com.example.floating_bubble.HoverView;
import com.example.floating_bubble.window.HoverMenuService;
import com.skyinbrowser.LanguageSelector;
import com.skyinbrowser.R;
import com.skyinbrowser.splash;
import com.skyinbrowser.tabs.TabsMain;

import java.util.Collections;
import java.util.List;

import static com.skyinbrowser.Floating.App.CHANNEL_ID;

public class SingleSectionNotificationHoverMenuService extends HoverMenuService {

    private static final String TAG = "SingleSectionNotificationHoverMenuService";

    @Override
    protected int getForegroundNotificationId() {
        return 1000;
    }

    @Nullable
    @Override
    protected Notification getForegroundNotification() {
        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(com.example.floating_bubble.R.drawable.tab_background)
                .setContentTitle("SKY!N Floating Mode")
                .setContentText("Floating Mode is running in a foreground.")
                .build();

        startForeground(1,notification);
        return notification;
    }

    @Override
    protected void onHoverMenuLaunched(@NonNull Intent intent, @NonNull HoverView hoverView) {
        hoverView.setMenu(createHoverMenu());
        hoverView.collapse();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hoverView.expand();
            }
        }, 100);
    }

    @NonNull
    private HoverMenu createHoverMenu() {
        return new SingleSectionHoverMenu(getApplicationContext());
    }

    private static class SingleSectionHoverMenu extends HoverMenu {

        private Context mContext;
        private Section mSection,mSection2;

        private SingleSectionHoverMenu(@NonNull Context context) {
            mContext = context;

            mSection = new Section(
                    new SectionId("1"),
                    createTabView(),
                    createScreen()
            );
        }

        private View createTabView() {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(com.example.floating_bubble.R.drawable.tab_background);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            return imageView;
        }

        private Content createScreen() {
            return new HoverMenuScreen(mContext, "SKY!N Floating Mode");
        }

        @Override
        public String getId() {
            return "singlesectionmenu_foreground";
        }

        @Override
        public int getSectionCount() {
            return 1;
        }

        @Nullable
        @Override
        public Section getSection(int index) {
            if (0 == index) {
                return mSection;
            } else {
                return null;
            }
        }

        @Nullable
        @Override
        public Section getSection(@NonNull SectionId sectionId) {
            if (sectionId.equals(mSection.getId())) {
                return mSection;
            } else {
                return null;
            }
        }

        @NonNull
        @Override
        public List<Section> getSections() {
            return Collections.singletonList(mSection);
        }
    }
}
