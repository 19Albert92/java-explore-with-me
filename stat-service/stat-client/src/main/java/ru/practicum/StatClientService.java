package ru.practicum;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shared.UtilConstant;
import shared.dto.EndpointHit;
import shared.dto.ErrorResponse;
import shared.dto.ViewStats;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Service
public class StatClientService {

    private final Gson gson = new Gson();

    private final HttpClient httpClient;

    private final String serviceUrl;

    public StatClientService(@Value("${stat.service.url}") String serviceUrl) {
        this.serviceUrl = serviceUrl;
        this.httpClient = HttpClient.newBuilder().build();
    }

    public void saveHit(String app, String uri, String ip, String timestamp) throws
            URISyntaxException, IOException, InterruptedException, RuntimeException {

        EndpointHit newHit = new EndpointHit(app, uri, ip, timestamp);

        String hitJson = gson.toJson(newHit);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serviceUrl + "/hit"))
                .POST(HttpRequest.BodyPublishers.ofString(hitJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (send.statusCode() != 201) {

            ErrorResponse errorResponse = getError(send.body());

            throw new RuntimeException(errorResponse.getError());
        }
    }

    public List<ViewStats> getStats(
            String start,
            String end,
            String[] uris,
            boolean unique
    ) throws
            URISyntaxException, IOException, InterruptedException, RuntimeException {

        StringBuilder url = new StringBuilder(serviceUrl + "/stats?");

        StringJoiner joiner = new StringJoiner("&");
        joiner.add("start=" + encodeUTF8(LocalDateTime.parse(start, UtilConstant.FORMATTER)));
        joiner.add("end=" + encodeUTF8(LocalDateTime.parse(end, UtilConstant.FORMATTER)));
        joiner.add("unique=" + unique);

        if (uris != null && uris.length != 0) {
            joiner.add("uris=" + encodeUTF8(uris));
        }

        url.append(joiner);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url.toString()))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 400) {

            ErrorResponse errorResponse = getError(response.body());

            throw new RuntimeException(errorResponse.getError());
        }

        Type listType = new TypeToken<List<ViewStats>>() {}.getType();

        return gson.fromJson(response.body(), listType);
    }

    private String encodeUTF8(Object object) {
        return URLEncoder.encode(String.valueOf(object), StandardCharsets.UTF_8);
    }

    private ErrorResponse getError(String body) {

        Type listType = new TypeToken<ErrorResponse>() {}.getType();

        return gson.fromJson(body, listType);
    }
}
