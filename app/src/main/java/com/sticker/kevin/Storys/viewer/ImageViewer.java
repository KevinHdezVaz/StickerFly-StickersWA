package com.sticker.kevin.Storys.viewer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sticker.kevin.R;
import com.sticker.kevin.Storys.HelperMethods;

import java.io.File;


public class ImageViewer extends AppCompatActivity {
    HelperMethods helperMethods;
     private FloatingActionButton btnFlotante;
    private InterstitialAd mInterstitialAd;
     int position=0;
    File f;



    class SomeClass implements View.OnClickListener {
        private final ImageViewer imageViewer;
        private final File file;

        class SomeOtherClass implements Runnable {
            private final SomeClass context;
            private final File file;

            SomeOtherClass(SomeClass someClass, File file) {
                context = someClass;
                this.file = file;
            }



            @Override
            public void run() {
                try {
                    HelperMethods.transfer(this.file);
                    Toast.makeText(imageViewer, "Imagen Guardada Correctamente", Toast.LENGTH_SHORT).show();
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("GridView", new StringBuffer().append("onClick: Error: ").append(e.getMessage()).toString());
                }
            }
        }

        SomeClass(ImageViewer imageViewer, File file) {
            this.imageViewer = imageViewer;
            this.file = file;

        }

        @Override
        public void onClick(View view) {
            new SomeOtherClass(this, this.file).run();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        helperMethods = new HelperMethods(this);
         final Intent intent = getIntent();
        String string = intent.getExtras().getString("pos");
        position = intent.getExtras().getInt("position");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8882667917768463/6537639330");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        f = new File(string);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo);
        FloatingActionButton btnflotante = findViewById(R.id.save);
        btnflotante.setOnClickListener(downloadMediaItem(f));
        Glide.with(this).load(this.f).into(photoView);
    }


    public View.OnClickListener downloadMediaItem(File file) {
        return new SomeClass(this, file);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }


}
