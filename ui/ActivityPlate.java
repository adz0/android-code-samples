package code.examples.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Gravity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import code.examples.R;
import code.examples.dao.ConfigApp;
import code.examples.dialog.DialogNumberPickerWeight;
import code.examples.listelements.IPresenterElemList;
import code.examples.listelements.IPresenterFinder;
import code.examples.plate.IPresenterEditElem;
import code.examples.plate.IPresenterEditWeight;
import code.examples.plate.IPresenterPlate;
import code.examples.widgets.IPresenterWidget;

public class ActivityPlate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
    , PlateFragment.OnFragmentInteractionListener
    , EditElementFragment.OnFragmentInteractionListener
    , AnalyticsFragment.OnFragmentInteractionListener
    , DialogNumberPickerWeight.OnFragmentInteractionListener
    , SearchView.OnQueryTextListener
	
{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int orientationScreen;

    private SearchView searchView;
    private MenuItem menuItemSearchView;

    private FragmentManager fm;

    @Inject IPresenterElemList presenterViewList;
    @Inject IPresenterPlate presenterPlate;
    @Inject IPresenterEditElem presenterEditElem;
    @Inject IPresenterEditWeight presenterEditWeight;
    @Inject IPresenterWidget presenterWidgetPFC;
    @Inject IPresenterFinder presenterFinder;

    public IPresenterElemList getPresenterViewList(){return presenterViewList;}
    public IPresenterPlate getPresenterPlate(){return presenterPlate;}
    public IPresenterEditElem getPresenterEditElem() {return presenterEditElem;}
    public IPresenterEditWeight getPresenterEditWeight() {return presenterEditWeight;}
    public IPresenterWidget getPresenterWidgetPFC(){ return presenterWidgetPFC;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);


        fm = getSupportFragmentManager();
        if(savedInstanceState==null) {
            FragmentTransaction transaction = fm.beginTransaction();

            transaction.add(R.id.fragment_plate, PlateFragment.newInstance(), "plate");
            transaction.add(R.id.activity_plate_list, ListElementsFragment.newInstance(), "list elements");

            transaction.commit();
        }

        orientationScreen = getResources().getConfiguration().orientation;
        ConfigApp.getInstance().orientationScreen = orientationScreen;
        ConfigApp.getInstance().deviceDensity = getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_plate, menu);

        // Associate searchable configuration with SearchView
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        menuItemSearchView = menu.findItem(R.id.menu_action_find);
        searchView = (SearchView)menuItemSearchView.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(this);


        if(orientationScreen == Configuration.ORIENTATION_PORTRAIT) {
            MenuItem openElementsItem = menu.add(Menu.NONE, 0x01, 101, "Open elements");
            openElementsItem.setIcon(R.drawable.ic_element_list_bulleted_black);
            openElementsItem.setShowAsAction( MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==0x01){
            drawerLayout.openDrawer(Gravity.END);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleSearchIntent(intent);
    }
    private void handleSearchIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String querySearch = intent.getStringExtra(SearchManager.QUERY);
            onFragmentInteraction(Uri.fromParts(querySearch, "find result", "find fragment"));
        }
    }

    //region SearchView.OnQueryTextListener
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.setIconified(true);
        searchView.clearFocus();
        menuItemSearchView.collapseActionView();

        if(orientationScreen== Configuration.ORIENTATION_PORTRAIT){
            drawerLayout.openDrawer(Gravity.END);
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }//endregion SearchView.OnQueryTextListener


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation textView item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_diary) {
            // Handle the camera action
        } else if (id == R.id.nav_list_diet) {

        } else if (id == R.id.nav_analytics) {
            // open analytica fragment
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment analyticsFragment = fm.findFragmentByTag("analytics");
            if(analyticsFragment==null){
                analyticsFragment = AnalyticsFragment.newInstance();
            }
            transaction.replace(R.id.fragment_plate, analyticsFragment, "analytics");
            transaction.addToBackStack("analytics");
            transaction.commit();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        String scheme = uri.getScheme();
        String action = uri.getSchemeSpecificPart();
        String fragment=uri.getFragment();
        if(action.equalsIgnoreCase("edit enabled") && fragment.equalsIgnoreCase("plate fragment")){
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment editFragment = fm.findFragmentByTag("edit element");
            if(editFragment==null){
                editFragment = EditElementFragment.newInstance();
            }
            transaction.replace(R.id.activity_plate_list, editFragment, "edit element");
            transaction.commit();

            // check for drawer panel, open
            if(orientationScreen== Configuration.ORIENTATION_PORTRAIT){
                drawerLayout.openDrawer(Gravity.END);
            }

        }else if(action.equalsIgnoreCase("edit disabled") && fragment.equalsIgnoreCase("edit fragment")){
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment listFragment = fm.findFragmentByTag("list elements");
            if(listFragment==null)
                listFragment = ListElementsFragment.newInstance();
            transaction.replace(R.id.activity_plate_list, listFragment, "list elements");
            transaction.commit();

        }else if(action.equalsIgnoreCase("find result") && fragment.equalsIgnoreCase("find fragment")){
            Fragment fragment1 = fm.findFragmentById(R.id.activity_plate_list);
            if(fragment1 instanceof EditElementFragment){
                FragmentTransaction transaction = fm.beginTransaction();
                Fragment listFragment = fm.findFragmentByTag("list elements");
                if(listFragment==null)
                    listFragment = ListElementsFragment.newInstance();
                transaction.replace(R.id.activity_plate_list, listFragment, "list elements");
                transaction.commit();
            }

            presenterFinder.onFindMatches(scheme);
        }
        else if(action.equalsIgnoreCase("edit weight on picker")){
            try {
                int strWeight = Integer.parseInt(fragment);
                presenterEditWeight.onChangeWeight(strWeight);
            }catch (NumberFormatException e){
                Log.e("PARSE WEIGHT", e.getMessage());
            }
        }

    }
}
