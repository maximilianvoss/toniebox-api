package rocks.voss.toniebox;

import rocks.voss.toniebox.beans.amazon.AmazonBean;
import rocks.voss.toniebox.beans.amazon.TonieContentBean;
import rocks.voss.toniebox.beans.toniebox.TonieChapterBean;
import rocks.voss.toniebox.beans.toniebox.TonieUpdateBean;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TonieHandler {
    private RequestHandler requestHandler = new RequestHandler();

    public TonieHandler(String login, String password) throws IOException {
        requestHandler.Login(login, password);
    }

    public List<Tonie> getTonies() throws IOException {
        return requestHandler.getTonies();
    }

    public TonieContentBean getTonieDetails(Tonie tonie) throws IOException {
        requestHandler.getToniePage(tonie);
        return requestHandler.getTonieDetails(tonie);
    }

    public void deleteTonieContent(Tonie tonie) throws IOException {
        TonieContentBean tonieContentBean = getTonieDetails(tonie);
        TonieUpdateBean tonieUpdateBean = new TonieUpdateBean();
        tonieUpdateBean.setContent(tonieContentBean.getData());
        tonieUpdateBean.setDeletedChapters(tonieContentBean.getData().getChapters());
        tonieUpdateBean.setCurrentChapters(new TonieChapterBean[]{});
        requestHandler.updateTonie(tonie, tonieUpdateBean);
    }

    public void changeTonieName(Tonie tonie, String name) throws IOException {
        getTonieDetails(tonie);
        requestHandler.changeTonieName(tonie, name);
    }

    public void uploadFile(Tonie tonie, String title, String path) throws IOException {
        TonieContentBean tonieContentBean = getTonieDetails(tonie);

        AmazonBean amazonBean = requestHandler.getAmazonCredentials();
        requestHandler.uploadFile(amazonBean, new File(path));

        TonieUpdateBean tonieUpdateBean = new TonieUpdateBean();
        tonieUpdateBean.setContent(tonieContentBean.getData());
        tonieUpdateBean.setDeletedChapters(new TonieChapterBean[]{});

        int chapterSize = tonieContentBean.getData().getChapters().length;
        TonieChapterBean chapters[] = new TonieChapterBean[chapterSize + 1];
        for ( int i = 0 ; i < chapterSize; i++ ) {
            chapters[i] = tonieContentBean.getData().getChapters()[i];
        }
        chapters[chapterSize] = new TonieChapterBean();
        chapters[chapterSize].setTitle(title);
        chapters[chapterSize].setFile(amazonBean.getUuid());
        chapters[chapterSize].setPosition(chapterSize + 1);
        tonieUpdateBean.setCurrentChapters(chapters);

        requestHandler.updateTonie(tonie, tonieUpdateBean);
    }
}
