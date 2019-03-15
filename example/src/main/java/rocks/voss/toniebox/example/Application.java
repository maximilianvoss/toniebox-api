package rocks.voss.toniebox.example;

import org.apache.commons.io.IOUtils;
import rocks.voss.toniebox.TonieHandler;
import rocks.voss.toniebox.beans.toniebox.Chapter;
import rocks.voss.toniebox.beans.toniebox.CreativeTonie;
import rocks.voss.toniebox.beans.toniebox.Household;
import rocks.voss.toniebox.beans.toniebox.Me;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Application {
    private final static String USERNAME = "<YOUR TONIEBOX USERNAME>";
    private final static String PASSWORD = "<YOUR TONIEBOX PASSWORD";

    public static void main(String[] args) throws IOException {
        TonieHandler tonieHandler = new TonieHandler();
        tonieHandler.login(USERNAME, PASSWORD);

        // get what is stored about you as a person
        Me me = tonieHandler.getMe();
        System.out.println(me);

        // get all households you're in & select first one
        List<Household> households = tonieHandler.getHouseholds();
        Household household = households.get(0);

        // get all creative tonies & select first one
        List<CreativeTonie> creativeTonies = tonieHandler.getCreativeTonies(household);
        CreativeTonie creativeTonie = creativeTonies.get(0);

        // load a MP3 out of the resource as dummy
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        File tempFile = File.createTempFile("test", ".mp3");
        tempFile.deleteOnExit();
        IOUtils.copy(loader.getResourceAsStream("file.mp3"), new FileOutputStream(tempFile));

        // Get the latest states of the tonie
        System.out.println("Number of chapters on the tonie: " + creativeTonie.getChaptersPresent());

        // upload the dummy MP3 to the tonie box
        creativeTonie.uploadFile("--THIS IS A TEST UPLOAD--", tempFile.getAbsolutePath());
        creativeTonie.commit();

        // Refresh and get the latest states of the tonie
        creativeTonie.refresh();
        System.out.println("Number of chapters on the tonie: " + creativeTonie.getChaptersPresent());

        // find the uploaded chapter again and delete it
        Chapter chapter = creativeTonie.findChapterByTitle("--THIS IS A TEST UPLOAD--");
        creativeTonie.deleteChapter(chapter);
        creativeTonie.commit();

        // Refresh and get the latest states of the tonie
        creativeTonie.refresh();
        System.out.println("Number of chapters on the tonie: " + creativeTonie.getChaptersPresent());

        // rename the creative tonie and reset the name
        String oldName = creativeTonie.getName();
        creativeTonie.setName("--THIS IS A TEST NAME--");
        creativeTonie.commit();
        creativeTonie.setName(oldName);
        creativeTonie.commit();
    }
}
