package com.skyinbrowser.CustomJavaFiles;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class TabScrollView extends HorizontalScrollView {

    private Runnable scrollTask;
    private int initialPosition;

    private int newCheck=100;
    private static  final String TAG="MyScrollView";

    public interface onScrollStoppedListener{
        void onScrollStopped();
    }

    private onScrollStoppedListener onScrollStoppedListener;

    public TabScrollView(Context context, AttributeSet attrs){
        super(context,attrs);

        scrollTask=new Runnable() {
            @Override
            public void run() {
                int newPosition=getScrollX();
                if (initialPosition-newPosition==0){
                    if (onScrollStoppedListener!=null){
                        onScrollStoppedListener.onScrollStopped();
                    }
                }else {
                    initialPosition=getScrollX();
                    TabScrollView.this.postDelayed(scrollTask,newCheck);
                }
            }
        };
    }

    public void setOnScrollStoppedListener(TabScrollView.onScrollStoppedListener listener){
        onScrollStoppedListener=listener;
    }

    public void startScrollerTask(){
        initialPosition =getScrollX();
        TabScrollView.this.postDelayed(scrollTask,newCheck);
    }
}
