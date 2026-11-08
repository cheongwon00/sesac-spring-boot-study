# 취미 위시리스트와 예산 추천

## 1. 프로젝트 개요

사용자가 소장하거나 관심 있는 아이템을 등록하고 조회할 수 있는 Spring Boot 기반 REST API입니다. 등록된 아이템 중 입력한 예산 이하의 상품을 가격이 낮은 순서대로 최대 3개까지 추천하는 기능을 제공합니다.

### 주요 기능

- 아이템 등록
- 전체 아이템 조회
- ID를 이용한 단건 조회
- 예산에 맞는 아이템 추천
- 요청값 및 도메인 규칙 검증
- 일관된 오류 응답 제공

### 기술 스택

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Jakarta Bean Validation
- Gradle
- JUnit 5, Mockito, MockMvc

### 프로젝트 구조

```text
src/main/java/com/example/personal_project
├── presentation     # Controller, 요청/응답 DTO, 예외 처리
├── application      # 비즈니스 로직과 유효성 검사
├── domain           # 도메인 객체와 Repository 인터페이스
└── infrastructure   # 메모리 기반 Repository 구현체
```

현재 데이터는 `ListCollectionItemRepository`의 메모리에 저장됩니다. 따라서 애플리케이션을 종료하면 등록된 데이터가 사라집니다.

### 아이템 규칙

| 필드 | 타입 | 필수 여부 | 규칙 |
| --- | --- | --- | --- |
| `id` | Long | 응답에만 포함 | 등록 시 자동 생성 |
| `name` | String | 필수 | 양끝 공백 제거 후 1~20자 |
| `price` | Integer | 필수 | 0 이상 |

## 2. API 설명

### API 목록

| 기능 | Method | Endpoint |
| --- | --- | --- |
| 아이템 등록 | `POST` | `/item` |
| 전체 아이템 조회 | `GET` | `/item` |
| 아이템 단건 조회 | `GET` | `/item/{id}` |
| 예산별 아이템 추천 | `GET` | `/item/recommendations?budget={budget}` |

### 2.1 아이템 등록

새로운 아이템을 등록합니다. `id`는 서버에서 자동으로 생성하며, `name`의 양끝 공백은 제거한 뒤 저장합니다.

```http
POST /item
Content-Type: application/json
```

요청 본문:

```json
{
  "name": "키보드",
  "price": 50000
}
```

성공 응답 — `200 OK`:

```json
{
  "id": 1,
  "name": "키보드",
  "price": 50000
}
```

### 2.2 전체 아이템 조회

등록된 모든 아이템을 조회합니다.

```http
GET /item
```

성공 응답 — `200 OK`:

```json
[
  {
    "id": 1,
    "name": "키보드",
    "price": 50000
  },
  {
    "id": 2,
    "name": "마우스",
    "price": 30000
  }
]
```

등록된 아이템이 없으면 빈 배열을 반환합니다.

```json
[]
```

### 2.3 아이템 단건 조회

ID와 일치하는 아이템 한 개를 조회합니다.

```http
GET /item/{id}
```

요청 예시:

```http
GET /item/1
```

성공 응답 — `200 OK`:

```json
{
  "id": 1,
  "name": "키보드",
  "price": 50000
}
```

존재하지 않는 ID 응답 — `404 Not Found`:

```json
{
  "errors": [
    "해당 id로 item을 찾을 수 없습니다"
  ]
}
```

### 2.4 예산별 아이템 추천

가격이 입력한 예산 이하인 아이템을 가격 오름차순으로 정렬하여 최대 3개까지 반환합니다.

```http
GET /item/recommendations?budget={budget}
```

요청 예시:

```http
GET /item/recommendations?budget=100000
```

성공 응답 — `200 OK`:

```json
[
  {
    "id": 3,
    "name": "장패드",
    "price": 10000
  },
  {
    "id": 2,
    "name": "마우스",
    "price": 30000
  },
  {
    "id": 1,
    "name": "키보드",
    "price": 50000
  }
]
```

`budget`은 0 이상이어야 합니다. 음수를 전달하면 `400 Bad Request`를 반환합니다.

```json
{
  "errors": [
    "예산은 0 이상입니다."
  ]
}
```

### 오류 응답 형식

요청값이나 도메인 규칙을 위반하면 `400 Bad Request`와 함께 오류 메시지 목록을 반환합니다.

```json
{
  "errors": [
    "name, 널이어서는 안됩니다",
    "price, 널이어서는 안됩니다"
  ]
}
```

오류 메시지의 기본 문구는 실행 환경의 Validation 메시지 설정에 따라 달라질 수 있습니다.
