package com.prottonne.jersey.service;

import RSPackage.MyResponse;
import RSPackage.Request;
import org.springframework.stereotype.Service;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@Service
public class RestClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${endpoint}")
    private String myEndpoint;

    @Value("${connection.timeup}")
    private String connectionTimeup;

    @Value("${read.timeup}")
    private String readTimeup;

    private final Client client;

    public RestClient() {
        /**
         * You could use
         *
         * @Autowired private Client client;
         *
         */
        this.client = ClientBuilder.newClient();
        this.client.property(ClientProperties.CONNECT_TIMEOUT, connectionTimeup);
        this.client.property(ClientProperties.READ_TIMEOUT, readTimeup);
    }

    public MyResponse method(String data) {
        return genericMethod(data, MyResponse.class, myEndpoint);
    }

    public <T> T genericMethod(String data, Class<T> entityType, String endpoint) {

        check(data);

        WebTarget webTarget = client.target(endpoint);

        logger.info("url {}", webTarget);

        Builder builder = webTarget.request(MediaType.APPLICATION_JSON);

        Request message = getMessage(data);

        Entity<Request> request = Entity.entity(message, MediaType.APPLICATION_JSON);

        logger.info("request {}", request);

        try {
            Response response = builder.post(request);

            logger.info("response {}", response);

            check(response);

            T myResponse = response.readEntity(entityType);

            logger.info("myResponse {}", myResponse);

            return myResponse;
        } catch (Exception e) {
            logger.error("error", e);
            throw new RuntimeException(e);
        }

    }

    private void check(Response response) {
        if (HttpStatus.OK.value() != response.getStatus()) {
            logger.error("HttpStatus No OK, {}", response.getStatus());
            throw new RuntimeException("HttpStatus No OK");
        }
    }

    private void check(String data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Request getMessage(String data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
