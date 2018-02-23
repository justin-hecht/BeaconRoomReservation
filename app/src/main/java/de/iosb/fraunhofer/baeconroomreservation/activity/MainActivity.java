package de.iosb.fraunhofer.baeconroomreservation.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;

import java.io.IOException;

import de.iosb.fraunhofer.baeconroomreservation.Constants;
import de.iosb.fraunhofer.baeconroomreservation.R;
import de.iosb.fraunhofer.baeconroomreservation.fragments.CalendarFragment;
import de.iosb.fraunhofer.baeconroomreservation.fragments.RoomListFragment;
import de.iosb.fraunhofer.baeconroomreservation.fragments.SearchFragment;
import de.iosb.fraunhofer.baeconroomreservation.rest.Communicator;

/**
 * This is main class. This class start when the application starts. It extendes BaseActivity so iz has
 * side menu and also has bottom navigation. With the bottom navigation it changes fragments,
 * It also checks if users is logged in and ehat kind of permision he has if he is logged in.
 *
 * @author Viseslav Sako
 */
public class MainActivity extends BaseActivity
{
    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_CODE = 1111;
    private static final String AUTH_TOKEN_TYPE_ACC_REG = "read_only";
    private BottomNavigationView bottomNavigationView;
    Fragment fragment;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        if (!havePermissions()) {
            Log.i(TAG, "Requesting permissions needed for this app.");
            requestPermissions();
        }

        super.onCreate(savedInstanceState);

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>()
        {
            @Override
            protected Boolean doInBackground(Void... params) {
                AccountManager am = AccountManager.get(getApplicationContext());
                Account[] accounts = am.getAccountsByType(Constants.ACCOUNT_TYPE);
                Account account;
                if (accounts.length > 0) {
                    account = accounts[0];
                    AccountManagerFuture<Bundle> response;
                    am.invalidateAuthToken(Constants.ACCOUNT_TYPE,Communicator.token);
                    response = am.getAuthToken(account, AUTH_TOKEN_TYPE_ACC_REG, null, MainActivity.this, null, null);
                    try {
                        Bundle b = response.getResult();
                        Communicator.admin = Boolean.parseBoolean(am.getUserData(account, Constants.ADMIN));
                        Communicator.token = b.getString(AccountManager.KEY_AUTHTOKEN);
                        Communicator.setContext(getApplicationContext());
                        return true;
                    } catch (AuthenticatorException | IOException | OperationCanceledException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    am.addAccount(Constants.ACCOUNT_TYPE, AUTH_TOKEN_TYPE_ACC_REG, null, null, MainActivity.this, null, null);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean start)
            {
                if(start)
                {
                    fragment = RoomListFragment.instanceOf(false);

                    if (savedInstanceState != null)
                    {
                        fragment = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
                        setNavigationListener();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.root_layout, fragment)
                                .commit();
                    }
                    else
                    {
                        setNavigationListener();
                        checkIfAdmin();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.root_layout, fragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        };
        task.execute();


        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_example;
    }

    private void setNavigationListener()
    {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_calendar:
                                fragment = CalendarFragment.instanceOf();
                                break;
                            case R.id.action_search:
                                fragment = SearchFragment.instanceOf();
                                break;
                            case R.id.action_favorite:
                                fragment = RoomListFragment.instanceOf(true);
                                break;
                            default:
                                fragment = RoomListFragment.instanceOf(false);
                        }
                        switchToFragment(fragment);
                        return true;
                    }
                });
    }

    private void switchToFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    private boolean havePermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

       try {
           getSupportFragmentManager().putFragment(outState, "myFragmentName", fragment);
       }catch (NullPointerException e){

       }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        fragment = getSupportFragmentManager().findFragmentById(R.id.root_layout);
        if(fragment instanceof CalendarFragment)
        {
            bottomNavigationView.setSelectedItemId(R.id.action_calendar);
        }
        else if(fragment instanceof SearchFragment)
        {
            bottomNavigationView.setSelectedItemId(R.id.action_search);
        }
        else if(fragment instanceof RoomListFragment)
        {
            if(((RoomListFragment) fragment).isFavorite())
            {
                bottomNavigationView.setSelectedItemId(R.id.action_favorite);
            }else
            {
                bottomNavigationView.setSelectedItemId(R.id.action_nearby);
            }
        }
    }
}
