![Filters Reborn Banner](https://lookonthebrightsi.de/mc-mods/filters-reborn/images/filters-reborn-banner.png)

![Minecraft](https://img.shields.io/static/v1?label=&message=1.16.1&color=2d2d2d&labelColor=dddddd&style=for-the-badge&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAZdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuMjCGJ1kDAAACoElEQVQ4T22SeU8aURTF/ULGtNRWWVQY9lXABWldIDPIMgVbNgEVtaa0damiqGBdipXaJcY2ofEf4ycbTt97pVAabzK5b27u+Z377kwXgK77QthRy7OfXbeJM+ttqKSXN8sdwbT/A0L7elmsYqrPHZmROLPh5YkV4oEBwaKuHj+yyJptLDoAhbq3O1V1XCVObY3FL24mfn5oRPrcwSCRfQOyNWcjVjZdCbtcdwcgXrXUspdOKbDN/XE9tiBJMhXHT60gUIT2dMhcDLMc3NVKQklz0QIkf5qlyEcO6Qs7yPhMJB4amDMFimQSmqNlE8SKAZFzDfxHfVILIIZ10sJ3OwIbcqSuiOjchkzNCboHev9o2YhgiUP8mxnLN24I6/3ghYdtQG5iUMpFBuCP9iKwLsfiLyeCp2rMnZgwX3NArGoxW1Ridl+BzLEVKa8KSxOqNmDdz0kFnxaLHhWEgAyZigWhHXL+pEDy2ozsDxv8vAzTnh7w5kcghqCaFmCT10of4iPIT2mRdPUh4HoCcVwBH/8Ac2kzUkEV5r3EfVSOvbAJa5NDyI0r2oDtWb1EClh+OoC3Pg7v/Bw7p939yI4rsRW2Y3lKh01eh7WpIRyKZqzyjjYgPdIvlaMWRqYuG7wWryYHsRM0sFolZiPvQ3jheIwSmSBPdkByG/B6Wi3RYiVmRX7GiAPiUCRisii8D+jZNKvPBrHCW1GY0bAz6WkDCtOaSyKQFsi4K5NqNiZtehN2Y5uAShETqolhBqJXpfdPuPsuWwAaRdHSkxdc11mPqkGnyY4pyKbpl1GyJ0Pel7yqBoFcF3zqno5f+d8ohYy9Sx7lzQpxo1eirluCDgt++00p6uxttrG4F/A39sJGZWZMfrcp6O6+5kaVzXJHAOj6DeSs8qw5o8oxAAAAAElFTkSuQmCC) ![Curseforge](http://cf.way2muchnoise.eu/full_filters-reborn_downloads.svg?badge_style=for_the_badge)

# Filters Reborrn (desc. from original mod)

Filters introduces filters into the Creative GUI based on tags introduced in Minecraft 1.13. Although creative groups exist, some contain hundreds of items which can make it difficult to locate a specific item. Filters makes it easy to find an item by adding new filter tabs to the left side of the creative screen that allow you to limit the items shown based on the category they fall under. It also uses the new Minecraft tags which makes it easily for players to change them around easily using a resource pack. Mod developers get access to register custom filters for their own mod.. Originally this filter system was an exclusive to MrCrayfish's Furniture Mod however I decided to make it not only a seperate mod because of it's useful functionality.

### Features:
* Pre-configured filters for vanilla creative tabs
* Easy to use API for developers to register custom filters
* Support to override tags via resource packs

### Screenshots:
![Screenshot 1](https://lookonthebrightsi.de/mc-mods/filters-reborn/textures/filters_1.png)
![Screenshot 2](https://lookonthebrightsi.de/mc-mods/filters-reborn/textures/filters_2.png)
![Screenshot 3](https://lookonthebrightsi.de/mc-mods/filters-reborn/textures/filters_3.png)
![Screenshot 4](https://lookonthebrightsi.de/mc-mods/filters-reborn/textures/filters_4.png)

### Developers:
If you are a developer and want to add Filters support to your own mod, you can simply do so by adding this to your build.gradle file.

```gradle
repositories {
     maven {
          name = "justAm0dd3r Maven"
          url = "lookonthebrightsi.de/files/maven/"
     }
     [....OTHER REPOSITORIES SUCH AS JEI...]
}
dependencies {
     [..... OTHER DEPENDENCIES ...]
    compile fg.deobf("justAm0dd3r.filters-reborn:filters_reborn-1.16.1:[FILTERS_REBORN_VERSION]")
}
```
Replace [FILTERS_REBORN_VERSION] with the current/your desired version.
