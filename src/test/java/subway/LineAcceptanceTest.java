package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * When 새로운 지하철 노선 정보를 입력하고
     * Then 관리자가 노선을 생성하면
     * Then 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        long stationId1 = createStation("강남역").body().jsonPath().getLong("id");
        long stationId2 = createStation("신논현역").body().jsonPath().getLong("id");

        // when
        createLine("신분당선", "bg-red-600", stationId1, stationId2, 10);

        // then
        List<String> stationNames = getLines().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("신분당선");
    }

    /**
     * When 여러 개의 지하철 노선이 등록되어 있고
     * Then 관리자가 지하철 노선 목록을 조회하면
     * Then 모든 지하철 노선 목록이 반환된다.
     */
    @DisplayName("지하철 노선을 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        long stationId1 = createStation("강남역").body().jsonPath().getLong("id");
        long stationId2 = createStation("신논현역").body().jsonPath().getLong("id");
        long stationId3 = createStation("미금역").body().jsonPath().getLong("id");
        long stationId4 = createStation("정자역").body().jsonPath().getLong("id");

        // when
        createLine("신분당선", "bg-red-600", stationId1, stationId2, 10).body().jsonPath().getLong("id");
        createLine("분당선", "bg-yellow-600", stationId3, stationId4, 5).body().jsonPath().getLong("id");

        // then
        List<String> stationNames = getLines().body().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsExactly("신분당선", "분당선");
    }

    /**
     * When 특정 지하철 노선이 등록되어 있고
     * Then 관리자가 해당 노선을 조회하면
     * Then 해당 노선의 정보가 반환된다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        // given
        long stationId1 = createStation("강남역").body().jsonPath().getLong("id");
        long stationId2 = createStation("신논현역").body().jsonPath().getLong("id");
        long lineId = createLine("신분당선", "bg-red-600", stationId1, stationId2, 10).body().jsonPath().getLong("id");

        // when
        String stationNames = getLine(lineId).jsonPath().getString("name");

        // then
        assertThat(stationNames).isEqualTo("신분당선");
    }

    /**
     * When 특정 지하철 노선이 등록되어 있고
     * Then 관리자가 해당 노선을 수정하면
     * Then 해당 노선의 정보가 수정된다.
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        long stationId1 = createStation("강남역").body().jsonPath().getLong("id");
        long stationId2 = createStation("신논현역").body().jsonPath().getLong("id");
        long lineId = createLine("신분당선", "bg-red-600", stationId1, stationId2, 10).body().jsonPath().getLong("id");

        // when
        Map<String, String> stationLineMap = new HashMap<>();
        stationLineMap.put("name", "다른분당선");
        stationLineMap.put("color", "bg-red-600");

        RestAssured.given().log().all()
                .body(stationLineMap)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        // then
        String stationName = getLine(lineId).jsonPath().getString("name");
        assertThat(stationName).isEqualTo("다른분당선");
    }

    /**
     * When 특정 지하철 노선이 등록되어 있고
     * Then 관리자가 해당 노선을 삭제하면
     * Then 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        long stationId1 = createStation("강남역").body().jsonPath().getLong("id");
        long stationId2 = createStation("신논현역").body().jsonPath().getLong("id");
        long lineId = createLine("신분당선", "bg-red-600", stationId1, stationId2, 10).body().jsonPath().getLong("id");

        // when
        RestAssured.given().log().all()
                .when().delete("/lines/" + lineId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        List<String> stationNames = getLines().jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain("신분당선");
    }

    private ExtractableResponse createStation(String name) {
        Map<String, String> station = new HashMap<>();
        station.put("name", name);
        return RestAssured.given().log().all()
                .body(station)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private ExtractableResponse getLines() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse getLine(long lineId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + lineId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse createLine(String name, String color, long upStationId, long downStationId, long distance) {
        Map<String, Object> stationLineMap = new HashMap<>();
        stationLineMap.put("name", name);
        stationLineMap.put("color", color);
        stationLineMap.put("upStationId", upStationId);
        stationLineMap.put("downStationId", downStationId);
        stationLineMap.put("distance", distance);

        return RestAssured.given().log().all()
                .body(stationLineMap)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }
}