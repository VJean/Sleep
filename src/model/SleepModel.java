package model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

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
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.profile = mapper.readValue(f, SleepProfile.class);
            System.out.println(this.profile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProfile(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(System.out, this.profile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SleepProfile getProfile() {
        return profile;
    }
}
