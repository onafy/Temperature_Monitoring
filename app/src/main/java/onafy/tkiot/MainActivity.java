package onafy.tkiot;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView suhudalam, suhuluar, tanggal;
    private ProgressDialog pd;
    String url = "https://api.thingspeak.com/channels/469980/fields/1/last";
    String url2 = "https://api.thingspeak.com/channels/455506/fields/1/last";

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);


        suhudalam = (TextView) findViewById(R.id.kondisi1);
        suhuluar = (TextView) findViewById(R.id.kondisi2);
        tanggal = (TextView) findViewById(R.id.tanggal);

        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        //tanggal
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        tanggal.setText(currentDateTimeString);
        getSqlDetails();


    }

    /* Action Bar Setting*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                getSqlDetails();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* -----------end of action bar setting-------------------------- */



    /* ----------- requesr handler ------------------------------------*/

    private void getSqlDetails() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        tanggal.setText(currentDateTimeString);
        if(isOnline()) {
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.hide();
                            suhudalam.setText(response);
                            // suhuluar.setText(suhuluarr);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null) {

                                Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                Log.d("error", String.valueOf(error));
                            }
                        }
                    }

            );

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);





            //KEDUA
            pd.show();
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET,
                    url2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.hide();
                            suhuluar.setText(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null) {

                                Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                Log.d("error", String.valueOf(error));
                            }
                        }
                    }

            );

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest2);
        }
        else{
            Toast.makeText(this, "Maaf tidak ada koneksi internet", Toast.LENGTH_LONG).show();
        }
    }
}