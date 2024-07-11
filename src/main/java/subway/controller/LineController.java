package subway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.model.LineResponse;
import subway.service.LineService;
import subway.model.LineRequest;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class LineController {
    private final LineService lineService;

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(
            @RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping(value = "/lines/{id}")
    public ResponseEntity<LineResponse> showLine(
            @PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLine(id));
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> updateLine(
            @RequestBody LineRequest lineRequest,
            @PathVariable Long id) {
        lineService.updateLineById(id, lineRequest);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(
            @PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }
}
