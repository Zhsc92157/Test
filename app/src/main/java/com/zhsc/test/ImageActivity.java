package com.zhsc.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.bumptech.glide.Glide;
import com.zhsc.test.util.Base64Util;
import com.zhsc.test.util.FileUtil;
import com.zhsc.test.util.HttpUtil;

import java.io.File;
import java.net.URLEncoder;

import static com.zhsc.test.AuthService.getAuth;


public class ImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private String imagePath = null;
    private StringBuffer sb = new StringBuffer("");
    private String result = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123)
                showRecognitionResult();
            else
                Toast.makeText(getApplicationContext(),"分析中",Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //初始化待识别图片
        initView();

        new Thread(){
            @Override
            public void run() {
                super.run();
                httpsPost();
                formulaRecognition();
                handler.sendEmptyMessage(0x123);
            }
        }.start();
    }

    private void httpsPost(){

        //通用识别url
        String host = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
        //文件路径
        String filePath = imagePath;
        try {
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
            Log.e("imgStr by urlEncoder",params);
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            String accessToken = getAuth();
            Log.e("httpsPost", accessToken);
            //获取识别结果
            String result = HttpUtil.post(host,accessToken,params);
            System.out.println("result---->"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取传递的图片
     */
    private void initView() {
        imageView = findViewById(R.id.image);
        Bundle getBasket = getIntent().getExtras();
        imagePath = getBasket.getString("FilePath");
        Glide.with(this).load(imagePath).into(imageView);
    }

    //TODO 公式识别
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
                    WordSimple word = wordSimple;
                    sb.append(word.getWords());
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

    //TODO 显示公式识别结果
    private void showRecognitionResult() {

    }

}
