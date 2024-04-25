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
import com.skyinbrowser.DatabaseAndUnits.HistoryRecordAdapter;
import com.skyinbrowser.DatabaseAndUnits.Record;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.IOnBackPressed;
import com.skyinbrowser.R;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FloatHistory extends Fragment implements IOnBackPressed {

    Button openmainhistory,opensplithistory,clearAllbtn;
    ImageView menubtn,close;
    CardView mainActionView,animVisible;
    LinearLayout popupLayout,toolbar;
    private ListView floatListView;
    Intent returnintent = new Intent();

    HistoryRecordAdapter recordAdapter;
    List<Record> recordList;
    int location;
    RecordAction action;
    String title;
    List<Record> list;

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
        view = inflater.inflate(R.layout.float_history, container, false);
        mContext = view.getContext();

        openmainhistory=view.findViewById(R.id.openmainhistoryfromfloat);
        opensplithistory=view.findViewById(R.id.opensplithistoryfromfloat);
        menubtn=view.findViewById(R.id.floatHistoryPopup);
        popupLayout=view.findViewById(R.id.floathistorypopuplayout);
        mainActionView=view.findViewById(R.id.floatHistoryActionView);
        clearAllbtn=view.findViewById(R.id.clearAllFloatHistory);
        floatListView = (ListView) view.findViewById(R.id.floatlistView);
        animVisible=view.findViewById(R.id.floatMainBG);
        toolbar=view.findViewById(R.id.floatToolbar);
        close=view.findViewById(R.id.closeFloatHistory);

        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.frag_enter);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.VISIBLE);

        action = new RecordAction(mContext);
        action.open(false);
        list = action.listFloatHistory();
        recordAdapter = new HistoryRecordAdapter(mContext, R.layout.new_hiatory_list_layout, list);
        floatListView.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();

        LottieAnimationView animationView=view.findViewById(R.id.floatHistoryListEmptyLottieAnim);
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
        floatListView.setEmptyView(animationView);

        //set an onItemClickListener to the ListView
        floatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override public void run() { getActivity().runOnUiThread(new Runnable() {
                        @Override public void run() {
                            title = list.get(i).getURL();

                            fragClose("FloatHistoryCloseWithFrag");
                            sendData(title);

                            Bundle bundle=getArguments();
                            if (bundle != null){
                                if (bundle.containsKey("opened settings")){
                                    fragClose("FloatHistoryCloseWithFragAndSetting");
                                }
                            }
                        }}); }},100);
            }
        });

        floatListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id1) {
                recordList=list;
                location=position;
                final Record record = recordList.get(location);
                title = record.getTitle();
                String url=record.getURL();
                String time= String.valueOf(record.getTime());

                Bundle modify_intent = new Bundle();
                modify_intent.putString("floatHistory","floatHistory");
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
                deleteActivity.setTargetFragment(FloatHistory.this,11);
                deleteActivity.show(fragmentTransaction,"dialog");
                return true;
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragClose("FloatHistoryClose");
            }
        });

        menubtn.setOnClickListener(new View.OnClickListener() {
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

        opensplithistory.setOnClickListener(new View.OnClickListener() {
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

        openmainhistory.setOnClickListener(new View.OnClickListener() {
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

        clearAllbtn.setOnClickListener(new View.OnClickListener() {
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
                        popupLayout.setVisibility(View.GONE);
                        action.clearFloatHistory();
                        list.removeAll(list);
                        recordAdapter.notifyDataSetChanged();
                        floatListView.setAdapter(recordAdapter);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        popupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation lefttoright= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_left_to_right);
                lefttoright.setDuration(200);
                mainActionView.startAnimation(lefttoright);
                lefttoright.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mainActionView.setVisibility(View.GONE);

                        new java.util.Timer().schedule(
                                new java.util.TimerTask(){
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                popupLayout.setVisibility(View.GONE);
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
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==11){
            if (resultCode==RESULT_OK){
                list.remove(location);
                recordAdapter.notifyDataSetChanged();
                floatListView.setAdapter(recordAdapter);
            }
        }
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
        fragClose("FloatHistoryClose");
        return true;
    }
}