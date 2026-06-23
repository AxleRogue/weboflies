# Web Of Lies - Project Guidelines

## Project Overview
**Web Of Lies** is a story-driven Minecraft mod for version 1.20.1 (Forge). It introduces a forgotten dimension ruled by the King of Lies, featuring unique spider species, brood mothers, and complex breeding mechanics.

## 1. Entity Guidelines

### 1.1 Spider Species
- **Day Behavior:** Aggressive. They spin webs in trees and on the ground. Navigation should be made difficult for players.
- **Night Behavior:** Hunting and scouting. They actively seek out players.
- **Visuals:** Must have emissive textures for night variants.
    - Eyes must glow **red**.
    - Black Widows must also have a glowing **red hourglass**.

### 1.2 Spider Egg Entity (`SpiderEgg`)
- **Soul Binding:** Every `SpiderEgg` must be "soul-bound" to its parent.
- **Inheritance:** The parent's species and traits determine the outcome of the hatched entity.
- **Implementation:** Use NBT or Capability systems to store parent data on the `SpiderEgg` entity.

### 1.3 Brood Mothers
- Rare, powerful variants that serve as mini-bosses or centers of spider colonies.
- They should have unique AI for managing their brood.

## 2. Textures and Assets

### 2.1 Emissive Textures
- Emissive layers should be separate or handled via a custom shader/renderer (e.g., using `PostChain` or specific Forge/Vanilla emissive rendering techniques).
- Filename convention for emissive layers: `[entity_name]_emissive.png` or `[entity_name]_glow.png`.
- Ensure the base texture and emissive layer align perfectly.

### 2.2 Texture Paths
- Follow standard Minecraft/Forge conventions: `src/main/resources/assets/weboflies/textures/entity/spiders/`.

## 3. Dimension & Environment

### 3.1 Web Generation
- Spiders should dynamically place web blocks or custom "Sticky Web" blocks.
- Generation should occur in trees and on the ground during the day.

### 3.2 Atmosphere
- The dimension should feel abandoned and forgotten. Use fog, custom sky colors, and ambient sounds to reinforce the "Web of Lies" theme.

## 4. Coding Standards

### 4.1 Registries
- Use `DeferredRegister` for all registries (Blocks, Items, Entities, etc.).
- Namespace: `weboflies`.

### 4.2 Naming Conventions
- **Java Classes:** PascalCase (e.g., `BlackWidowEntity`).
- **Registry Names:** snake_case (e.g., `black_widow`).
- **Assets/JSON:** snake_case.

### 4.3 Testing
- When adding new spiders, verify both Day and Night AI behaviors.
- Ensure the `SpiderEgg` hatching logic correctly reads parent data.

## 5. Story Integration
- Code and assets should support the narrative of a "dimension forgotten and abandoned by the king of lies."
- Use item lore, advancement descriptions, and environmental storytelling where possible.
