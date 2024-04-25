package com.skyinbrowser.bookmark;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.skyinbrowser.DatabaseAndUnits.BookmarkRecordAdapter;
import com.skyinbrowser.DatabaseAndUnits.Record;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.IOnBackPressed;
import com.skyinbrowser.R;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class BookmarkActivity extends Fragment implements IOnBackPressed {

    private static final String TAG = "BookmarkActivity";
    private ListView bookmark_listView;

    LinearLayout toolbar;
    TextView textTitle;
    ImageView btnclose_bookmark,bookmark_popup;
    public static Activity bookmarkActivity;
    Button clearAllbtn,addBokmarkBtn;
    LinearLayout popuplayout;
    private CardView mainactionview;
    private CardView animVisible;

    BookmarkRecordAdapter recordAdapter;
    List<Record> recordList;
    int location;
    RecordAction action;
    String title;
    List<Record> list;

    public static final int REQUEST_CODE =11;
    public static final int RESULT_CODE=12;

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
        view = inflater.inflate(R.layout.bookmark_activity, container, false);
        mContext = view.getContext();

        animVisible=view.findViewById(R.id.bookmarkAnimVisible);
        toolbar=view.findViewById(R.id.bookmarkToolbar);
        bookmark_listView = (ListView) view.findViewById(R.id.bookmark_listview);
        mainactionview=view.findViewById(R.id.mainBookmarkActionVew);
        popuplayout=view.findViewById(R.id.mainbookmarkpopuplayout);
        addBokmarkBtn=view.findViewById(R.id.addBookmarkFromAct);
        clearAllbtn=view.findViewById(R.id.clearAllBookmarkFromAct);

        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.frag_enter);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.VISIBLE);

        action = new RecordAction(mContext);
        action.open(false);
        list = action.listBookmarks();
        recordAdapter = new BookmarkRecordAdapter(mContext, R.layout.bookmark_content, list);
        bookmark_listView.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();

        //bookmark_listView.setEmptyView(findViewById(R.id.empty));
        btnclose_bookmark=view.findViewById(R.id.btnclose_bookmark);
        bookmark_popup=view.findViewById(R.id.bookmark_popup);

        LottieAnimationView animationView=view.findViewById(R.id.bookmarkListEmptyLottieAnim);
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
        bookmark_listView.setEmptyView(animationView);

        btnclose_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragClose("bookmarkClose");
            }
        });

        bookmark_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuplayout.setVisibility(View.VISIBLE);
                Animation righttoleeft= AnimationUtils.loadAnimation(mContext,
                        R.anim.slide_right_to_eft);
                righttoleeft.setDuration(200);
                mainactionview.startAnimation(righttoleeft);
                mainactionview.setVisibility(View.VISIBLE);
            }
        });

        addBokmarkBtn.setOnClickListener(new View.OnClickListener() {
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
                        mainactionview.setVisibility(View.GONE);
                        popuplayout.setVisibility(View.GONE);

                        String webview_full_url="";
                        Intent add_mem = new Intent(mContext, BookmarkAdderAndEdit.class);
                        add_mem.putExtra("webview_full_url",webview_full_url);
                        startActivityForResult(add_mem,REQUEST_CODE);
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
                        mainactionview.setVisibility(View.GONE);
                        popuplayout.setVisibility(View.GONE);
                        action.clearBookmarks();
                        list.removeAll(list);
                        recordAdapter.notifyDataSetChanged();
                        bookmark_listView.setAdapter(recordAdapter);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        // OnCLickListiner For List Items
        bookmark_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {


            }
        });

        bookmark_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id1) {
                recordList=list;
                location=position;
                final Record record = recordList.get(location);
                title = record.getTitle();
                String url=record.getURL();
                String time= String.valueOf(record.getTime());
                String update="update karne ke liye";

                Bundle modify_intent=new Bundle();
                modify_intent.putString("update",update);
                modify_intent.putString("title", title);
                modify_intent.putString("desc", url);
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

                DialogFragment deleteActivity=new BookmarkLongClick();
                deleteActivity.setArguments(modify_intent);
                deleteActivity.setTargetFragment(BookmarkActivity.this,250);
                deleteActivity.show(fragmentTransaction,"dialog");
                return true;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==250){
            if (resultCode==RESULT_OK){
                list.remove(location);
                recordAdapter.notifyDataSetChanged();
                bookmark_listView.setAdapter(recordAdapter);
            }
        }

        if (requestCode==REQUEST_CODE){
            if (resultCode== RESULT_CODE){
                recordAdapter.notifyDataSetChanged();
            }
        }
    }

    private void popupclose() {
        Animation lefttoright= AnimationUtils.loadAnimation(mContext,
                R.anim.slide_left_to_right);
        lefttoright.setDuration(200);
        mainactionview.startAnimation(lefttoright);
        lefttoright.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                mainactionview.setVisibility(View.GONE);
                popuplayout.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
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
        fragClose("bookmarkClose");
        return true;
    }
}
