package model;

import java.io.File;

/**
 * Created by JeanV on 20/03/2016.
 */
public class SleepModel {
    private static SleepModel ourInstance = new SleepModel();

    private SleepProfile profile;

    private SleepModel(){

    }

    public static SleepModel getInstance() {
        return ourInstance;
    }


    public void loadProfile(File f){

    }

}
