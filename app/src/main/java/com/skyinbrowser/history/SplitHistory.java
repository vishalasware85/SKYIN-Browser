package com.skyinbrowser.history;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.lguipeng.library.animcheckbox.AnimCheckBox;
import com.skyinbrowser.DatabaseAndUnits.HistoryRecordAdapter;
import com.skyinbrowser.DatabaseAndUnits.Record;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.IOnBackPressed;
import com.skyinbrowser.R;

import java.util.List;

public class SplitHistory extends Fragment implements IOnBackPressed {
    CardView mainActionView,popupLayout,splitClearLayout,mainBG;
    Button upperHistory,lowerHistory,openFloatHistory,openMainHistory,clearAllLayout,clearAllbtn;
    ImageView mainMenu,closeBtn;
    AnimCheckBox upperCheck,lowerCheck;
    TextView clearUppertxt,clearLowertext;
    LinearLayout toolbar;

    HistoryRecordAdapter upperrecordAdapter,lowerrecordAdapter;
    List<Record> upperrecordList,lowerrecordList;
    int upperlocation,lowerlocation;
    RecordAction action;
    String uppertitle,lowertitle;
    List<Record> upperlist;
    List<Record> lowerlist;

    Intent returnintent = new Intent();
    private ListView upperListView,lowerListView;

    public static Activity splitHistory;

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

    public static HistoryActivity getInstance(){
        HistoryActivity frag=new HistoryActivity();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.split_history, container, false);
        mContext = view.getContext();

