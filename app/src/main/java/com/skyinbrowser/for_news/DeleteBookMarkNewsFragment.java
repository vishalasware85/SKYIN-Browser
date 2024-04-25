package com.skyinbrowser.for_news;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.skyinbrowser.DatabaseAndUnits.NewsBookmarkRecord;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;

import java.io.File;

public class DeleteBookMarkNewsFragment extends DialogFragment {

    private Button removebtn,share;
    private TextView title;

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
        view = inflater.inflate(R.layout.activity_news_bookmark_delete, container, false);
        mContext = view.getContext();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        title=view.findViewById(R.id.titleNewsBookmarkDelete);
        removebtn=view.findViewById(R.id.removeNewsBookmarkBtn);
        share=view.findViewById(R.id.shareNewsBookmarkBtn);

        Bundle receivedIntent=this.getArguments();
        if (receivedIntent != null) {
            if (receivedIntent.containsKey("url")) {
                String url=receivedIntent.getString("url");
                String titleTxt=receivedIntent.getString("title");
                String fileName=receivedIntent.getString("fileName");

                title.setText(titleTxt);

                RecordAction action = new RecordAction(mContext);
                action.open(false);
                removebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NewsBookmarkRecord record=new NewsBookmarkRecord("",url,"","","","",0);
                        action.deleteNewsBookmark(record);
                        if (receivedIntent.containsKey("englishNews")) {
                            getFilePath(fileName);
                        }
                        sendData("remove");
                        dismiss();
                    }
                });

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title=receivedIntent.getString("title");
                        String mUrl=receivedIntent.getString("url");
                        try{
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plan");
                            String body = title + "\n" + mUrl + "\n" + "\n" + "Shared from the SK!YN Browser" + "\n";
                            i.putExtra(Intent.EXTRA_TEXT, body);
                            startActivity(Intent.createChooser(i, "On the way you are"));
                            dismiss();
                        }catch (Exception e){
                            Toast.makeText(mContext, "Hmm.. Sorry, \nCannot be share", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        return view;
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        sendData("dismiss");
        super.onDismiss(dialog);
    }

    private static boolean getFilePath(String filename1) {
        File dirPath=new File(Environment.getExternalStorageDirectory()+"/SKY!N Browser");
        File Saved_Pages=new File(dirPath,"/.SavedPages");
        return (new File(Saved_Pages,filename1).delete());
    }
}