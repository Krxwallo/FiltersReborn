![Filters Reborn Banner](https://lookonthebrightsi.de/mc-mods/filters-reborn/images/filters-reborn-banner.png)

![Curseforge](http://cf.way2muchnoise.eu/title/filters-reborn.svg?badge_style=for_the_badge)
![Downloads](http://cf.way2muchnoise.eu/full_filters-reborn_downloads.svg?badge_style=for_the_badge)
![Versions](http://cf.way2muchnoise.eu/versions/filters-reborn.svg?badge_style=for_the_badge)
[![Issues](https://img.shields.io/github/issues/Krxwallo/FiltersReborn?logo=github&style=for-the-badge)](https://www.github.com/Krxwallo/FiltersReborn/issues)
[![Last Commit](https://img.shields.io/github/last-commit/Krxwallo/FiltersReborn?logo=github&style=for-the-badge)](https://www.github.com/Krxwallo/FiltersReborn)
[![Code](https://img.shields.io/github/languages/top/Krxwallo/FiltersReborn?logo=github&style=for-the-badge)](https://www.github.com/Krxwallo/FiltersReborn)
[![Repo Size](https://img.shields.io/github/repo-size/Krxwallo/FiltersReborn?logo=github&style=for-the-badge)](https://www.github.com/Krxwallo/FiltersReborn)


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
          name = "Cursemaven"
          url = "https://www.cursemaven.com"
     }
     
     // ... OTHER REPOSITORIES SUCH AS JEI ...
}
dependencies {
     // ... OTHER DEPENDENCIES ... 
    compile fg.deobf("curse.maven:fr-397288:FILE_ID")
}
```
Replace ```FILE_ID``` with the current/your desired version.
