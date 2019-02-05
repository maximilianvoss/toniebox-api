package rocks.voss.toniebox;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import rocks.voss.toniebox.beans.amazon.AmazonBean;
import rocks.voss.toniebox.beans.amazon.TonieContentBean;
import rocks.voss.toniebox.beans.toniebox.TonieUpdateBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler {
    private HttpClient httpClient = new HttpClient();
    private Cookie[] cookies;
    private String crfToken;

    public void Login(String login, String password) throws IOException {
        GetMethod getMethod = new GetMethod(URLBuilder.getTonieUrl(Constants.LOGIN_PAGE));
        executeMethod(getMethod);

        PostMethod postMethod = new PostMethod(URLBuilder.getTonieUrl(Constants.LOGIN_PAGE));
        postMethod.getParams().setCookiePolicy(org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        postMethod.setRequestHeader("Upgrade-Insecure-Requests", "1");
        postMethod.setParameter("csrfmiddlewaretoken", crfToken);
        postMethod.setParameter("login", login);
        postMethod.setParameter("password", password);
        postMethod.setParameter("submit", "Login+now");
        executeMethod(postMethod);
    }

    private void extractCRFToken(HttpMethod method) throws IOException {
        String response = method.getResponseBodyAsString();
        if ( response == null ) {
            return;
        }
        Pattern pattern = Pattern.compile("name='csrfmiddlewaretoken' value='(\\w+)'");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            crfToken = matcher.group(1);
        }
    }

    public List<Tonie> getTonies() throws IOException {
        List<Tonie> tonies = new ArrayList<>();

        GetMethod getMethod = new GetMethod(URLBuilder.getTonieUrl(Constants.SUMMARY_PAGE));
        executeMethod(getMethod);

        Pattern pattern = Pattern.compile("<a href=\"/tonies/(\\w+)/\" title=.+\n\\s+<span>(.+)</span>");
        Matcher matcher = pattern.matcher(getMethod.getResponseBodyAsString());
        while (matcher.find()) {
            Tonie tonie = new Tonie();
            tonie.setTonieId(matcher.group(1));
            tonie.setName(matcher.group(2));
            tonies.add(tonie);
        }
        return tonies;
    }

    public void getToniePage(Tonie tonie) throws IOException {
        GetMethod getMethod = new GetMethod(URLBuilder.getTonieUrl(Constants.TONIE_PAGE, tonie));
        executeMethod(getMethod);
    }

    public TonieContentBean getTonieDetails(Tonie tonie) throws IOException {
        GetMethod getMethod = new GetMethod(URLBuilder.getTonieUrl(Constants.TONIE_CONTENT, tonie));
        executeMethod(getMethod);
        return TonieContentBean.createBean(getMethod.getResponseBodyAsString());
    }

    public void updateTonie(Tonie tonie, TonieUpdateBean updateBean) throws IOException {
        PutMethod putMethod = new PutMethod(URLBuilder.getTonieUrl(Constants.TONIE_CONTENT, tonie));
        putMethod.setRequestHeader("X-CSRFToken", crfToken);
        putMethod.setRequestEntity(new StringRequestEntity(updateBean.getJson().toString(), "application/json", "UTF-8"));
        executeMethod(putMethod);
    }

    public void changeTonieName(Tonie tonie, String name) throws IOException {
        PostMethod postMethod = new PostMethod(URLBuilder.getTonieUrl(Constants.TONIE_NAME, tonie));
        postMethod.setRequestBody(new NameValuePair[]{new NameValuePair("csrfmiddlewaretoken", crfToken), new NameValuePair("name", name)});
        executeMethod(postMethod);
    }

    public AmazonBean getAmazonCredentials() throws IOException {
        GetMethod getMethod = new GetMethod(URLBuilder.getTonieUrl(Constants.TONIE_AMAZON_PRE_SIGNED_URL, crfToken));
        executeMethod(getMethod);
        return AmazonBean.createBean(getMethod.getResponseBodyAsString());
    }

    public void uploadFile(AmazonBean amazonBean, File file) throws IOException {
        PostMethod postMethod = new PostMethod(amazonBean.getUrl().getUrl());

        Part[] parts = {
                new StringPart("key", amazonBean.getUrl().getFields().getKey()),
                new StringPart("x-amz-algorithm", amazonBean.getUrl().getFields().getXAmzAlgorithm()),
                new StringPart("x-amz-credential", amazonBean.getUrl().getFields().getXAmzCredential()),
                new StringPart("x-amz-date", amazonBean.getUrl().getFields().getXAmzDate()),
                new StringPart("policy", amazonBean.getUrl().getFields().getPolicy()),
                new StringPart("x-amz-signature", amazonBean.getUrl().getFields().getXAmzSignature()),
                new FilePart("file", "filename", file)
        };

        postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));
        executeMethod(postMethod);
    }

    private void executeMethod(HttpMethod method) throws IOException {
        httpClient.getState().addCookies(cookies);
        httpClient.executeMethod(method);
        extractCRFToken(method);
        cookies = httpClient.getState().getCookies();
    }
}
