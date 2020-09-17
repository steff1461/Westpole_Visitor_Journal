package be.technobel.westpole_visitor_journal.controller;

import be.technobel.westpole_visitor_journal.repository.entity.StoredVisitorEntity;
import be.technobel.westpole_visitor_journal.service.StoredVisitorService;
import be.technobel.westpole_visitor_journal.service.VisitorService;
import be.technobel.westpole_visitor_journal.service.model.StoredVisitorDto;
import be.technobel.westpole_visitor_journal.utils.LangProvider;
import be.technobel.westpole_visitor_journal.utils.mapper.StoredVisitorMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.time.LocalDate;
import java.util.Optional;

public class HttpApplication extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpApplication.class);
    private final VisitorService visitorService = new VisitorService();
    private final StoredVisitorService storedVisitorService = new StoredVisitorService();
    private final String route = "/api/v1/";

    public HttpApplication() {

    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        LOGGER.info("START");

        System.setProperty("file.encoding", "UTF-8");

        Router router = Router.router(vertx);
        router.route()
                .handler(CorsHandler.create(".*."));

        router.errorHandler(500, rc -> {
            System.err.println("Handling failure");
            Throwable failure = rc.failure();
            if (failure != null) {
                failure.printStackTrace();
            }
        });


        //Auth

        //GET

        router.get(route + "lang")
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

        router.get(route + "visitors-by-company")
                .handler(this::visitorsByCompany);
        //POST

        router.post().handler(BodyHandler.create());

        router.post(route + "create-visitor")
                .handler(this::createVisitor);

        router.post(route + "visitor-leave")
                .handler(this::signOutVisitor);

        router.post(route + "visitor-leave-by-name")
                .handler(this::signOutByName);

        router.post(route + "visitor-stored")
                .handler(this::findIfStored);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }


    //--------------------POST----------------

    private void signOutByName(RoutingContext routingContext) {

        LOGGER.info("SIGN-OUT-BY-NAME");

        String response = visitorService.signOutByName(routingContext.getBodyAsJson());

        routingContext.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(response);
    }

    private void signOutVisitor(RoutingContext routingContext) {


        LOGGER.info("SIGN-OUT");

        String response = visitorService.signOutVisitor(routingContext.getBodyAsJson());

        routingContext.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(response);
    }


    private void findIfStored(RoutingContext routingContext) {

        LOGGER.info("IS-STORED");

        Optional<StoredVisitorDto> dto = storedVisitorService.findIfStored(routingContext.getBodyAsJson());
        StringBuilder response = new StringBuilder();

        dto.ifPresentOrElse(storedVisitorDto -> response.append(StoredVisitorMapper.mapAsJson(storedVisitorDto)),
                            () -> response.append("NOT-STORED"));

        routingContext.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(response.toString());
    }


    private void createVisitor(RoutingContext routingContext) {

        LOGGER.info("CREATE-ONE");
        JsonObject request = routingContext.getBodyAsJson();

        Optional<StoredVisitorDto> svDto = storedVisitorService.findIfStored(request);

        StoredVisitorEntity svEntity =
                svDto.map(storedVisitorDto -> {
                    storedVisitorDto.setCompanyName(request.getString("companyName"))
                            .setlPlate(request.getString("lPlate"));
                    return StoredVisitorMapper.toEntity(storedVisitorDto);
                }).orElseGet(() -> storedVisitorService.createNewStoredV(request));

        String response = visitorService.createNewVisitor(svEntity, request.getString("contactName"));

        routingContext.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(response);
    }

    //---------------GET-----------------------

    private void retrieveLang(RoutingContext routingContext) {

        LOGGER.info("LANG");

        String langType = routingContext.request()
                .getParam("lang");

        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application/json; charset = UTF-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(LangProvider.retrieveLangTxt(langType)));
    }

    private void visitorsByCompany(RoutingContext routingContext) {


        LOGGER.info("BY-COMPANY");

        String companyName = routingContext.request()
                .getParam("companyName");

        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application/json;")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(visitorService.findAllByCompany(companyName)));
    }

    private void currentVisitors(RoutingContext routingContext) {

        LOGGER.info("CURRENT-VISITORS");

        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application/json; charset=utf-8")
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
                .putHeader("content-type", "application/json; charset=utf-8")
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
                .putHeader("content-type", "application/json; charset=utf-8")
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
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(visitorService.findAllByMonth(date)));
    }

    private void allfromVisitors(RoutingContext routingContext) {

        LOGGER.info("ALL");
        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(String.valueOf(visitorService.findAll()));
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        LOGGER.info("STOP");
    }
}
