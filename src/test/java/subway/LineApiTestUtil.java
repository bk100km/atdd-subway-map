package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import subway.model.LineRequest;
import subway.model.StationRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.CommonApiTestUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineApiTestUtil {

    public static ExtractableResponse 역_생성(String name) {
        StationRequest stationRequest = new StationRequest(name);
        ExtractableResponse<Response> response = post("/stations", stationRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    public static ExtractableResponse 노선_목록_조회() {
        ExtractableResponse<Response> response = get("/lines");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    public static void 노선_목록_조회_시_해당_노선_포함(String name) {
        assertThat(노선_목록_조회()
                .body().jsonPath().getList("name"))
                .contains(name);
    }

    public static void 노선_목록_조회_시_모든_노선_포함(List<String> list) {
        assertThat(노선_목록_조회()
                .body().jsonPath().getList("name"))
                .containsAll(list);
    }

    public static void 노선_목록_조회_시_해당_노선_미포함(String name) {
        assertThat(노선_목록_조회()
                .body().jsonPath().getList("name"))
                .doesNotContain(name);
    }

    public static ExtractableResponse 노선_조회(long lineId) {
        ExtractableResponse<Response> response = get("/lines/" + lineId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    public static void 노선_조회_시_해당_노선_일치(long lineId, String name) {
        assertThat(노선_조회(lineId)
                .body().jsonPath().getString("name"))
                .isEqualTo(name);
    }

    public static ExtractableResponse 노선_생성(String name, String color, long upStationId, long downStationId, long distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);

        ExtractableResponse<Response> response = post("/lines", lineRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    public static ExtractableResponse 역_생성_후_노선_생성(String name, String color, String upStationName, String downStationName, long distance) {
        long upStationId = 역_생성(upStationName).body().jsonPath().getLong("id");
        long downStationId = 역_생성(downStationName).body().jsonPath().getLong("id");

        return 노선_생성(name, color, upStationId, downStationId, distance);
    }

    public static ExtractableResponse 노선_수정(long lineId, String name, String color) {
        Map<String, String> stationLineMap = new HashMap<>();
        stationLineMap.put("name", name);
        stationLineMap.put("color", color);

        ExtractableResponse<Response> response = put("/lines/" + lineId, stationLineMap);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response;
    }

    public static ExtractableResponse 노선_삭제(long lineId) {
        ExtractableResponse<Response> response = delete("/lines/" + lineId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        return response;
    }
}
