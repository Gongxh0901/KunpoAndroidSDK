package com.kunpo.kunposdk.network.core;

import android.util.Log;

import com.kunpo.kunposdk.utils.KunpoLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class KunpoHttpProxy {
    private static final String TAG = "NetWork/BFHttpProxy";
    private static KunpoHttpProxy _instance; //单例
    private KunpoCache _cache; //缓存
    ExecutorService _executor; //线程池

    private KunpoHttpProxy() {

        _cache = new KunpoCache(10);
        _executor = Executors.newFixedThreadPool(10);
    }

    public static synchronized KunpoHttpProxy getInstance() {
        if (_instance == null)
            _instance = new KunpoHttpProxy();
        return _instance;
    }

    /** 同步请求方法 */
    public synchronized KunpoResponse execute(String url, KunpoRequest request) {
        return syncExecute(url, request);
    }

    /** 异步请求方法 */
    public synchronized void execute(String url, KunpoRequest request, KunpoRequestCallBack callBack) {
        asynExecute(url, request, callBack);
    }

    /** 同步请求 */
    private KunpoResponse syncExecute(String url, KunpoRequest request) {
        KunpoResponse response = _cache.get(url, request);
        if (response != null) {
            return response;
        }
        return null;
    }

    /** 异步请求 */
    private void asynExecute(String url, KunpoRequest request, KunpoRequestCallBack callBack) {
        executor(url, request, callBack);
    }

    /**
     * 网络请求加入线程池
     * @param url
     * @param request
     * @param callBack
     */
    private void executor(String url, KunpoRequest request, KunpoRequestCallBack callBack) {
        _executor.execute(new KunpoRequestThread(url, request, callBack));
    }

    class KunpoRequestThread implements Runnable {
        private String _url;
        private KunpoRequest _request;
        private KunpoRequestCallBack _callBack;

        public KunpoRequestThread(String url, KunpoRequest request, KunpoRequestCallBack callBack) {
            this._url = url;
            this._request = request;
            this._callBack = callBack;
        }

        @Override
        public void run() {
            KunpoResponse response = null;
            //发起网络请求
            if (_request.getRequestMethod().equals("GET"))
                response = get(_request);
            else
                response = post(_request);

            //设置回调
            if (response == null) {
                _callBack.error(null);
                return;
            }
            //设置回调
            if (response.getCode() == 200) {
                _callBack.success(response);
                //请求成功，加入缓存
                _cache.put(_url, _request, response);
                KunpoLog.i(TAG, "response from network");
            } else
                _callBack.error(response);
        }

        /** post请求 */
        private KunpoResponse post(KunpoRequest request) {
            KunpoResponse response = null;
            try {
                //设置请求
                URL requestUrl = new URL(_url);
                if (_url.startsWith("https")) {
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) requestUrl.openConnection();
                    httpsURLConnection.setRequestMethod("POST");
                    httpsURLConnection.setReadTimeout(10000);
                    httpsURLConnection.setConnectTimeout(10000);
                    httpsURLConnection.setDoOutput(true);
                    httpsURLConnection.setDoInput(true);
                    //设置请求头
                    Map<String, String> headers = request.getHeaders();
                    for (String key : headers.keySet()) {
                        httpsURLConnection.setRequestProperty(key, headers.get(key));
                    }
                    JSONObject jsonObject = new JSONObject();
                    Map<String, Object> params = request.getParams();
                    for (Object key : params.keySet()) {
                        jsonObject.put((String) key, params.get(key));
                    }
                    //向服务器端写请求体,json格式
                    PrintWriter out = new PrintWriter(httpsURLConnection.getOutputStream());
                    out.write(jsonObject.toString());
                    out.flush();
                    out.close();
                    //获取请求数据
                    int code = httpsURLConnection.getResponseCode();
                    String message = httpsURLConnection.getResponseMessage();
                    if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED || code == HttpURLConnection.HTTP_ACCEPTED) {
                        InputStream is = httpsURLConnection.getInputStream();
                        String body = getStringFromInputStream(is);
                        response = new KunpoResponse(code, message, body);
                    } else {
                        InputStream is = httpsURLConnection.getErrorStream();
                        String body = getStringFromInputStream(is);
                        response = new KunpoResponse(code, message,body);
                    }
                } else {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    //设置请求头
                    Map<String, String> headers = request.getHeaders();
                    for (String key : headers.keySet()) {
                        httpURLConnection.setRequestProperty(key, headers.get(key));
                    }
                    JSONObject jsonObject = new JSONObject();
                    Map<String, Object> params = request.getParams();
                    for (Object key : params.keySet()) {
                        jsonObject.put((String) key, params.get(key));
                    }
                    //向服务器端写请求体,json格式
                    PrintWriter out = new PrintWriter(httpURLConnection.getOutputStream());
                    out.write(jsonObject.toString());
                    out.flush();
                    out.close();
                    //获取请求数据
                    int code = httpURLConnection.getResponseCode();
                    String message = httpURLConnection.getResponseMessage();
                    if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED || code == HttpURLConnection.HTTP_ACCEPTED) {
                        InputStream is = httpURLConnection.getInputStream();
                        String body = getStringFromInputStream(is);
                        response = new KunpoResponse(code, message, body);
                    } else {
                        InputStream is = httpURLConnection.getErrorStream();
                        String body = getStringFromInputStream(is);
                        response = new KunpoResponse(code, message,body);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate =  new Date(System.currentTimeMillis());
                String   time   =   formatter.format(curDate);
                KunpoLog.d(TAG, "Response:"+ time + ", " + e.getMessage());
            }
            return response;
        }

        /** get请求 */
        private KunpoResponse get(KunpoRequest request) {
            KunpoResponse response = null;
            //拼接url
            boolean isFirst = true;
            String tmpUrl = _url + "?";
            Map<String, Object> params = request.getParams();
            for (Object key : params.keySet()) {
                if (isFirst) {
                    tmpUrl += (key + "=" + params.get(key));
                    isFirst = false;
                } else {
                    tmpUrl += ("&" + key + "=" + params.get(key));
                }
            }
            try {
                //设置请求
                if (tmpUrl.startsWith("https")) {
                    URL requestUrl = new URL(tmpUrl);
                    HttpsURLConnection httpURLConnection = (HttpsURLConnection) requestUrl.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setDoOutput(false);
                    httpURLConnection.setDoInput(true);
                    //设置请求头
                    Map<String, String> headers = request.getHeaders();
                    if (headers != null) {
                        for (String key : headers.keySet()) {
                            httpURLConnection.setRequestProperty(key, headers.get(key));
                        }
                    }
                    //获取返回数据 HttpURLConnection.HTTP_OK， HttpURLConnection.HTTP_CREATED， HttpURLConnection.HTTP_ACCEPTED
                    int code = httpURLConnection.getResponseCode();
                    String message = httpURLConnection.getResponseMessage();
                    if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED || code == HttpURLConnection.HTTP_ACCEPTED) {
                        InputStream is = httpURLConnection.getInputStream();
                        String body = getStringFromInputStream(is);
                        response = new KunpoResponse(code, message, body);
                    } else {
                        InputStream is = httpURLConnection.getErrorStream();
                        String body = getStringFromInputStream(is);
                        response = new KunpoResponse(code, message,body);
                    }
                }else {
                    URL requestUrl = new URL(tmpUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setDoOutput(false);
                    httpURLConnection.setDoInput(true);
                    //设置请求头
                    Map<String, String> headers = request.getHeaders();
                    if (headers != null) {
                        for (String key : headers.keySet()) {
                            httpURLConnection.setRequestProperty(key, headers.get(key));
                        }
                    }
                    //获取返回数据 HttpURLConnection.HTTP_OK， HttpURLConnection.HTTP_CREATED， HttpURLConnection.HTTP_ACCEPTED
                    int code = httpURLConnection.getResponseCode();
                    String message = httpURLConnection.getResponseMessage();
                    if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED || code == HttpURLConnection.HTTP_ACCEPTED) {
                        InputStream is = httpURLConnection.getInputStream();
                        String body = getStringFromInputStream(is);
                        response = new KunpoResponse(code, message, body);
                    } else {
                        InputStream is = httpURLConnection.getErrorStream();
                        String body = getStringFromInputStream(is);
                        response = new KunpoResponse(code, message,body);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                SimpleDateFormat formatter   =   new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate =  new Date(System.currentTimeMillis());
                String   time   =   formatter.format(curDate);
                KunpoLog.d(TAG, "Response:"+ time + ", " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                SimpleDateFormat formatter   =   new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate =  new Date(System.currentTimeMillis());
                String   time   =   formatter.format(curDate);
                KunpoLog.d(TAG, "Response:"+ time + ", " + e.getMessage());
            }

            return response;
        }

        private String getStringFromInputStream(InputStream is) throws IOException {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            is.close();
            String body = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
            os.close();
            return body;
        }
    }
}
