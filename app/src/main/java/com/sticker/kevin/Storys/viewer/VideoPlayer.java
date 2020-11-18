package com.sticker.kevin.Storys.viewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sticker.kevin.R;
import com.sticker.kevin.Storys.HelperMethods;
import java.io.File;

public class VideoPlayer extends AppCompatActivity implements EasyVideoCallback {
    HelperMethods helperMethods;
    FloatingActionButton botonGuardar;
     private InterstitialAd mInterstitialAd;
    private EasyVideoPlayer player;
    int position=0;
    File f;

    class SomeClass implements View.OnClickListener {
        private final VideoPlayer videoPlayer;
        private final File file;

        class SomeOtherClass implements Runnable {
            private final VideoPlayer.SomeClass context;
            private final File file;
            SomeOtherClass(VideoPlayer.SomeClass someClass, File file) {
                context = someClass;
                this.file = file;
            }

            @Override
            public void run() {
                try { //aqui muestra la recompensa de video
                    HelperMethods.transfer(this.file);
                    Toast.makeText(videoPlayer, "Video Guardado Correctamente", Toast.LENGTH_SHORT).show();
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
        SomeClass(VideoPlayer videoPlayer, File file) {
            this.videoPlayer = videoPlayer;
            this.file = file;
        }

        @Override
        public void onClick(View view) {
            new VideoPlayer.SomeClass.SomeOtherClass(this, this.file).run();
        }
    }

    @Override
    public void onPaused(EasyVideoPlayer easyVideoPlayer) {
    }

    @Override
    public void onStarted(EasyVideoPlayer easyVideoPlayer) {
    }

    @Override
    public void onSubmit(EasyVideoPlayer easyVideoPlayer, Uri uri) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        helperMethods = new HelperMethods(this);
        this.helperMethods = new HelperMethods(this);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        this.f = new File(intent.getExtras().getString("pos"));
        this.position = intent.getExtras().getInt("position");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8882667917768463/6537639330");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        FloatingActionButton btnflotante = findViewById(R.id.savee);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        btnflotante.setOnClickListener(downloadMediaItem(this.f));
        this.player = (EasyVideoPlayer) findViewById(R.id.player);
        this.player.setCallback(this);
        this.player.setSource(Uri.fromFile(this.f));
    }

    public void sendBackData() {
        if (this.f.exists()) {
            this.f.delete();
        }
        Intent intent = new Intent();
        intent.putExtra("pos", this.position);
        setResult(-1, intent);
    }


    public View.OnClickListener downloadMediaItem(File file) {
        return new VideoPlayer.SomeClass(this, file);
    }


    @Override
    protected void onPause() {
        super.onPause();
        this.player.pause();
    }

    @Override
    public void onPreparing(EasyVideoPlayer easyVideoPlayer) {
        Log.d("EVP-Sample", "onPreparing()");
    }

    @Override
    public void onPrepared(EasyVideoPlayer easyVideoPlayer) {
        Log.d("EVP-Sample", "onPrepared()");
    }

    @Override
    public void onBuffering(int i) {
        Log.d("EVP-Sample", new StringBuffer().append(new StringBuffer().append("onBuffering(): ").append(i).toString()).append("%").toString());
    }

    @Override
    public void onError(EasyVideoPlayer easyVideoPlayer, Exception exception) {
        Log.d("EVP-Sample", new StringBuffer().append("onError(): ").append(exception.getMessage()).toString());
    }

    @Override
    public void onCompletion(EasyVideoPlayer easyVideoPlayer) {
        Log.d("EVP-Sample", "onCompletion()");
    }

    @Override
    public void onRetry(EasyVideoPlayer easyVideoPlayer, Uri uri) {
        Toast.makeText(this, "Retry", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }


}
