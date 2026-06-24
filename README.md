# Web Of Lies

**Web Of Lies** is a story-driven Minecraft mod for version 1.20.1 (Forge). It introduces a forgotten and abandoned dimension ruled by the King of Lies, featuring unique spider species, advanced breeding mechanics, and a haunting atmosphere.

## 🕸️ Project Overview
Centuries ago, the King of Lies spun a massive web, creating a dimension that has since been forgotten by time. This world is like a dark reflection of the Overworld, dominated by giant arachnids and shrouded in mystery. Players must navigate treacherous terrain, survive the night-time hunts, and uncover the secrets of the Dark Forest.

## 🕷️ Key Features

### 1. Unique Entities
- **Black Widow:** Peaceful and neutral during the day, but a fierce hunter at night. They extend the `Animal` class for breeding while implementing `Enemy` for hostile interactions. Features emissive glowing red eyes and hourglasses.
- **Black Widow Brood Mother:** A powerful boss that guards the heart of spider colonies. She features advanced AI, including pounce attacks, ranged web projectiles, and the ability to summon Black Widows for aid. Always aggressive and maintains mini-boss status.
- **Baby Black Widow:** A vulnerable stage of the spider lifecycle. They flee from danger and call for help from nearby adults. Peaceful during the day, hostile at night.
- **Spider Egg:** Soul-bound entities that hatch into 4 babies. They inherit the species and traits of their parents.
- **Corpse System:** Defeated Black Widow variants (Adult, Baby, and Brood Mother) now leave behind "severed" corpses with scattered body parts on the ground in the Dark Forest.
- **Mob Dismemberment:** Custom spiders now feature a realistic dismemberment system where body parts (head, body, legs) physically separate and fly off with random momentum upon death, inspired by the "Mob Dismemberment" mod.
- **Nametag System:** Entities now feature dynamic nametags that display their current health (e.g., "Black Widow 20.0/20.0") and update in real-time.
- **Spider Jockey Prevention:** Black Widows can no longer be ridden by skeletons or other entities, ensuring they remain independent hunters.
- **Atmospheric Storytelling:** Added "mumbling" inner thoughts for players exploring the Dark Forest and server-wide broadcasts for slaying major mod entities.

### 2. The Dark Forest Dimension
- **Terrain:** Now uses **Amplified** noise settings for a more rugged and imposing landscape.
- **Poison Fang Swamp Biome:** A murky, swamp-like biome featuring acid green water, dark swamp green grass, and dark swamp green sky. Constant warm rain sets the atmosphere.
- **Spider Root Forest Biome:** A dense forest filled with Spider Root Trees. Now uses a Dark Oak Forest layout for a thicker canopy.
- **Vegetation:**
  - **Gooseberry Bush:** A harvestable bush that provides Goose Berries. Spiders will actively harvest these to breed.
- **Atmospheric Effects:**
  - **Black Fog:** A thick, claustrophobic pitch-black fog that smoothly descends upon the dimension as night falls and clears at dawn.
  - **Blood Splatters:** Custom particle effects—green goo for spiders and red blood for players—when taking damage within the dimension.
  - **Custom Music:** Immersive audio tracks that enhance the spooky atmosphere of the Spider Root Forest. *(Note: Music is currently experiencing technical issues in some biomes and is scheduled for a fix in the next update.)*
- **Custom Spawning:** Only mod-specific spiders and eggs spawn here; vanilla animals and mobs are strictly blocked. Spiders spawn as `CREATURE` category entities during the day.

### 3. Advanced Teleportation
- **Homeward Cobweb:** A craftable block that acts as a beacon. When placed, it can be named and added to a cross-dimensional navigation network with a custom GUI.
- **Thread of Fate:** A mystical item that allows players to teleport to a random Homeward Cobweb in their current dimension.
- **Portal Mechanics:** Access the Dark Forest by right-clicking a vanilla cobweb. You will be safely transported to the surface of the new dimension at the heart of the forest.

### 4. Gameplay Mechanics
- **Story Events:** Dynamic dialogue and narrations triggered by your actions, such as entering the dimension, defeating bosses, or harvesting resources.
- **Breeding:** Black Widows can be bred using Fermented Spider Eyes or Goose Berries. After breeding, spiders will become "pregnant" and eventually build a cobweb nest to lay a clutch of eggs.
- **Nesting & Guarding:** Black Widows will guard their egg nests and defend them against intruders.
- **Day/Night Cycle:** Most spiders are now peaceful during the day (registered as `CREATURE` animals) but become hostile at night. The Brood Mother remains always hostile.
- **Advancements:** A new set of advancements to guide you through the mod's features, from entering the Dark Forest to becoming a Master Slayer.
- **Web Placement:** Spiders dynamically place webs throughout the world, making navigation difficult for the unprepared.
- **Loot Sensitivity:** Use a sword to harvest string from Spiderwebs or to safely retrieve a Homeward Cobweb.

## 🛠️ Getting Started
1. **Entering the Dimension:** Find a vanilla cobweb in the Overworld and right-click it to be transported to the surface of the Dark Forest at (0, 0).
2. **Survival:** Be wary of the night! Spiders become aggressive and a black fog will obscure your vision.
3. **Teleportation:** Craft a Homeward Cobweb and a Thread of Fate to manage your travels between worlds using the global web network.

## 🏆 Advancements
The mod features a custom advancement tree that guides you through the story:
- **Web Of Lies:** Enter the Dark Forest.
- **That was easy:** Kill a Black Widow.
- **Growing Hunger:** Witness a Baby Black Widow.
- **Get squished:** Destroy a Spider Egg.
- **Home is where the Web is:** Obtain a Homeward Cobweb.
- **Tied by Fate:** Obtain a Thread of Fate.
- **Kill it with fire:** Defeat the Brood Mother using fire or a Fire Aspect weapon.
- **BlackWidow Master Slayer:** The ultimate challenge for those who conquer every aspect of the mod.

## 🎨 Asset Standards
- **Mod Logo:** Look for the signature "Web Of Lies" spider logo in the mod list to verify your installation.
- **Emissive Textures:** All spiders feature high-quality emissive textures for night hunting mode (Glowing red eyes/hourglass).
- **Transparency:** Blocks like the **Homeward Cobweb**, **Haunted Cobweb**, and **Spiderweb** use cutout rendering for realistic transparency.
- **Dynamic Lighting:** Custom **Glow Light** blocks provide thematic colored illumination (Red for spiders, Lime Green for eggs) that follows entities.

## 📄 License
This project is licensed under the **GNU Affero General Public License v3.0 (AGPL-3.0)**. See the [LICENSE](LICENSE) file for details.

---
*Developed as a story-driven phobia experience. Explore with caution.*
