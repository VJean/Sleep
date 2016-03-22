package model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Observable;

/**
 * Created by JeanV on 20/03/2016.
 */
public class SleepModel extends Observable{
    private static SleepModel ourInstance = new SleepModel();

    private SleepProfile profile = new SleepProfile();

    private SleepModel(){

    }

    public static SleepModel getInstance() {
        return ourInstance;
    }


    public void loadProfile(File f){
        ObjectMapper mapper = new ObjectMapper();
        try {
            SleepProfile parsedProfile = mapper.readValue(f, SleepProfile.class);
            this.updateProfile(parsedProfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProfile(SleepProfile p) {
        this.profile.setName(p.getName());
        this.profile.setPlaces(p.getPlaces());
        this.profile.setSleepItems(p.getSleepItems());

        // notify observers
        setChanged();
        notifyObservers(this.profile);
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
