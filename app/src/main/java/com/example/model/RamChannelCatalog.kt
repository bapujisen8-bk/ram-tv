package com.example.model

/**
 * Central Catalog Provider for 400+ Live TV Channels across all categories and Indian languages:
 * 
 * 1. ODIA (47+ channels: OTV, Kanak, Kalinga, News7, Nandighosha, Argus, Tarang, Zee Sarthak, Colors Odia, Alankar, Prarthana, etc.)
 * 2. INDIA_FTA (50 channels: DD National, DD News, Sansad TV 1 & 2, DD Sports, DD Kisan, DD Bharati, DD Retro, State DDs)
 * 3. MOVIES (40 channels: Goldmines, B4U Kadak, Zee Anmol Cinema, Star Utsav Movies, B4U Movies, Rishtey Cineplex, Dhinchaak, Manoranjan)
 * 4. NEWS (50 channels: Aaj Tak, ABP News, India TV, Zee News, NDTV India, Republic Bharat, Times Now Navbharat, News18, DD News)
 * 5. SPORTS (25 channels: DD Sports 1.0 & 2.0, Eurosport India, 1Sports, Cricket Live, Pro Kabaddi, ISL Football, Olympic Channel)
 * 6. MUSIC (35 channels: B4U Music, 9XM, Mastiii, Zee ETC, MTV Beats, 9X Jalwa, MH ONE, Punjabi Hits, Sufi Music, Bollywood Beats)
 * 7. DEVOTIONAL (40 channels: Aastha TV, Sanskar TV, Arihant, Vedic, Sadhna, Satsang, Lord Jagannath Puri Darshan, Tirupati Balaji)
 * 8. KIDS (25 channels: Chhota Bheem, Motu Patlu, Little Krishna, Roll No 21, Shiva, Pogo, Cartoon Classics, Tenali Raman, Panchatantra)
 * 9. REGIONAL (65 channels: Bengali, Telugu, Tamil, Malayalam, Kannada, Marathi, Gujarati, Punjabi, Assamese, Nagamese, Kashmiri, Urdu, Bhojpuri)
 * 10. INFOTAINMENT (25 channels: Discovery Nature, Nat Geo Wild, Animal Planet, ISRO Space, NASA TV, History TV18, TravelXP, Food Food)
 * 
 * Every channel has a unique 3-digit serialNumber (101 to 525)
 * enabling instant remote number dialing on Android TV & phone screens!
 */
object RamChannelCatalog {

    fun getAllChannels(): List<RamChannel> {
        val list = mutableListOf<RamChannel>()

        // 1. Handcrafted Odia Channels (Serial 101 - 147) - 47 channels
        list.addAll(EXTENDED_RAM_CHANNELS)

        // 2. National India FTA & Public Broadcasters (Serial 151 - 200) - 50 channels
        list.addAll(buildIndiaFtaChannels())

        // 3. Indian & Regional Movies Channels (Serial 201 - 240) - 40 channels
        list.addAll(buildMoviesChannels())

        // 4. National & International News Channels (Serial 241 - 290) - 50 channels
        list.addAll(buildNewsChannels())

        // 5. Sports & Live Action Channels (Serial 291 - 315) - 25 channels
        list.addAll(buildSportsChannels())

        // 6. Music & Entertainment Channels (Serial 321 - 355) - 35 channels
        list.addAll(buildMusicChannels())

        // 7. Devotional & Spiritual Channels (Serial 361 - 400) - 40 channels
        list.addAll(buildDevotionalChannels())

        // 8. Kids & Animated Adventure Channels (Serial 401 - 425) - 25 channels
        list.addAll(buildKidsChannels())

        // 9. Regional Multi-Language Indian Channels (Serial 431 - 495) - 65 channels
        list.addAll(buildRegionalChannels())

        // 10. Infotainment, Wildlife & Knowledge Channels (Serial 501 - 525) - 25 channels
        list.addAll(buildInfotainmentChannels())

        return list
    }
}
