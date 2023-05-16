package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.*;
import subway.domain.*;
import subway.dto.PathResponse;
import subway.dto.StationResponse;
import subway.exception.InvalidStationException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {
    private SectionDao sectionDao;
    private StationDao stationDao;
    private LineDao lineDao;

    public PathService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathResponse findPath(Long startStationId, Long endStationId) {
        Lines lines = new Lines();
        lineDao.findAll()
                .forEach(it -> lines.addNewLine(it.getName(), new Sections(toSections(sectionDao.findByLineId(it.getId())))));

        StationEntity startStation = stationDao.findById(startStationId)
                .orElseThrow(InvalidStationException::new);
        StationEntity endStation = stationDao.findById(endStationId)
                .orElseThrow(InvalidStationException::new);

        List<Station> stations = toStations(stationDao.findAll());
        List<Section> sections = lines.getLines().stream()
                .flatMap(it -> it.getSections().getSections().stream())
                .collect(Collectors.toList());

        Graph graph = new Graph(stations, sections);
        List<String> pathStations = graph.findPath(startStation.getName(), endStation.getName());
        double pathDistance = graph.findPathDistance(startStation.getName(), endStation.getName());

        return new PathResponse(makeStationResponses(pathStations), (int) pathDistance, null);
    }

    private List<Station> toStations(List<StationEntity> findStations) {
        return findStations.stream()
                .map(it -> new Station(it.getName()))
                .collect(Collectors.toList());
    }

    private List<Section> toSections(List<SectionEntity> findSections) {
        Map<Long, String> stations = stationDao.findAll()
                .stream()
                .collect(Collectors.toMap(StationEntity::getId, StationEntity::getName));

        return findSections.stream()
                .map(it -> new Section(
                        new Station(stations.get(it.getStartStationId())),
                        new Station(stations.get(it.getEndStationId())),
                        new Distance(it.getDistance()))
                )
                .collect(Collectors.toList());
    }

    private List<StationResponse> makeStationResponses(List<String> stationNames) {

        Map<String, Long> stations = stationDao.findAll().stream()
                .collect(Collectors.toMap(StationEntity::getName, StationEntity::getId));

        List<StationResponse> stationResponses = stationNames.stream()
                .map(it -> new StationResponse(stations.get(it), it))
                .collect(Collectors.toList());

        return stationResponses;
    }
}
