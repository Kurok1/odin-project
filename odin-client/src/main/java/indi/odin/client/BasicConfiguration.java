package indi.odin.client;

import java.util.List;

/**
 * 基础配置
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public abstract class BasicConfiguration {

    private List<ServerConfiguration> serverList;

    public List<ServerConfiguration> getServerList() {
        return serverList;
    }

    public void setServerList(List<ServerConfiguration> serverList) {
        this.serverList = serverList;
    }

    public static class ServerConfiguration {
        private String address;
        private int port;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

}
