package rocks.voss.toniebox;

import rocks.voss.toniebox.beans.Tonie;
import rocks.voss.toniebox.beans.amazon.AmazonBean;
import rocks.voss.toniebox.beans.toniebox.TonieContentBean;
import rocks.voss.toniebox.beans.toniebox.TonieChapterBean;
import rocks.voss.toniebox.beans.toniebox.TonieUpdateBean;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TonieHandler {
    private final RequestHandler requestHandler = new RequestHandler();

    /**
     * Constructor to initialize the TonieHandler to run any action on your tonies
     * @param username for your Tonie Account
     * @param password for your Tonie Account
     * @throws IOException will be thrown if something goes wrong
     */
    public TonieHandler(String username, String password) throws IOException {
        requestHandler.Login(username, password);
    }

    /**
     * This method loads all available Tonies you may want to interact with
     * @return a list of Tonies
     * @throws IOException will be thrown if something goes wrong
     */
    public List<Tonie> getTonies() throws IOException {
        return requestHandler.getTonies();
    }

    /**
     * This methods gives you all the details of the Tonies which is stored in the web (like chapters, etc)
     * @param tonie you want to know more about
     * @return detailed Tonie information
     * @throws IOException will be thrown if something goes wrong
     */
    public TonieContentBean getTonieDetails(Tonie tonie) throws IOException {
        requestHandler.getToniePage(tonie);
        return requestHandler.getTonieDetails(tonie);
    }

    /**
     * This method deletes the entire content stored on the tonie
     * @param tonie you want to erase
     * @throws IOException will be thrown if something goes wrong
     */
    public void deleteTonieContent(Tonie tonie) throws IOException {
        TonieContentBean tonieContentBean = getTonieDetails(tonie);
        TonieUpdateBean tonieUpdateBean = new TonieUpdateBean();
        tonieUpdateBean.setContent(tonieContentBean.getData());
        tonieUpdateBean.setDeletedChapters(tonieContentBean.getData().getChapters());
        tonieUpdateBean.setCurrentChapters(new TonieChapterBean[]{});
        requestHandler.updateTonie(tonie, tonieUpdateBean);
    }

    /**
     * Tbis method can change the tonies name
     * @param tonie whose name you wnat to change
     * @param name you want to set
     * @throws IOException will be thrown if something goes wrong
     */
    public void changeTonieName(Tonie tonie, String name) throws IOException {
        getTonieDetails(tonie);
        requestHandler.changeTonieName(tonie, name);
    }

    /**
     * This method uploads a new file to the associated Tonie
     * @param tonie to which you want to bind the new uploaded file
     * @param title you want to set, visible on the my tonie website
     * @param path to the file you want to upload
     * @throws IOException will be thrown if something goes wrong
     */
    public void uploadFile(Tonie tonie, String title, String path) throws IOException {
        TonieContentBean tonieContentBean = getTonieDetails(tonie);

        AmazonBean amazonBean = requestHandler.getAmazonCredentials();
        requestHandler.uploadFile(amazonBean, new File(path));

        TonieUpdateBean tonieUpdateBean = new TonieUpdateBean();
        tonieUpdateBean.setContent(tonieContentBean.getData());
        tonieUpdateBean.setDeletedChapters(new TonieChapterBean[]{});

        int chapterSize = tonieContentBean.getData().getChapters().length;
        TonieChapterBean chapters[] = new TonieChapterBean[chapterSize + 1];
        System.arraycopy(tonieContentBean.getData().getChapters(), 0, chapters, 0, chapterSize);
        chapters[chapterSize] = new TonieChapterBean();
        chapters[chapterSize].setTitle(title);
        chapters[chapterSize].setFile(amazonBean.getUuid());
        chapters[chapterSize].setPosition(chapterSize + 1);
        tonieUpdateBean.setCurrentChapters(chapters);

        requestHandler.updateTonie(tonie, tonieUpdateBean);
    }
}
