app/
├── manifests/
│   └── AndroidManifest.xml
├── kotlin+java/
│   └── com.example.likebox/
│       ├── data/
│       │   ├── firebase/
│       │   │   ├── FirebaseService
│       │   │   └── FirebaseServiceImpl
│       │   ├── model.dto/
│       │   │   ├── MusicContentDto
│       │   │   ├── PlatformAuthDto
│       │   │   └── TrackDto.kt
│       │   └── repository/
│       │       └── MusicRepositoryImpl
└──                 AuthRepositoryImpl
PlatformRepositoryImpl
│       ├── di/
│       │   └── FirebaseModule
RepositoryModule
│       ├── domain/
│       │   ├── model/
│       │   │   ├── Album
│       │   │   ├── ContentType
│       │   │   ├── MusicContent
│       │   │   ├── MusicPlatform
│       │   │   ├── PlatformConnection.kt
│       │   │   ├── Playlist
│       │   │   ├── Settings
│       │   │   ├── Track
│       │   │   └── User
│       │   ├── repository/
│       │   │   ├── MusicRepository
│       │   │   ├── AuthRepository
│       │   │   ├── PlatformRepository
│       │   │   └── PlatformAuthRepository
│       │   └── usecase/
│       │       └── Auth
│       │           └── CheckAuthStatusUseCase
│       │       ├── GetMusicListUseCase
│       │       └── SyncContentUseCase
│       └── presentation/
│           ├── state/
│           ├── view/
│           │   ├── navigation/
│           │   │   ├── NavigationBar.kt
│           │   │   ├── NavigationHost.kt
│           │   │   └── NavigationState.kt
│           │   └── screens/
│           │       ├── auth/
│           │       │   ├── platform/
│           │       │   │   ├── OnBoardingScreen.kt
│           │       │   │   ├── SignInScreen.kt
│           │       │   │   └── SignUpScreen.kt
│           │       ├── home/
│           │       │   └── HomeScreen.kt
│           │       ├── library/
│           │       │   ├── libraryScreen.kt
│           │       │   └── PlaylistsScreen
│           │       ├── search/
│           │       │   └── SearchScreen
│           │       └── settings/
│           │           ├── SettingsScreen
│           │           └── Screens
│           ├── theme/
│           │   ├── Buttons.kt
│           │   ├── Color.kt
│           │   ├── components.kt
│           │   ├── Font.kt
│           │   ├── Theme.kt
│           │   └── Type.kt
│           ├── viewModel/
│           │   ├── ContentViewModel.kt
│           │   └── NavigationViewModel.kt
│           └── MainActivity