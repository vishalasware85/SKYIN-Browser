package com.skyinbrowser.history;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.airbnb.lottie.LottieAnimationView;
import com.skyinbrowser.DatabaseAndUnits.Record;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.DatabaseAndUnits.HistoryRecordAdapter;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.IOnBackPressed;
import com.skyinbrowser.R;

import java.util.List;

public class HistoryActivity extends Fragment implements IOnBackPressed {

    ImageView closebtn,mainhistory_menu;
    Button openfloathistory,opensplithistory,clearAllbtn;
    LinearLayout mainHistoryList,popuplayout,toolbar;

    CardView mainactionview,animVisible;
    //for main history
    HistoryRecordAdapter recordAdapter;
    List<Record> recordList;
    int location;
    RecordAction action;
    List<Record> list;

    private ListView mListView;

    String title;
    public static Activity mainHistory;

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
        view = inflater.inflate(R.layout.history_activity, container, false);
        mContext = view.getContext();

        closebtn=view.findViewById(R.id.closeHistory11);
        mainHistoryList=view.findViewById(R.id.mainHistoryList);
        mainactionview=view.findViewById(R.id.mainHistoryActionView);
        mListView = (ListView) view.findViewById(R.id.listView);
        mainhistory_menu=view.findViewById(R.id.history_popup);
        popuplayout=view.findViewById(R.id.mainhistorypopuplayout);
        openfloathistory=view.findViewById(R.id.openfloathistoryfrommain);
        opensplithistory=view.findViewById(R.id.opensplithistoryfrommain);
        clearAllbtn=view.findViewById(R.id.clearallmainhistory);
        animVisible=view.findViewById(R.id.mainhistoryAnimVisible);
        toolbar=view.findViewById(R.id.historyToolbar);

        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.frag_enter);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.VISIBLE);

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fragClose("MainHistoryClose");
            }
        });

        action = new RecordAction(mContext);
        action.open(false);

        list = action.listHistory();
        recordAdapter = new HistoryRecordAdapter(mContext, R.layout.new_hiatory_list_layout, list);
        mListView.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();

        LottieAnimationView animationView=view.findViewById(R.id.historyListEmptyLottieAnim);
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
        mListView.setEmptyView(animationView);

        // OnCLickListiner For List Items
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override public void run() { getActivity().runOnUiThread(new Runnable() {
                        @Override public void run() {
                            title = list.get(position).getURL();

                            fragClose("MainHistoryCloseWithFrag");
                            sendData(title);

                            Bundle bundle=getArguments();
                            if (bundle != null){
                                if (bundle.containsKey("opened settings")){
                                    fragClose("MainHistoryCloseWithFragAndSetting");
                                }
                            }
                        }}); }},100);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id1) {
                recordList=list;
                location=position;
                final Record record = recordList.get(location);
                title = record.getTitle();
                String url=record.getURL();
                String time= String.valueOf(record.getTime());

                Bundle modify_intent=new Bundle();
                modify_intent.putString("mainHistory","mainHistory");
                modify_intent.putString("title", title);
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
                deleteActivity.setTargetFragment(HistoryActivity.this,11);
                deleteActivity.show(fragmentTransaction,"dialog");
                return true;
            }
        });

        mainhistory_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuplayout.setVisibility(View.VISIBLE);
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
                                        mainactionview.startAnimation(righttoleeft);
                                        mainactionview.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        },
                        100
                );


            }
        });



        openfloathistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation lefttoright= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right);
                mainactionview.startAnimation(lefttoright);
                lefttoright.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mainactionview=view.findViewById(R.id.mainHistoryActionView);
                        popuplayout=view.findViewById(R.id.mainhistorypopuplayout);
                        mainactionview.setVisibility(View.GONE);

                        popuplayout.setVisibility(View.GONE);
                        Bundle bundle=getArguments();
                        if (bundle != null){
                            if (bundle.containsKey("opened settings")){
                                bundle.putString("opened settings", "opened settings");
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

        opensplithistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation lefttoright= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right);
                mainactionview.startAnimation(lefttoright);
                lefttoright.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mainactionview=view.findViewById(R.id.mainHistoryActionView);
                        popuplayout=view.findViewById(R.id.mainhistorypopuplayout);
                        mainactionview.setVisibility(View.GONE);
                        Bundle recivedintent = getArguments();
                        if (recivedintent != null){
                            if (recivedintent.containsKey("opened settings")){
                                recivedintent.putString("opened settings","opened settings");
                                fragClose("OpenSplitHistoryWithSettings");
                            }
                        }else {
                            fragClose("OpenSplitHistoryWithoutSettings");
                        }
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        clearAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation lefttoright= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right);
                mainactionview.startAnimation(lefttoright);
                lefttoright.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mainactionview=view.findViewById(R.id.mainHistoryActionView);
                        popuplayout=view.findViewById(R.id.mainhistorypopuplayout);
                        mainactionview.setVisibility(View.GONE);
                        popuplayout.setVisibility(View.GONE);
                        action.clearHistory();
                        list.removeAll(list);
                        recordAdapter.notifyDataSetChanged();
                        mListView.setAdapter(recordAdapter);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        popuplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupclose();
            }
        });

        return view;
    }

    public void popupclose() {
        Animation lefttoright= AnimationUtils.loadAnimation(mContext,
                R.anim.slide_left_to_right);
        lefttoright.setDuration(200);
        mainactionview.startAnimation(lefttoright);
        lefttoright.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                mainactionview=view.findViewById(R.id.mainHistoryActionView);
                popuplayout=view.findViewById(R.id.mainhistorypopuplayout);
                mainactionview.setVisibility(View.GONE);

                new java.util.Timer().schedule(
                        new java.util.TimerTask(){
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        popuplayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        },
                        100
                );

            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==11){
            if (resultCode==Activity.RESULT_OK){
                String string=data.getStringExtra("remove");
                if (string != null && string.equals("remove")){
                    list.remove(location);
                    recordAdapter.notifyDataSetChanged();
                    mListView.setAdapter(recordAdapter);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }

    private void fragClose(String string){
        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.frag_exit);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.GONE);
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
        fragClose("MainHistoryClose");
        return true;
    }
}