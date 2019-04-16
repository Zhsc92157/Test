package com.zhsc.test;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.zhsc.test.util.Base64Util;
import com.zhsc.test.util.FileUtil;
import com.zhsc.test.util.HttpUtil;

import org.json.JSONException;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.zhsc.test.AuthService.getAuth;

public class ImageActivity extends AppCompatActivity {

    /**
     * 用于处理返回得json格式字符串得数据类
     */
    public class UserBean{
        private LocationBean location;
        private String words;
        public class LocationBean{
            private int width;
            private int top;
            private int left;
            private int height;
        }
    }

    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private float real_width;
    private float real_height;
    private float image_width;
    private float image_height;

    private String imagePath = null;//保存图片路径
    private StringBuffer sb = new StringBuffer("");

    private String result = null;//http返回的识别结果存储

    private List<String> wordsList = new ArrayList<>();//存储识别的每行字符串
    private List<UserBean.LocationBean> locationList = new ArrayList<>();//每行识别的字符串的位置信息

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                Toast.makeText(getApplicationContext(), "分析完成", Toast.LENGTH_LONG).show();
                showRecognitionResult();
            }
            else
                Toast.makeText(getApplicationContext(),"分析中",Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        //初始化待识别图片
        initView();

        new Thread(){
            @Override
            public void run() {
                super.run();
                httpsPost();
                //formulaRecognition();
                handler.sendEmptyMessage(0x123);
            }
        }.start();
    }

    /**
     * 向服务器发送请求  得到返回得识别结果和位置信息
     */
    private void httpsPost(){
        //通用带精确位置识别url
        String host = "https://aip.baidubce.com/rest/2.0/ocr/v1/general";
        //文件路径
        String filePath = imagePath;
        try {
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            //Base64处理再进行UrlEncode操作
            String imgStr = Base64Util.encode(imgData);
            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
            Log.e("imgStr by urlEncoder",params);
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            String accessToken = getAuth();
            Log.e("httpsPost", accessToken);
            //获取识别结果
            result = HttpUtil.post(host,accessToken,params);
            //对获得的结果格式进行处理
            dealWithResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理服务器返回得位置和识别结果信息
     * @param string    //Json格式字符串
     * @throws JSONException
     */
    private void dealWithResult(String string) throws JSONException {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(string).getAsJsonObject();
        JsonArray wordJsonArray = jsonObject.getAsJsonArray("words_result");

        Gson gson = new Gson();
        ArrayList<UserBean> userBeanArrayList = new ArrayList<>();

        for(JsonElement user:wordJsonArray){
            UserBean userBean = gson.fromJson(user, new TypeToken<UserBean>() {}.getType());
            userBeanArrayList.add(userBean);
            wordsList.add(userBean.words);
            locationList.add(userBean.location);
        }
    }


    /**
     * 获取传递的图片
     */
    private void initView() {
        relativeLayout = findViewById(R.id.image_activity_relativeLayout);
        imageView = findViewById(R.id.image);
        Bundle getBasket = getIntent().getExtras();
        if (getBasket != null) {
            imagePath = getBasket.getString("FilePath");
            real_width = getBasket.getInt("FileWidth");
            real_height = getBasket.getInt("FileHeight");
        }

        Glide.with(getApplicationContext())
                .load(imagePath)
                .into(imageView);

    }

    private void formulaRecognition() {
        GeneralBasicParams params = new GeneralBasicParams();
        params.setDetectDirection(true);
        params.setImageFile(new File(imagePath));

        OCR.getInstance(getApplicationContext()).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                Log.e("token",token);
            }

            @Override
            public void onError(OCRError ocrError) {
                Log.e("initAccessToken","失败");
            }

        },getApplicationContext());


        OCR.getInstance(getApplicationContext()).recognizeGeneralBasic(params, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult generalResult) {
                for (WordSimple wordSimple: generalResult.getWordList()){
                    sb.append(wordSimple.getWords());
                    sb.append("\n");
                }
                result = generalResult.getJsonRes();
            }

            @Override
            public void onError(OCRError ocrError) {
                Log.e("recognizeGeneralBasic","失败");
            }
        });
    }

    /**
     * 展示识别得结果
     */
    private void showRecognitionResult() {
        for (int i=0;i<wordsList.size();i++) {
            addView(locationList.get(i).width,
                    locationList.get(i).height,
                    locationList.get(i).left,
                    locationList.get(i).top);
        }
    }

    /**
     * 添加一个红框标出识别得文字或公式
     * 传入内容为获取到得位置信息
     * @param width
     * @param height
     * @param left
     * @param top
     */
    public void addView(float width,float height,float left,float top){
        GradientDrawable gradientDrawable = new GradientDrawable();
        //设置矩形
        gradientDrawable.setGradientType(GradientDrawable.RECTANGLE);
        //颜色透明
        gradientDrawable.setColor(Color.parseColor("#00000000"));
        gradientDrawable.setStroke(5,Color.parseColor("#FF0000"));

        ImageView view = new ImageView(relativeLayout.getContext());
        view.setImageDrawable(gradientDrawable);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        image_width = displayMetrics.widthPixels;
        image_height = relativeLayout.getHeight();
        float multiple = image_width/real_width;

        float addHeightTemp = (image_height-real_height*multiple)/2;

        width = width*multiple;
        height = height*multiple;
        left = left*multiple;
        top = top*multiple+addHeightTemp;

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams((int) width,(int) height);
        params.leftMargin = (int) left;
        params.topMargin = (int) top;
        params.height = (int) height;
        params.width = (int) width;

        view.setLayoutParams(params);
        relativeLayout.addView(view,params);
    }

}
