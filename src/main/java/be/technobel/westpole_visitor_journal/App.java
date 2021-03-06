package be.technobel.westpole_visitor_journal;

import be.technobel.westpole_visitor_journal.controller.HttpApplication;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class App {

    public static void main(String[] args) {

        final Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new HttpApplication(),
                             new DeploymentOptions().setWorker(true));
    }
}
