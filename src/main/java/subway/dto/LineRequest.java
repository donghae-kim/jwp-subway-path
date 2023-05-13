package subway.dto;

public class LineRequest {
    private String lineName;

    private String upStation;
    private String downStation;
    private Integer distance;

    public LineRequest() {
    }

    public LineRequest(String lineName, String upStation, String downStation, Integer distance) {
        this.lineName = lineName;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getUpStation() {
        return upStation;
    }

    public String getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
