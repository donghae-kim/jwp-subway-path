# jwp-subway-path

## 기능 요구 사항

### Domain

- Lines
    - 전체 호선 관리
- Line (1호선, 2호선, ...)
    - LineInfo
    - 상행 종점
    - 하행 종점
        - 새로 등록되는 노선에 따라, 종점이 변경될 수 있다.
    - Sections(호선에 존재하는 역들의 구간 정보)
- LineInfo
    - 호선 이름
        - 호선 이름은 중복될 수 없다.
        - 호선 이름은 1글자 이상, 10글자 이하여야 한다.
    - 호선 색상
- Sections
    - Section이 중복될 수 없다.
        - StartStation과 EndStation이 같은 두 개의 Section을 가질 수 없다.
        - 역이 등록될 경우 거리 정보를 고려해야 한다.
            - A역 (3km) B역 인 경우, A역 (10km) C역 추가 시 예외
            - A역 (3km) B역 인 경우, A역 (1km) C역 추가 시, A역 (1km) C역, C역 (2km) B역 이 된다.
        - 역이 삭제될 경우 거리 정보를 고려해야 한다.
            - A역 (1km) C역, C역 (2km) B역에서 C역을 삭제하는 경우,
                - A역 (3km) B역이 된다.
                - A역 - C역 Section과 C역 - B역 Section이 삭제 된다.
            - 역이 두 개일 때, 삭제할 경우 두 역이 모두 제거된다.
- Section
    - StartStation
    - EndStation
        - 시작 역과 도착 역이 동일할 수 없다.
        - 두 개의 역을 반드시 등록해야 한다.
    - Distance
- Station 역
    - 이름
        - 역 이름은 1글자 이상, 10글자 이하여야 한다.
- Distance 역간거리
    - 거리
        - 양의 정수만 가능하다.
        - 두 역 사이의 거리는 10Km 이하여야 한다.

### API 명세

노선에 호선 등록

```text
POST /subway/lines

Request {
    lineName : "1호선",
    lineColor : "파랑색"
}

Response Headers {
    Status : 201
    Location : /subway/lines/{line_id}
}
```

노선에 역 구간 등록

```text
POST /subway/lines/{line_id}

Request {
    startStation : "회기",
    endStation : "청량리",
    distance : 3
}

Response Headers {
    Status : 200
}
```

노선에 역 제거

```text
DELETE /subway/lines/sections/{station_id}

Response Headers {
    Status : 204
}
```

노선 상세 조회

```text
GET /subway/lines/{line_id}

Response Headers {
    Status : 200
}

Response Body {
    lineName : "1호선",
    lineColor : "파랑색",
    stations : [
        "회기", "청량리"   
   ]
}
```

노선 목록 조회

```text
GET /subway/lines

Response Headers {
    Status : 200
}

Response Body {
    lines : [
        {
            id : 1L,
            lineName : "1호선",
            lineColor : "파랑색",
            stations : [
              "회기", "청량리"   
            ]
        },
        {
            id : 2L,
            lineName : "2호선",
            lineColor : "초록색",
            stations : [
             "잠실", "잠실새내"   
            ]
        }
    ]
}
```