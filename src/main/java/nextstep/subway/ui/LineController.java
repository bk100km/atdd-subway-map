package nextstep.subway.ui;

import nextstep.subway.applicaion.LineCommandService;
import nextstep.subway.applicaion.LineQueryService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineQueryService lineQueryService;
    private final LineCommandService lineCommandService;

    public LineController(LineQueryService lineQueryService, LineCommandService lineCommandService) {
        this.lineQueryService = lineQueryService;
        this.lineCommandService = lineCommandService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineCommandService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineQueryService.findAllLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineQueryService.findLine(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineCommandService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineCommandService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
