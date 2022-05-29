package indi.odin;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * empty implements {@link URLStreamHandler}
 *
 * @author <a href="mailto:maimengzzz@gmail">韩超</a>
 * @since 1.0.0
 */
public class OdinURLStreamHandler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return null;
    }

}
