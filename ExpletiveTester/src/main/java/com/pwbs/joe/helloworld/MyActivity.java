package com.pwbs.joe.helloworld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.pwbs.joe.helloworld.MESSAGE";
    public boolean bFirstClick = true;
    public int clickCount = 1;
    private CensorMessageTask _censorTask; // = new CensorMessageTask(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Spinner spinner = (Spinner) findViewById(R.id.ratingSpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Ratings, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        ProgressBar pbar = (ProgressBar) findViewById(R.id.pBar);
        pbar.setVisibility(View.GONE);
    }

    public void changeText(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String[] message = new String[2];
        message[0] = editText.getText().toString();
        Spinner ratingSpinner = (Spinner) findViewById(R.id.ratingSpinner);
        message[1] = ratingSpinner.getSelectedItem().toString();
        _censorTask = new CensorMessageTask(this);
       // if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
       //     _censorTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, message);
       // else {
       //     _censorTask.execute(message);
       // }
        _censorTask.execute(message);

        //censorTask.doInBackground(message);
        String msg = _censorTask.GetResult();
      //  apimsg = apimsg.FromJsonStringContent(_censorTask.GetResult());
      //  editText.setText(apimsg.getCensoredMessage());
      //  if (!bFirstClick) {
       //     editText.setText(editText.getText() + " " + Integer.toString(++clickCount) + " Times.");
      //  }
      //  bFirstClick = false;
        //intent.putExtra(MyActivity.EXTRA_MESSAGE, message);
        // startActivity(intent);
    }

    public void SetCensoredMessage(String message) {
        TextView editText = (TextView) findViewById(R.id.original_message);
        if (editText != null) {
            editText.setText(message);
        } else {
            editText.setText("Error! - messge was NULL");
        }
    }

    private class CensorMessageTask extends AsyncTask<String, String, String> {

        private ProgressDialog progDialog;
        private Exception exception;
        private String result;
        private MyActivity activity;
        private Context context;

        public CensorMessageTask(MyActivity activity) {
            super();
            this.activity = activity;
            this.context = this.activity.getApplicationContext();

        }

        public String GetResult() {
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDialog = ProgressDialog.show(this.activity, "Censoring...", this.context.getResources().getString(R.string.progMessage), true, false);
            //progressBar.setVisibility(View.VISIBLE);
            //responseView.setText("");
        }

        @Override
        protected String doInBackground(String... params) {
            String message = params[0];
            String request;
            String rating = params[1];
            //String result;
            apiMessage msg = new apiMessage();
            msg.SetOriginalMessage(message);
            msg.SetRating(rating);
            try {
                URL url = new URL("http://Expletives.azurewebsites.net/api/ExpletiveService");
                //URL url = new URL("http://cliffe_ave:64334/api/ExpletiveService");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    request = msg.ToJsonStringContent();

                    urlConnection.setDoOutput(true);
                    //urlConnection.setDoInput(true);
                    urlConnection.setFixedLengthStreamingMode(request.length());
                    //urlConnection.setChunkedStreamingMode(0);
                    urlConnection.setRequestProperty("Content-Type", "text/json");
                    //urlConnection.setRequestProperty("Accept", "text/json");
                    urlConnection.setRequestMethod("POST");
                    urlConnection.connect();

                    //Write
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                    String[] progress = new String[] {request, ""};
                    publishProgress(progress);
                    writer.write(request);
                    writer.close();
                    os.close();
                    int i = urlConnection.getResponseCode();
                    // OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                    // writeStream(out);
                    if (i != 200) {
                        InputStream is = urlConnection.getErrorStream();
                        InputStreamReader ir = new InputStreamReader(is, "UTF-8");
                        BufferedReader br = new BufferedReader(ir);

                        String line = null;
                        StringBuilder sb = new StringBuilder();

                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }

                        br.close();
                        ir.close();
                        is.close();
                        result = "!:" + sb.toString();
                    }
                    else
                    {
                        //Read
                        InputStream is = urlConnection.getInputStream();
                        InputStreamReader ir = new InputStreamReader(is, "UTF-8");
                        BufferedReader br = new BufferedReader(ir);

                        String line = null;
                        StringBuilder sb = new StringBuilder();

                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }

                        br.close();
                        ir.close();
                        is.close();

                        result = sb.toString();
                        // InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        // readStream(in);

                    }
                   // urlConnection.disconnect();


                } finally {

                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                result = e.getMessage();
                return null;

            }
            return result;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            TextView editText = (TextView) findViewById(R.id.original_message);

            if (editText != null) {
                ProgressBar pbar = (ProgressBar) findViewById(R.id.pBar);
                pbar.setProgress(50);
                editText.setText(progress[0]);
            } else {
                editText.setText("messge was NULL");
            }
        }

        @Override
        protected void onPostExecute(String response) {
            progDialog.dismiss();

            if (response == null) {
                response = "THERE WAS AN ERROR";
                this.activity.SetCensoredMessage(response);
            }
            else {
                apiMessage apimsg = new apiMessage();
                if (response.contains("!:"))
                    apimsg.SetCensoredMessage(response);
                else
                {
                    apimsg = apimsg.FromJsonStringContent(response);

                    //apimsg = apimsg.FromJsonStringContent(_censorTask.GetResult());
                    //editText.setText(apimsg.getCensoredMessage());

                }
                this.activity.SetCensoredMessage(apimsg.getCensoredMessage());
                //progressBar.setVisibility(View.GONE);
            }
            Log.i("INFO", response);
            //responseView.setText(response);
        }
    }
}






