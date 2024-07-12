package subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.LineApiTestUtil.*;

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
        long stationId1 = 역_생성("강남역").body().jsonPath().getLong("id");
        long stationId2 = 역_생성("신논현역").body().jsonPath().getLong("id");

        // when
        노선_생성("신분당선", "bg-red-600", stationId1, stationId2, 10);

        // then
        노선_목록_조회_시_해당_노선_포함("신분당선");
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
        역_생성_후_노선_생성("신분당선", "bg-red-600", "강남역", "신논현역", 10);
        역_생성_후_노선_생성("분당선", "bg-yellow-600", "미금역", "정자역", 5);

        // when
        List<String> stationNames = 노선_목록_조회().body().jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames).containsAll(List.of("신분당선", "분당선"));
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
        long lineId = 역_생성_후_노선_생성("신분당선", "bg-red-600", "강남역", "신논현역", 10).body().jsonPath().getLong("id");

        // when
        String stationNames = 노선_조회(lineId).jsonPath().getString("name");

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
        long lineId = 역_생성_후_노선_생성("신분당선", "bg-red-600", "강남역", "신논현역", 10).body().jsonPath().getLong("id");

        // when
        노선_수정(lineId, "다른분당선", "bg-blue-600");

        // then_
        노선_조회_시_해당_노선_일치(lineId, "다른분당선");
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
        long lineId = 역_생성_후_노선_생성("신분당선", "bg-red-600", "강남역", "신논현역", 10).body().jsonPath().getLong("id");

        // when
        노선_삭제(lineId);

        // then
        노선_목록_조회_시_해당_노선_미포함("신분당선");
    }
}
