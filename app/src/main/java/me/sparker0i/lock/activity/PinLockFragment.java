package me.sparker0i.lock.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import me.sparker0i.lawnchair.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PinLockFragment extends Fragment {


    View view;

    public PinLockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pin_lock, container, false);
        final EditText password = view.findViewById(R.id.password);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        Button button = view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals(preferences.getString("password" , ""))) {
                    ((LockActivity) view.getContext()).unlock();
                }
            }
        });
        return view;
    }

}