        upperHistory=view.findViewById(R.id.upperSplitHistorybtn);
        lowerHistory=view.findViewById(R.id.lowerSplitHistoryBtn);
        mainMenu=view.findViewById(R.id.splithistorypopupbtn);
        closeBtn=view.findViewById(R.id.closesplithistory);
        upperListView=view.findViewById(R.id.uppersplitlistView);
        lowerListView=view.findViewById(R.id.lowersplitlistView);
        popupLayout=view.findViewById(R.id.splithistorypopuplayout);
        mainActionView=view.findViewById(R.id.splitHistoryActionView);
        openFloatHistory=view.findViewById(R.id.openfloathistoryfromsplit);
        openMainHistory=view.findViewById(R.id.openmainhistoryfromsplit);
        clearAllLayout=view.findViewById(R.id.clearAllSplitHistory);
        splitClearLayout=view.findViewById(R.id.splitClearLayout);
        clearAllbtn=view.findViewById(R.id.splitclearBtn);
        upperCheck=view.findViewById(R.id.upperHisChecked);
        lowerCheck=view.findViewById(R.id.lowerHisChecked);
        clearUppertxt=view.findViewById(R.id.tvUpperSplHisCheck);
        clearLowertext=view.findViewById(R.id.tvLowerSplHisCheck);
        mainBG=view.findViewById(R.id.splitmainBG);
        toolbar=view.findViewById(R.id.splitToolbar);

        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.frag_enter);
        animation.setDuration(100);
        mainBG.startAnimation(animation);
        mainBG.setVisibility(View.VISIBLE);

        action = new RecordAction(mContext);
        action.open(false);

        upperlist = action.listUpperSplitHistory();
        upperrecordAdapter = new HistoryRecordAdapter(mContext, R.layout.new_hiatory_list_layout, upperlist);
        upperListView.setAdapter(upperrecordAdapter);
        upperrecordAdapter.notifyDataSetChanged();

        // OnCLickListiner For List Items
        upperListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override public void run() { getActivity().runOnUiThread(new Runnable() {
                        @Override public void run() {
                            uppertitle = upperlist.get(position).getURL();
                            fragClose("SplitHistoryCloseWithFrag");
                            sendData(uppertitle);

                            Bundle bundle=getArguments();
                            if (bundle != null){
                                if (bundle.containsKey("opened settings")){
                                    fragClose("SplitHistoryCloseWithFragAndSetting");
                                }
                            }
                        }}); }},100);
            }
        });

        upperListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id1) {
                upperrecordList=upperlist;
                upperlocation=position;
                final Record record = upperrecordList.get(upperlocation);
                uppertitle = record.getTitle();
                String url=record.getURL();
                String time= String.valueOf(record.getTime());

                Bundle modify_intent = new Bundle();
                modify_intent.putString("splitHistoryUpper","splitHistoryUpper");
                modify_intent.putString("title", uppertitle);
                modify_intent.putString("url", url);
                modify_intent.putString("time", time);

                Bundle bundle=getArguments();
                if (bundle != null){
                    if (bundle.containsKey("opened settings")){
                        modify_intent.putString("opened settings", "opened settings");
                    }
                }

                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                Fragment prev=getFragmentManager().findFragmentByTag("dialog");
                if (prev != null){
                    fragmentTransaction.remove(prev);
                }
                fragmentTransaction.addToBackStack(null);

                DialogFragment deleteActivity=new DeleteActivity();
                deleteActivity.setArguments(modify_intent);
                deleteActivity.setTargetFragment(SplitHistory.this,11);
                deleteActivity.show(fragmentTransaction,"dialog");

                return true;
            }
        });

        lowerlist = action.listLowerSplitHistory();
        lowerrecordAdapter = new HistoryRecordAdapter(mContext, R.layout.new_hiatory_list_layout, lowerlist);
        lowerListView.setAdapter(lowerrecordAdapter);
        lowerrecordAdapter.notifyDataSetChanged();

        // OnCLickListiner For List Items
        lowerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override public void run() { getActivity().runOnUiThread(new Runnable() {
                        @Override public void run() {
                            lowertitle = lowerlist.get(position).getURL();

                            fragClose("SplitHistoryCloseWithFrag");
                            sendData(lowertitle);

                            Bundle bundle=getArguments();
                            if (bundle != null){
                                if (bundle.containsKey("opened settings")){
                                    fragClose("SplitHistoryCloseWithFragAndSetting");
                                }
                            }
                        }}); }},100);
            }
        });

        lowerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id1) {
                lowerrecordList=lowerlist;
                lowerlocation=position;
                final Record record = lowerrecordList.get(lowerlocation);
                lowertitle = record.getTitle();
                String url=record.getURL();
                String time= String.valueOf(record.getTime());

                Bundle modify_intent = new Bundle();
                modify_intent.putString("splitHistoryLower","splitHistoryLower");
                modify_intent.putString("title", lowertitle);
                modify_intent.putString("url", url);
                modify_intent.putString("time", time);

                Bundle bundle=getArguments();
                if (bundle != null){
                    if (bundle.containsKey("opened settings")){
                        modify_intent.putString("opened settings", "opened settings");
                    }
                }

                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                Fragment prev=getFragmentManager().findFragmentByTag("dialog");
                if (prev != null){
                    fragmentTransaction.remove(prev);
                }
                fragmentTransaction.addToBackStack(null);

                DialogFragment deleteActivity=new DeleteActivity();
                deleteActivity.setArguments(modify_intent);
                deleteActivity.setTargetFragment(SplitHistory.this,12);
                deleteActivity.show(fragmentTransaction,"dialog");
                return true;
            }
        });

        LottieAnimationView animationView=view.findViewById(R.id.splitHistoryListEmptyLottieAnim);
        animationView.setAnimation(R.raw.empty_state);
        animationView.playAnimation();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) { }
            @Override
            public void onAnimationEnd(Animator animator) {
                animationView.setMinAndMaxFrame(110,175);
                animationView.reverseAnimationSpeed();
                animationView.playAnimation();
            }
            @Override public void onAnimationCancel(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) { }
        });
        upperListView.setEmptyView(animationView);
        lowerListView.setEmptyView(animationView);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragClose("SplitHistoryClose");
            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupLayout.setVisibility(View.VISIBLE);
                new java.util.Timer().schedule(
                        new java.util.TimerTask(){
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Animation righttoleeft= AnimationUtils.loadAnimation(mContext,
                                                R.anim.slide_right_to_eft);
                                        righttoleeft.setDuration(200);
                                        mainActionView.startAnimation(righttoleeft);
                                        mainActionView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        },
                        100
                );
            }
        });


        openFloatHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation lefttoright= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right);
                mainActionView.startAnimation(lefttoright);
                lefttoright.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mainActionView.setVisibility(View.GONE);
                        Bundle recivedintent = getArguments();
                        if (recivedintent != null){
                            if (recivedintent.containsKey("opened settings")){
                                recivedintent.putString("opened settings","opened settings");
                                fragClose("OpenFloatHistoryWithSettings");
                            }
                        }else {
                            fragClose("OpenFloatHistoryWithoutSettings");
                        }
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });


        openMainHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation lefttoright= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right);
                mainActionView.startAnimation(lefttoright);
                lefttoright.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mainActionView.setVisibility(View.GONE);

                        Bundle recivedintent = getArguments();
                        if (recivedintent != null){
                            if (recivedintent.containsKey("opened settings")){
                                recivedintent.putString("opened settings","opened settings");
                                fragClose("historyActivity");
                            }
                        } else {
                            fragClose("historyActivity");
                        }
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        clearAllLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (splitClearLayout.getVisibility()==View.GONE){
                    Animation animation= AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_right_to_eft);
                    splitClearLayout.startAnimation(animation);
                    splitClearLayout.setVisibility(View.VISIBLE);
                }else {
                    Animation animation= AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_left_to_right);
                    splitClearLayout.startAnimation(animation);
                    splitClearLayout.setVisibility(View.GONE);
                }
            }
        });

        clearUppertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean animation = true;
                if (upperCheck.isChecked()){
                    upperCheck.setChecked(false, animation);
                }else {
                    upperCheck.setChecked(true,animation);
                }
            }
        });

        clearLowertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean animation = true;
                if (lowerCheck.isChecked()){
                    lowerCheck.setChecked(false,animation);
                }else {
                    lowerCheck.setChecked(true,animation);
                }
            }
        });

        clearAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upperCheck.isChecked()){
                    Animation animation= AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_left_to_right);
                    splitClearLayout.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            splitClearLayout.setVisibility(View.GONE);
                            Animation animation1= AnimationUtils.loadAnimation(mContext,
                                    R.anim.slide_left_to_right);
                            mainActionView.startAnimation(animation1);

                            animation1.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) { }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    mainActionView.setVisibility(View.GONE);
                                    popupLayout.setVisibility(View.GONE);
                                    action.clearUpperSplitHistory();
                                    upperlist.removeAll(upperlist);
                                    upperrecordAdapter.notifyDataSetChanged();
                                    upperListView.setAdapter(upperrecordAdapter);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) { }
                            });
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }else { }

                if (lowerCheck.isChecked()){
                    Animation animation= AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_left_to_right);
                    splitClearLayout.startAnimation(animation);

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            splitClearLayout.setVisibility(View.GONE);
                            Animation animation1= AnimationUtils.loadAnimation(mContext,
                                    R.anim.slide_left_to_right);
                            mainActionView.startAnimation(animation1);


                            animation1.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) { }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    mainActionView.setVisibility(View.GONE);
                                    popupLayout.setVisibility(View.GONE);
                                    action.clearLowerSplitHistory();
                                    lowerlist.removeAll(lowerlist);
                                    lowerrecordAdapter.notifyDataSetChanged();
                                    lowerListView.setAdapter(lowerrecordAdapter);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) { }
                            });
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }else { }

                if (upperCheck.isChecked() && lowerCheck.isChecked()){
                    Animation animation= AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_left_to_right);
                    splitClearLayout.startAnimation(animation);

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            splitClearLayout.setVisibility(View.GONE);
                            Animation animation1= AnimationUtils.loadAnimation(mContext,
                                    R.anim.slide_left_to_right);
                            mainActionView.startAnimation(animation1);

                            animation1.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) { }
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    mainActionView.setVisibility(View.GONE);
                                    popupLayout.setVisibility(View.GONE);
                                    action.clearUpperSplitHistory();
                                    upperlist.removeAll(upperlist);
                                    upperrecordAdapter.notifyDataSetChanged();
                                    upperListView.setAdapter(upperrecordAdapter);
                                    action.clearLowerSplitHistory();
                                    lowerlist.removeAll(lowerlist);
                                    lowerrecordAdapter.notifyDataSetChanged();
                                    lowerListView.setAdapter(lowerrecordAdapter);
                                }
                                @Override
                                public void onAnimationRepeat(Animation animation) { }
                            });
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }
            }
        });

        upperHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowerHistory.setTextColor(Color.parseColor("#ADFFFFFF"));
                upperHistory.setTextColor(Color.parseColor("#FFFFFFFF"));

                if (upperListView.getVisibility()==View.GONE){
                    Animation animation= AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_left_to_right);
                    animation.setDuration(300);
                    lowerListView.startAnimation(animation);
                    lowerListView.setVisibility(View.GONE);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation animation1= AnimationUtils.loadAnimation(mContext,
                                    R.anim.slide_left_to_right_visible);
                            upperListView.startAnimation(animation1);
                            upperListView.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }else {}
            }
        });

        lowerHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upperHistory.setTextColor(Color.parseColor("#ADFFFFFF"));
                lowerHistory.setTextColor(Color.parseColor("#FFFFFFFF"));

                if (lowerListView.getVisibility()==View.GONE){
                    Animation animation= AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_right_to_left_gone);
                    animation.setDuration(300);
                    upperListView.startAnimation(animation);
                    upperListView.setVisibility(View.GONE);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation animation1= AnimationUtils.loadAnimation(mContext,
                                    R.anim.slide_right_to_eft);
                            animation1.setDuration(300);
                            lowerListView.startAnimation(animation1);
                            lowerListView.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }
            }
        });

        return view;
    }

    public void poopupClose(View view) {
        if (splitClearLayout.getVisibility()==View.VISIBLE){
            Animation animation= AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_left_to_right);
            splitClearLayout.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {
                    splitClearLayout.setVisibility(View.GONE);
                    Animation animation1= AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_left_to_right);
                    mainActionView.startAnimation(animation1);

                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) { }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mainActionView.setVisibility(View.GONE);
                            popupLayout.setVisibility(View.GONE);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) { }
                    });
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        }else {
            Animation animation1= AnimationUtils.loadAnimation(mContext,
                    R.anim.slide_left_to_right);
            mainActionView.startAnimation(animation1);

            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {
                    mainActionView.setVisibility(View.GONE);
                    popupLayout.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==11&&resultCode==Activity.RESULT_OK){
            upperlist.remove(upperlocation);
            upperrecordAdapter.notifyDataSetChanged();
            upperListView.setAdapter(upperrecordAdapter);
        }

        if (requestCode==12&&resultCode==Activity.RESULT_OK){
            lowerlist.remove(lowerlocation);
            lowerrecordAdapter.notifyDataSetChanged();
            lowerListView.setAdapter(lowerrecordAdapter);
        }
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }

    private void fragClose(String string){
        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.frag_exit);
        animation.setDuration(100);
        mainBG.startAnimation(animation);
        mainBG.setVisibility(View.GONE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                sendData(string);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    @Override
    public boolean onBackPressed() {
        fragClose("SplitHistoryClose");
        return true;
    }
}
