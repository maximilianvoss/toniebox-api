package rocks.voss.toniebox.examples;

import rocks.voss.toniebox.TonieHandler;
import rocks.voss.toniebox.beans.toniebox.Me;

import java.io.IOException;
import java.util.List;

public class Household {
    public static void main(String[] args) throws IOException {
        TonieHandler tonieHandler = new TonieHandler();
        tonieHandler.login(Constants.USERNAME, Constants.PASSWORD);

        // get what is stored about you as a person
        Me me = tonieHandler.getMe();
        System.out.println(me);

        // get all households and show their names
        List<rocks.voss.toniebox.beans.toniebox.Household> households = tonieHandler.getHouseholds();
        for (rocks.voss.toniebox.beans.toniebox.Household household : households) {
            System.out.println(household.getName());
        }

        tonieHandler.disconnect();
    }
}
