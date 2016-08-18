package com.khinthirisoe.mymoviecollection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;


public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridDataArr;
    ProgressWheel progressBar;
    GridView gridView;

//    private final String URL = "https://api.themoviedb.org/3/discover/movie?api_key=f620efedee20f579541e84617932d567";

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        progressBar = (ProgressWheel) view.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        final String PARM_APPID = "api_key";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter(PARM_APPID, "f620efedee20f579541e84617932d567");

        String myUrl = builder.build().toString();

        new FetchMovieTask().execute(myUrl);

        GridView mGridView = (GridView) view.findViewById(R.id.gridView);
        mGridDataArr = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_view_item, mGridDataArr);
        mGridView.setAdapter(mGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(PrefUtils.M_ORIGINAL_TITLE, mGridDataArr.get(i).getTitle());
                intent.putExtra(PrefUtils.M_POSTER, mGridDataArr.get(i).getImage());
                intent.putExtra(PrefUtils.M_OVERVIEW, mGridDataArr.get(i).getOverview());
                intent.putExtra(PrefUtils.M_USER_RATING, mGridDataArr.get(i).getUserRating());
                intent.putExtra(PrefUtils.M_RELEASE_DATE, mGridDataArr.get(i).getReleaseDate());
                intent.putExtra(PrefUtils.M_POPULARITY, mGridDataArr.get(i).getPopularity());
                startActivity(intent);
            }
        });

        return view;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Integer> {

        private final String TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    result = 1;// Successful
                    parseResult(response);

                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                mGridAdapter.setGridData(mGridDataArr);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        mGridAdapter.setGridData(mGridDataArr);
        super.onResume();
    }

    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        stream.close();
        return result;
    }


    private void parseResult(String result) {

        try {
            JSONObject moviesObj = new JSONObject(result);
            JSONArray moviesArr = moviesObj.optJSONArray(PrefUtils.M_JSON_ARRAY);
            for (int i = 0; i < moviesArr.length(); i++) {
                JSONObject post = moviesArr.optJSONObject(i);
                GridItem item = new GridItem();
                item.setTitle(post.optString(PrefUtils.M_ORIGINAL_TITLE));
                item.setImage("http://image.tmdb.org/t/p/w500/" + post.optString(PrefUtils.M_POSTER));
                item.setOverview(post.optString(PrefUtils.M_OVERVIEW));
                item.setUserRating(post.optString(PrefUtils.M_USER_RATING));
                item.setReleaseDate(post.optString(PrefUtils.M_RELEASE_DATE));
                item.setPopularity(post.optDouble(PrefUtils.M_POPULARITY));
                mGridDataArr.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SortedBy();
    }

    private void SortedBy() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//        int value = Integer.parseInt((prefs.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_default))));

        String sort = prefs.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_popularity));
        Log.d(TAG, sort);

        if (sort.equals(getString(R.string.pref_sort_popularity))) {
            Collections.sort(mGridDataArr, GridItem.PopularityComparator);
        } else if (sort.equals(getString(R.string.pref_sort_user_rating))) {
            Collections.sort(mGridDataArr, GridItem.RatingComparator);
        } else {
            Snackbar.make(progressBar, "neither of them is selected", Snackbar.LENGTH_SHORT).show();
        }

//        if (value == 1) {
//            Collections.sort(mGridDataArr, GridItem.RatingComparator);
//
//        } else if (value == 0) {
//            Collections.sort(mGridDataArr, GridItem.PopularityComparator);
//        }
//        else {
//            Snackbar.make(progressBar,"neither of them is selected",Snackbar.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onStart() {
        SortedBy();
        super.onStart();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

}