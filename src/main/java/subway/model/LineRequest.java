package subway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LineRequest {
    private String name;
    private String color;
    private long upStationId;
    private long downStationId;
    private long distance;
}
