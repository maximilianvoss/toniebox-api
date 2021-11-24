package rocks.voss.toniebox;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import rocks.voss.toniebox.beans.Transformer;
import rocks.voss.toniebox.beans.amazon.AmazonBean;
import rocks.voss.toniebox.beans.toniebox.Chapter;
import rocks.voss.toniebox.beans.toniebox.CreativeTonie;
import rocks.voss.toniebox.beans.toniebox.Household;
import rocks.voss.toniebox.beans.toniebox.JWTToken;
import rocks.voss.toniebox.beans.toniebox.Login;
import rocks.voss.toniebox.beans.toniebox.Me;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler {

    private final Logger log = Logger.getLogger(getClass().getName());

    private HttpHost proxy = null;
    private JWTToken jwtToken;
    private final Header[] headerContentTypeJson = new Header[]{new BasicHeader("Content-Type", "application/json")};

    public RequestHandler() {
    }

    public RequestHandler(String proxySchema, String proxyHost, int proxyPort) {
        this.proxy = new HttpHost(proxyHost, proxyPort, proxySchema);
    }

    public void Login(Login loginBean) throws IOException {
        jwtToken = executeLoginRequest(loginBean);
    }

    @SneakyThrows
    private JWTToken executeLoginRequest(Login loginBean) {
        HttpPost post = new HttpPost(Constants.OPENID_CONNECT);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("grant_type", "password"));
        params.add(new BasicNameValuePair("client_id", "my-tonies"));
        params.add(new BasicNameValuePair("scope", "openid"));
        params.add(new BasicNameValuePair("username", loginBean.getEmail()));
        params.add(new BasicNameValuePair("password", loginBean.getPassword()));
        post.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(post);
        return Transformer.createBean(JWTToken.class, response.getEntity().getContent());
    }

    public Me getMe() throws IOException {
        return executeGetRequest(Constants.ME, jwtToken, Me.class);
    }

    public Household[] getHouseholds() throws IOException {
        return executeGetRequest(Constants.HOUSEHOLDS, jwtToken, Household[].class);
    }

    public CreativeTonie[] getCreativeTonies(Household household) throws IOException {
        CreativeTonie[] creativeTonieBeans = executeGetRequest(URLBuilder.getUrl(Constants.CREATIVE_TONIES, household), jwtToken, CreativeTonie[].class);
        for (CreativeTonie creativeTonie : creativeTonieBeans) {
            creativeTonie.setHousehold(household);
            creativeTonie.setRequestHandler(this);
        }
        return creativeTonieBeans;
    }

    public CreativeTonie refreshTonie(CreativeTonie tonie) throws IOException {
        CreativeTonie returnTonie = executeGetRequest(URLBuilder.getUrl(Constants.CREATIVE_TONIE, tonie), jwtToken, CreativeTonie.class);
        returnTonie.setHousehold(tonie.getHousehold());
        return returnTonie;
    }

    public void commitTonie(CreativeTonie tonie) throws IOException {
        executePatchRequest(URLBuilder.getUrl(Constants.CREATIVE_TONIE, tonie), headerContentTypeJson,
                new StringEntity(Transformer.getJsonString(tonie), "UTF-8"), jwtToken, null);
    }

    public void uploadFile(CreativeTonie tonie, File file, String title) throws IOException {
        HttpEntity emptyBlock = new StringEntity("{headers:{}}", "UTF-8");
        AmazonBean amazonBean = executePostRequest(Constants.FILE_UPLOAD, headerContentTypeJson, emptyBlock, jwtToken, AmazonBean.class);

        HttpEntity entity = MultipartEntityBuilder.create()
                .setLaxMode()
                .addTextBody("key", amazonBean.getRequest().getFields().getKey())
                .addTextBody("x-amz-algorithm", amazonBean.getRequest().getFields().getXAmzAlgorithm())
                .addTextBody("x-amz-credential", amazonBean.getRequest().getFields().getXAmzCredential())
                .addTextBody("x-amz-date", amazonBean.getRequest().getFields().getXAmzDate())
                .addTextBody("policy", amazonBean.getRequest().getFields().getPolicy())
                .addTextBody("x-amz-signature", amazonBean.getRequest().getFields().getXAmzSignature())
                .addTextBody("x-amz-security-token", amazonBean.getRequest().getFields().getXAmzSecurityToken())
                .addBinaryBody("file", file, ContentType.DEFAULT_BINARY, amazonBean.getRequest().getFields().getKey())
                .build();

        executePostRequest(Constants.FILE_UPLOAD_AMAZON, null, entity, null, null);

        int chapterSize = tonie.getChapters().length;
        Chapter[] chapters = new Chapter[chapterSize + 1];
        System.arraycopy(tonie.getChapters(), 0, chapters, 0, chapterSize);
        chapters[chapterSize] = new Chapter();
        chapters[chapterSize].setTitle(title);
        chapters[chapterSize].setFile(amazonBean.getFileId());
        chapters[chapterSize].setId(amazonBean.getRequest().getFields().getKey());
        tonie.setChapters(chapters);
    }

    public void disconnect() throws IOException {
        executeDeleteRequest(Constants.SESSION, jwtToken, null);
    }

    private <T> T executeGetRequest(String URI, JWTToken jwtToken, Class<T> clazz) throws IOException {
        HttpGet method = new HttpGet(URI);
        return executeRequest(method, jwtToken, clazz);
    }

    private <T> T executeDeleteRequest(String URI, JWTToken jwtToken, Class<T> clazz) throws IOException {
        HttpDelete method = new HttpDelete(URI);
        return executeRequest(method, jwtToken, clazz);
    }

    private <T> T executePostRequest(String URI, Header[] headers, HttpEntity entity, JWTToken jwtToken, Class<T> clazz) throws IOException {
        HttpPost method = new HttpPost(URI);
        if (headers != null) {
            method.setHeaders(headers);
        }
        method.setEntity(entity);
        return executeRequest(method, jwtToken, clazz);
    }

    private <T> T executePatchRequest(String URI, Header[] headers, HttpEntity entity, JWTToken jwtToken, Class<T> clazz) throws IOException {
        HttpPatch method = new HttpPatch(URI);
        if (headers != null) {
            method.setHeaders(headers);
        }
        method.setEntity(entity);
        return executeRequest(method, jwtToken, clazz);
    }

    private <T> T executeRequest(HttpRequestBase method, JWTToken jwtToken, Class<T> clazz) throws IOException {
        int CONNECTION_TIMEOUT_MS = 5000;

        if (jwtToken != null) {
            method.addHeader("Authorization", "Bearer " + jwtToken.getAccessToken());
        }

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS)
                .setSocketTimeout(CONNECTION_TIMEOUT_MS)
                .build();
        method.setConfig(requestConfig);

        log.debug(method.toString());

        CloseableHttpResponse response;
        response = getHttpClient().execute(method);
        log.debug("Status Code: " + response.getStatusLine());
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        try (InputStream inputStream = entity.getContent()) {
            if (clazz == null) {
                String body = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                log.debug("Response body: " + body);
                return null;
            }
            return Transformer.createBean(clazz, inputStream);
        }
    }

    private CloseableHttpClient getHttpClient() {
        if (proxy == null) {
            return HttpClients.createDefault();
        } else {
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            return HttpClients.custom()
                    .setRoutePlanner(routePlanner)
                    .build();
        }
    }
}
