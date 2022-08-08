package indi.odin;

import indi.odin.client.BasicConfiguration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 连接源
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public abstract class ConnectionSource<C extends BasicConfiguration> {

    protected final String name;
    protected final URL url;

    protected final Map<String, String> params = new HashMap<>();
    protected String host;
    protected int port;
    protected SupportProduct product;
    protected String path;

    public ConnectionSource(String name, String url) throws MalformedURLException {
        this(name, new URL(null, url, new OdinURLStreamHandler()));
    }

    public ConnectionSource(String name, URL url) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(url);
        this.name = name;
        this.url = url;
        readUrl();
    }

    private void readUrl() {
        UrlReader reader = new UrlReader(this.url);

        this.host = reader.getHost();
        this.port = reader.getPort();
        this.path = reader.getPath();
        this.product = SupportProduct.read(reader.getProtocol());

        Collection<String> keys = reader.getKeys();

        for (String key : keys)
            this.params.put(key, reader.getValue(key));
    }

    public BasicConfiguration.ServerConfiguration getServer() {
        BasicConfiguration.ServerConfiguration serverConfiguration = new BasicConfiguration.ServerConfiguration();
        serverConfiguration.setAddress(getHost());
        serverConfiguration.setPort(getPort());

        return serverConfiguration;
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public SupportProduct getProduct() {
        return product;
    }
    public abstract C resolveConfiguration();
}
