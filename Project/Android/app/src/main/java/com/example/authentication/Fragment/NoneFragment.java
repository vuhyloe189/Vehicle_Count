package com.example.authentication.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.example.authentication.R;

public class NoneFragment extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate( savedInstanceState );
        super.setContentView( R.layout.none_fragment );
    }
}
