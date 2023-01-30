package subway.line;

public class LineUpdateRequest {
    private final String name;
    private final String color;

    public LineUpdateRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
