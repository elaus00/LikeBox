app/
├── src/
│   ├── main/
│   │   ├── java/com/example/likebox/
│   │   │   ├── di/                          // Dependency Injection
│   │   │   │   ├── AppModule.kt
│   │   │   │   └── NetworkModule.kt
│   │   │   │
│   │   │   ├── data/                        // 데이터 계층
│   │   │   │   ├── api/                     // API 관련 클래스
│   │   │   │   │   ├── SpotifyApi.kt
│   │   │   │   │   ├── AppleMusicApi.kt
│   │   │   │   │   └── YoutubeMusicApi.kt
│   │   │   │   │
│   │   │   │   ├── repository/              // 리포지토리 구현
│   │   │   │   │   ├── MusicRepository.kt
│   │   │   │   │   ├── AuthRepository.kt
│   │   │   │   │   └── UserRepository.kt
│   │   │   │   │
│   │   │   │   └── model/                   // 데이터 모델
│   │   │   │       ├── Song.kt
│   │   │   │       ├── Playlist.kt
│   │   │   │       └── User.kt
│   │   │   │
│   │   │   ├── domain/                      // 비즈니스 로직 계층
│   │   │   │   ├── usecase/                 // Use cases
│   │   │   │   │   ├── auth/
│   │   │   │   │   │   ├── LoginUseCase.kt
│   │   │   │   │   │   └── ConnectPlatformUseCase.kt
│   │   │   │   │   │
│   │   │   │   │   ├── music/
│   │   │   │   │   │   ├── SyncLikedSongsUseCase.kt
│   │   │   │   │   │   └── GetPlaylistsUseCase.kt
│   │   │   │   │   │
│   │   │   │   └── repository/              // Repository interfaces
│   │   │   │       ├── IMusicRepository.kt
│   │   │   │       └── IUserRepository.kt
│   │   │   │
│   │   │   ├── ui/                          // UI 계층
│   │   │   │   ├── main/                    // 메인 화면
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   └── MainViewModel.kt
│   │   │   │   │
│   │   │   │   ├── auth/                    // 인증 관련 화면
│   │   │   │   │   ├── LoginActivity.kt
│   │   │   │   │   └── LoginViewModel.kt
│   │   │   │   │
│   │   │   │   ├── songs/                   // 좋아요 곡 관련 화면
│   │   │   │   │   ├── LikedSongsFragment.kt
│   │   │   │   │   └── LikedSongsViewModel.kt
│   │   │   │   │
│   │   │   │   ├── playlists/              // 플레이리스트 관련 화면
│   │   │   │   │   ├── PlaylistsFragment.kt
│   │   │   │   │   └── PlaylistsViewModel.kt
│   │   │   │   │
│   │   │   │   └── settings/               // 설정 화면
│   │   │   │       ├── SettingsFragment.kt
│   │   │   │       └── SettingsViewModel.kt
│   │   │   │
│   │   │   ├── util/                       // 유틸리티 클래스들
│   │   │   │   ├── Constants.kt
│   │   │   │   ├── Extensions.kt
│   │   │   │   └── DateUtils.kt
│   │   │   │
│   │   │   └── LikeBoxApplication.kt       // Application 클래스
│   │   │
│   │   ├── res/                            // 리소스 파일들
│   │   │   ├── layout/                     // 레이아웃 XML 파일들
│   │   │   ├── values/                     // 값 리소스 파일들
│   │   │   └── drawable/                   // 이미지 리소스 파일들
│   │   │
│   │   └── AndroidManifest.xml
│   │
│   └── test/                               // 테스트 코드
│       ├── java/
│       │   └── com/example/likebox/
│       │       ├── repository/
│       │       ├── usecase/
│       │       └── viewmodel/
│       │
│       └── resources/