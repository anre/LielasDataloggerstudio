package org.lielas.lielasdataloggerstudio.main;

import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.UsbCube.UsbCube;
import org.lielas.lielasdataloggerstudio.R;
import org.lielas.lielasdataloggerstudio.main.CommunicationInterface.UsbCube.UsbCubeSerialInterface;
import org.lielas.lielasdataloggerstudio.main.Fragments.ClockSettingsFragment;
import org.lielas.lielasdataloggerstudio.main.Fragments.ConnectFragment;
import org.lielas.lielasdataloggerstudio.main.Fragments.DataFragment;
import org.lielas.lielasdataloggerstudio.main.Fragments.ExportFragment;
import org.lielas.lielasdataloggerstudio.main.Fragments.LoggerSettingsFragment;
import org.lielas.lielasdataloggerstudio.main.Fragments.SettingsFragment;
import org.lielas.lielasdataloggerstudio.main.Fragments.ViewFragment;
import org.lielas.lielasdataloggerstudio.main.Tasks.UpdateManager;

import java.util.ArrayList;
import java.util.List;

public class Main extends ActionBarActivity {

    MyPageAdapter pageAdapter;

    int connectFragmentId = 0;
    int settingsFragmentId = 0;
    int dataFragmentId = 0;
    int viewFragmentId = 0;
    int exportFragmentId = 0;

    Logger logger;
    UpdateManager updateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        updateManager = new UpdateManager();

        logger = new UsbCube();
        logger.setCommunicationInterface(new UsbCubeSerialInterface());

        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

        UsbBroadcastReceiver broadcastReceiver = new UsbBroadcastReceiver(logger, updateManager);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_connect:
                pager.setCurrentItem(connectFragmentId);
                return true;
            case R.id.action_settings:
                pager.setCurrentItem(settingsFragmentId);
                return true;
            case R.id.action_data:
                pager.setCurrentItem(dataFragmentId);
                return true;
            case R.id.action_view:
                pager.setCurrentItem(viewFragmentId);
                return true;
            case R.id.action_export:
                pager.setCurrentItem(exportFragmentId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();

        //connect Fragment
        ConnectFragment connectFragment = ConnectFragment.newInstance();
        connectFragment.setLogger(logger);
        connectFragment.setUpdateManager(updateManager);
        updateManager.addFragment(connectFragment);
        fList.add(connectFragment);
        connectFragmentId = fList.size() - 1;

        //settings Fragment
        SettingsFragment settingsFragment = SettingsFragment.newInstance();
        settingsFragment.setLogger(logger);
        settingsFragment.setUpdateManager(updateManager);
        updateManager.addFragment(settingsFragment);
        fList.add(settingsFragment);
        settingsFragmentId = fList.size() - 1;

        //clock settings Fragment
        ClockSettingsFragment csf = ClockSettingsFragment.newInstance();
        csf.setLogger(logger);
        csf.setUpdateManager(updateManager);
        updateManager.addFragment(csf);
        fList.add(csf);


        //Logger settings Fragment
        LoggerSettingsFragment lsf = LoggerSettingsFragment.newInstance();
        lsf.setLogger(logger);
        lsf.setUpdateManager(updateManager);
        updateManager.addFragment(lsf);
        fList.add(lsf);

        //data fragment
        DataFragment dataFragment = DataFragment.newInstance();
        dataFragment.setLogger(logger);
        dataFragment.setUpdateManager(updateManager);
        updateManager.addFragment(dataFragment);
        fList.add(dataFragment);
        dataFragmentId = fList.size() - 1;


        //view fragment
        ViewFragment viewFragment = ViewFragment.newInstance();
        viewFragment.setLogger(logger);
        viewFragment.setUpdateManager(updateManager);
        updateManager.addFragment(viewFragment);
        fList.add(viewFragment);
        viewFragmentId = fList.size() - 1;

        //export fragment
        ExportFragment exportFragment = ExportFragment.newInstance();
        exportFragment.setLogger(logger);
        exportFragment.setUpdateManager(updateManager);
        updateManager.addFragment(exportFragment);
        fList.add(exportFragment);
        exportFragmentId = fList.size() - 1;


        return fList;
    }

}