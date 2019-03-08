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

    public static void main(String[] args) throws IOException, InterruptedException {
        TonieHandler tonieHandler = new TonieHandler(USERNAME, PASSWORD);

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
        tempFile.getAbsoluteFile();
        IOUtils.copy(loader.getResourceAsStream("file.mp3"), new FileOutputStream(tempFile));

        // upload the dummy MP3 to the tonie box
        creativeTonie.uploadFile("--THIS IS A TEST UPLOAD--", tempFile.getAbsolutePath());

        // find the uploaded chapter again and delete it
        Chapter chapter = creativeTonie.findChapterByTitle("--THIS IS A TEST UPLOAD--");
        creativeTonie.deleteChapter(chapter);

        // rename the creative tonie and reset the name
        String oldName = creativeTonie.getName();
        creativeTonie.setName("--THIS IS A TEST NAME--");
        creativeTonie.setName(oldName);

        // do a commit - write changes to tonie box
        // there seems to be a request limit per second/minute so don't commit every small change
        creativeTonie.commit();
    }
}
