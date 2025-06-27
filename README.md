# ❓What's 픽블엽
픽블엽은 피크민 블룸을 좀 더 잘(?) 즐기기 위해 시작한 프로젝트 입니다.

피크민 블룸은 포켓몬고와 같은 AR기반 게임입니다.

픽블엽은 컨텐츠 중 하나인 엽서를 얻기위해 많은 유저들이 커뮤니티에 공유하던 정보를 지도상에 표시하여 엽서 정보와 위치를 편하게 확인 할 수 있도록 하는게 목표입니다. 

자세한 정보는 [블로그](https://velog.io/@no-int/%ED%94%BD%EB%B8%94%EC%97%BD.-%ED%94%BC%ED%81%AC%EB%AF%BC-%EB%B8%94%EB%A3%B8%EC%9D%84-%EC%A2%80-%EB%8D%94-%ED%8E%B8%ED%95%98%EA%B2%8C)를 확인해주세요!

# 👁️‍🗨️프로젝트 기능 살펴보기
- 엽서 조회 기능
  - 픽블엽의 주된 목적으로 지도를 클릭하면 해당 좌표를 중심, 보여지는 화면 2가지를 기준으로 최대 20개의 엽서를 표시.
    - 비로그인 상태로 사용 가능
- 좌표 표시 기능
  - 지도 우클릭시 해당 지점의 좌표를 표시
    - 로그인한 유저라면 해당 지점에 바로 엽서 등록 신청이 가능함.
- 회원 기능
  - google 소셜로그인 구현.
  - 회원의 목적은 엽서등록 신청, 엽서 수정, 엽서 신고로 주된 목적이 아니라 session 방식으로 간단히 구현. 
- 엽서 등록 신청
  - 로그인한 유저 한정 새로운 엽서를 등록 신청.
    - 추후 관리자의 승인 후 엽서 표시.
- 엽서 수정 신청
  - 로그인한 유저 한정 엽서 정보를 수정 신청.
    - 추후 관리자의 승인 후 엽서 수정 반영.
  - 엽서 정보의 정확성을 높이기 위해 수정 신청을 받음.
- 엽서 신고 기능(추가예정)
  - 로그인한 유저 한정 엽서 신고.
    - 추후 관리자의 승인 후 엽서 신고 반영.
  - 오류가 있는 엽서를 배제하기 위한 기능.
- 메일링 기능
  - 엽서 신청 및 수정 등 유저가 요청한 작업의 검토가 끝나면 유저 메일로 알림.
  
# 🎯목표
- 같은 게임을 즐기는 사람들에게 편의성을 제공.
- 실 서비스를 운영하는 경험을 쌓고 운영하면서 알지 못 했던 영역(infra) 및 문제를 경험.
  - 실제로 간단하지만 중국발 봇 공격 및 IOS문제 대응 등 새로알게 된 문제들을 직접 격고 대응하며 블로깅 함.

# 📚이슈 블로깅
- [피크민 블룸 엽서 지도 프로젝트 - "픽블엽" 소개](https://velog.io/@no-int/%ED%94%BD%EB%B8%94%EC%97%BD.-%ED%94%BC%ED%81%AC%EB%AF%BC-%EB%B8%94%EB%A3%B8%EC%9D%84-%EC%A2%80-%EB%8D%94-%ED%8E%B8%ED%95%98%EA%B2%8C)
- [t2.micro OOM 예방 – SWAP 설정으로 안전하게!](https://velog.io/@no-int/t2.micro-OOM-%EC%98%88%EB%B0%A9-SWAP-%EC%84%A4%EC%A0%95%EC%9C%BC%EB%A1%9C-%EC%95%88%EC%A0%84%ED%95%98%EA%B2%8C)
- [☠️ 내 서버를 노리는 수상한 요청들: /index.php 로그 분석과 방어](https://velog.io/@no-int/%EB%82%B4-%EC%84%9C%EB%B2%84%EB%A5%BC-%EB%85%B8%EB%A6%AC%EB%8A%94-%EC%88%98%EC%83%81%ED%95%9C-%EC%9A%94%EC%B2%AD%EB%93%A4-index.php-%EB%A1%9C%EA%B7%B8-%EB%B6%84%EC%84%9D%EA%B3%BC-%EB%B0%A9%EC%96%B4)
- [Apple Touch Icon 대응기](https://velog.io/@no-int/Apple-Touch-Icon-%EB%8C%80%EC%9D%91%EA%B8%B0)
- [Spring Boot에서 SMTP로 HTML 메일 보내기 (Feat.Gmail)](https://velog.io/@no-int/Spring-Boot%EC%97%90%EC%84%9C-SMTP%EB%A1%9C-HTML-%EB%A9%94%EC%9D%BC-%EB%B3%B4%EB%82%B4%EA%B8%B0-Feat.Gmail)
- [📬 Spring Event 도입과 Class의 책임 분리](https://velog.io/@no-int/Spring-Event-%EB%8F%84%EC%9E%85%EA%B3%BC-Class%EC%9D%98-%EC%B1%85%EC%9E%84-%EB%B6%84%EB%A6%AC)

# 🛠️사용 기술
 - Java 21
 - Spring boot 3.2
 - MySQL8.0 / S3
 - JPA(Hibernate6) + QueryDSL

