package com.example.lenovo.thejamroom.activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lenovo.thejamroom.Constants;
import com.example.lenovo.thejamroom.R;
import com.example.lenovo.thejamroom.async.uploadFile;
import com.example.lenovo.thejamroom.util.CommonsUtil;
import com.example.lenovo.thejamroom.util.UserSession;

import java.io.File;

public class PublishActivity extends Fragment {

    EditText txtTitle;
    EditText txtArtist;
    EditText txtFileName;
    Spinner  spinGenre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        final View view = inflater.inflate(R.layout.activity_publish, container, false);

        spinGenre = (Spinner)view.findViewById(R.id.spinGenre);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.genre_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinGenre.setAdapter(adapter);

        txtTitle = (EditText)view.findViewById(R.id.editSongName);
        txtArtist = (EditText)view.findViewById(R.id.editArtist);
        txtFileName = (EditText)view.findViewById(R.id.editFileName);


        Button publish= (Button) view.findViewById(R.id.btnPublish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
        return view;

    }

     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void uploadFile(){

        try{
            uploadFile upload = new uploadFile(getActivity());
            File musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            File[] allFiles = musicDirectory.listFiles();
            String data[] = new String[8];

            data[0] = allFiles[0].getAbsolutePath();
            data[1] = Constants.SERVER_URL+Constants.UPLOAD_URL;
            data[2] = txtTitle.getText().toString();
            data[3] = txtArtist.getText().toString();
            data[4] = txtFileName.getText().toString();
            data[5] = spinGenre.getSelectedItem().toString();
            data[6] = CommonsUtil.currentLocation;
            data[7] = UserSession.getUsername();
            upload.execute(data);
            String response = upload.get();

            if(response.contains("successfully")){
                Toast.makeText(getActivity(),"Your Song was published sucessfully!",Toast.LENGTH_SHORT);
            }
            else{
                Toast.makeText(getActivity(),"Unfortunately, could not publish your song.",Toast.LENGTH_SHORT);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
