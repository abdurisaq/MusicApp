Music App

This is a music app built with Kotlin that allows users to download videos and audio files, create playlists, and enjoy music player functionalities like background play, shuffle, and Bluetooth control.
Features

    Download Videos and Audio: Download media from YouTube.
    Playlist Creation: Organize your downloaded media into playlists.
    Background Playback: Continue playing music in the background.
    Shuffle: Shuffle your playlists for a randomized playback experience.
    Bluetooth Controls: Control playback using Bluetooth devices.

Requirements

  YouTube API Key: Due to YouTube's rate limits on search calls, the app requires your own API key.

   Add a local.properties file in the root of your project with the following:

  properties

    apikey="YOUR_YOUTUBE_API_KEY"

  Replace "YOUR_YOUTUBE_API_KEY" with your actual key.

  IDE: You can build the app using Android Studio or IntelliJ IDEA.

  Alternatively, you can use cmdline-tools to build via the command line.

Installation
Using an IDE

Clone this repository:

    bash

    git clone https://github.com/yourusername/music-app.git
    cd music-app

  Open the project in your preferred IDE (e.g., Android Studio, IntelliJ ).

  Add your YouTube API key to local.properties as mentioned above.

  Build and run the app on your device or emulator.

Building from the Command Line

    Clone the repository:

    bash


    git clone https://github.com/abdurisaq/MusicApp
    cd music-app

Add your YouTube API key to local.properties:

properties

apikey="YOUR_YOUTUBE_API_KEY"

Build the project using cmdline-tools:

bash
```
./gradlew build
```
Once built, you can install the APK manually on your Android device.

bash
```
    adb install app/build/outputs/apk/debug/app-debug.apk
```
Note

There is no universal APK available at the moment due to the need for a personal YouTube API key.
