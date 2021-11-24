package rocks.voss.toniebox.examples;

import rocks.voss.toniebox.TonieHandler;
import rocks.voss.toniebox.beans.toniebox.CreativeTonie;
import rocks.voss.toniebox.beans.toniebox.Household;
import rocks.voss.toniebox.beans.toniebox.Me;

import java.io.IOException;
import java.util.List;

public class RenameTonie {
    public static void main(String[] args) throws IOException {
        TonieHandler tonieHandler = new TonieHandler();
        tonieHandler.login(Constants.USERNAME, Constants.PASSWORD);

        // get what is stored about you as a person
        Me me = tonieHandler.getMe();

        // get all households you're in & select first one
        List<rocks.voss.toniebox.beans.toniebox.Household> households = tonieHandler.getHouseholds();
        Household household = households.get(0);

        // get all creative tonies & select first one
        List<CreativeTonie> creativeTonies = tonieHandler.getCreativeTonies(household);
        CreativeTonie creativeTonie = creativeTonies.get(0);

        // rename the creative tonie and reset the name
        String oldName = creativeTonie.getName();
        creativeTonie.setName("--THIS IS A TEST NAME--");
        creativeTonie.commit();
        creativeTonie.setName(oldName);
        creativeTonie.commit();

        tonieHandler.disconnect();
    }
}
