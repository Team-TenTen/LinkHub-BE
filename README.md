![스크린샷 2024-01-23 오전 1 42 31](https://github.com/Team-TenTen/LinkHub-BE/assets/108216455/aa341f05-3915-425a-b439-c26e644425d9)
## 🔗 LinkHub 🔗
>  링크 아카이빙 및 공유 서비스
## 🔗 프로젝트 기간 🔗

> 프로젝트 기간 : 2023/10/10 ~ 2023/12/05

## 🔗 프로젝트 기획 배경 및 동기 🔗
>  저희는 아래와 같은 불편함들을 해결하기 위해 LinkHub 서비스를 기획했습니다.
- 기존 브라우저의 북마크만으로는 계층 별로 정리하거나 구분하기에 한계가 존재한다.
- 기존 북마크의 경우, 묶인 링크를 한 사람만 확인할 수 있으며 다른 사용자와의 공유가 불편하다.
- 슬랙 혹은 카카오 단톡방에 링크 공유 시 다른 텍스트 메세지들에 의해 해당 링크가 자주 묻힌다.
- 슬랙 혹은 단톡방에서 팀원들이 해당 링크를 봤는지 확인하고 싶다.
- 슬랙 혹은 카카오 단톡방에 링크를 공유하면 해당 링크를 본 인원들을 파악하고 싶다.
- 지역 맛집 혹은 쇼핑몰 등을 검색하면 양질의 정보가 아닌 많은 광고들로 인해 정보를 필터링하는 추가적인 비용이 소모된다.
## 🔗 핵심 기능 🔗
- **링크 아카이빙**: 
  <br>링크의 메타태그를 통해 자동으로 제목을 추천해주며 태그를 통해 추후 필터링의 용이성을 올림
  <img width="948" alt="스크린샷 2024-01-23 오전 1 38 16" src="https://github.com/Team-TenTen/LinkHub-BE/assets/108216455/370b6e66-141d-415c-b685-5da911142f87">

  
- **공유 아카이빙 공간**:
  <br>스페이스 초대 기능을 통해 언제든지 팀원들을 초대할 수 있으며 viewer 및 editor 등의 세부 권한 지정이 가능하다. 또한 설정 시 팀원들의 링크 조회 이력을 표시할 수 있다.
  <img width="957" alt="스크린샷 2024-01-23 오전 1 38 33" src="https://github.com/Team-TenTen/LinkHub-BE/assets/108216455/53bf0690-1e53-4e83-8db2-e140971eb1fd">


- **즐겨찾기**:
  <br>공개된 스페이스(링크 저장소)는 즐겨찾기 기능을 통해 구독을 할 수 있다. 또한 많은 유저들에게 즐겨찾기된 스페이스는 메인화면 즐겨찾기 순 옵션에서 상위에 노출되게 된다.
  <img width="948" alt="스크린샷 2024-01-23 오전 1 38 47" src="https://github.com/Team-TenTen/LinkHub-BE/assets/108216455/5a6d8d7a-6dfe-4433-9b0f-878b2ef89741">


- **가져오기**:
  <br>공개된 스페이스를(링크 저장소) 복사하여 편집 가능한 나의 스페이스로 만들 수 있다. 즐겨찾기와 다르게 원본의 이후 변경사항은 반영되지 않는다.
  <img width="955" alt="스크린샷 2024-01-23 오전 1 39 06" src="https://github.com/Team-TenTen/LinkHub-BE/assets/108216455/b4539f72-a9ff-4120-ad98-c431a2e26f9d">


## 👨‍👩‍👦 서버 팀원 소개
| Team Leader |Developer |                                   Developer                                    |
|:---:|:---:|:------------------------------------------------------------------------------:|
|[윤영운](https://github.com/young970)| [김민희](https://github.com/KimMinheee) |                   [안재영](https://github.com/JaeyoungAhn)                |
| ![yyw](https://github.com/Team-TenTen/LinkHub-BE/assets/90172648/b1a43ad7-3995-4729-a767-327f560ff427)|![c1a86da69fcdb09be539e42fb5b17f8d-sticker](https://github.com/Team-TenTen/LinkHub-BE/assets/90172648/d7cf2cd2-7f53-4e43-9c54-17048e921060)|![ajy](https://github.com/Team-TenTen/LinkHub-BE/assets/90172648/a363ce18-1898-4546-ad2c-19b5925c5483)
|


## 💻 기술스택
### 개발 환경
  <img src="https://img.shields.io/badge/java_17 -007396?style=for-the-badge&logo=java&logoColor=white"></a>
  <img src="https://img.shields.io/badge/springboot 3.1.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"></a>
  <img src="https://img.shields.io/badge/-Spring Data JPA-gray?style=for-the-badge&logoColor=white"/></a>
  <img src="https://img.shields.io/badge/Query DSL-0078D4?style=for-the-badge&logo=Spring Data JPA&logoColor=white"/></a>
  <img src="https://img.shields.io/badge/mysql 8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> </a>
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"/></a>
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"></a>
  <img src="https://img.shields.io/badge/Junit-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"/></a>
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white"/></a>
  <img src="https://img.shields.io/badge/Spring Oauth2-000000?style=for-the-badge&logo=oauth2&logoColor=white"></a>
  <img src="https://img.shields.io/badge/mapstruct-6DB33F?style=for-the-badge&logo=mapstruct&logoColor=white"></a>

### 인프라
<img src="https://img.shields.io/badge/amazon aws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"></a>
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"></a>
<img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white"></a>
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white"></a>
<img src="https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white"></a>
<img src="https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white"></a>
<img src="https://img.shields.io/badge/Grafana Loki-F46800?style=for-the-badge&logo=grafana&logoColor=white"></a>


## 👨‍👩‍👦 협업 툴
|                                                  Notion                                                   |                                                       Slack                                                        |                                                        Discord                                                        |                                                        GitHub                                                        |                                                              Erd Cloud                                                               |                                                        Figma                                                        |
|:---------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------:|
| <img src="https://img.shields.io/badge/Notion-FFFFFF?style=flat-square&logo=Notion&logoColor=black"/></a> | <img src="https://img.shields.io/badge/slack-232F3E?style=flat-square&logo=slack&logoColor=white&style=flat"/></a> | <img src="https://img.shields.io/badge/Discord-232F3E?style=flat-square&logo=Discord&logoColor=blue&style=flat"/></a> | <img src="https://img.shields.io/badge/Github-000000?style=flat-square&logo=Github&logoColor=white&style=flat"/></a> | <img src="https://img.shields.io/badge/Erd Cloud-0052CC?style=flat-square&logo=erdcloud%20software&logoColor=white&style=flat"/></a> | <img src="https://img.shields.io/badge/Figma-50B584?style=flat-square&logo=Figma&logoColor=white&style=flat"/></a> |


## 🏰 아키텍처
<img width="738" alt="서버 아키텍처(v3)" src="https://github.com/Team-TenTen/LinkHub-BE/assets/108216455/4e964e90-be09-4b72-a46b-2066f6076b5c">


## 📘 ERD
![스크린샷 2023-12-03 오후 4 44 07](https://github.com/Team-TenTen/LinkHub-BE/assets/90172648/15b82f59-5f85-4567-996d-66652ac44aa0)


    
## 문서

[📁 LinkHub API 명세서 ](https://www.notion.so/prgrms/API-c9e7dd4d09b246999a0022273810e4f7?pvs=4) <br>
[📔 LinkHub Git 전략 ](https://www.notion.so/prgrms/e254eecae50d40adb34c28a2ebb46301?pvs=4) <br>
[🔗 LinkHub 기획서](https://www.notion.so/prgrms/LinkHub-546003d57aa34297a09ee98efa65cc25?pvs=4) <br>
[💽 LinkHub ERD Cloud](https://www.erdcloud.com/d/MMe8PPsA6NXLDDwQX) <br>
