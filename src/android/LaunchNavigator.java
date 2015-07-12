package org.apache.cordova.launchnavigator;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class LaunchNavigator extends CordovaPlugin {

    private static final String LOG_TAG = "LaunchNavigator";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        boolean result;

        Log.d(LOG_TAG, action);

        if ("navigate".equals(action)) {
            result = this.navigate(args, callbackContext);
        } else {
            Log.e(LOG_TAG, "Invalid action");
            result = false;
        }
        
        if (result == true) {
            callbackContext.success();
        }
        return result;
    }

    private boolean navigate(JSONArray args, CallbackContext callbackContext){
        boolean result;
        try {
            String slat = "", slon = "", dlat = "", dlon = "";

            String type = args.getString(0);

            JSONArray pos = args.optJSONArray(1);
            JSONArray pos2 = args.optJSONArray(2);

            if (null != pos && null != pos2) {
                dlat = pos.getString(0);
                dlon = pos.getString(1);

                slat = pos2.getString(0);
                slon = pos2.getString(1);

                result = this.doNavigate(type, slat, slon, dlat, dlon, callbackContext);
            } else if (null != pos) {
                dlat = pos.getString(0);
                dlon = pos.getString(1);

                result = this.doViewMap(type, dlat, dlon, callbackContext);
            } else {
                result = false;
            }

        }catch( JSONException e ) {
            Log.e(LOG_TAG, "Exception occurred: ".concat(e.getMessage()));
            result = false;
        }
        return result;
    }


    private boolean doViewMap(String type, String dlat, String dlon, CallbackContext callbackContext){
        boolean result;
        try {
            if (type.equals("baidu")) {
                String url = String.format("bdapp://map/marker?location=%s,%s&title=%s&content=%s&coord_type=gcj02&src=duduche|parking"
                        , dlat, dlon, dlat + "," + dlon, dlat + "," + dlon);
                Log.d(LOG_TAG, url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.setPackage("com.baidu.BaiduMap");
                this.cordova.getActivity().startActivity(intent);
                result = true;
            } else if (type.equals("amap")) {
                String url = String.format("androidamap://viewMap?sourceApplication=duduche&lat=%s&lon=%s&poiname=%s&dev=0"
                        , dlat, dlon, dlat + "," + dlon);
                Log.d(LOG_TAG, url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.setPackage("com.autonavi.minimap");
                this.cordova.getActivity().startActivity(intent);

                result = true;
            } else {
                String msg = "Invalid type.";
                callbackContext.error(msg);
                result = false;
            }
        }catch( Exception e ) {
            String msg = e.getMessage();
            Log.e(LOG_TAG, "Exception occurred: ".concat(msg));
            callbackContext.error(msg);
            result = false;
        }

        return result;
    }


    private boolean doNavigate(String type, String slat, String slon, String dlat, String dlon, CallbackContext callbackContext){
        boolean result;
        try {
            if (type.equals("baidu")) {
                String url = String.format("bdapp://map/direction?origin=%s,%s&destination=%s,%s&coord_type=gcj02&mode=driving&src=duduche|parking"
                        , slat, slon, dlat, dlon);
                Log.d(LOG_TAG, url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.setPackage("com.baidu.BaiduMap");
                this.cordova.getActivity().startActivity(intent);
                result = true;
            } else if (type.equals("amap")) {
                String url = String.format("androidamap://route?sourceApplication=duduche&slat=%s&slon=%s&sname=%s&dlat=%s&dlon=%s&dname=%s&dev=0&m=0&t=2"
                        , slat, slon, slat + "," + slon, dlat, dlon, dlat + "," + dlon);
                Log.d(LOG_TAG, url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                intent.setPackage("com.autonavi.minimap");
                this.cordova.getActivity().startActivity(intent);

                result = true;
            } else {
                String msg = "Invalid type.";
                callbackContext.error(msg);
                result = false;
            }
        }catch( Exception e ) {
            String msg = e.getMessage();
            Log.e(LOG_TAG, "Exception occurred: ".concat(msg));
            callbackContext.error(msg);
            result = false;
        }

        return result;
    }

}