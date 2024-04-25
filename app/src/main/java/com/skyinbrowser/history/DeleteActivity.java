package com.skyinbrowser.history;

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
import android.widget.Toast;

import com.skyinbrowser.DatabaseAndUnits.Record;
import com.skyinbrowser.DatabaseAndUnits.RecordAction;
import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;
import com.skyinbrowser.bookmark.BookmarkAdderAndEdit;

public class DeleteActivity extends DialogFragment {

    Button removebtn,addToBookmarks,copy;
    TextView editText;

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
        view = inflater.inflate(R.layout.activity_history_delete, container, false);
        mContext = view.getContext();

        if (getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        removebtn=view.findViewById(R.id.removeHistory);
        addToBookmarks=view.findViewById(R.id.addtobookmarksHistory);
        copy=view.findViewById(R.id.copy_history);
        editText=view.findViewById(R.id.urlHistoryET);

        Bundle receivedIntent=this.getArguments();

        if (receivedIntent != null){
            if (receivedIntent.containsKey("mainHistory")){
                String selectedName = receivedIntent.getString("title");
                String url=receivedIntent.getString("url");
                String time=receivedIntent.getString("time");
                RecordAction action = new RecordAction(mContext);
                action.open(false);
                long timel=Long.parseLong(time);

                editText.setText(url);

                removebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Record record=new Record("","",timel);
                        action.deleteHistory(record);

                        Intent intent=new Intent();
                        intent.putExtra("remove","remove");
                        getTargetFragment().onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,intent);

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

                addToBookmarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bookmarkIntent = new Intent(mContext, BookmarkAdderAndEdit.class);
                        bookmarkIntent.putExtra("webview_full_url", url);
                        startActivity(bookmarkIntent);
                    }
                });
            }
        }


        if (receivedIntent != null){
            if (receivedIntent.containsKey("floatHistory")){
                String selectedName = receivedIntent.getString("title");
                String url=receivedIntent.getString("url");
                String time=receivedIntent.getString("time");
                RecordAction action = new RecordAction(mContext);
                action.open(false);
                long timel=Long.parseLong(time);

                editText.setText(url);

                removebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Record record=new Record(selectedName,url,timel);
                        action.deleteFloatHistory(record);

                        Intent intent=new Intent();
                        intent.putExtra("remove","remove");
                        getTargetFragment().onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,intent);

                        new java.util.Timer().schedule(
                                new java.util.TimerTask(){
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismiss();
                                            }
                                        });
                                    }
                                },
                                100
                        );
                    }
                });

                addToBookmarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bookmarkIntent = new Intent(mContext, BookmarkAdderAndEdit.class);
                        bookmarkIntent.putExtra("webview_full_url", url);
                        startActivity(bookmarkIntent);
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
            }
        }


        if (receivedIntent != null){
            if (receivedIntent.containsKey("splitHistoryUpper")){
                String selectedName = receivedIntent.getString("title");
                String url=receivedIntent.getString("url");
                String time=receivedIntent.getString("time");
                RecordAction action = new RecordAction(mContext);
                action.open(false);
                long timel=Long.parseLong(time);

                editText.setText(url);

                removebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Record record=new Record(selectedName,url,timel);
                        action.deleteUpperSplitHistory(record);

                        Intent intent=new Intent();
                        intent.putExtra("remove","remove");
                        getTargetFragment().onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,intent);

                        new java.util.Timer().schedule(
                                new java.util.TimerTask(){
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismiss();
                                            }
                                        });
                                    }
                                },
                                100
                        );
                    }
                });

                addToBookmarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bookmarkIntent = new Intent(mContext, BookmarkAdderAndEdit.class);
                        bookmarkIntent.putExtra("webview_full_url", url);
                        startActivity(bookmarkIntent);
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
            }
        }


        if (receivedIntent != null){
            if (receivedIntent.containsKey("splitHistoryLower")){
                String selectedName = receivedIntent.getString("title");
                String url=receivedIntent.getString("url");
                String time=receivedIntent.getString("time");
                RecordAction action = new RecordAction(mContext);
                action.open(false);
                long timel=Long.parseLong(time);

                editText.setText(url);

                removebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Record record=new Record(selectedName,url,timel);
                        action.deleteLowerSplitHistory(record);

                        Intent intent=new Intent();
                        intent.putExtra("remove","remove");
                        getTargetFragment().onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,intent);

                        new java.util.Timer().schedule(
                                new java.util.TimerTask(){
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismiss();
                                            }
                                        });
                                    }
                                },
                                100
                        );
                    }
                });

                addToBookmarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bookmarkIntent = new Intent(mContext, BookmarkAdderAndEdit.class);
                        bookmarkIntent.putExtra("webview_full_url", url);
                        startActivity(bookmarkIntent);
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
            }
        }


        return view;
    }

    private void toastMessage(String message){
        Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show();
    }
}
