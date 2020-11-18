package com.sticker.kevin.Storys.recycler;


import android.content.Context;
import android.os.Build;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.view.ActionMode;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.ads.InterstitialAd;
import com.sticker.kevin.R;
import com.sticker.kevin.Storys.GenericAdapter;
import com.sticker.kevin.Storys.HelperMethods;
import com.sticker.kevin.Storys.InstanceHandler;
import com.sticker.kevin.Storys.adapter.WAImageAdapter;
import com.sticker.kevin.Storys.adapter.WAVideoAdapter;

import com.sticker.kevin.Storys.fragments.wa.WAImageFragment;
import com.sticker.kevin.Storys.fragments.wa.WAVideoFragment;
import com.sticker.kevin.Storys.model.WAImageModel;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by SONU on 22/03/16.
 */
public class ToolbarActionModeCallback implements ActionMode.Callback {

    private Context context;
    private WAVideoAdapter waVideoAdapter;
    private WAImageAdapter waImageAdapter;
    private ArrayList<WAImageModel> message_models;
    private InterstitialAd mInterstitialAd;
    WAImageFragment waImageFragment;
    WAVideoFragment waVideoFragment;
    String s = "";


    public ToolbarActionModeCallback(Context context, GenericAdapter<?> adapter, ArrayList<WAImageModel> message_models, InstanceHandler<?> instance) {
        this.context = context;
        this.waVideoAdapter = waVideoAdapter;
        this.message_models = message_models;
        s = instance.getValue().getClass().getSimpleName();
        switch (s) {
            case "WAVideoFragment":
                waVideoFragment = (WAVideoFragment) instance.getValue();
                waVideoAdapter = (WAVideoAdapter) adapter.getValue();
                mInterstitialAd= waVideoFragment.getmInterstitialAd();
                break;
            case "WAImageFragment":
                waImageFragment = (WAImageFragment) instance.getValue();
                mInterstitialAd= waImageFragment.getmInterstitialAd();
                waImageAdapter = (WAImageAdapter) adapter.getValue();
                break;
        }
    }
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.selection_menu, menu);//Inflate the menu_main over action mode
        return true;
    }
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu_main according to SDK Levels
        if (Build.VERSION.SDK_INT < 11) {
             MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_save), MenuItemCompat.SHOW_AS_ACTION_NEVER);
        } else {
             menu.findItem(R.id.action_save).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        SparseBooleanArray selectedIds;
        int size;
        switch (item.getItemId()) {

            case R.id.action_save:
                switch (s) {
                    case "WAVideoFragment":
                        selectedIds = waVideoAdapter.getSelectedIds();
                        for (size = selectedIds.size() - 1; size >= 0; size--) {
                            if (selectedIds.valueAt(size)) {
                                HelperMethods.transfer(new File((String) waVideoAdapter.getItem(selectedIds.keyAt(size)).getPath()));
                            }
                        }
                        Toast.makeText(context, "Videos Guardado Correctamente", Toast.LENGTH_SHORT).show();
                        mode.finish();//Finish action mode

                        return true;


                }
                return false;
        }
        return false;
    }


        @Override
        public void onDestroyActionMode (ActionMode mode){

            //When action mode destroyed remove selected selections and set action mode to null
            //First check current fragment action mode
            Fragment recyclerFragment;
            switch (s) {
                case "WAVideoFragment":
                    waVideoAdapter.removeSelection();  // remove selection
                    recyclerFragment = ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_wa_video);//Get recycler fragment
                    if (recyclerFragment != null)
                        ((WAVideoFragment) recyclerFragment).setNullToActionMode();//Set action mode null
                    break;

                case "WAImageFragment":
                    waImageAdapter.removeSelection();  // remove selection
                    recyclerFragment = ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.fragment_wa_image);//Get recycler fragment
                    if (recyclerFragment != null)
                        ((WAImageFragment) recyclerFragment).setNullToActionMode();//Set action mode null
                    break;
            }
        }
    }
