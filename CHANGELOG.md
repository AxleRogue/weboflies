# Changelog

All notable changes to the **Web Of Lies** mod will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0-beta] - 2026-06-22

### Changed
- **World Generation:**
    - Replaced custom Big Spider Oak and Dark Oak Bush features with vanilla Giant Dark Oak trees for better compatibility and performance.
    - Added the `tall_dark_oak` feature for extra-tall trees in the Spider Root Forest.
    - Removed obsolete spider root system and tall tree features.
- **Loot Tables:**
    - Updated `Spiderweb` to drop string when broken with a sword.
    - Updated `Homeward Cobweb` to only drop when broken with a sword.
- **Audio:**
    - Fixed biome music by adjusting playback delays and verifying sound registration.
- **Beta Status:** Formally marked the 1.0.0 release as a Beta.
- **Project Metadata:** Correctly configured the mod logo file in `mods.toml` and `build.gradle` for in-game display.

### Added
- **Dimension & Biome:**
    - Introduced "The Dark Forest" dimension, accessible by right-clicking a vanilla cobweb.
    - Added the "Spider Root Forest" biome featuring vanilla Giant Dark Oak trees, Rose Bushes, and Snowberry Bushes.
    - Implemented a night-time exclusive black fog effect for atmospheric immersion.
    - Added custom ambient music for the Spider Root Forest.
- **Entities:**
    - Prevented Black Widows from being used as mounts (no more spider jockeys).
- **Story & Atmosphere:**
    - Implemented a "mumbling" system where players in the Dark Forest express inner thoughts.
    - Added server-wide broadcasts for slaying mod entities (delayed 5s to prevent spam).
    - **Black Widow:** A neutral-by-day, hostile-by-night hunter with emissive red glowing eyes and hourglass.
    - **Black Widow Brood Mother:** A boss entity with pounce attacks, ranged web projectiles, and minion summoning.
    - **Baby Black Widow:** A growth stage that flees from players and calls for adult protection.
    - **Spider Egg:** A soul-bound entity that hatches into multiple babies based on parent traits.
    - **Corpse System:** Spiders now leave behind "severed" body parts scattered on the ground (supports Adult, Baby, and Brood Mother variants). Added part name aliasing and safety checks to prevent `NoSuchElementException` during model baking.
- **Mechanics:**
    - **Dynamic Webbing:** Spiders now randomly place "Spiderweb" blocks to hinder player movement.
    - **Blood System:** Added green goo particles for spiders and red blood particles for players upon taking damage.
    - **Nameplate HUD:** Entities now display real-time health bars above their heads.
    - **Teleportation Network:**
        - **Homeward Cobweb:** A named, cross-dimensional teleportation beacon.
        - **Thread of Fate:** An item used to teleport to random Homeward Cobwebs within the current dimension.
    - **Story Events:** Action-triggered dialogue and narrations for key milestones (exploration, combat, resource gathering).
- **Items & Blocks:**
    - **Haunted Cobweb:** A dangerous web fired by the Brood Mother that slows and damages players.
    - **Mod-Specific Creative Tabs:** Dedicated tabs for "Web Of Lies Blocks" and "Web Of Lies Items".
    - **Wool Recipes:** Added ability to craft white and colored wool directly from string.
- **Advancements:**
    - A complete progression tree from "Web Of Lies" (entry) to the "BlackWidow Master Slayer" challenge.

### Fixed
- Fixed critical "Failed to load registries" crashes related to worldgen feature configurations.
- Resolved "void spawning" issues by implementing a fixed-coordinate surface-finding portal logic.
- Corrected emissive texture rendering to ensure glowing effects only appear at night.
- Fixed block transparency issues (black boxes) for cobweb variants using cutout rendering.
- Adjusted entity damage logic to respect player armor values.
- Nerfed Brood Mother by replacing mini-boss summons with standard Black Widows.
- Fixed `NullPointerException` in `SpiderEgg` equipment handling.
