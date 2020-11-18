package com.sticker.kevin.Storys.fragments.wa;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sticker.kevin.R;
import com.sticker.kevin.Storys.GenericAdapter;
import com.sticker.kevin.Storys.HelperMethods;
import com.sticker.kevin.Storys.InstanceHandler;
import com.sticker.kevin.Storys.adapter.WAVideoAdapter;
import com.sticker.kevin.Storys.model.WAImageModel;
import com.sticker.kevin.Storys.recycler.RecyclerClick_Listener;
import com.sticker.kevin.Storys.recycler.RecyclerTouchListener;
import com.sticker.kevin.Storys.recycler.ToolbarActionModeCallback;
import com.sticker.kevin.Storys.viewer.VideoPlayer;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WAVideoFragment extends Fragment {
    private static WAVideoFragment mInstance;
    RecyclerView recyclerView;
    FragmentActivity activity;
    ProgressBar progressBar;
    private AdView mAdView;
    FloatingActionButton fab;
    WAVideoAdapter waVideoAdapter;
    ArrayList<WAImageModel> arrayList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private static View v;
    private ActionMode mActionMode;
    private InterstitialAd mInterstitialAd;

    public WAVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_wa_video, container, false);
        activity = getActivity();
        mInstance = this;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.ref_wa_video);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_wa_video);
        progressBar = (ProgressBar) v.findViewById(R.id.progressbar_wa_video);

        populateRecyclerView();
        implementRecyclerViewClickListeners();
/*
        MobileAds.initialize(activity, getString(R.string.admob_app_id));
        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        AdView adView = new AdView(activity);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-6699687107380271/2126437250");
        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        swipeRefreshLayout.setColorSchemeResources(new int[]{R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark});
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        fab = (FloatingActionButton) v.findViewById(R.id.wa_video_fab_save_all);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAll();

            }
        });
        return v;
    }

    private void populateRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        getStatus();
        waVideoAdapter = new WAVideoAdapter(activity, arrayList);
        recyclerView.setAdapter(waVideoAdapter);
        waVideoAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    //implementando el click y el click largo
    private void implementRecyclerViewClickListeners() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                if (mActionMode != null)
                    onListItemSelect(position);
                else {
                    String str = waVideoAdapter.getItem(position).getPath();
                    try {
                        Intent intent = new Intent(getActivity(), VideoPlayer.class);
                        intent.putExtra("pos", str);
                        intent.putExtra("position", position);
                        startActivityForResult(intent, 101);
                    } catch (Throwable e) {
                        throw new NoClassDefFoundError(e.getMessage());
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                mActionMode = null;
                onListItemSelect(position);
            }
        }));
    }


    //List item select method
    private void onListItemSelect(int position) {
        waVideoAdapter.toggleSelection(position);//Toggle the selection
        List<Fragment> fragments;

        boolean hasCheckedItems = waVideoAdapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null) {
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolbarActionModeCallback(getActivity(),new GenericAdapter<WAVideoAdapter>(waVideoAdapter), arrayList,new InstanceHandler<WAVideoFragment>(mInstance)));
        }
        else if (!hasCheckedItems && mActionMode != null)
        // there no selected items, finish the actionMode
        {
            mActionMode.finish();
            mActionMode=null;
        }

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(waVideoAdapter
                    .getSelectedCount()) + " seleccionados");


    }
    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    private void saveAll() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        // set title
        alertDialogBuilder.setTitle("");

        // set dialog message
        alertDialogBuilder
                .setMessage("A continuacion descargara todos los estados. \n¿Desea Continuar?")
                .setCancelable(false)
                .setPositiveButton("SI",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        File[] listFiles = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/WhatsApp/Media/.Statuses/").toString()).listFiles();
                        if (waVideoAdapter.getItemCount() == 0) {
                            Toast.makeText(activity, "No se encuentran estados disponibles", Toast.LENGTH_SHORT).show();
                        } else {
                            int i = 0;
                            while (i < listFiles.length) {
                                try {
                                    File file = listFiles[i];
                                    String str = file.getName().toString();
                                    if (str.endsWith(".mp4") || str.endsWith(".avi") || str.endsWith(".mkv") || str.endsWith(".gif")) {
                                        HelperMethods helperMethods = new HelperMethods(activity.getApplicationContext());
                                        HelperMethods.transfer(file);
                                    }
                                    i++;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                            Toast.makeText(activity, "Guardado Correctamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });


        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        // show it
        alertDialog.show();
    }

    public void getStatus(){
        File[] listFiles = new File(new StringBuffer().append(Environment.getExternalStorageDirectory().getAbsolutePath()).append("/WhatsApp/Media/.Statuses/").toString()).listFiles();
        if (listFiles != null && listFiles.length >= 1) {
            Arrays.sort(listFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        }
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.getName().endsWith(".mp4") || file.getName().endsWith(".avi") || file.getName().endsWith(".mkv") || file.getName().endsWith(".gif")) {
                    WAImageModel model=new WAImageModel(file.getAbsolutePath());
                    arrayList.add(model);
                }
            }
        }
    }

    public void refresh() {
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        waVideoAdapter.updateData(new ArrayList<WAImageModel>());
        populateRecyclerView();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null)  {
            if(resultCode == -1)
            {
                refresh();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                refresh();
            }
        }
    }
    public InterstitialAd getmInterstitialAd() {
        return mInterstitialAd;
    }
}


