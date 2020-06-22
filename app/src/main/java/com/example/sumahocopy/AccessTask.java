package com.example.sumahocopy;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

public class AccessTask extends AsyncTask<Integer, Integer, String> {
    private TextView textView;

    public AccessTask(TextView txtView)
    {
        super();
        textView = txtView;
    }

    @Override
    protected String doInBackground(Integer... value)
    {
        SmbFile dir;
        String host = "192.168.1.200";
        String user = "user";
        String pswd = "pass";
        String str="**";
        String pathTo = "/mnt/sdcard/Movies/"; //Androidのダウンロード先
        String pathFrom = "/mnt/sdcard/DCIM/Camera/"; //Androidのupload 元

        try
        {
            // String path = "smb://" + user + ":" + pswd + "@" + host + "/disk2/temp/";
            String path = "smb://"  + host + "/disk/VIDEO/mp4/";
            String path_to = "smb://"  + host + "/disk/Photos/";



            dir = new SmbFile(path);
            SmbFile[] files = dir.listFiles();

            for (int i = 0; i < files.length; i++) {

                String filePath = files[i].getPath();
                Log.i("log",filePath);

                SmbFileInputStream sfis = new SmbFileInputStream(filePath);


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
        textView.setText(param);
    }
}

