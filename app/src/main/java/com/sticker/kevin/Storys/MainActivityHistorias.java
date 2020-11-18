package com.sticker.kevin.Storys;


import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sticker.kevin.R;
import com.sticker.kevin.StickerPackListActivity;
import com.sticker.kevin.Storys.fragments.wa.WAFragment;

import java.io.File;


public class MainActivityHistorias extends AppCompatActivity {
    Dialog epicDialog;
    ImageView cerrar;
    Button botonvam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_historias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarKota);
         if (getSupportActionBar() != null) {

             getSupportActionBar().setTitle("StickerFly");
         }

        StickerPackListActivity.cargarAnuncio();

        try {
            if (!isMyServiceRunning(Class.forName("com.whatsappstatus.saver.service.NotificationService"))) {
                try {
                    startService(new Intent(this, Class.forName("com.whatsappstatus.saver.service.NotificationService")));
                } catch (Throwable e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

            //con este llama el fragmento principal

            Fragment fragment = new WAFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.framelayout, fragment).commit();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_help){
          mostrarInfo();
        }

        return super.onOptionsItemSelected(item);
    }


    public void stash() {
        File file = new File(new StringBuffer().append(new StringBuffer().append(Environment.getExternalStorageDirectory()).append(File.separator).toString()).append("WhatsApp/Media/.Statuses").toString());
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        new File(new StringBuffer().append(new StringBuffer().append(Environment.getExternalStorageDirectory()).append(File.separator).toString()).append("StorySaver/").toString()).mkdirs();
    }
    private boolean checkInstallation(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private boolean isMyServiceRunning(Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }public void mostrarInfo(){
        epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.custom_popup2);
        cerrar = (ImageView)epicDialog.findViewById(R.id.cerrarVentana);
        botonvam = (Button)epicDialog.findViewById(R.id.botonvamo);

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                epicDialog.dismiss();
            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();

        botonvam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.formula.kevin.vale");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

    }


}

