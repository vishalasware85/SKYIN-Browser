package com.skyinbrowser;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.skyinbrowser.CustomJavaFiles.FragmentToActivity;

public class PermissionDenyDialog extends DialogFragment {

    private FragmentToActivity mCallback;
    private Context mContext;
    private View view;
    private TextView msgText;
    private Button reqOnce,cancelBtn;

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
        view = inflater.inflate(R.layout.permission_deny_dialog, container, false);
        mContext = view.getContext();

        if (getDialog() != null && getDialog().getWindow() != null){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        msgText=view.findViewById(R.id.msgDialogTxt);
        reqOnce=view.findViewById(R.id.reqOnceCameraDialog);
        cancelBtn=view.findViewById(R.id.cancelMsgDialog);

        Bundle bundle=this.getArguments();
        if (bundle != null){
            if (bundle.containsKey("cameraMsg")){
                msgText.setText(mContext.getResources().getString(R.string.cameramsgdialog));
                dismiss();
            }

            if (bundle.containsKey("storageMsg")){
                msgText.setText(mContext.getResources().getString(R.string.storagemsgdialog));
                dismiss();
            }
        }

        reqOnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgText.getText().toString().equals(mContext.getResources().getString(R.string.cameramsgdialog))){
                }

                if (msgText.getText().toString().equals(mContext.getResources().getString(R.string.storagemsgdialog))){

                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        sendData("dismiss");
        super.onDismiss(dialog);
    }

    private void sendData(String comm) {
        mCallback.communicate(comm);
    }
}
