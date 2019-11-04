package com.example.my_contacts.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.my_contacts.R;
import com.example.my_contacts.adapters.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class FragmentImport extends Fragment {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private  final int[] icons = {R.drawable.ic_person,R.drawable.ic_star};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getFragmentManager());
        adapter.addFragment(new FragmentsContacts(),"Contacts");
        //adapter.addFragment(new Fragment_Calls(),"Calls");
        adapter.addFragment(new Fragment_Favourite(),"Favourite");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //tabLayout.setTabTextColors(android.R.color.white,android.R.color.white);
        for (int i=0; i<tabLayout.getTabCount();i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setIcon(icons[i]);
        }
        return view;

    }


    }
