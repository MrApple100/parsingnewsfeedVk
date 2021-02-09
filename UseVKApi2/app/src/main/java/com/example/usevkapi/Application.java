package com.example.usevkapi;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;



public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
        tokenTracker.startTracking();
    }

    private VKAccessTokenTracker tokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(@Nullable VKAccessToken oldToken, @Nullable VKAccessToken newToken) {
            if(newToken==null){
                Intent intent=new Intent(Application.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };


}
