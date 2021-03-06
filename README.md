# 2021-SW-Hackerton

<img src="https://user-images.githubusercontent.com/42195466/126633147-ba3dd6ea-4479-4700-9a3a-285c57d62ace.PNG" width="300px" height="400px">

# " KNUs(크누어스) "  
  ※ KNUs는 안드로이드 스튜디오에서 제작한 프로그램입니다. 
  
  ※ 이 어플은 "Pixel 2 API 29"를 기준으로 작성된 프로그램입니다.
  
  ※ (주의) 비밀번호는 8자리 이상 작성하여야 합니다.

- 팀명 : 면상에 지건

- 팀원 : 17 심컴 박상면(팀장, 면상), 17 심컴 류진호(지), 17 심컴 이건형(건)

- 제목 : 

  KNUs(크누어스) 는 경북대학교 학생들의 복수, 단체를 의미하며 us는 "함께" 한다는 의미로 저희가 커뮤니케이션을 형성하여 유대감이나 친목 형성이 목표라는 점에서 유래되었습니다.

- 로고 : 

  경북대를 상징하는 '소'와 붉은 색을 합친 붉은 소의 위에 메신저(커뮤니티)를 나타낼 수 있는 말풍선을 합쳐서 디자인하였습니다.

## 주제

- **대학생들을 위한 익명 음성 및 화상 채팅 플랫폼**

## 목적

- **COVID-19**로 변화된 대학 생활

  - "설레는 마음으로 입학했지만 고등학교 때와 다른게 없어서 슬퍼요..." - 중고 신입 20학번 -
  - "동아리에 가입을 했는데 누가 있는지도 몰라요... 혹시 저만 있나요?" - 신입 동아리원 박OO-
  - "친구랑 시험기간에 학교에서 남아 같이 공부하는 내 로망이...." - IT대학 컴OO학부 최XX -
  - "혼밥은 멈춰...! 나랑 같이 밥 먹을 사람 어디 없나?" - 3년째 화장실에서 혼밥하는 이ㅁㅁ -
  - "롤 서폿구함!! 너만 오면 5인큐 고고" - 만년 실버인 익명의 제보자 -

  

  불과 2년 전에는 동기들과 수업을 같이 듣고 밥을 먹거나, 노래방, 게임 등을 즐기며 학업뿐만 아니라 친목활동에도 힘을 썼으며 선후배 간의 소통도 활발하게 이루어졌습니다.
  하지만, 코로나의 영향으로 인해 비대면으로 전환이 되면서 예전과 같은 친목과 소통 및 교류가 어려워졌습니다.

  이러한 문제점을 해결하고자 **학생들 사이의 친목 커뮤니티**를 만들기로 했습니다. 

## 설명

- #### 쌍방향 통신 기반

사용자가 방을 개설하고 다른 사람들이 그 방에 들어가 스피커(발언자)의 대화를 들을 수 있습니다.  대화방 리스트를 보고 들어온 청취자들도 대화에 참여할 수 있다. 여느 SNS(인스타그램, 페이스북, 유튜브 등)와 달리 모두가 실시간으로 대화를 통해 소통할 수 있다.

- #### 익명성 보장

발언의 자유를 보장하여 더욱더 적극적인 참여를 유도하기 위하여 익명성을 가지도록 설계하였습니다. 대표 대학 익명 커뮤니티 "에브리타임"과 다른 점은 본인의 프로필에 전공이 표시되도록 하였습니다. (차후 추가 예정)

- #### 자유롭고 다양한 주제 생성

사용자가 원하는 주제로 대화방을 열기 때문에 각자의 관심분야나 원하는 주제의 방에 참여할 수 있습니다. 예를 들어, 대학 전공별로 방을 생성할 수도 있고 게임이나 음악 같은 취미생활 공유를 위한 방을 생성할 수도 있습니다. 이로 인해, 단절되어 있던 선후배 간의 교류 혹은 동아리 부원들 간의 소통, 동기들끼리의 친목 등 여러 가지 주제로 온라인상에서 모일 수 있습니다.

##### 이러한 기능을 가진 저희 KNUs(크누어스)를 통해 코로나로 인해 외롭고 단절된 이 시국에 방역수칙을 어기지 않으면서 합법적으로 온라인상에 모일 수 있습니다.

## 예시 화면
<pre>
                              [방 목록 검색 예시]                                        [방 접속 예시]
</pre>      
<p align="center">
  <img src="https://user-images.githubusercontent.com/42195466/126719177-1fc5bb8f-76be-4e67-995f-6355016c06db.gif" width="40%"/>        <img src="https://user-images.githubusercontent.com/42195466/126719973-a8812b08-9431-4e82-84f0-5d7f59c4ded3.gif" width="40%"/>
</p>

## 세부 기능

- 개발 환경 : Android Studio (4.1.2), Firebase
- 개발 언어 : Kotlin

- #### 회원가입 및 로그인

  - DB(Firebase Authentification)와 연동하여 기능 구현
  - 회원가입시 비밀번호는 8자리 이상으로 작성하여야함.
  - 계정의 이메일, 패스워드, 학번, 이름, 닉네임, 성별, 관심 카테고리를 입력하여 회원 가입 가능
  - 추후 학교 및 학과 인증 구현 예정

- #### 방 생성 및 검색

  - DB(Firebase Realtime Database)와 연동하여 기능 구현

  - 원하는 제목으로 방 생성하면 다른 사용자들의 화면에 실시간 업데이트

  - 검색 창을 통해 제목으로 검색 가능

- #### 프로필 및 설정 메뉴

  - 프로필 사진, 닉네임, 이메일 등 확인 가능 (추후 변경 기능 구현 예정)
  - 로그아웃 기능
  - 타 프로필 클릭을 통해 개인 메시지 전송 기능 (추후 구현 예정)

- #### 음성 채팅 및 화상 채팅

  - 방 생성 시 음성 또는 화상 모드 선택 가능
  - 방 입장 후, 자유로운 대화 (관련 API가 유료인 관계로 제한적 기능 구현)

## 성장 가능성

- 경북대학교만이 아닌 전국 모든 대학교를 대상으로 플랫폼 구축
- 타 대학끼리 연계하여 교류할 수 있는 플랫폼으로 발전 가능
- 코로나19가 끝나도 학생들 간의 대표 학교 커뮤니티로 발전 가능
- 대한민국을 빛낸 올해의 어플 1위 후보.......가 되기를 원함

## 시연 영상

- 호스트 시점 영상

  https://youtu.be/dVHAKgTQ4l8

+ 참여자 시점 영상

  https://youtu.be/TENlw4P24qk
