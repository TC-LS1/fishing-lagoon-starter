package com.drpicox.fishingLagoon.client;

import com.drpicox.fishingLagoon.actions.Action;
import com.drpicox.fishingLagoon.actions.ActionParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import java.lang.reflect.Type;
import java.util.List;

// http://www.mkyong.com/webservices/jax-rs/restful-java-client-with-jersey-client/
public class FishingLagoonClient {

    private String serverUrl;
    private String botToken;
    private ActionParser actionParser;

    private Client client;
    private Gson gson;

    public FishingLagoonClient(String serverUrl, String botToken, ActionParser actionParser) {
        this.serverUrl = serverUrl;
        this.botToken = botToken;
        this.actionParser = actionParser;

        client = Client.create();
        gson = new Gson();
    }

    public ClientBot getBot() {
        var response = get("/bots/" + botToken);
        return gson.fromJson(response, ClientBot.class);
    }

    public List<ClientRound> listRounds() {
        var response = get("/rounds");
        return roundsFromJson(response);
    }

    public List<ClientRound> listActiveRounds() {
        var response = get("/rounds?isActive=1");
        return roundsFromJson(response);
    }

    public ClientRound createRound(String roundDescription) {
        var response = post("/rounds?botToken=" + botToken, roundDescription);
        return roundFromJson(response);
    }

    public ClientRound getRound(String roundId) {
        var response = get("/rounds/" + roundId + "?botToken=" + botToken);
        return roundFromJson(response);
    }

    public ClientRound seat(String roundId, int lagoonIndex) {
        var data = "" + lagoonIndex;
        var response = put("/rounds/" + roundId + "/seats/" + botToken, data);
        return roundFromJson(response);
    }

    public ClientRound command(String roundId, List<Action> actions) {
        var data = actionParser.toString(actions);
        var response = put("/rounds/" + roundId + "/commands/" + botToken, data);
        return roundFromJson(response);
    }

    public ClientRound roundFromJson(String response) {
        ClientRound clientRound = gson.fromJson(response, ClientRound.class);
        clientRound.parseActions(actionParser);
        return clientRound;
    }

    private List<ClientRound> roundsFromJson(String response) {
        var listType = new TypeToken<List<ClientRound>>(){}.getType();
        List<ClientRound> result = gson.fromJson(response, listType);
        for (var round: result) {
            round.parseActions(actionParser);
        }
        return result;
    }


    private String get(String url) {
        return handle(client.resource(serverUrl + url).get(ClientResponse.class));
    }

    private String post(String url, String data) {
        return handle(client.resource(serverUrl + url).post(ClientResponse.class, data));
    }

    private String put(String url, String data) {
        return handle(client.resource(serverUrl + url).put(ClientResponse.class, data));
    }

    private String handle(ClientResponse response) {
        if (response.getStatus() >= 400) {
            throw new FishingLagoonClientException(response.getEntity(String.class));
        }
        return response.getEntity(String.class);
    }
}
