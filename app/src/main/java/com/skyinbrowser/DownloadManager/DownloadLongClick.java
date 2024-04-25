package com.skyinbrowser.DownloadManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;
import com.skyinbrowser.R;

public class DownloadLongClick extends DialogFragment{

    private FragmentToActivity mCallback;
    private Context mContext;
    private View view;

    private TextView diaplayLinktxt;
    private Button copy,delete,update;
    private EditText editText;

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
        view = inflater.inflate(R.layout.download_long_click, container, false);
        mContext = view.getContext();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        editText=view.findViewById(R.id.downloadUrlEditText);
        diaplayLinktxt=view.findViewById(R.id.urlDownloadtxt);
        copy=view.findViewById(R.id.copy_downloadUrl);
        delete=view.findViewById(R.id.remove_downloadFile);
        update=view.findViewById(R.id.updateDownloadUrl);

        Bundle intent = this.getArguments();
        String urlFile=intent.getString("downloadURL");
        int downloadId=intent.getInt("downloadId",0);

        diaplayLinktxt.setText(diaplayLinktxt.getText().toString() + urlFile);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String copytxt=diaplayLinktxt.getText().toString();
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
                dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent("deleteDownload");
                intent1.putExtra("downloadId",downloadId);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent1);
                dismiss();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getVisibility()==View.VISIBLE){
                    if (editText.getText().toString().equals("")){
                        Toast.makeText(mContext, "Please enter the valid URL", Toast.LENGTH_SHORT).show();
                    }else {
                        String newUrl=editText.getText().toString();
                        Intent intent1=new Intent("updateDownload");
                        intent1.putExtra("url",newUrl);
                        intent1.putExtra("downloadId",downloadId);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent1);
                    }
                }else {
                    editText.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }
}