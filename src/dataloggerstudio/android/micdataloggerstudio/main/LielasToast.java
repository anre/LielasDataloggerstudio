package org.lielas.micdataloggerstudio.main;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Andi on 08.04.2015.
 */
public class LielasToast {

    public static void show(String text, Context context){
        if(context == null || text == null){
            return;
        }

        Toast t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        t.show();
    }
}
