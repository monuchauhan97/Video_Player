package com.allformats.video.player.downloader.privatevideobrowser.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class Vid_player_Utils {
    private Vid_player_Utils() {
    }

    public static void disableSSLCertificateChecking() {
        TrustManager[] trustManagerArr = {new X509TrustManager() { 
            @Override
            public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            }

            @Override 
            public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            }

            @Override 
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, trustManagerArr, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(instance.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String str, SSLSession sSLSession) {
                    return true;
                }
            });
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity activity, IBinder iBinder) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && iBinder != null) {
            inputMethodManager.hideSoftInputFromWindow(iBinder, 0);
        }
    }

    public static String getHost(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        int indexOf = str.indexOf("//");
        int i = indexOf == -1 ? 0 : indexOf + 2;
        int indexOf2 = str.indexOf(47, i);
        if (indexOf2 < 0) {
            indexOf2 = str.length();
        }
        int indexOf3 = str.indexOf(58, i);
        if (indexOf3 > 0 && indexOf3 < indexOf2) {
            indexOf2 = indexOf3;
        }
        return str.substring(i, indexOf2);
    }

    public static String getBaseDomain(String str) {
        String host = getHost(str);
        int indexOf = host.indexOf(46);
        int lastIndexOf = host.lastIndexOf(46);
        int i = 0;
        while (indexOf < lastIndexOf) {
            i = indexOf + 1;
            indexOf = host.indexOf(46, i);
        }
        return i > 0 ? host.substring(i) : host;
    }
}
