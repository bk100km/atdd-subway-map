package subway.model;

import lombok.Getter;

import java.util.List;

@Getter
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;

    public LineResponse(Line line) {
        id = line.getId();
        name = line.getName();
        color = line.getColor();
        stations = null;
    }
}
