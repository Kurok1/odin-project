package indi.odin;

import java.net.URL;
import java.util.*;

/**
 * TODO
 *
 * @author <a href="mailto:hanchao@66yunlian.com">韩超</a>
 * @since 1.0.0
 */
public class UrlReader {

    private final URL url;
    private final Map<String, String> params = new HashMap<>();

    public UrlReader(URL url) {
        Objects.requireNonNull(url);
        this.url = url;
        resolve();
    }

    private void resolve() {
        String query = url.getQuery();
        if (query != null && query.length() > 0) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                this.params.put(keyValue[0], keyValue[1]);
            }
        }
    }

    public String getProtocol() {
        return this.url.getProtocol();
    }

    public String getHost() {
        return this.url.getHost();
    }

    public int getPort() {
        return this.url.getPort();
    }

    public String getPath() {
        return this.url.getPath().substring(1);
    }

    public String getValue(String key) {
        return this.params.get(key);
    }

    public Collection<String> getKeys() {
        return Collections.unmodifiableSet(params.keySet());
    }
}
