# GraphQL
GraphQL은 페이스북에서 개발한 데이터 질의어이다.  
RESTful API를 대체할 수 있으며, GraphQL은 사용자가 어떤 데이터가 필요한지 명시할 수 있게 해주는 강타입 언어이다.  
이러한 구조를 통해 불필요한 데이터를 받게 되거나 필요한 데이터를 받지 못하는 문제를 피할 수 있다.  

## sql vs GraphQL  
sql은 데이터베이스 시스템에 저장된 데이터를 효율적으로 가져오는 것이 목적이다.  
gql은 웹 클라이언트가 데이터를 서버로부터 효율적으로 가져오는 것이 목적이다.  

## Rest API vs GraphQL API  
Rest API는 url과 HTTP Method를 조합하므로 여러 EndPoint가 존재한다.  
반면에, gql API은 단 하나의 Endpoint만 존재한다.  
Rest API는 각 EndPoint마다 데이터베이스 쿼리가 달리지는 반면, gql API는 gql 스키마의 타입마다 데이터베이스 쿼리가 달라진다.  

# GraphQL의 특징  
gql은 데이터베이스나 플랫폼, 네트워크에 종속적이지 않다.  
일반적으로 gql의 인터페이스간 송수신은 Application Layer의 HTTP POST 메서드와 웹소켓 프로토콜을 사용한다.  
필요에 따라 Transport Layer의 TCP/UDP나 DataLink Layer의 이더넷 프레임을 활용 가능하다.  
<img width="780" alt="스크린샷 2021-10-13 오후 7 12 31" src="https://user-images.githubusercontent.com/66231761/137113953-bbe2763d-442c-41aa-af9f-07d59e61fd5b.png">
REST API에서 프론트앤드 프로그래머는 백앤드 프로그래머가 작성한 API의 request/response 형식에 의존하게 된다.
그러나 gql API에선 이런 의존도가 사라진다. 다만, 데이터 스키마에 대한 협업 의존성은 존재한다.  

# GraphQL의 구조
## 쿼리(Query) & 뮤테이션(Mutation)  
쿼리는 데이터를 읽는데(read) 사용하고 뮤테이션은 데이터를 조작(create, update, delete)하는데 사용한다.
<img width="683" alt="스크린샷 2021-10-13 오후 7 32 22" src="https://user-images.githubusercontent.com/66231761/137116837-8f406973-fe40-4881-85e9-fff13911c80c.png">

## 일반 Query & Operation Name Query
일반 Query는 정적 쿼리를 말하고, Operation Name Query는 동적 쿼리를 의미한다고 이해하면 된다.  
```query
// 일반 query
{
  human(id: "1000") {
    name
    height
  }
}

// Operation Name Query
query HeroNameAndFriends($episode: Episode) {
  hero(episode: $episode) {
    name
    friends {
      name
    }
  }
}
```

Operation Name Query는 한번의 네트워크 왕복으로 여러 개체가 연관된 데이터를 모두 가져올 수 있다는 것이다.
```query
query getStudentInfomation($studentId: ID) {
  personalInfo(studentId: $studentId) {
    name
    address1
    address2
    major
  }
  classInfo(year: 2018, studentId: $studentId) {
    classCode
    className
    teacher {
      name
      major
    }
    classRoom {
      id
      maintainer {
        name
      }
    }
  }
  SATInfo(schoolCode: 0412, studentId: $studnetId) {
    totalScore
    dueDate
  }
}
```
비유하자면 데이터베이스의 Procedure 개념과 유사하다.  
REST API도 Join을 이용해서 위 예시처럼 여러 개체를 통합하여 데이터를 제공 가능하지만,  
데이터베이스의 Procedure는 DBA나 백엔드 프로그래머가 작성하고 관리하고,  
gql의 Operation Name Query는 클라이언트 프로그래머가 작성하고 관리한다는 점이 다르다.

## 스키마(Schema) & 타입(Type)  
```query
type Character {
  name: String!
  appearsIn: [Episode!]!
}
```
- 오브젝트 타입: Character
- 필드: name, appearsIn
- 스칼라 타입: String, ID, int 등
- 느낌표(!): 필수 값(Not Null)
- 대괄호([, ])

# Reference  
- https://tech.kakao.com/2019/08/01/graphql-basic/  
- https://ko.wikipedia.org/wiki/%EA%B7%B8%EB%9E%98%ED%94%84QL  
- https://blog.apollographql.com/graphql-vs-rest-5d425123e34b
