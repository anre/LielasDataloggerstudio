package org.lielas.lielasdataloggerstudio.main;

import android.app.Application;

import org.acra.*;
import org.acra.annotation.*;
import org.lielas.lielasdataloggerstudio.R;

@ReportsCrashes(
        formKey = "",
        formUri = "http://http://134.119.24.50:5984/acra-lielas/_design/acra-storage/_update/report",
        reportType = org.acra.sender.HttpSender.Type.JSON,
        httpMethod = org.acra.sender.HttpSender.Method.PUT,
        formUriBasicAuthLogin="ntiontefeentaidessonatio",
        formUriBasicAuthPassword="CxqXGN2txNUA6fjdsALGQbdc",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.errorToast
        //forceCloseDialogAfterToast = false,
        /*resDialogText = R.string.errorTxt,
        resDialogCommentPrompt = R.string.errorTxtComment,
        resDialogTitle = R.string.errorTitle*/)
public class LielasDataloggerstudio extends Application{

    @Override
    public void onCreate(){
        super.onCreate();
        ACRA.init(this);
    }
}

