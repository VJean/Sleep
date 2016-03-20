package model;

/**
 * Created by JeanV on 20/03/2016.
 */
public class Profile {
    private static Profile ourInstance = new Profile();

    public static Profile getInstance() {
        return ourInstance;
    }

    private Profile() {
    }
}
