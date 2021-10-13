# GraphQL
GraphQL은 페이스북에서 개발한 데이터 질의어이다.  
RESTful API를 대체할 수 있으며, GraphQL은 사용자가 어떤 데이터가 필요한지 명시할 수 있게 해주는 강타입 언어이다.  
이러한 구조를 통해 불필요한 데이터를 받게 되거나 필요한 데이터를 받지 못하는 문제를 피할 수 있다.  

### sql vs GraphQL  
sql은 데이터베이스 시스템에 저장된 데이터를 효율적으로 가져오는 것이 목적이다.  
gql은 웹 클라이언트가 데이터를 서버로부터 효율적으로 가져오는 것이 목적이다.  

### Rest API vs GraphQL API  
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
### 쿼리(Query) & 뮤테이션(Mutation)  
쿼리는 데이터를 읽는데(read) 사용하고 뮤테이션은 데이터를 조작(create, update, delete)하는데 사용한다.  
<img width="683" alt="스크린샷 2021-10-13 오후 7 32 22" src="https://user-images.githubusercontent.com/66231761/137116837-8f406973-fe40-4881-85e9-fff13911c80c.png">

### 일반 Query & Operation Name Query
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

### 스키마(Schema) & 타입(Type)  
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
- 대괄호([, ]): 배열을 의미(array)

### 리졸버(Resolver)
REST API와 다르게 gql API에서는 데이터를 가져오는 구체적인 과정을 직접 구현해야 한다.  
그 구체적인 과정은 Resolver가 담당하고 프로그래머가 Resolver를 직접 구현해야한다는 부담은 있지만,  
이를 통해서 데이터 source의 종류에 상관 없이 구현이 가능하다.  
예를 들면, Resolver를 통해 데이터베이스나 일반 파일에 접근 가능하고 http, SOAP와 같은 네트워크 프로토콜로 원격 데이터를 가져올 수도 있다.  

```query
type Query {
  users: [User]
  user(id: ID): User
  limits: [Limit]
  limit(UserId: ID): Limit
  paymentsByUser(userId: ID): [Payment]
}

/*
 * User와 Limit은 1:1 관계
 * User와 Payment는 1:N 관계
 */

type User {
  id: ID!
  name: String!
  sex: SEX!
  birthDay: String!
  phoneNumber: String!
}

type Limit {
  id: ID!
  UserId: ID
  max: Int!
  amount: Int
  user User
}

type Payment {
  id: ID!
  limit: Limit!
  user: User!
	pg: PaymentGateway!
	productName: String!
	amount: Int!
	ref: String
	createdAt: String!
	updatedAt: String!
}
```
gql 쿼리에서는 각각의 필드마다 함수가 하나씩 존재한다고 생각하면 편하다.  
이런 각각의 함수를 Resolver라고 한다.  
필드가 스칼라 값인 경우, 더 이상의 연쇄적인 Resolver 호출이 일어나지 않고 실행이 즉시 종료된다.  
필드가 스칼라 의외의 우리가 정의한 타입인 경우, 해당 타입의 리졸버를 호출하게 된다.  
이런 연쇄적인 Resolver 호출 과정은 DFS와 비슷하고 'Graph'라는 단어를 쓴 이유라고 추측할 수 있다.  
Resolver를 이용하여 필요한 데이터만 최적화하여 호출할 수 있다.  

```query
Query: {
  paymentsByUser: async (parent, { userId }, context, info) => {
      const limit = await Limit.findOne({ where: { UserId: userId } })
      const payments = await Payment.findAll({ where: { LimitId: limit.id } })
      return payments        
  },  
},
Payment: {
  limit: async (payment, args, context, info) => {
    return await Limit.findOne({ where: { id: payment.LimitId } })
  }
}
```
리졸버 함수는 위와 같이 총 4개의 인자를 받는다.  
- 첫번째 인자(parent)는 연쇄적 Resolver 호출에서 부모 Resolver가 리턴한 객체이다. 이 객체로 현재 Resolver가 내보낼 값을 조절할 수 있다.
- 두번째 인자(args)는 쿼리에서 입력으로 넣은 인자다.
- 세번째 인자(context)는 모든 Resolver에 전달된다. 주로 미들웨어를 통해 입력된 로그인 정보나 권한 같은 정보를 가진다.
- 네번째 인자(info)는 Schema 정보와 현재 쿼리의 특정 필드 정보를 가진다. 잘 사용하지 않는 필드이다.

### 인트로스펙션(Introspection)  
REST API 협업 방식에서는 API 명세서를 주고받는 절차가 필수적이었다.  
때때로 API 변경사항을 제때 반영하지 못하거나 프론트앤드에서 일일이 명세서를 확인해야해서 작업 생산성을 저해시키는 원인이 되기도 한다.  
이런 문제를 해결하는 것이 gql의 Introspection이다.  
Introspection은 서버 자체에서 현재 서버에 정의된 스키마의 실시간 정보를 공유할 수 있게한다.  
Client Side에서는 백엔드 개발자에게 따로 요청할 필요 없이 Introspection에 명시된 실시간 스키마 정보를 보고, 그에 맞게 쿼리문을 작성하면 된다.   
Introspection용 쿼리가 따로 존재하지만 굳이 스키마 Introspection을 위해 gql 쿼리문을 작성할 필요가 없다.  
대부분의 서버용 gql 라이브러리에는 쿼리용 IDE를 제공한다.  
<img width="679" alt="스크린샷 2021-10-13 오후 8 56 23" src="https://user-images.githubusercontent.com/66231761/137127918-be8607eb-41f4-4d20-8b9e-61be8de382d2.png">

개발자는 Introspection을 활용하여 직접 Query나 Mutation, 필드 스키마를 확인할 수 있다.  
다만 보안상의 이유로 스키마 공개는 신중해야 한다.  
gql 라이브러리는 해당 기능을 켜고 끄게 하는 옵션이 존재한다.  

# GraphQL의 다양한 라이브러리들
gql 자체는 쿼리 언어이다.  
gql을 구체적으로 활용할 수 있도록 돕는 라이브러리들이 존재한다.  

1. Relay
2. Apollo GraphQL





# Reference  
- https://tech.kakao.com/2019/08/01/graphql-basic/  
- https://ko.wikipedia.org/wiki/%EA%B7%B8%EB%9E%98%ED%94%84QL  
- https://blog.apollographql.com/graphql-vs-rest-5d425123e34b
