package com.example.usevkapi;


import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.view.VerifiedKeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKObject;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiGroups;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKScopes;
import com.vk.sdk.api.model.VKUsersArray;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private String[] scope = new String[]{VKScope.FRIENDS,VKScope.GROUPS,VKScope.WALL};
    private RecyclerView Llist;
    private Button update;
    private Bitmap photo;
    private ImageView image;
    Handler handler;
    ArrayList<Bitmap> arraybtm=new ArrayList<Bitmap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(VKSdk.getApiVersion());
        Llist=(RecyclerView) findViewById(R.id.list);
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        System.out.println(fingerprints[0]);
        VKSdk.login(this, scope);
        update=(Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                VKRequest request= new VKApiGroups().getById(VKParameters.from("group_ids","vordigans"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                       // VKApiWall wall=(VKApiWall) response.parsedModel;
                        ArrayList<String> arrayList=new ArrayList<String>();
                        VKList vkList=(VKList) response.parsedModel;
                       //try {
                           VKRequest vkRequest =new VKRequest("newsfeed.get",VKParameters.from(VKApiConst.COUNT,10,VKApiConst.FILTERS,"post,photos,notes,friends"));
                            //VKRequest vkRequest = new VKApiWall().get(VKParameters.from(VKApiConst.OWNER_ID,"-"+vkList.get(0).fields.getInt("id"), VKApiConst.COUNT, 100));
                            vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {
                                    super.onComplete(response);
                                    arraybtm.clear();
                                    ArrayList<String> arrayList1=new ArrayList<String>();
                                    ArrayList<String> arrayListURL=new ArrayList<String>();
                                    Bitmap btm;
                                    //photo=new (Bitmap) ;
                                    try{
                                        JSONObject jsonObject=(JSONObject) response.json.get("response");
                                        JSONArray jsonArray=(JSONArray) jsonObject.get("items");
                                        for(int i=0;i<jsonArray.length();i++) {
                                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                                            if(jsonObject1.has("attachments")){
                                                JSONArray jsonArray2 =(JSONArray) jsonObject1.get("attachments");
                                                for(int j=0;j<jsonArray2.length();j++) {
                                                    JSONObject jsonObject2 = (JSONObject) jsonArray2.get(j);
                                                    if(jsonObject2.getString("type").equals("photo")) {
                                                        JSONObject jsonObject3 = (JSONObject) jsonObject2.get("photo");
                                                        arrayListURL.add(jsonObject3.getString("photo_604"));
                                                        arrayList1.add(jsonObject3.getString("photo_604"));
                                                    }else{
                                                        arrayList1.add(jsonArray2.getString(j));
                                                    }

                                                }
                                                }else{
                                                arrayList1.add(jsonObject1.getString("text"));
                                            }
                                        }

                                        handler = new Handler() {   // создание хэндлера
                                            @Override
                                            public void handleMessage(Message msg) {
                                                super.handleMessage(msg);
                                                Adapterimage adapter=new Adapterimage(getApplicationContext(),arraybtm);
                                                Llist.setAdapter(adapter);
                                            }
                                        };
                                        LoadListImage loadListImage=new LoadListImage(arrayListURL);
                                        loadListImage.start();


                                                //arraybtm.add(downloadImage(arrayListURL.get(i)));

                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                        //}catch(JSONException jsonException){
                        //   jsonException.printStackTrace();
                        //}
                        ArrayList<String> stringArrayList =new ArrayList<String>();
                        for(int i=0;i<vkList.size();i++){
                            stringArrayList.add(vkList.get(i).toString());
                        }
                        Adaptertext adapter=new Adaptertext(getApplicationContext(),stringArrayList);
                        Llist.setAdapter(adapter);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {

                VKRequest request= VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS,"last_name,first_name"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKList list=(VKList) response.parsedModel;
                        ArrayList<String> stringArrayList =new ArrayList<String>();
                        for(int i=0;i<list.size();i++){
                            stringArrayList.add(list.get(i).toString());
                        }
                        System.out.println("1");
                        Adaptertext Adapter=new Adaptertext(MainActivity.this,stringArrayList);
                        Llist.setAdapter(Adapter);
                        System.out.println("2");
                    }
                });
                Toast.makeText(getApplicationContext(),"hiResult",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(),"hiError",Toast.LENGTH_LONG).show();
            }
        }))
            super.onActivityResult(requestCode, resultCode, data);

    }
    class LoadListImage extends Thread {
        private ArrayList<String> arrayListURL=new ArrayList<String>();
        public LoadListImage(ArrayList<String> arrayListURL) {
            this.arrayListURL=arrayListURL;
        }

        @Override
        public void run() {
            for(int i=0;i<arrayListURL.size();i++){
                int tempindex=i;
                try{
                    arraybtm.add(Picasso.with(getApplicationContext()).load(arrayListURL.get(tempindex)).placeholder(R.drawable.ic_launcher_background).error(R.drawable.vk_gray_transparent_shape).get());
                }catch(IOException e){
                    e.getStackTrace();
                }
            }
            handler.sendEmptyMessage(1);  // отправка сообщения хендлеру

        }
    }

}