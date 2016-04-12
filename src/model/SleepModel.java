package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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

    public void saveProfile(File f){
        ObjectMapper mapper = new ObjectMapper();
        // pretty print
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(f, this.profile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SleepProfile getProfile() {
        return profile;
    }

    public boolean addItem(SleepItem si) {
        boolean canAdd = true;

        // TODO rather use iterators and a while-loop and exit when canAdd = false
        for (SleepItem item : this.getProfile().getSleepItems()) {
            if (item.overlaps(si))
                canAdd = false;
        }

        if (canAdd) {
            this.getProfile().getSleepItems().add(si);

            setChanged();
            notifyObservers(this.profile);
        }

        return canAdd;
    }
}
