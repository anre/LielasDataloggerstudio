package org.lielas.micdataloggerstudio.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.lielas.dataloggerstudio.lib.Logger.Logger;
import org.lielas.dataloggerstudio.lib.Logger.mic.MicUSBStick;
import org.lielas.dataloggerstudio.lib.LoggerRecord;
import org.lielas.micdataloggerstudio.R;
import org.lielas.micdataloggerstudio.main.CommunicationInterface.mic.MicSerialInterface;
import org.lielas.micdataloggerstudio.main.Fragments.ConnectFragment;
import org.lielas.micdataloggerstudio.main.Fragments.DataFragment;
import org.lielas.micdataloggerstudio.main.Fragments.DeviceInfoFragment;
import org.lielas.micdataloggerstudio.main.Fragments.ExportFragment;
import org.lielas.micdataloggerstudio.main.Fragments.GraphFragment;
import org.lielas.micdataloggerstudio.main.Fragments.LiveFragment;
import org.lielas.micdataloggerstudio.main.Fragments.SettingsFragment;
import org.lielas.micdataloggerstudio.main.Fragments.ViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andi on 06.04.2015.
 */
public class Main extends ActionBarActivity{

    MyPageAdapter pageAdapter;
    Logger logger;
    UpdateManager updateManager;

    int connectId = 0;
    int settingsId = 0;
    int dataId = 0;
    int viewId = 0;
    int exportId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        updateManager = new UpdateManager();

        logger = new MicUSBStick();
        logger.setCommunicationInterface(new MicSerialInterface());

        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

        LoggerRecord lr = new LoggerRecord(logger);
        LoggerRecordManager.getInstance().setActiveLoggerRecord(lr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);

        switch(item.getItemId()){
            case R.id.action_connect:
                pager.setCurrentItem(connectId);
                break;
            case R.id.action_settings:
                pager.setCurrentItem(settingsId);
                break;
            case R.id.action_data:
                pager.setCurrentItem(dataId);
                break;
            case R.id.action_view:
                pager.setCurrentItem(viewId);
                break;
            case R.id.action_export:
                pager.setCurrentItem(exportId);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();


        //connect Fragment
        ConnectFragment connectFragment = ConnectFragment.newInstance();
        connectFragment.setLogger(logger);
        connectFragment.setUpdateManager(updateManager);
        updateManager.addFragment(connectFragment);
        fList.add(connectFragment);
        connectId = fList.size() - 1;

        //Device Info Fragment
        DeviceInfoFragment deviceInfoFragment = DeviceInfoFragment.newInstance();
        deviceInfoFragment.setLogger(logger);
        deviceInfoFragment.setUpdateManager(updateManager);
        updateManager.addFragment(deviceInfoFragment);
        fList.add(deviceInfoFragment);

        //Settings Fragment
        SettingsFragment settingsFragment = SettingsFragment.newInstance();
        settingsFragment.setLogger(logger);
        settingsFragment.setUpdateManager(updateManager);
        updateManager.addFragment(settingsFragment);
        fList.add(settingsFragment);
        settingsId = fList.size() - 1;

        //Data Fragment
        DataFragment dataFragment = DataFragment.newInstance();
        dataFragment.setLogger(logger);
        dataFragment.setUpdateManager(updateManager);
        updateManager.addFragment(dataFragment);
        fList.add(dataFragment);
        dataId = fList.size() - 1;

        //View Fragment
        ViewFragment viewFragment = ViewFragment.newInstance();
        viewFragment.setLogger(logger);
        viewFragment.setUpdateManager(updateManager);
        updateManager.addFragment(viewFragment);
        fList.add(viewFragment);
        viewId = fList.size() - 1;

        // Graph Fragment
        GraphFragment graphFragment = GraphFragment.newInstance();
        graphFragment.setLogger(logger);
        graphFragment.setUpdateManager(updateManager);
        updateManager.addFragment(graphFragment);
        fList.add(graphFragment);


        //Export Fragment
        ExportFragment exportFragment = ExportFragment.newInstance();
        exportFragment.setLogger(logger);
        exportFragment.setUpdateManager(updateManager);
        updateManager.addFragment(exportFragment);
        fList.add(exportFragment);
        exportId = fList.size() - 1;

        //Live Fragment
        /*LiveFragment liveFragment = LiveFragment.newInstance();
        liveFragment.setLogger(logger);
        liveFragment.setUpdateManager(updateManager);
        updateManager.addFragment(liveFragment);
        fList.add(liveFragment);*/



        return fList;
    }

}
