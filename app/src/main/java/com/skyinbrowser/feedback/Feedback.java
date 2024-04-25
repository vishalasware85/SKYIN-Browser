package com.skyinbrowser.feedback;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.CustomJavaFiles.IOnBackPressed;
import com.skyinbrowser.R;

public class Feedback extends Fragment implements IOnBackPressed {

    Button buttonSend;
    EditText textSubject;
    EditText textMessage;
    ImageButton feedbackBack;
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
        view = inflater.inflate(R.layout.feedbac_activity, container, false);
        mContext = view.getContext();

        animVisible=view.findViewById(R.id.feedbackAnimVisible);

        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.frag_enter);
        animation.setDuration(100);
        animVisible.startAnimation(animation);
        animVisible.setVisibility(View.VISIBLE);

        buttonSend = (Button)view.findViewById(R.id.buttonSend);
        textSubject = (EditText)view.findViewById(R.id.editTextSubject);
        textMessage = (EditText)view.findViewById(R.id.editTextMessage);
        feedbackBack=view.findViewById(R.id.feedbackBack);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String us="skyinbrowser@gmail.com";
                String subject = textSubject.getText().toString();
                String message = textMessage.getText().toString();
                if (subject.equals("") && message.equals("")){
                    Toast.makeText(mContext, "Please fill the subject and message.", Toast.LENGTH_SHORT).show();
                }else {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ us});
                    //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                    //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, message);
                    //need this to prompts email client only
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
            }
        });

        feedbackBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        return view;
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }

    @Override
    public boolean onBackPressed() {
        Back();
        return true;
    }

    private void Back(){
        Animation animation8= AnimationUtils.loadAnimation(mContext,R.anim.frag_exit);
        animation8.setDuration(100);
        animVisible.startAnimation(animation8);
        animVisible.setVisibility(View.GONE);
        animation8.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                sendData("FeedbackActivityClose");
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }
}