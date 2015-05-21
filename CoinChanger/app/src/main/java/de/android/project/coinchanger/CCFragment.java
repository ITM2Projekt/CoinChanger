package de.android.project.coinchanger;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank
 * 20.05.2015.
 */
public class CCFragment extends Fragment {

    // //////////////////////////////////////////////////////////
    // Constants
    // //////////////////////////////////////////////////////////
    public static final String TAG = "CCFragment";
    public static final int Rates_Type = 1;
    public static final int Names_Type = 2;

    // //////////////////////////////////////////////////////////
    // Variables
    // //////////////////////////////////////////////////////////
    protected String currencyRatesURL = "http://openexchangerates.org/api/latest.json?app_id=f0d9d295f975446eb3314b1d7fcc9037";
    protected String currencyNameURL = "http://openexchangerates.org/api/currencies.json?app_id=f0d9d295f975446eb3314b1d7fcc9037";
    protected ViewGroup contentView;
    protected AppCompatEditText editText;
    protected AppCompatSpinner spinner;
    protected ListView listView;
    protected ArrayAdapter<CurrencyItem> spinnerAdapter;
    protected CurrencyItem currentCurrencyItem;
    protected List<CurrencyItem> currencyItemList;
    protected CCListAdapter listAdapter;

    // //////////////////////////////////////////////////////////
    // Getters and Setters
    // //////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////
    // Lifecycle
    // //////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = (ViewGroup) inflater.inflate(R.layout.fragment_cc, container, false);

        new LoadContentAsync(getActivity(), currencyRatesURL, null, Rates_Type).execute();

        editText = (AppCompatEditText) contentView.findViewById(R.id.inputEditText);
        if (editText != null) {
            editText.setHint(R.string.STR_SelectCurrency);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (listAdapter != null) {
                        float value;
                        if (s != null && !s.toString().isEmpty()) {
                            value = Float.parseFloat(s.toString());
                        } else {
                            value = 1;
                        }
                        listAdapter.setCurrencyValue(value);
                    }
                }
            });

        }
        spinner = (AppCompatSpinner) contentView.findViewById(R.id.selectorSpinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    CurrencyItem currentCurrencyItem = (CurrencyItem) spinner.getAdapter().getItem(position);
                    if (currentCurrencyItem != null) {
                        CCFragment.this.currentCurrencyItem = currentCurrencyItem;
                        listAdapter.setCurrentCurrencyItem(currentCurrencyItem);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        listView = (ListView) contentView.findViewById(R.id.listView);

        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // //////////////////////////////////////////////////////////
    // Overridden Methods
    // //////////////////////////////////////////////////////////


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(/**MenuResourceID**/0, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // //////////////////////////////////////////////////////////
    // Methods
    // //////////////////////////////////////////////////////////
    public void showProgressBar() {
        if (contentView != null) {
            ProgressBar mProgress = (ProgressBar) contentView.findViewById(R.id.progressbar);
            if (mProgress != null) {
                mProgress.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideProgressBar() {
        if (contentView != null) {
            ProgressBar mProgress = (ProgressBar) contentView.findViewById(R.id.progressbar);
            if (mProgress != null) {
                mProgress.setVisibility(View.GONE);
            }
        }
    }

    // //////////////////////////////////////////////////////////
    // Bus
    // //////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////
    // AsyncTasks
    // //////////////////////////////////////////////////////////

    private class LoadContentAsync extends AsyncTask<Void, Integer, byte[]> {

        protected String mURL;
        protected List<NameValuePair> mHttpHeader;
        protected OkHttpClient client = new OkHttpClient();
        protected Call call;
        protected Context context;
        private int type;

        protected LoadContentAsync(Context context, String mURL, List<NameValuePair> mHttpHeader, int type) {

            this.mURL = mURL;
            this.mHttpHeader = mHttpHeader;
            this.context = context;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected byte[] doInBackground(Void... parsers) {

            try {
                Response response = loadContent(mURL, mHttpHeader);
                if (response != null) {
                    ResponseBody body = response.body();

                    if (body != null) {
                        return body.bytes();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] response) {
            hideProgressBar();
            try {
                if (response != null) {
                    String json = new String(response, "UTF-8");
                    if (json != null) {
                        Gson gson = new Gson();
                        if (type == Rates_Type) {
                            CurrencyRatesResponseObject object = gson.fromJson(json, CurrencyRatesResponseObject.class);
                            if (object != null) {
                                currencyItemList = new ArrayList<>();
                                for (Map.Entry<String, Float> stringFloatEntry : object.getRates().entrySet()) {

                                    if (stringFloatEntry.getKey().equalsIgnoreCase(object.getBase())) {
                                        currentCurrencyItem = new CurrencyItem(stringFloatEntry.getKey(), stringFloatEntry.getValue());
                                    }

                                    currencyItemList.add(new CurrencyItem(stringFloatEntry.getKey(), stringFloatEntry.getValue()));
                                }


                                Collections.sort(currencyItemList, new Comparator<CurrencyItem>() {
                                    @Override
                                    public int compare(CurrencyItem lhs, CurrencyItem rhs) {
                                        return lhs.getName().compareTo(rhs.getName());
                                    }
                                });



                                if (spinner != null) {
//                                spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, spinnerList);
                                    spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, currencyItemList);
                                    spinner.setAdapter(spinnerAdapter);
                                    int counter = 0;
                                    for (CurrencyItem currencyItem : currencyItemList) {

                                        if(currencyItem.getName().equalsIgnoreCase("EUR")){
                                            break;
                                        }
                                        counter++;
                                    }
                                    spinner.setSelection(counter, true);
                                }

                                if (listView != null) {
                                    listAdapter = new CCListAdapter(context, currencyItemList, currentCurrencyItem);
                                    listAdapter.setCurrentCurrencyItem(currencyItemList.get(0));
                                    listAdapter.setCurrencyValue(1);
                                    listView.setAdapter(listAdapter);
                                }
                                new LoadContentAsync(context, currencyNameURL, null, Names_Type).execute();
                            }
                        } else if (type == Names_Type) {

                            HashMap<String, String> map = gson.fromJson(json, HashMap.class);
                            if (currencyItemList != null) {
                                for (CurrencyItem currencyItem : currencyItemList) {
                                    if (map.containsKey(currencyItem.getName())) {
                                        currencyItem.setFullName(map.get(currencyItem.getName()));
                                    }
                                }
                            }
                            listAdapter.refillAdapter(currencyItemList);
                            spinnerAdapter.notifyDataSetChanged();

                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        private Response loadContent(String mURL, List<NameValuePair> mHttpHeader) {

            Response response = null;

            if (mURL == null) {
                return null;
            }

            try {

                Request.Builder builder = new Request.Builder();
                builder.url(mURL);
                if (mHttpHeader != null) {
                    for (NameValuePair pair : mHttpHeader) {
                        builder.addHeader(pair.getName(), pair.getValue());
                    }
                }

                builder.method("GET", null);

                Request request = builder.build();
                call = client.newCall(request);
                response = call.execute();
                call = null;

            } catch (Exception e) {
                e.printStackTrace();
                call = null;
            }

            return response;
        }
    }

    // //////////////////////////////////////////////////////////
    // Inner Classes
    // //////////////////////////////////////////////////////////


}
