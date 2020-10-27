package rocks.voss.toniebox.beans.toniebox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import rocks.voss.toniebox.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreativeTonie {
    private String id;
    private String name;
    private boolean live;
    private boolean isPrivate;
    private String imageUrl;
    private String[] transcodingErrors;
    private boolean transcoding;
    private float secondsPresent;
    private float secondsRemaining;
    private int chaptersPresent;
    private int chaptersRemaining;
    private Chapter[] chapters;
    private String householdId;

    @JsonIgnore
    private Household household;

    @JsonIgnore
    private RequestHandler requestHandler;

    /**
     * Searchs for a chapter with given title
     *
     * @param title you're searching for
     * @return the chapter or null if not found
     */
    @JsonIgnore
    public Chapter findChapterByTitle(String title) {
        for (Chapter chapter : chapters) {
            if (StringUtils.equals(chapter.getTitle(), title)) {
                return chapter;
            }
        }
        return null;
    }

    /**
     * Deletes a chapter from the tonie
     *
     * @param chapter to be delete
     */
    @JsonIgnore
    public void deleteChapter(Chapter chapter) {
        List<Chapter> chapters = new ArrayList<>();
        for (Chapter chapterIter : this.chapters) {
            if (!StringUtils.equals(chapterIter.getId(), chapter.getId())) {
                chapters.add(chapterIter);
            }
        }
        this.chapters = chapters.toArray(new Chapter[]{});
    }

    /**
     * Uploads a file to Tonie box
     *
     * @param title of the new track
     * @param path  to the file which shall be uploaded
     * @throws IOException will be thrown if something goes wrong
     */
    @JsonIgnore
    public void uploadFile(String title, String path) throws IOException {
        requestHandler.uploadFile(this, new File(path), title);
    }

    /**
     * Save all changes to tonie
     * @throws IOException will be thrown if something goes wrong
     */
    @JsonIgnore
    public void commit() throws IOException {
        requestHandler.commitTonie(this);
    }

    /**
     * Refetch the newest updates on the tonie from the webserver
     * @throws IOException will be thrown is something goes wrong
     */
    @JsonIgnore
    public void refresh() throws IOException {
       CreativeTonie tmp =  requestHandler.refreshTonie(this);
       this.setId(tmp.getId());
       this.setName(tmp.getName());
       this.setLive(tmp.isLive());
       this.setPrivate(tmp.isPrivate);
       this.setImageUrl(tmp.getImageUrl());
       this.setTranscoding(tmp.isTranscoding());
       this.setSecondsPresent(tmp.getSecondsPresent());
       this.setSecondsRemaining(tmp.getChaptersRemaining());
       this.setChaptersPresent(tmp.getChaptersPresent());
       this.setChaptersRemaining(tmp.getChaptersRemaining());
       this.setChapters(tmp.getChapters());
       this.setHouseholdId(tmp.getHouseholdId());
    }
}
