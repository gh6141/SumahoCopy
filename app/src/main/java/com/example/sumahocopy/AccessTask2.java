package com.example.sumahocopy;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.File;

import java.io.FileInputStream;
import jcifs.smb.SmbFile;

import jcifs.smb.SmbFileOutputStream;


public class AccessTask2 extends AsyncTask<Integer, Integer, String> {
    private TextView textView;
    private ProgressBar pgBar;
    private String tona;
    private String fromsmh;
    private String ipaddress;

    public AccessTask2(TextView txtView,ProgressBar pb,String tonas,String fromsumaho,String ipaddr)
    {
        super();
        textView = txtView;
        pgBar=pb;
        tona=tonas;
        fromsmh=fromsumaho;
        ipaddress=ipaddr;
    }

    @Override
    protected String doInBackground(Integer... value)
    {
        File dir;
        String user = "user";
        String pswd = "pass";
        String str="**";
        //String pathFrom = "/mnt/sdcard/DCIM/Camera/"; //Androidのupload 元
        String pathFrom=fromsmh;
        try
        {
            // String path = "smb://" + user + ":" + pswd + "@" + host + "/disk2/temp/";
            //String path_to = "smb://192.168.1.200/disk/Photos/";
            String path_to="smb://"+ipaddress+"/"+tona;
            dir = new File(pathFrom);
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                String filePath = files[i].getPath();
                FileInputStream sfis = new FileInputStream(filePath);
                publishProgress((Integer)(100*i/files.length));
                SmbFile dirTo = new SmbFile(path_to);
                if (!dirTo.exists()) {
                    dirTo.mkdirs();
                }

                SmbFileOutputStream fos = new SmbFileOutputStream(path_to + files[i].getName());
                byte buf[] = new byte[1024];
                int len;
                while ((len =sfis.read(buf)) != -1){
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fos.close();
                sfis.close();
                files[i].delete();
            }
            str = Integer.toString(files.length) + "個のファイルをNASへ移動しました";
        }
        catch(Exception e)
        {
            Log.e("Err", e.toString());
        }
        return str;
    }
    @Override
    protected void onPostExecute(String param)
    {
        pgBar.setProgress(100);
        textView.setText(param);
    }

    // 途中経過をメインスレッドに返す
    @Override
    protected void onProgressUpdate(Integer... progress) {
        textView.setText("スマホからNASへ送り出し中..");
        pgBar.setProgress(progress[0]);
    }




}

