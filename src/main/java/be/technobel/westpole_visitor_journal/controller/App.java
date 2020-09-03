package be.technobel.westpole_visitor_journal.controller;

import io.vertx.core.Vertx;

public class App {

    public static void main(String[] args) {

        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpApplication());

    }
}
