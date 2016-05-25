package com.khinthirisoe.mymoviecollection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    public DetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle extras = getActivity().getIntent().getExtras();

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView txt_title = (TextView) view.findViewById(R.id.title);
        TextView txt_overview = (TextView) view.findViewById(R.id.overview);
        TextView txt_rating = (TextView) view.findViewById(R.id.rating);
        TextView txt_release = (TextView) view.findViewById(R.id.release);

        Picasso.with(getContext()).load(extras.getString(PrefUtils.M_POSTER)).into(imageView);
        txt_title.setText(extras.getString(PrefUtils.M_ORIGINAL_TITLE));
        txt_overview.setText(extras.getString(PrefUtils.M_OVERVIEW));
        txt_rating.setText("Rating : " + extras.getString(PrefUtils.M_USER_RATING));
        txt_release.setText("Release Date : " + extras.getString(PrefUtils.M_RELEASE_DATE));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
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
