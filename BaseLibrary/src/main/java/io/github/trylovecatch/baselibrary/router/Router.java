package io.github.trylovecatch.baselibrary.router;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import io.github.trylovecatch.baselibrary.router.router.IntentParam;
import io.github.trylovecatch.baselibrary.router.router.IntentUri;

/**
 * 路由功能
 * 封装了Intent的跳转
 *
 * 主要用于：
 * 1、不同module之间，而且又不互相依赖，并且需要相互跳转
 * 2、普通的Activity跳转
 *
 * Activity需要在AndroidManifest.xml里面配置如下信息：
 * 1、<category android:name="android.intent.category.DEFAULT" />
 * 2、<action android:name="android.intent.action.VIEW" />
 * 3、<data
         android:host="com.xxx.xxxActivity"
         android:scheme="tlc" />
 *
 *
 * Created by lipeng21 on 2017/6/13.
 */

public final class Router {

    private Context context;

    public Router(Context context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> service) {

        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                StringBuilder urlBuilder = new StringBuilder();

                IntentUri fullUri = method.getAnnotation(IntentUri.class);
                if (fullUri != null) {
                    urlBuilder.append(fullUri.value());
                } else {
                    throw new IllegalArgumentException("");
                }

                Annotation[][] parameterAnnotations = method.getParameterAnnotations();

                HashMap<String, Object> serializedParams = new HashMap<>();

                int position = 0;
                for (int i = 0; i < parameterAnnotations.length; i++) {
                    Annotation[] annotations = parameterAnnotations[i];
                    if (annotations == null || annotations.length == 0)
                        break;

                    Annotation annotation = annotations[0];
                    if (annotation instanceof IntentParam) {
                        IntentParam intentExtrasParam = (IntentParam) annotation;
                        serializedParams.put(intentExtrasParam.value(), args[i]);
                    }
                }

                //执行Activity跳转操作
                performJump(urlBuilder.toString(), serializedParams);
                return null;
            }
        });
    }

    /**
     * 执行Activity跳转操作
     *
     * @param routerUri Intent跳转URI
     */
    private void performJump(String routerUri, HashMap<String, Object> serializedParams) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(routerUri));
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : serializedParams.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Integer) {
                bundle.putInt(key, Integer.parseInt(value.toString()));
            } else if (value instanceof Long) {
                bundle.putLong(key, Long.parseLong(value.toString()));
            } else if (value instanceof Double) {
                bundle.putDouble(key, Double.parseDouble(value.toString()));
            } else if (value instanceof Short) {
                bundle.putShort(key, Short.parseShort(value.toString()));
            } else if (value instanceof Float) {
                bundle.putFloat(key, Float.parseFloat(value.toString()));
            } else if (value instanceof String) {
                bundle.putString(key, (String) value);
            } else if (value instanceof CharSequence) {
                bundle.putCharSequence(key, (CharSequence) value);
            } else if (value.getClass().isArray()) {
                if (int[].class.isInstance(value)) {
                    bundle.putIntArray(key, (int[]) value);
                } else if (long[].class.isInstance(value)) {
                    bundle.putLongArray(key, (long[]) value);
                } else if (double[].class.isInstance(value)) {
                    bundle.putDoubleArray(key, (double[]) value);
                } else if (short[].class.isInstance(value)) {
                    bundle.putShortArray(key, (short[]) value);
                } else if (float[].class.isInstance(value)) {
                    bundle.putFloatArray(key, (float[]) value);
                } else if (String[].class.isInstance(value)) {
                    bundle.putStringArray(key, (String[]) value);
                } else if (CharSequence[].class.isInstance(value)) {
                    bundle.putCharSequenceArray(key, (CharSequence[]) value);
                } else if (Parcelable[].class.isInstance(value)) {
                    bundle.putParcelableArray(key, (Parcelable[]) value);
                }
            } else if (value instanceof ArrayList && !((ArrayList) value).isEmpty()) {
                ArrayList list = (ArrayList) value;
                if (list.get(0) instanceof Integer) {
                    bundle.putIntegerArrayList(key, (ArrayList<Integer>) value);
                } else if (list.get(0) instanceof String) {
                    bundle.putStringArrayList(key, (ArrayList<String>) value);
                } else if (list.get(0) instanceof CharSequence) {
                    bundle.putCharSequenceArrayList(key, (ArrayList<CharSequence>) value);
                } else if (list.get(0) instanceof Parcelable) {
                    bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) value);
                }
            } else if (value instanceof Parcelable) {
                bundle.putParcelable(key, (Parcelable) value);
            } else if (value instanceof Serializable) {
                bundle.putSerializable(key, (Serializable) value);
            } else {
                throw new IllegalArgumentException("不支持的参数类型！");
            }
        }
        intent.putExtras(bundle);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        if (!activities.isEmpty()) {
            context.startActivity(intent);
        }
    }

    public void setContext(Context context){
        this.context = context;
    }
}
