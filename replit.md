# RaM Tv Project Notes

## Overview
RaM Tv is a premium dark live-TV streaming application built with Kotlin and Jetpack Compose for Android-sized screens and tablet/desktop viewports.

## Core Features & Architecture
- **Mobile-first responsive layout**: Adaptive bottom navigation for mobile handheld devices, collapsible menu drawer, and adaptive sidebar navigation for wider/desktop screens.
- **Dark premium theme with lime live accent**: Charcoal/obsidian backgrounds (`#0B0F17`, `#111827`, `#1F2937`) paired with vibrant Lime Green accents (`#A3E635` / `#84CC16`) and pulsing LIVE status indicators.
- **Home discovery screen**:
  - Hero featured channel card with live preview badge, viewer count, current show title, quick play button, and audio toggle.
  - Category selector with instant filtering (All, News, Sports, Movies, Entertainment, Music, Odia, Tech/Science).
  - Search bar with instant real-time query filtering.
  - Channels grid and list with logo avatars, channel category, viewers count, resolution tags, and progress bar for current program.
- **Focused video player route**:
  - Dedicated player view with player controls, live badge, channel metadata, aspect ratio toggle, mute/unmute, sleep timer, and zapping controls (Previous / Next channel).
  - Program schedule guide drawer/sheet.
- **Favorites persistence**:
  - Room database backed local persistence (with Room DAO & entity mirroring the user's favorite channel selections).
- **Updates & Downloads screen**:
  - Offline playlist sync, M3U status, cached EPG schedules, app update status, and storage usage metrics.
- **Settings Screen**:
  - Stream quality selector (Auto, 1080p, 720p, 480p).
  - Hardware acceleration & low-latency streaming toggles.
  - Background audio playback preference.
  - Push notifications toggle for scheduled favorite shows.
  - Custom M3U Playlist Manager (structure ready for authorized M3U playlist URLs and future admin panel integration).
- **Typed demo channel catalog**:
  - Detailed channel dataset including regional Odia, national news, sports, entertainment, movies, and documentary channels with active mock & live streams.
