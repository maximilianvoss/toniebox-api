package rocks.voss.toniebox.examples;

import rocks.voss.toniebox.TonieHandler;
import rocks.voss.toniebox.beans.toniebox.Chapter;
import rocks.voss.toniebox.beans.toniebox.CreativeTonie;
import rocks.voss.toniebox.beans.toniebox.Household;

import java.io.IOException;
import java.util.List;

public class ListAllTracks {
    public static void main(String[] args) throws IOException {
        TonieHandler tonieHandler = new TonieHandler();
        tonieHandler.login(Constants.USERNAME, Constants.PASSWORD);

        List<rocks.voss.toniebox.beans.toniebox.Household> households = tonieHandler.getHouseholds();
        for (Household household : households) {
            System.out.println("Household: " + household.getName());

            List<CreativeTonie> creativeTonies = tonieHandler.getCreativeTonies(household);
            for (CreativeTonie creativeTonie : creativeTonies) {
                System.out.println("Tonie: " + creativeTonie.getName());
                Chapter chapters[] = creativeTonie.getChapters();
                for (Chapter chapter : chapters) {
                    System.out.println(chapter.getId() + "\t" + chapter.getTitle());
                }
                System.out.println("");
            }
            System.out.println("");
        }
        tonieHandler.disconnect();
    }
}
