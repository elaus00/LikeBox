app/
├── src/
├── main/
├── java/
└── com/
└── example.likebox/
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Shape.kt
│   │   ├── Theme.kt
│   │   └── Typography.kt
│   ├── components/
│   │   ├── CommonButton.kt
│   │   ├── LoadingIndicator.kt
│   │   └── ...
│   ├── navigation/
│   │   ├── NavGraph.kt
│   │   ├── Screens.kt
│   │   └── BottomNavBar.kt
│   └── screen/
│       ├── home/
│       │   ├── HomeScreen.kt
│       │   └── HomeViewModel.kt
│       ├── login/
│       │   ├── LoginScreen.kt
│       │   └── LoginViewModel.kt
│       ├── playlist/
│       │   ├── PlaylistScreen.kt
│       │   └── PlaylistViewModel.kt
│       ├── settings/
│       │   ├── SettingsScreen.kt
│       │   └── SettingsViewModel.kt
│       └── notification/
│           ├── NotificationScreen.kt
│           └── NotificationViewModel.kt
├── data/
│   ├── model/
│   │   ├── User.kt
│   │   ├── Track.kt
│   │   ├── Playlist.kt
│   │   └── Notification.kt
│   ├── repository/
│   │   ├── AuthRepository.kt
│   │   ├── TrackRepository.kt
│   │   ├── PlaylistRepository.kt
│   │   └── NotificationRepository.kt
│   └── source/
│       ├── remote/
│       │   ├── FirebaseAuthSource.kt
│       │   ├── FirestoreSource.kt
│       │   └── SpotifyApiService.kt
│       └── local/
│           └── LocalDatabase.kt
├── domain/
│   └── usecase/
│       ├── LoginUseCase.kt
│       ├── SyncTracksUseCase.kt
│       └── CreatePlaylistUseCase.kt
├── di/
│   └── AppModule.kt
├── utils/
│   ├── Constants.kt
│   └── Extensions.kt
└── App.kt
└── res/
├── values/
├── drawable/
└── ...
