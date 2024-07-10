package subway;

import lombok.Getter;

@Getter
public class LineRequest {
    private String name;
    private String color;
    private long upStationId;
    private long downStationId;
    private long distance;
}
