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
│       ├── di/
│       │   └── FirebaseModule
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
│       │   │   └── PlatformAuthRepository
│       │   └── usecase/
│       │       ├── GetMusicListUseCase
│       │       └── SyncContentUseCase
│       └── presentation/
│           ├── state/
│           │   └── ContentViewState.kt
│           ├── view/
│           │   ├── components/
│           │   │   └── components.kt
│           │   └── navigation/
│           │       ├── NavBarItems.kt
│           │       ├── Navigation.kt
│           │       ├── NavigationBar.kt
│           │       ├── NavigationProvider.kt
│           │       └── Screen.kt
│           ├── screens/
│           │   ├── home/
│           │   │   └── HomeScreen.kt
│           │   ├── library/
│           │   │   └── libraryScreen.kt
│           │   ├── notification/
│           │   ├── register/
│           │   │   ├── OnBoarding.kt
│           │   │   ├── SignInScreen.kt
│           │   │   └── SignUpScreen.kt
│           │   ├── search/
│           │   └── settings/
│           │       └── MainScreen.kt
│           ├── theme/
│           │   ├── Buttons.kt
│           │   ├── Color.kt
│           │   ├── Theme.kt
│           │   └── Type.kt
│           ├── viewmodel/
│           │   ├── ContentViewModel.kt
│           │   └── NavigationViewModel.kt
│           ├── MainActivity
│           └── LikeBox