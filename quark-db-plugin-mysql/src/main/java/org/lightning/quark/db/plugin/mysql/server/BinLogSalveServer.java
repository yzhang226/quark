package org.lightning.quark.db.plugin.mysql.server;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import org.lightning.quark.core.model.db.DataSourceParam;
import org.lightning.quark.core.subscribe.RowChangeDispatcher;
import org.lightning.quark.db.datasource.DSFactory;
import org.lightning.quark.db.plugin.mysql.binlog.BinLogEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;

/**
 * Created by cook on 2018/3/9
 */
public class BinLogSalveServer extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(BinLogSalveServer.class);

    private DataSourceParam param;
    private String host;
    private int port;
    private RowChangeDispatcher dispatcher;

    public BinLogSalveServer(DataSourceParam param, RowChangeDispatcher dispatcher) {
        this.param = param;
        URI uri = URI.create(param.getUrl().substring(5));
        host = uri.getHost();
        port = uri.getPort();
        this.dispatcher = dispatcher;

        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                BinaryLogClient client = new BinaryLogClient(host, port, param.getUsername(), param.getPassword());
                BinLogEventListener binLogEventListener = new BinLogEventListener(dispatcher);
                client.registerEventListener(binLogEventListener);

                logger.info("connecting to data-source");

                client.connect();
            } catch (IOException e) {
                logger.error("", e);
            }
            logger.warn("server client is disconnected, try reconnect");
        }
    }

}
