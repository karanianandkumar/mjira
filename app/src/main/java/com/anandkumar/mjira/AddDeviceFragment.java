package com.anandkumar.mjira;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddDeviceFragment extends Fragment {

    private EditText dName;
    private EditText dIMEI;
    private Button addButton;
    private ProgressDialog progress;

    private TextInputLayout inputLayoutDevice, inputLayoutIMEI;

    public AddDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_device, container, false);

        inputLayoutDevice = (TextInputLayout) view.findViewById(R.id.input_device_name);

        inputLayoutIMEI = (TextInputLayout) view.findViewById(R.id.input_imei);

        dName=(EditText)view.findViewById(R.id.deviceName);
        dIMEI=(EditText)view.findViewById(R.id.deviceIMEI);
        addButton=(Button)view.findViewById(R.id.addDevice);

        Intent intent=getActivity().getIntent();
        final String currentUser=intent.getStringExtra("currentUser");


        dName.addTextChangedListener(new MyTextWatcher(dName));

        dIMEI.addTextChangedListener(new MyTextWatcher(dIMEI));

        progress = new ProgressDialog(getActivity());

        progress.setMessage("Wait while Adding Device...");
        progress.setCancelable(false);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device device=new Device();
                device.setOwner(currentUser);
                device.setName(dName.getText().toString());
                device.setImei(dIMEI.getText().toString());
                addNewDetails(device);

            }
        });

        return view;
    }



    private void addNewDetails(Device device) {
        if (!validateName()) {
            return;
        }



        if (!validateIMEI()) {
            return;
        }
        progress.show();
        Backendless.Persistence.save(device, new AsyncCallback<Device>() {
            @Override
            public void handleResponse(Device response) {
                progress.dismiss();
                Toast.makeText(getActivity(),"Device Added Successfully",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progress.dismiss();
                Toast.makeText(getActivity(),"Failed to add Device",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateName() {
        if (dName.getText().toString().trim().isEmpty()) {
            dName.setError(getString(R.string.err_msg_name));
            requestFocus(dName);
            return false;
        } else {
            inputLayoutDevice.setErrorEnabled(false);
        }

        return true;
    }



    private boolean validateIMEI() {
        if (dIMEI.getText().toString().trim().isEmpty()) {
            dIMEI.setError(getString(R.string.err_msg_imei));
            requestFocus(dIMEI);
            return false;
        } else {
            inputLayoutIMEI.setErrorEnabled(false);
        }

        return true;

    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.deviceName:
                    validateName();
                    break;

                case R.id.deviceIMEI:
                    validateIMEI();
                    break;
            }
        }
    }


}
