package rocks.voss.toniebox;

import rocks.voss.toniebox.beans.toniebox.CreativeTonie;
import rocks.voss.toniebox.beans.toniebox.Household;
import rocks.voss.toniebox.beans.toniebox.Login;
import rocks.voss.toniebox.beans.toniebox.Me;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TonieHandler {
    final private RequestHandler requestHandler;

    /**
     * Default constructor
     */
    public TonieHandler() {
        requestHandler = new RequestHandler();
    }

    /**
     * Constructor for Proxy usage
     * @param proxySchema http/https
     * @param proxyHost to which proxy host to connect
     * @param proxyPort port on which the proxy listens
     */
    public TonieHandler(String proxySchema, String proxyHost, int proxyPort) {
        requestHandler = new RequestHandler(proxySchema, proxyHost, proxyPort);
    }

    /**
     * Login the toniebox user
     *
     * @param username for your Toniebox Account
     * @param password for your Toniebox Account
     * @throws IOException will be thrown if something goes wrong
     */
    public void login(String username, String password) throws IOException {
        Login loginBean = new Login();
        loginBean.setEmail(username);
        loginBean.setPassword(password);
        requestHandler.Login(loginBean);
    }

    /**
     * This method retieves the households you're in
     *
     * @return list of households
     * @throws IOException will be thrown if something goes wrong
     */
    public List<Household> getHouseholds() throws IOException {
        return Arrays.asList(requestHandler.getHouseholds());
    }

    /**
     * This method loads all available Tonies you may want to interact with
     *
     * @param household the household to which the Tonie belongs
     * @return a list of CreativeTonies
     * @throws IOException will be thrown if something goes wrong
     */
    public List<CreativeTonie> getCreativeTonies(Household household) throws IOException {
        return Arrays.asList(requestHandler.getCreativeTonies(household));
    }

    /**
     * This method retrieves your personal information
     *
     * @return personal information
     * @throws IOException will be thrown if something goes wrong
     */
    public Me getMe() throws IOException {
        return requestHandler.getMe();
    }

    /**
     * This method disconnects and destroies the sessoin from Tonie
     * @throws IOException
     */
    public void disconnect() throws IOException {
        requestHandler.disconnect();
    }
}
