package com.example.ankit.assigment.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AppUtil {



    public static String getDateFromDate(String stringDate, String dateFormatExpected,String inputFormat) {

        DateFormat originalFormat = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
        originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat targetFormat = new SimpleDateFormat(dateFormatExpected);
        Date date = null;
        String formattedDate = "";
        try {
            date = originalFormat.parse(stringDate);
            formattedDate = targetFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager;
        boolean connected = false;
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            //System.out.println("CheckConnectivity Exception: " + e.getMessage());
            //Log.v("connectivity", e.toString());
        }
        return connected;
    }
    public static double getFileSizeInMB(File file) {
        return (double) file.length() / (1024 * 1024);
    }
    public static String getMimeType(Context context, String filePath) {
        Uri uri=Uri.fromFile(new File(filePath));
        String mimeType = "";
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType == null ? "" : mimeType;
    }
}
