package com.skyinbrowser.bookmark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.skyinbrowser.DatabaseAndUnits.Record;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;

public class BookmarkLongClick extends DialogFragment {

    Button delete,edit,copy,open;
    TextView editText;
    private long _id;
    RecordAction action;

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
        view = inflater.inflate(R.layout.bookmark_long_click, container, false);
        mContext = view.getContext();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        delete=view.findViewById(R.id.remove_bookmark);
        edit=view.findViewById(R.id.edit_bookmark);
        copy=view.findViewById(R.id.copy_bookmark);
        open=view.findViewById(R.id.open_bookmark);
        editText=view.findViewById(R.id.urlBookmarkET);

        action = new RecordAction(mContext);
        action.open(false);

        Bundle intent = this.getArguments();
        String update=intent.getString("update");
        String title=intent.getString("title");
        String url=intent.getString("desc");
        String time = intent.getString("time");
        _id = Long.parseLong(time);

        editText.setText(url);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record record=new Record(title,url,_id);
                action.deleteBookmark(record);
                Intent intent=new Intent();
                intent.putExtra("remove","remove");
                getTargetFragment().onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,intent);

                dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modify_intent = new Intent(mContext, BookmarkAdderAndEdit.class);
                modify_intent.putExtra("update",update);
                modify_intent.putExtra("title", title);
                modify_intent.putExtra("url", url);
                modify_intent.putExtra("time", time);
                startActivity(modify_intent);
                dismiss();
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String copytxt=editText.getText().toString();
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                            mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(copytxt);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                            mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("url", copytxt);
                    clipboard.setPrimaryClip(clip);
                }
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String copytxt=editText.getText().toString();

                Bundle bundle=getArguments();
                if (bundle != null){
                    if (bundle.containsKey("opened settings")){
                        new java.util.Timer().schedule(
                                new java.util.TimerTask(){
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                sendData("bookmarkCloseWithFragAndSett");
                                                dismiss();
                                            }
                                        });
                                    }
                                },
                                100
                        );
                    }else {
                        new java.util.Timer().schedule(
                                new java.util.TimerTask(){
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                sendData("bookmarkCloseWithFrag");
                                                dismiss();
                                            }
                                        });
                                    }
                                },
                                100
                        );
                    }
                }
                sendData(copytxt);
            }
        });

        return view;
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }
}
