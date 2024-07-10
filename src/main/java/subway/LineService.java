package subway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return lineRepository.findById(id)
                .map(this::createLineResponse)
                .orElse(null);
    }

    @Transactional
    public void updateLineById(Long id, LineRequest lineRequest) {
        Optional<Line> line = lineRepository.findById(id);
        line.ifPresent(existingLine -> updateLine(existingLine, lineRequest));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line);
    }

    private void updateLine(Line line, LineRequest lineRequest) {
        line.setName(lineRequest.getName());
        line.setColor(lineRequest.getColor());
        lineRepository.save(line);
    }
}
