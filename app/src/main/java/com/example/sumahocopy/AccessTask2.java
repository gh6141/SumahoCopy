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
        String str="";
        //String pathFrom = "/mnt/sdcard/DCIM/Camera/"; //Androidのupload 元
        String pathFrom="/mnt/sdcard/"+fromsmh;
        String   path_to="smb://"+ipaddress+"/"+tona;
        try
        {
            // String path = "smb://" + user + ":" + pswd + "@" + host + "/disk2/temp/";
            //String path_to = "smb://192.168.1.200/disk/Photos/";

            dir = new File(pathFrom);
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                String filePath = files[i].getPath();
                FileInputStream sfis = new FileInputStream(filePath);

                SmbFile dirTo = new SmbFile(path_to);
                if (!dirTo.exists()) {
                    dirTo.mkdirs();
                }

                SmbFileOutputStream fos = new SmbFileOutputStream(path_to + files[i].getName());
                int sz=10240000;
                byte buf[] = new byte[sz];
                int len;
                float dv=0;
                while ((len =sfis.read(buf)) != -1){
                    fos.write(buf, 0, len);
                    dv=dv+sz;
                    publishProgress((int)(100*(i+dv/files[i].length())/files.length));
                 //   publishProgress((Integer)(100*i/files.length));
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
            str="Error ストレージの権限を許可してください";
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

