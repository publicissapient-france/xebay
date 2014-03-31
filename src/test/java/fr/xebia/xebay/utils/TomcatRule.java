package fr.xebia.xebay.utils;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.naming.resources.VirtualDirContext;
import org.junit.rules.ExternalResource;

import java.io.File;

public class TomcatRule extends ExternalResource {
    private Tomcat tomcat;
    private boolean test = true;

    public static void main(String[] args) throws Throwable {
        TomcatRule webServer = new TomcatRule();
        Runtime.getRuntime().addShutdownHook(new Thread(webServer::after));
        webServer.test = false;
        webServer.before();
        webServer.tomcat.getServer().await();
    }

    @Override
    protected void before() throws Throwable {
        if (launchedFromMaven()) {
            return;
        }

        if (test) {
            System.setProperty("xebay.test", "true");
        }

        tomcat = new Tomcat();
        tomcat.setPort(8080);

        Context ctx = tomcat.addWebapp("/", new File("src/main/webapp").getAbsolutePath());
        VirtualDirContext resources = new VirtualDirContext();
        resources.setExtraResourcePaths("/WEB-INF/classes=target/classes/");
        ctx.setResources(resources);

        tomcat.start();
    }

    @Override
    protected void after() {
        if (launchedFromMaven()) {
            return;
        }

        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    private boolean launchedFromMaven() {
        // maven builds put "localRepository" property to system properties
        return System.getProperty("localRepository") != null;
    }
}
