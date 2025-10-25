# 매출 관리 시스템

식당 매출을 손쉽게 기록하고 조회할 수 있는 웹 애플리케이션입니다.

## 🚀 주요 기능

- **매출 입력**: 매일의 매출을 빠르고 정확하게 기록
- **매출 조회**: 날짜별 매출 현황을 한눈에 확인
- **모바일 최적화**: 스마트폰에서도 편리하게 사용 가능
- **보안 인증**: Supabase Auth를 통한 안전한 로그인
- **실시간 조회**: 저장과 동시에 조회 가능

## 🛠 기술 스택

### 프론트엔드
- **Next.js 15** - React 프레임워크
- **TypeScript** - 타입 안전성
- **Tailwind CSS** - 스타일링
- **shadcn/ui** - UI 컴포넌트
- **Supabase** - 인증 및 데이터베이스

### 백엔드
- **Spring Boot 3.5** - Java 프레임워크
- **Spring Security** - 보안
- **JPA/Hibernate** - ORM
- **PostgreSQL** - 데이터베이스
- **JWT** - 토큰 인증

## 📋 사전 요구사항

- Node.js 18+ 
- Java 17+
- PostgreSQL 13+
- Supabase 계정

## 🚀 설치 및 실행

### 1. Supabase 설정

1. [Supabase](https://supabase.com)에서 새 프로젝트 생성
2. 데이터베이스에서 다음 테이블 생성:

```sql
CREATE TABLE sales_record (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    store_id UUID NOT NULL,
    record_date DATE NOT NULL,
    customer_count INTEGER NOT NULL,
    amount INTEGER NOT NULL,
    memo TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by_user_id UUID NOT NULL
);

CREATE INDEX idx_sales_record_store_date ON sales_record(store_id, record_date);
```

3. Supabase 프로젝트 설정에서 JWT Secret 복사

### 2. 백엔드 설정

```bash
cd backend

# 의존성 설치
./gradlew build

# 환경 변수 설정
export SUPABASE_URL=your-supabase-url
export SUPABASE_ANON_KEY=your-anon-key
export SUPABASE_JWT_SECRET=your-jwt-secret
export CORS_ALLOWED_ORIGINS=http://localhost:3000

# 데이터베이스 설정 (application.properties 수정)
# spring.datasource.url=jdbc:postgresql://localhost:5432/tbr_db
# spring.datasource.username=your-username
# spring.datasource.password=your-password

# 애플리케이션 실행
./gradlew bootRun
```

### 3. 프론트엔드 설정

```bash
cd frontend

# 의존성 설치
npm install

# 환경 변수 설정 (.env.local 파일 생성)
NEXT_PUBLIC_SUPABASE_URL=your-supabase-url
NEXT_PUBLIC_SUPABASE_ANON_KEY=your-anon-key
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080

# 개발 서버 실행
npm run dev
```

## 📱 사용 방법

### 1. 로그인
- Supabase Auth를 통해 이메일/비밀번호로 로그인
- 로그인 후 자동으로 매출 입력 페이지로 이동

### 2. 매출 입력
- 매출 발생일자 (기본값: 오늘)
- 고객 수
- 금액 (천 단위 콤마 자동 표시)
- 메모 (선택사항)
- 저장 후 동일 날짜 반복 입력 가능

### 3. 매출 조회
- 날짜 선택으로 특정 날짜 매출 조회
- 총 매출 금액 및 고객 수 요약
- 상세 매출 내역 리스트
- 빈 상태 시 매출 입력으로 이동 가능

## 🔧 API 엔드포인트

### 매출 입력
```
POST /api/sales
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "date": "2024-01-15",
  "customerCount": 25,
  "amount": 150000,
  "memo": "점심 단체 예약"
}
```

### 매출 조회
```
GET /api/sales?date=2024-01-15
Authorization: Bearer <jwt-token>
```

## 🏗 프로젝트 구조

```
practice_tbr/
├── backend/                 # Spring Boot 백엔드
│   ├── src/main/java/com/practice/tbr/
│   │   ├── controller/      # REST API 컨트롤러
│   │   ├── service/        # 비즈니스 로직
│   │   ├── repository/     # 데이터 접근 계층
│   │   ├── entity/         # JPA 엔티티
│   │   ├── dto/           # 데이터 전송 객체
│   │   └── security/      # JWT 인증 설정
│   └── build.gradle
├── frontend/               # Next.js 프론트엔드
│   ├── src/
│   │   ├── app/           # 페이지 라우팅
│   │   ├── components/    # React 컴포넌트
│   │   ├── hooks/         # 커스텀 훅
│   │   └── lib/           # 유틸리티 및 설정
│   └── package.json
└── docs/                   # 프로젝트 문서
    ├── prd.md            # 제품 요구사항
    ├── ia.md             # 정보 구조
    └── design.md         # 디자인 가이드
```

## 🔒 보안

- JWT 토큰 기반 인증
- CORS 설정으로 프론트엔드 도메인만 허용
- 매장별 데이터 격리 (store_id 기반)
- 입력 데이터 검증 (미래 날짜, 음수 금액 등)

## 📱 반응형 디자인

- 모바일 우선 설계
- 데스크탑에서 확장된 레이아웃
- 터치 친화적 UI/UX

## 🚀 배포

### 백엔드 (AWS EC2)
```bash
# JAR 파일 빌드
./gradlew bootJar

# EC2에서 실행
java -jar build/libs/tbr-0.0.1-SNAPSHOT.jar
```

### 프론트엔드 (Vercel)
```bash
# Vercel CLI 설치
npm i -g vercel

# 배포
vercel --prod
```

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 📞 지원

문제가 있거나 질문이 있으시면 이슈를 생성해 주세요.
