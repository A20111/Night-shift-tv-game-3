# Night Shift — TV game with phone controller

This project is a small Android TV party game. The TV runs the game and hosts a local controller webpage; a phone on the same Wi-Fi acts as the controller.

## Build the Android TV APK
1. Upload the contents of this folder to a GitHub repository (keep `.github/workflows/build.yml`).
2. Open **Actions** and run **Build Night Shift APK** (or push a commit).
3. After the workflow completes, open the `latest-build` release and download `app-debug.apk`.
4. Install the APK on an Android TV / Android TV box that allows sideloading.

## Play
1. Launch **Night Shift** on the TV.
2. The TV screen displays a local URL such as `http://192.168.x.x:8080`.
3. Connect your phone to the same Wi-Fi network and open that URL in the phone's browser.
4. Tap **Tap to start**, drag in the large control area to aim, and tap **Grab** to collect the three fuses.

## Notes
- This APK is intended for Android TV / Android TV boxes. It does not turn a regular smart TV into an Android TV.
- Phone and TV must be on the same local Wi-Fi; guest Wi-Fi / client isolation may block the connection.
- The supplied source project was adapted from the files you uploaded. This is a simple original mini-game, not a copy of Netflix's game.
