package be.technobel.westpole_visitor_journal.controller;

import be.technobel.westpole_visitor_journal.model.dto.StoredVisitorDto;
import be.technobel.westpole_visitor_journal.model.dto.UserDto;
import be.technobel.westpole_visitor_journal.model.dto.VisitorDto;
import be.technobel.westpole_visitor_journal.model.entity.StoredVisitorEntity;
import be.technobel.westpole_visitor_journal.service.DirectoryService;
import be.technobel.westpole_visitor_journal.service.StoredVisitorService;
import be.technobel.westpole_visitor_journal.service.UserService;
import be.technobel.westpole_visitor_journal.service.VisitorService;
import be.technobel.westpole_visitor_journal.utils.LangProvider;
import be.technobel.westpole_visitor_journal.utils.mapper.StoredVisitorMapper;
import be.technobel.westpole_visitor_journal.utils.mapper.UserMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HttpApplication extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpApplication.class);
    private final VisitorService visitorService = new VisitorService();
    private final StoredVisitorService storedVisitorService = new StoredVisitorService();
    private final UserService userService = new UserService();
    private final String route = "/api/v1/";
    private final JWTAuthOptions config =
            new JWTAuthOptions().setKeyStore(new KeyStoreOptions()
                                                     .setPath("keystore/keystore.jceks")
                                                     .setPassword("secret"));
    //TODO change jceks password

    private final String hostURL = "http://localhost:63343";

    public HttpApplication() {

    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        LOGGER.info("START");
        JWTAuth provider = JWTAuth.create(vertx, config);
        Router router = Router.router(vertx);

        router.route()
                .handler(CorsHandler.create(hostURL)
                                 .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                                 .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                                 .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                                 .allowCredentials(true)
                                 .allowedHeader("Access-Control-Request-Method")
                                 .allowedHeader("Access-Control-Allow-Credentials")
                                 .allowedHeader("Access-Control-Allow-Origin")
                                 .allowedHeader("Access-Control-Allow-Headers")
                                 .allowedHeader("Authorization")
                                 .allowedHeader("Content-Type"));

        router.route()
                .path(route + "*")
                .handler(JWTAuthHandler.create(provider));

        router.route()
                .path("/authenticate")
                .handler(CorsHandler.create(hostURL));


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

        router.post()
                .handler(BodyHandler.create());

        router.post(route + "create-visitor")
                .handler(this::createVisitor);

        router.post(route + "visitor-leave")
                .handler(this::signOutVisitor);

        router.post(route + "visitor-leave-by-name")
                .handler(this::signOutByName);

        router.post(route + "visitor-stored")
                .handler(this::findIfStored);

        //Authentication
        router.post("/authenticate")
                .handler(this::authenticateUser);

        //Active directory
        router.get(route + "directory")
                .handler(this::fromDirectory);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
    }

    //ROUTE METHODS


    //--------------------POST----------------

    private void authenticateUser(RoutingContext routingContext) {

        LOGGER.info("AUTHENTICATE");

        UserDto dto = userService.authenticateUser(UserMapper.mapFromJson(routingContext.getBodyAsJson()));

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "POST")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        if (dto != null) {

            String token = generateToken(dto);
            routingContext.response()
                    .setStatusCode(200)
                    .end(token);
        } else
            routingContext.response()
                    .setStatusCode(404)
                    .end();
    }

    private void signOutByName(RoutingContext routingContext) {

        LOGGER.info("SIGN-OUT-BY-NAME");

        Optional<String> response = visitorService.signOutByName(routingContext.getBodyAsJson());
        routingContext
                .response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "POST")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        response.ifPresentOrElse(

                s -> routingContext.response()
                        .setStatusCode(200)
                        .end(s),

                () -> routingContext.response()
                        .setStatusCode(404)
                        .end());
    }

    private void signOutVisitor(RoutingContext routingContext) {

        LOGGER.info("SIGN-OUT");

        Optional<String> response = visitorService.signOutVisitor(routingContext.getBodyAsJson());

        routingContext
                .response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "POST")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        response.ifPresentOrElse(
                s -> routingContext.response()
                        .setStatusCode(200)
                        .end(s),

                () -> routingContext.response()
                        .setStatusCode(404)
                        .end());
    }

    private void findIfStored(RoutingContext routingContext) {

        LOGGER.info("IS-STORED");

        Optional<StoredVisitorDto> dto = storedVisitorService.findIfStored(routingContext.getBodyAsJson());
        routingContext
                .response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "POST")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        dto.ifPresentOrElse(

                svDto -> routingContext.response()
                        .setStatusCode(200)
                        .end(Json.encode(svDto)),
                () -> routingContext.response()
                        .setStatusCode(404)
                        .end());
    }

    private void createVisitor(RoutingContext routingContext) {

        LOGGER.info("CREATE-ONE");
        JsonObject request = routingContext.getBodyAsJson();

        Optional<StoredVisitorDto> svDto = storedVisitorService.findIfStored(request);
        StoredVisitorEntity svEntity =
                svDto.map(storedVisitorDto ->
                                  StoredVisitorMapper.toEntity(storedVisitorDto
                                                                       .setCompanyName(request.getString("companyName"))
                                                                       .setlPlate(request.getString("lPlate")))
                         ).orElseGet(() -> storedVisitorService.createNewStoredV(request));

        Optional<String> response = visitorService.createNewVisitor(svEntity, request.getString("contactName"));

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "POST")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        response.ifPresentOrElse(
                s -> routingContext
                        .response()
                        .setStatusCode(201)
                        .end(s),
                () -> routingContext
                        .response()
                        .setStatusCode(400));
    }

    //---------------GET-----------------------
    private void retrieveLang(RoutingContext routingContext) {

        LOGGER.info("LANG");

        String params = routingContext.request().getHeader("userName");
        System.out.println(params);


        String langType = routingContext.request()
                .getParam("lang");

        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application/json; charset = UTF-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "GET")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization")
                .end(String.valueOf(LangProvider.retrieveLangTxt(langType)));
    }

    private void visitorsByCompany(RoutingContext routingContext) {

        LOGGER.info("BY-COMPANY");

        String companyName = routingContext.request()
                .getParam("companyName");

        Optional<List<VisitorDto>> visitors = visitorService.findAllByCompany(companyName);

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "GET")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        visitors.ifPresentOrElse(
                visitorDtos -> routingContext
                        .response()
                        .setStatusCode(200)
                        .end(Json.encode(visitorDtos)),

                () -> routingContext.response()
                        .setStatusCode(404)
                        .end());
    }

    private void currentVisitors(RoutingContext routingContext) {

        LOGGER.info("CURRENT-VISITORS");

        Optional<List<VisitorDto>> visitors = visitorService.findAllCurrent();

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "GET")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        visitors.ifPresentOrElse(
                visitorDtos ->
                        routingContext
                                .response()
                                .setStatusCode(200)
                                .end(Json.encode(visitorDtos)),

                () -> routingContext
                        .response()
                        .setStatusCode(404)
                        .end());
    }

    private void visitorsByDay(RoutingContext routingContext) {

        LOGGER.info("BY-DAY");
        String dateParam = routingContext.request()
                .getParam("date");

        LocalDate date = LocalDate.parse(dateParam);
        Optional<List<VisitorDto>> visitors = visitorService.findAllByDay(date);

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "GET")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        visitors.ifPresentOrElse(
                visitorDtos -> routingContext
                        .response()
                        .setStatusCode(200)
                        .end(Json.encode(visitorDtos)),

                () -> routingContext
                        .response()
                        .setStatusCode(404)
                        .end()
                                );
    }

    private void visitorsByWeek(RoutingContext routingContext) {

        LOGGER.info("BY-WEEK");
        String dateParam = routingContext.request()
                .getParam("date");
        LocalDate date = LocalDate.parse(dateParam);

        Optional<List<VisitorDto>> visitors = visitorService.findAllByWeek(date);

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "GET")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        visitors.ifPresentOrElse(
                visitorDtos ->
                        routingContext
                                .response()
                                .setStatusCode(200)
                                .end(Json.encode(visitorDtos)),

                () -> routingContext
                        .response()
                        .setStatusCode(404)
                        .end()
                                );
    }

    private void visitorsByMonth(RoutingContext routingContext) {

        LOGGER.info("BY-MONTH");
        String dateParam = routingContext.request()
                .getParam("date");
        LocalDate date = LocalDate.parse(dateParam);

        Optional<List<VisitorDto>> visitors = visitorService.findAllByMonth(date);

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "GET")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        visitors.ifPresentOrElse(
                visitorDtos ->
                        routingContext
                                .response()
                                .setStatusCode(200)
                                .end(Json.encode(visitorDtos)),

                () -> routingContext
                        .response()
                        .setStatusCode(404)
                        .end());
    }

    private void allfromVisitors(RoutingContext routingContext) {

        LOGGER.info("ALL");

        Optional<List<VisitorDto>> visitors = visitorService.findAll();

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "GET")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        visitors.ifPresentOrElse(
                visitorDtos ->
                        routingContext
                                .response()
                                .setStatusCode(200)
                                .end(Json.encode(visitorDtos)),

                () -> routingContext
                        .response()
                        .setStatusCode(404)
                        .end());
    }

    private void fromDirectory(RoutingContext routingContext) {

        LOGGER.info("FROM DIRECTORY");

        DirectoryService provider = new DirectoryService();

        Optional<JsonArray> response = provider.infosFromDirectory();

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .putHeader("Access-Control-Allow-Origin", hostURL)
                .putHeader("Access-Control-Allow-Methods", "GET")
                .putHeader("Access-Control-Allow-Credentials", "true")
                .putHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " +
                        "Authorization");

        response.ifPresentOrElse(

                jsonArray ->
                        routingContext
                                .response()
                                .setStatusCode(200)
                                .end(String.valueOf(jsonArray)),

                () -> routingContext
                        .response()
                        .setStatusCode(404)
                        .end());
    }


    //Private methods

    private String generateToken(UserDto dto) {

        JWTAuth provider = JWTAuth.create(vertx, config);
        JsonObject object = new JsonObject();
        object.put(dto.getUserName(), dto.getPassword());

        JWTOptions opt =
                new JWTOptions()
                        .setAlgorithm("RS256")
                        .setIgnoreExpiration(true);

        return provider.generateToken(object, opt);
    }


    private boolean verifyToken(String token) {

        JWTAuth provider = JWTAuth.create(vertx, config);
        Future<User> user = provider.authenticate(new JsonObject().put("jwt", token));

        return user.succeeded();
    }


    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        LOGGER.info("STOP");
    }
}
