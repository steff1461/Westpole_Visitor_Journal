package be.technobel.westpole_visitor_journal.controller;

import be.technobel.westpole_visitor_journal.service.VisitorService;
import be.technobel.westpole_visitor_journal.utils.LangProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.time.LocalDate;

public class HttpApplication extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpApplication.class);
    private final VisitorService visitorService = new VisitorService();

    public HttpApplication() {


    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        LOGGER.info("START");

        Router router = Router.router(vertx);
        router.route().handler(CorsHandler.create("*"));


        String route = "/api/v1/";

        router.get(route+"lang")
                .handler(this::retrieveLang);

        router.get(route + "all-visitors")
                .handler(this::allfromVisitors);

        router.get(route + "visitors-by-month")
                .handler(this::visitorsByMonth);

        router.get(route + "visitors-by-week")
                .handler(this::visitorsByWeek);

        router.get(route + "visitors-by-day")
                .handler(this::visitorsByDay);

        router.get(route + "current-visitors")
                .handler(this::currentVisitors);

        router.post().handler(BodyHandler.create());

        router.post(route + "create-visitor")
                .handler(this::createVisitor);

        router.post(route + "visitor-leave")
                .handler(this::signOutVisitor);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }


    //--------------------POST----------------

    private void signOutVisitor(RoutingContext routingContext) {


        LOGGER.info("SIGN-OUT");

        String response= visitorService.signOutVisitor(routingContext.getBodyAsJson());


        routingContext.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(response);
    }


    private void createVisitor(RoutingContext routingContext) {

        LOGGER.info("CREATE-ONE");

        String response= visitorService.createNewVisitor(routingContext.getBodyAsJson());

        routingContext.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(response);
    }

    //---------------GET-----------------------

    private void retrieveLang(RoutingContext routingContext) {

        String langType= routingContext.request()
                .getParam("lang");

        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application.json")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(LangProvider.retrieveLangTxt(langType)));
    }

    private void currentVisitors(RoutingContext routingContext) {

        LOGGER.info("CURRENT-VISITORS");

        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application.json")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(visitorService.findAllCurrent()));

    }

    private void visitorsByDay(RoutingContext routingContext) {

        LOGGER.info("BY-DAY");
        String dateParam = routingContext.request()
                .getParam("date");

        LocalDate date = LocalDate.parse(dateParam);

        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application.json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(visitorService.findAllByDay(date)));
    }

    private void visitorsByWeek(RoutingContext routingContext) {

        LOGGER.info("BY-WEEK");
        String dateParam = routingContext.request()
                .getParam("date");
        LocalDate date = LocalDate.parse(dateParam);

        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application.json")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(visitorService.findAllByWeek(date)));
    }

    private void visitorsByMonth(RoutingContext routingContext) {

        LOGGER.info("BY-MONTH");
        String dateParam = routingContext.request()
                .getParam("date");
        LocalDate date = LocalDate.parse(dateParam);
        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application.json")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(visitorService.findAllByMonth(date)));
    }


    private void allfromVisitors(RoutingContext routingContext) {

        LOGGER.info("ALL");
        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application.json")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(visitorService.findAll()));
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        LOGGER.info("STOP");
    }
}
