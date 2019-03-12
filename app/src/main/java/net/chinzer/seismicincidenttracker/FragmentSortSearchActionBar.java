package net.chinzer.seismicincidenttracker;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;

public abstract class FragmentSortSearchActionBar extends Fragment {


    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.search_sort_actionbar, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId())
        {

            case R.id.search_button:
                //View searchButtonView = this.getActivity().findViewById(R.id.search_button);
                //PopupMenu searchMenu = new PopupMenu(this.getContext(), searchButtonView);
                return true;

            case R.id.sort_button:
                View sortButtonView = this.getActivity().findViewById(R.id.sort_button);
                PopupMenu sortMenu = new PopupMenu(this.getContext(), sortButtonView);
                sortMenu.inflate(R.menu.sort);
                //might want to split this into seprate class
                sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });
                sortMenu.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}