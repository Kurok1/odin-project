package indi.odin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接池
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 * @see java.util.Map
 */
public final class ConnectionPool {

    private final Map<String, ConnectionSource> sourceMap = new ConcurrentHashMap<>();

    private ConnectionPool() {

    }

    public static final ConnectionPool INSTANCE = new ConnectionPool();

    public void registerConnection(ConnectionSource source) {
        if (source == null)
            throw new NullPointerException();
        String name = source.getName();
        this.sourceMap.putIfAbsent(name, source);
    }

    public ConnectionSource getSource(String name) {
        return this.sourceMap.get(name);
    }

    public void clear() {
        this.sourceMap.clear();
    }

}
