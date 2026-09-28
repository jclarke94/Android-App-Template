# AppTemplate

A starting point for new Android apps, extracted from the working architecture of TenderNest
Android (Koin + Voyager, networking, Room storage, and a design-system component library) with
the product-specific screens stripped out. Sibling project to the iOS AppTemplate — same scope,
same pattern names, so a screen built against one platform's spec reads the same on the other.

## Pattern

Every screen follows **Screen → ViewModel → Content**:

- `nav/screen/<Feature>Screen.kt` — the Voyager `Screen` that resolves its ViewModel via
  `koinScreenModel()`, wires navigation (`LocalNavigator`, `.push`/`.pop`/`.replaceAll`), and
  wraps its `Content` composable in `ui/components/ScreenOverlay.kt` — the banner/alert/loading
  overlay every screen gets for free (see `ui/ViewModels/BaseViewModel.kt`).
- `ui/ViewModels/<Feature>ViewModel.kt` — extends `BaseViewModel`, which gives every screen
  `state: StateFlow<UIState>` (loading/loaded/error), a self-dismissing `banner`, and a blocking
  `alertMessage`, plus `showAlert(...)`/`showError(...)` helpers. Screens that need nothing more
  than the base (e.g. `ActivityViewModel`) are a one-liner.
- `ui/content/<Feature>Content.kt` — the actual layout, built from `ui/components/*` (buttons,
  dialogs, text styles, inputs) and `ui/theme/Color.kt`/`Font.kt`/`Type.kt`. Content composables
  take plain callbacks and don't know about Voyager or Koin — same split as the iOS template's
  View/Content.

`nav/screen/HomeScreen.kt` (`HomeTab`/`HomeScreen`/`HomeContent` + a pushed `DetailScreen`) and
`nav/screen/settings/SettingsScreen.kt` are the smallest examples of this. Auth
(`nav/screen/{Welcome,Login,Register,RegisterProfileImage,PasswordReset,Banned}Screen.kt`) is the
fullest one — Welcome → Login/Register → (optional profile image) → the logged-in app, plus a
Banned/suspended-account screen with an appeal form.

## Setting up a new project from this template

1. Copy this folder to your new project's location, `cd` in, and rename the app:
   - `app/build.gradle.kts`: `namespace`/`applicationId` (currently `com.sostudio.apptemplate`).
   - `settings.gradle.kts`: `rootProject.name`.
   - Package `com.sostudio.apptemplate` under `app/src/main/java/` → rename to your own package
     (Android Studio's Refactor > Rename Package handles the directory move + import rewrite).
   - `app/src/main/res/values/strings.xml`'s `app_name`.
   - `AndroidManifest.xml`'s deep-link `<data android:scheme="apptemplate" />` — also update
     `MainActivity.handleDeepLink()`'s `uri.scheme != "apptemplate"` check to match.
   - `AppDatabase`'s Room database filename (`"apptemplate.db"` in `di/DatabaseModule.kt`) and
     `data_extraction_rules.xml`/`backup_rules.xml`'s excluded paths.
2. Add your own `app/google-services.json` from your Firebase console project (a placeholder
   with fake IDs is committed here so the project builds out of the box — replace both the
   `com.sostudio.apptemplate` and `com.sostudio.apptemplate.staging` client entries with your
   real ones). Push notifications work once a real file is in place; the placeholder just lets
   Gradle sync/build without crashing.
3. Fill in `API_HOST` for each build type (`app/build.gradle.kts`'s `defaultConfig`/`staging`
   blocks) once you have a real API. `SENTRY_DSN` reads from a Gradle property or `SENTRY_DSN`
   env var — leave blank and Sentry no-ops.
4. Open in Android Studio, let Gradle sync (resolves Koin, Voyager, Retrofit, Room, Sentry,
   Firebase, Coil, Lottie from network on first sync), and run.

## What's included

- **Auth flow** (`nav/screen/*`, `ui/ViewModels/Auth/*`, `ui/content/auth/*`) — Welcome, Login,
  Register (+ profile image upload step), Password reset, and a Banned/suspended screen with an
  appeal form (currently posts through the generic developer-feedback endpoint as a stand-in —
  swap for a real appeals API).
- **Networking** (`data/remote/`) — `RetrofitClient`/`BaseService`/`ServiceManager` skeleton
  (Retrofit + OkHttp + kotlinx.serialization + the Sandwich response wrapper), `AuthApi` as the
  endpoint-definition template (register/login/logout-adjacent/password-reset/avatar
  upload/device-token/notification-settings/developer-feedback), `AuthRepo` as the
  repo/service-layer template.
- **Storage** (`data/db/`, `data/prefs/`) — Room (`AppDatabase`) with one `UserEntity` +
  `UserDao` as the entity/DAO pattern, plus `PrefsHelper` (`EncryptedSharedPreferences`) as the
  session/token store and device-preference cache.
- **Auth/session** (`data/prefs/PrefsHelper.kt`, `data/db/Logout.kt`) — encrypted token storage
  (not plain `SharedPreferences`), with a single `performLogout(appDatabase, prefsHelper)` every
  logout path should route through (sign-out, account deletion, biometric-lock log out, the 401
  auth-failure interceptor) — add your own feature's cache-clearing calls there as you build
  them out, instead of scattering partial cleanup across call sites.
- **Navigation** (`nav/`, Voyager) — `MainTabScreen` hosting a `TabNavigator` for `HomeTab`/
  `ActivityTab`, `TabSwitcher` for requesting a tab switch from outside the tab shell (e.g. after
  Settings pops off the root Navigator), `LocalBottomBarVisible`/`trackBottomBarVisibility` for
  hiding the bottom bar on a pushed screen, and `rootNavigator()` for reaching the true app-root
  Navigator from inside a tab (used by Settings and by every logout redirect). Add a `Tab` object
  and a branch in `MainTabScreen`'s bottom bar for each new tab.
- **Design system** (`ui/components/*`, `ui/theme/`) — buttons, dialogs (banner + blocking
  alert), text styles, input fields, a biometric-lock screen, a photo picker, a runtime-permission
  helper, `ui/theme/Color.kt`'s semantic tokens (`Primary`/`Accent`/`Highlight`/`Background` +
  `Success`/`Failure`/`Warning`, each with light+dark variants).
