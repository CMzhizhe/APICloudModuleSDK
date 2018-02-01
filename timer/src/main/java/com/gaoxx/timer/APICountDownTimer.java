package com.gaoxx.timer;

import android.util.Log;

import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 创建时间: 2018/1/30
 * gxx
 * 注释描述:时间倒计时
 */

public class APICountDownTimer extends UZModule {
    private String TAG = APICountDownTimer.class.getSimpleName();
    private boolean isClose = false;//是否关闭通知JS
    private boolean isStart = false;//是否开始执行定时操作
    private int delay = 0;//开始计时后，或N秒开始执行
    private boolean isLoop = false;//是否循环回调
    private int period = 1000;//间隔执行回调时间 1秒
    private static String ERRORMESSAGE="errorMessage";
    private static String STATUS="status";
    private static int FAILE=0; //失败
    private static int SUCCESS=1;//成功
    JSONObject ret = new JSONObject();
    JSONObject err = new JSONObject();

    private UZModuleContext moduleContex;

    public APICountDownTimer(UZWebView webView) {
        super(webView);
    }

    /**
     * 作者：GaoXiaoXiong
     * 创建时间:2018/1/30
     * 注释描述:线程执行
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //循环执行
            do {
                try {
                    Thread.sleep(period);
                    if(!isClose){
                        ret.put(STATUS,SUCCESS);
                        moduleContex.success(ret,!isLoop);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG,e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,e.getMessage());
                }
            }while (isLoop);
        }
    };


    /**
     * 作者：GaoXiaoXiong
     * 创建时间:2018/1/30
     * 注释描述:提供给JS调用
     *
     */
    public void jsmethod_startTime(UZModuleContext moduleContex) {
        try {
            if(isStart){
                Log.e(TAG,"已经被执行了，无法多次执行");
                return;
            }
            isStart = true;
            isClose  = false;
            this.moduleContex = moduleContex;
            delay = moduleContex.optInt("delay");
            isLoop = moduleContex.optBoolean("isLoop");
            period = moduleContex.optInt("period");
            if (delay < 0) {//返回错误的信息
                err.put(ERRORMESSAGE, "delay小于0");
                err.put(STATUS,FAILE);
                moduleContex.error(null, err, true);
                return;
            }

            if (period < 0) {//返回错误的信息
                err.put(ERRORMESSAGE, "period小于0");
                err.put(STATUS,FAILE);
                moduleContex.error(null, err, true);
                return;
            }

            Thread.sleep(delay);//延迟N秒后调用下面的线程
            Thread thread = new Thread(runnable);
            thread.start();
        }catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
    }

        /**
         *作者：GaoXiaoXiong
         *创建时间:2018/1/30
         *注释描述:关闭循环倒计时
         */

    public void jsmethod_closeTime(UZModuleContext moduleContex) {
        try {
            isStart = false;//定时操作执行关闭
            isClose = true;//关闭了
            isLoop = false;
            ret.put(STATUS,SUCCESS);
            moduleContex.success(ret,!isLoop);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG,e.getMessage());
        }
    }


}
