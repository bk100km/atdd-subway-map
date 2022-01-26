package nextstep.subway.applicaion.station.response;

import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.station.domain.Station;
import nextstep.subway.applicaion.station.domain.Stations;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class StationResponse {
	private Long id;
	private String name;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	private StationResponse() {
	}

	private StationResponse(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
		this.id = id;
		this.name = name;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public static StationResponse from(Station station) {
		return new StationResponse(station.getId(), station.getName(), station.getCreatedDate(),
				station.getModifiedDate());
	}

	public static List<StationResponse> fromList(List<Station> stations) {
		return stations.stream().map(StationResponse::from).collect(toList());
	}

	public static List<StationResponse> ofSections(Line line) {
		if (line.hasNoSections()) {
			return emptyList();
		}
		return StationResponse.fromList(Stations.ofLine(line));
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}
}
