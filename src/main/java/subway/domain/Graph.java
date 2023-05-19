package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Graph {
    private final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public Graph(List<Station> stations, List<Section> sections) {
        addVertex(stations);
        addEdge(sections);
    }

    private void addVertex(List<Station> stations) {
        for (Station station: stations) {
            graph.addVertex(station.getName());
        }
    }

    private void addEdge(List<Section> sections) {
        for (Section section: sections) {
            graph.setEdgeWeight(graph.addEdge(section.getStartStation().getName(),
                    section.getEndStation().getName()),
                    section.getDistance().getDistance());
        }
    }

    public List<String> findPath(String start, String end){
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return dijkstraShortestPath.getPath(start, end).getVertexList();
    }
    public double findPathDistance(String start, String end){
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return dijkstraShortestPath.getPath(start, end).getWeight();
    }
}
