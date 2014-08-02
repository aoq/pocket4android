package com.hyperionics.PocketTestBrowser;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *  Copyright (C) 2012 Hyperionics Technology LLC <http://www.hyperionics.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
public class AndyUtil {

    private static Application myApp = getApp();

    private static ArrayList<File> myTempFiles = new ArrayList<File>();

    private AndyUtil() {}

    public static boolean setApp(Application app) {
        myApp = app;
        getFirstInstalled(); // to initialize timestamp file on old versions of Android
        return true;
    }

    public static Application getApp() {
        if (myApp == null) {
            try {
                final Class<?> activityThreadClass =
                        Class.forName("android.app.ActivityThread");
                final Method method = activityThreadClass.getMethod("currentApplication");
                myApp = (Application) method.invoke(null, (Object[]) null);
            } catch (Exception e) {
                // handle exception
            }
        }
        // below gives me /mnt/sdcard/Android/data/com.hyperionics.pdfxTest/cache/tmpPdfx
        // File file = new File(app.getApplicationContext().getExternalCacheDir(), "tmpPdfx");
        return myApp;
    }

    public static Context getAppContext() {
        if (getApp() != null)
            return getApp().getApplicationContext();
        return null;
    }

    public static File getTempFile(String tmpName) {
        // getExternalCacheDir() returns something like /mnt/sdcard/Android/data/[package_name]/cache/
        File file = new File(getApp().getApplicationContext().getExternalCacheDir(), tmpName);
        myTempFiles.add(file);
        return file;
    }

    public static File getTempFile() {
        return getTempFile("temp", ".tmp");
    }

    public static File getTempFile(String prefix, String suffix) {
        File file = null;
        try {
            file = File.createTempFile(prefix, suffix, getApp().getApplicationContext().getExternalCacheDir());
            myTempFiles.add(file);
        } catch (Exception e) {}
        return file;
    }

    public static File getPermFile(String permName) {
        // getExternalFilesDir() returns something like /mnt/sdcard/Android/data/[package_name]/files/
        File file = new File(getApp().getApplicationContext().getExternalFilesDir(null), permName);
        return file;
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
        out.flush();
    }

    public static boolean copyFile(File fin, File fout) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(fin);
            out = new FileOutputStream(fout);
            copyFile(in, out);
            in.close();
            out.close();
        } catch(IOException e) {
            if (in != null) try {
                in.close();
            } catch (IOException e2) {}
            if (out != null) try {
                out.close();
            } catch (IOException e3) {}
            return false;
        }
        return true;
    }

    public static void deleteTempFiles() {
        for (File f : myTempFiles)
            //noinspection ResultOfMethodCallIgnored
            f.delete();
    }

    public static String getTtsCurrentEngine(TextToSpeech tts) {
        // returns TTS engine of the tts object passed, not the default system engine!
        if (tts != null) {
            final Class<?> ttsClass = tts.getClass();
            try {
                final Method method = ttsClass.getMethod("getCurrentEngine");
                String engName = (String) method.invoke(tts, (Object[]) null);
                return engName != null ? engName : "";
            } catch (Exception e) {}
            try {
                Field f = ttsClass.getDeclaredField("mCachedParams"); // Found in Android 2.3...
                f.setAccessible(true);
                String [] ss = (String[]) f.get(tts);
                for (int i = 0; i < ss.length-1; i++) {
                    if (ss[i].equals("engine"))
                        return ss[i+1];
                }
            } catch (Exception e) {
                Lt.d("Exception " + e);
            }
        }
        return "";
    }

    public static long getFirstInstalled() {
        long firstInstalled = 0;
        PackageManager pm = myApp.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(myApp.getApplicationContext().getPackageName(), pm.GET_SIGNATURES);
            try {
                try {
                    //noinspection AndroidLintNewApi
                    firstInstalled = pi.firstInstallTime;
                } catch (NoSuchFieldError e) {}
            } catch (Exception ee) {}
            if (firstInstalled == 0) { // old versions of Android don't have firstInstallTime in PackageInfo
                File dir;
                try {
                    dir = new File(getApp().getApplicationContext().getExternalCacheDir().getAbsolutePath() +
                        "/.config");
                } catch (Exception e) { dir = null; }
                if (dir != null && (dir.exists() || dir.mkdirs())) {
                    File fTimeStamp = new File(dir.getAbsolutePath() + "/.myconfig");
                    if (fTimeStamp.exists()) {
                        firstInstalled = fTimeStamp.lastModified();
                    } else {
                        // create this file - to make it slightly more confusing, write the signature there
                        OutputStream out;
                        try {
                            out = new FileOutputStream(fTimeStamp);
                            out.write(pi.signatures[0].toByteArray());
                            out.flush();
                            out.close();
                        } catch(Exception e) {
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {}
        return firstInstalled;
    }

    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long parseLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean dirWritable(String dir) {
        return dirWritable(new File(dir));
    }

    public static boolean dirWritable(File dirFile) {
        try {
            if (!dirFile.isDirectory())
                return false;
            File testFile = File.createTempFile("test", "tmp", dirFile);
            testFile.delete();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public interface OpCallback {
        public boolean runInBg();
        public void onFinished(boolean result);
    }

    public static void bgTask(Context context, boolean wantProgress, String progTitle, String progText, OpCallback cb) {
        BgTask task = new BgTask();
        task.myOpCallback = cb;
        if (wantProgress)
            task.progress = ProgressDialog.show(context,
                    progTitle == null ? "" : progTitle,
                    progText == null ? "" : progText,
                    true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

    private static class BgTask extends AsyncTask<Void, Boolean, Boolean> {
        ProgressDialog progress = null;
        OpCallback myOpCallback = null;

        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return myOpCallback.runInBg();
        }

        protected void onPostExecute(Boolean result) {
            if(progress != null)
                try { progress.dismiss(); } catch (Exception dont_care) {}
            if (myOpCallback != null)
                myOpCallback.onFinished(result);
            myOpCallback = null;
        }

    }

}
