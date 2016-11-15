package com.handm.dhruval.personsinfo.helper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.handm.dhruval.personsinfo.MainActivity;
import com.handm.dhruval.personsinfo.R;
import com.handm.dhruval.personsinfo.model.PersonInfo;


public class PersonInfoDialogFragment extends DialogFragment {
    private EditText firstName;
    private EditText lastName;
    private Button addButton;

    public PersonInfoDialogFragment() {}

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_info_dialog_fragment, container);

        firstName = (EditText)view.findViewById(R.id.editText_firstName);
        lastName = (EditText)view.findViewById(R.id.editText_LastName);
        addButton = (Button)view.findViewById(R.id.button_add);

        addButton.setOnClickListener(addButtonListener);
        return view;
    }

    private View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (lastName.getText().toString().isEmpty()) {
                lastName.setError("Please enter last name");
            } else if (firstName.getText().toString().isEmpty()) {
                firstName.setError("Please enter first name");
            } else {
                String capitalizedFirstName = firstName.getText().toString().substring(0, 1).toUpperCase() + firstName.getText().toString().substring(1).toLowerCase();
                String capitalizedLastName = lastName.getText().toString().substring(0, 1).toUpperCase() + lastName.getText().toString().substring(1).toLowerCase();

                MainActivity activity = (MainActivity)getActivity();
                activity.showEnteredInfo(new PersonInfo(capitalizedFirstName, capitalizedLastName));
                dismiss();
            }
        }
    };
}
