package subway.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    private String color;
    private long upStationId;
    private long downStationId;
    private long distance;

    public Line(LineRequest lineRequest) {
        name = lineRequest.getName();
        color = lineRequest.getColor();
        upStationId = lineRequest.getUpStationId();
        downStationId = lineRequest.getDownStationId();
        distance = lineRequest.getDistance();
    }
}
