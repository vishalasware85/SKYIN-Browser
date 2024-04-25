package com.skyinbrowser;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.IOnBackPressed;

public class About extends Fragment implements IOnBackPressed {

    private TextView txt4;
    ImageButton backBtn;
    private CardView animVisible;

    private FragmentToActivity mCallback;
    private Context mContext;
    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
        mContext = context;
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.about_activity, container, false);
        mContext = view.getContext();

        txt4=view.findViewById(R.id.abouttext4);
        backBtn=view.findViewById(R.id.aboutBack);
        animVisible=view.findViewById(R.id.aboutAnimVisible);

        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.frag_enter);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.VISIBLE);

        txt4.setText(BuildConfig.VERSION_NAME);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        return view;
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }

    @Override
    public boolean onBackPressed() {
        back();
        return true;
    }

    private void back(){
        Animation animation8= AnimationUtils.loadAnimation(mContext,R.anim.frag_exit);
        animation8.setDuration(100);
        animVisible.startAnimation(animation8);
        animVisible.setVisibility(View.GONE);
        animation8.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                sendData("AboutActivityClose");
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }
}