package com.example.sumahocopy;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

public class AccessTask extends AsyncTask<Integer, Integer, String> {
    private TextView textView;
    private ProgressBar pgBar;
    private String tosmh;
    private String fromna;
    private String ipaddress;

    public AccessTask(TextView txtView,ProgressBar pb,String tosumaho,String fromnas,String ipaddr)
    {
        super();
        textView = txtView;
        pgBar=pb;
        tosmh=tosumaho;
        fromna=fromnas;
        ipaddress=ipaddr;
    }

    @Override
    protected String doInBackground(Integer... value)
    {
        SmbFile dir;
        String user = "user";
        String pswd = "pass";
        String str="**";
        //String pathTo = "/mnt/sdcard/Movies/"; //Androidのダウンロード先
        String pathTo=tosmh;

        try
        {
            // String path = "smb://" + user + ":" + pswd + "@" + host + "/disk2/temp/";
           // String path = "smb://192.168.1.200/disk/VIDEO/mp4/";
            String path="smb://"+ipaddress+"/"+fromna;
            dir = new SmbFile(path);
            SmbFile[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                String filePath = files[i].getPath();
                SmbFileInputStream sfis = new SmbFileInputStream(filePath);
                publishProgress((Integer)(100*i/files.length));
                File dirTo = new File(pathTo);
                if (!dirTo.exists()) {
                    dirTo.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(pathTo + files[i].getName());
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
            str = Integer.toString(files.length) + "個のファイルをスマホに移動しました";
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
        textView.setText("NASからスマホへ取り込み中..");
        pgBar.setProgress(progress[0]);
    }
}

