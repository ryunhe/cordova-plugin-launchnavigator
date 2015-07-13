package org.apache.cordova.launchnavigator;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class LaunchNavigator extends CordovaPlugin {

    private static final String TAG = LaunchNavigator.class.getName();
    private static final String TYPE_BAIDU = "baidu";
    private static final String TYPE_AMAP = "amap";


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        boolean result;

        Log.d(TAG, action);

        if ("doLaunch".equals(action)) {
            result = doLaunch(args, callbackContext);
        } else {
            Log.e(TAG, "Invalid action");
            result = false;
        }

        if (result) {
            callbackContext.success();
        }
        return result;
    }

    private boolean doLaunch(JSONArray args, CallbackContext callbackContext) {
        boolean result = false;
        try {

            String type = args.getString(0);

            JSONArray dest = args.optJSONArray(1);
            JSONArray orig = args.optJSONArray(2);

            if (null != dest && null != orig) {
                result = this.doNavigate(type, dest.getString(0), dest.getString(1), dest.getString(2)
                        , orig.getString(0), orig.getString(1), orig.getString(2), callbackContext);
            } else if (null != dest) {
                result = this.doViewMap(type, dest.getString(0), dest.getString(1), dest.getString(2), callbackContext);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Exception occurred: ".concat(e.getMessage()));
        }

        return result;
    }


    private boolean doViewMap(String type, String name, String lng, String lat, CallbackContext callbackContext) {
        boolean result;
        try {
            if (type.equals(TYPE_BAIDU)) {
                String url = String.format("bdapp://map/marker?location=%s,%s&title=%s&content=%s&coord_type=gcj02&src=duduche|parking"
                        , lat, lng, name, name);
                Log.d(TAG, url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.setPackage("com.baidu.BaiduMap");
                cordova.getActivity().startActivity(intent);
                result = true;
            } else if (type.equals(TYPE_AMAP)) {
                String url = String.format("androidamap://viewMap?sourceApplication=duduche&lat=%s&lon=%s&poiname=%s&dev=0"
                        , lat, lng, name);
                Log.d(TAG, url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.setPackage("com.autonavi.minimap");
                cordova.getActivity().startActivity(intent);

                result = true;
            } else {
                callbackContext.error("Invalid type.");
                result = false;
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.e(TAG, "Exception occurred: ".concat(msg));
            callbackContext.error(msg);
            result = false;
        }

        return result;
    }


    private boolean doNavigate(String type, String dname, String dlat, String dlng
            , String oname, String olat, String olng, CallbackContext callbackContext) {
        boolean result;
        try {
            if (type.equals("baidu")) {
                String url = String.format("bdapp://map/direction?origin=%s,%s&destination=%s,%s&coord_type=gcj02&mode=driving&src=duduche|parking"
                        , olat, olng, dlat, dlng);
                Log.d(TAG, url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.setPackage("com.baidu.BaiduMap");
                cordova.getActivity().startActivity(intent);
                result = true;
            } else if (type.equals("amap")) {
                String url = String.format("androidamap://route?sourceApplication=duduche&slat=%s&slon=%s&sname=%s&dlat=%s&dlon=%s&dname=%s&dev=0&m=0&t=2"
                        , olat, olng, oname, dlat, dlng, dname);
                Log.d(TAG, url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.setPackage("com.autonavi.minimap");
                cordova.getActivity().startActivity(intent);

                result = true;
            } else {
                callbackContext.error("Invalid type.");
                result = false;
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            Log.e(TAG, "Exception occurred: ".concat(msg));
            callbackContext.error(msg);
            result = false;
        }

        return result;
    }

}