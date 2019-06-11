package com.killer.annoy;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import java.util.List;

public class KillerService extends TileService {
    @Override
    public void onTileAdded() {
        super.onTileAdded();

        Tile qsTile = getQsTile();
        qsTile.setState(Tile.STATE_ACTIVE);
        qsTile.updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();

        Tile qsTile = getQsTile();
        // kill something
        qsTile.setLabel("Killing...");
        qsTile.updateTile();
        kill();
    }

    public void kill() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String regex = pref.getString("kill", ".*youtube.*");
        int attempt = pref.getInt("num", 0) + 1;

        List<PackageInfo> packs = getApplicationContext().getPackageManager().getInstalledPackages(0);

        ActivityManager am = (ActivityManager)getSystemService(Activity.ACTIVITY_SERVICE);

        StringBuilder sb = new StringBuilder(String.format("KILL ATTEMPT #%d with %s\n", attempt, regex));
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(currentTime);
        sb.append("Executed Time: ");
        sb.append(strDate);
        sb.append('\n');

        for (PackageInfo pack : packs){
            if (Pattern.matches(regex, pack.packageName)) {
                sb.append(pack.packageName);
                sb.append('\n');
                am.killBackgroundProcesses(pack.packageName);
            }
        }

        // save SharedPreferences
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("log", sb.toString());
        editor.putInt("num", attempt);
        editor.commit();

        Tile qsTile = getQsTile();
        qsTile.setLabel("Killer");
        qsTile.updateTile();
    }
}