- **App shell** (`app/AppTemplateApp.kt`, `activity/MainActivity.kt`) — Sentry + Firebase setup,
  biometric app-lock overlay (`service/BiometricLockManager.kt`), Lottie splash screen, FCM push
  notifications (`service/AppTemplateFirebaseMessagingService.kt`).
- **Tests** — `app/src/test/.../TextSizeStageTest.kt` is a real (small) unit test for the one
  bit of non-trivial pure logic in the template; add your own as you build out features.

## What's intentionally left as a stub

- **In-app billing** — not included, matching the iOS template's StoreKit stub-out.
- **Push notification device-token registration** (`AuthRepo.deviceToken`) posts to
  `AuthApi.deviceToken` — implement that endpoint server-side, or replace it.
- **Real color palette** — `ui/theme/Color.kt` currently holds a neutral placeholder palette.
  Swap for your product's colors; the token *names* can stay the same.
- **Real fonts** — no `.ttf`/`.otf` files are bundled (licensing). `ui/theme/Font.kt` falls back
  to the system font (`FontFamily.Default`) — nothing crashes. Add your own files under
  `res/font/` and wire them into `Font.kt`/`Theme.kt`'s `MessageFont.fontFamily` the same way.
- **Lottie animation** — `SplashScreen`/`LoadingView`/`BiometricLockScreen` reference a
  `"apptemplate_..._animation.json"` asset by filename via `LottieCompositionSpec.Asset(...)`,
  which resolves at runtime rather than compile time — no file is bundled under `app/src/main/
  assets/`, so nothing renders (no crash) until you add one there. Rename the filenames in those
  three files to match whatever you add.
- **App icon** — `mipmap-anydpi-v26/ic_launcher.xml` currently points at Android Studio's
  default new-project template art (not a real icon). Regenerate via Android Studio's Image
  Asset tool with your own art.
- **Settings entry point** — `nav/screen/settings/SettingsScreen.kt` and `BannedScreen.kt` are
  both fully wired but nothing pushes them yet (same gap as the iOS template's `SettingsScreen`,
  which has no in-app trigger either) — add an account/settings icon wherever makes sense for
  your app, and a `getUser().bannedAt != null` check on foreground/return-from-background for
  `BannedScreen`.
- **Localizations beyond English** — only `res/values/strings.xml` exists, with the keys the
  retained screens actually reference. Add more `values-<locale>/strings.xml` folders as needed.
