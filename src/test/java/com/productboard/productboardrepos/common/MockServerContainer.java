package com.productboard.productboardrepos.common;

import org.testcontainers.containers.GenericContainer;

public class MockServerContainer extends GenericContainer<MockServerContainer> {

    public static final int PORT = 1080;

    static final String MOCKSERVER_IMAGE = "czdcm-quay.lx.ifortuna.cz/testcontainers/mockserver:5.10.0";

    public MockServerContainer() {
        super(MOCKSERVER_IMAGE);
        withCommand("-logLevel INFO -serverPort " + PORT);
        addExposedPorts(PORT);
    }

    public void close() {
        super.stop();
    }
}
