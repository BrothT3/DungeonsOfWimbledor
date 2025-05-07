//package com.wimbledor.assets.encounterProviders;
//
//import com.wimbledor.assets.BattleCard;
//import com.wimbledor.assets.encounters.EncounterCard;
//import com.wimbledor.assets.encounters.EncounterStage;
//import com.wimbledor.assets.encounters.StageOption;
//import com.wimbledor.engine.EncounterProvider;
//import com.wimbledor.entities.monsters.*;
//
//import java.util.List;
//
//// Fully branched dungeon chamber encounters, each terminal branch ends uniquely (null leads to next encounter)
//
//public class HauntedGladeEncounter implements EncounterProvider {
//    @Override
//    public EncounterCard create() {
//        // Unique endings
//        EncounterStage endUneasy = new EncounterStage(
//                "You slip away, uneasy and haunted by the glade’s whispers.",
//                List.of(new StageOption("C", "Continue",null, p -> {}, null, null))
//        );
//        EncounterStage endBlessed = new EncounterStage(
//                "A serene peace fills you; the blessing warms your spirit.",
//                List.of(new StageOption("C", "Continue", null,p -> {}, null, null))
//        );
//        EncounterStage endReward = new EncounterStage(
//                "Silver acorn and coin pouch in hand, you step forward with newfound fortune.",
//                List.of(new StageOption("C", "Continue", null,p -> {}, null, null))
//        );
//        EncounterStage endWounded = new EncounterStage(
//                "Bruised and bleeding, you crawl away to lick your wounds.",
//                List.of(new StageOption("C", "Continue", null,p -> {}, null, null))
//        );
//
//        // Branch stages
//        EncounterStage collapse = new EncounterStage(
//                "Obsidian shards rain down as the altar shatters. You barely escape the blast, blood trickling.",
//                List.of(new StageOption("R", "Retreat weakly","-2 health" ,p -> p.applyDamage(2), endWounded, null))
//        );
//
//        EncounterStage defeatWolves = new EncounterStage(
//                "With the spectral wolves vanquished, a pouch of silver coins gleams at their vanishing spot.",
//                List.of(new StageOption("C", "Take coins and go","+15 gold" ,p -> p.addGold(15), endReward, null))
//        );
//
//        EncounterStage statue = new EncounterStage(
//                "The statue’s hollow eyes glow. Wolves materialize—your honor demands fight or flight.",
//                List.of(
//                        new StageOption("F", "Fight",null ,null, new BattleCard("Spectral Wolves", List.of(new Wolf(), new Wolf()))),
//                        new StageOption("R", "Flee","you feel shame" ,p -> {                        }, endUneasy, null)
//                )
//        );
//
//        EncounterStage ritual = new EncounterStage(
//                "Runes flare, vines surge. You feel strength returning as ancient magics restore you.",
//                List.of(new StageOption("C", "Embrace renewal",null ,null /*p -> p.addStatus("Renewed")*/, endBlessed, null))
//        );
//
//        EncounterStage secret = new EncounterStage(
//                "A silver acorn pulses in your palm, and a hidden pouch jingles with coins.",
//                List.of(new StageOption("T", "Pocket treasures", null ,p -> {/* p.addItem("Silver Acorn");*/ p.addGold(5); }, ritual, null))
//        );
//
//        EncounterStage investigate = new EncounterStage(
//                "The altar hums with power; broken vines curl like fingers around it.",
//                List.of(
//                        new StageOption("R", "Read runes",null , p -> p.addXP(5), ritual, null),
//                        new StageOption("S", "Speak spell",null , p -> p.addStatus("Charmed"), statue, null),
//                        new StageOption("D", "Smash it", null ,p -> p.applyDamage(3), collapse, null)
//                )
//        );
//
//        EncounterStage survey = new EncounterStage(
//                "Soft footprints lead toward the altar; moonlight paints cryptic runes on stone.",
//                List.of(
//                        new StageOption("P", "Trail them", p -> {}, investigate, null),
//                        new StageOption("C", "Search bracken", p -> p.addXP(2), secret, null)
//                )
//        );
//
//        EncounterStage root = new EncounterStage(
//                "Cold stone floor fades beneath moss and mushrooms as you enter a vaulted glade. Silver-leafed oaks sway silently, and mist hugs the ground, whispering secrets.",
//                List.of(
//                        new StageOption("E", "Explore deeper", p -> {}, investigate, null),
//                        new StageOption("L", "Light torch", p -> p.addItem("Torch"), survey, null),
//                        new StageOption("F", "Flee unsettled", p -> p.addStatus("Uneasy"), endUneasy, null)
//                )
//        );
//
//        return new EncounterCard("Haunted Glade Chamber", root);
//    }
//}
//
//public class AncientRuinsEncounter implements EncounterProvider {
//    @Override
//    public EncounterCard create() {
//        // Unique endings
//        EncounterStage endExited = new EncounterStage(
//                "You leave the silent ruins, relic and tales in hand.",
//                List.of(new StageOption("C", "Continue", p -> {}, null, null))
//        );
//        EncounterStage endPoisoned = new EncounterStage(
//                "Vision blurs as poison courses through you; you stumble onward desperately.",
//                List.of(new StageOption("C", "Continue", p -> p.takeDamage(3), null, null))
//        );
//        EncounterStage endLoot = new EncounterStage(
//                "Gold and gems secure, you step away richer and wiser.",
//                List.of(new StageOption("C", "Continue", p -> {}, null, null))
//        );
//
//        // Branch stages
//        EncounterStage trap = new EncounterStage(
//                "Darts hiss past you; pain blossoms as poison takes hold.",
//                List.of(new StageOption("M", "Focus healing", p -> p.heal(3), endPoisoned, null))
//        );
//
//        EncounterStage chamber = new EncounterStage(
//                "The secret vault gleams with treasure—chalices, coins, and jeweled artifacts beckon.",
//                List.of(
//                        new StageOption("E", "Gather treasure", p -> p.addGold(25), trap, null),
//                        new StageOption("C", "Exit quietly", p -> p.addXP(1), endExited, null)
//                )
//        );
//
//        EncounterStage defeat = new EncounterStage(
//                "Stone guardians collapse, fragments clatter. A glimmering sigil lies among the rubble.",
//                List.of(new StageOption("S", "Retrieve sigil", p -> p.addItem("Ancient Sigil"), endLoot, null))
//        );
//
//        EncounterStage shadows = new EncounterStage(
//                "Stone sentinels stir in the gloom, eyes aflame as they advance.",
//                List.of(
//                        new StageOption("F", "Battle them", p -> {}, defeat, new BattleCard("Stone Guardians", List.of(new StoneGolem(), new StoneSentinel()))),
//                        new StageOption("R", "Retreat", p -> p.takeDamage(1), endExited, null)
//                )
//        );
//
//        EncounterStage carvings = new EncounterStage(
//                "Murals of dragons and kings stretch across the walls; one pillar hides a lever.",
//                List.of(
//                        new StageOption("A", "Pull lever", p -> {}, chamber, null),
//                        new StageOption("L", "Leave as is", p -> p.addXP(2), endExited, null)
//                )
//        );
//
//        EncounterStage scout = new EncounterStage(
//                "Your steps reveal hidden traps—pressure plates and arrow slits lie in wait.",
//                List.of(
//                        new StageOption("D", "Disarm traps", p -> p.addXP(2), carvings, null),
//                        new StageOption("A", "Avoid traps", p -> p.addStatus("Stealthy"), shadows, null)
//                )
//        );
//
//        EncounterStage root = new EncounterStage(
//                "You pass beneath a vine-choked arch into a hall of marble pillars. Water drips from ceilings, and distant echoes hint at hidden dangers.",
//                List.of(
//                        new StageOption("I", "Study murals", p -> {}, carvings, null),
//                        new StageOption("S", "Search for traps", p -> p.addXP(3), scout, null),
//                        new StageOption("P", "Peer into darkness", p -> {}, shadows, null)
//                )
//        );
//
//        return new EncounterCard("Ancient Ruins Chamber", root);
//    }
//}
//
//public class RiverCrossingEncounter implements EncounterProvider {
//    @Override
//    public EncounterCard create() {
//        // Unique endings
//        EncounterStage endSafe = new EncounterStage(
//                "You climb out of the cavern onto a stony ledge, water dripping off your cloak.",
//                List.of(new StageOption("C", "Continue", p -> {}, null, null))
//        );
//        EncounterStage endSignal = new EncounterStage(
//                "The smoke signal curls upward; soon you hear distant voices answering your call.",
//                List.of(new StageOption("C", "Continue", p -> p.addStatus("Awaiting Rescue"), null, null))
//        );
//
//        // Branch stages
//        EncounterStage raft = new EncounterStage(
//                "You lash driftwood and rope into a makeshift raft. Each creak echoes in the cavern.",
//                List.of(
//                        new StageOption("C", "Cross carefully", p -> {}, endSafe, null),
//                        new StageOption("D", "Decide it’s unsafe", p -> p.addXP(1), endSafe, null),
//                        new StageOption("S", "Light on fire and signal", p -> p.addStatus("Signal"), endSignal, null)
//                )
//        );
//
//        EncounterStage swim = new EncounterStage(
//                "The icy current yanks at you midstream. Legs numb, you claw your way onto a rocky shore.",
//                List.of(
//                        new StageOption("W", "Warm up", p -> p.heal(1), endSafe, null),
//                        new StageOption("R", "Rest against a boulder", p -> p.addStatus("Refreshed"), endSafe, null)
//                )
//        );
//
//        EncounterStage root = new EncounterStage(
//                "You slip through a narrow passage into a cavernous chamber where torchlight dances on churning underground waters. The river’s roar echoes off jagged walls, and the air tastes of minerals and damp earth. " +
//                        "Bioluminescent fungi on the banks cast ghostly blue reflections across the current.",
//                List.of(
//                        new StageOption("S", "Swim across", p -> p.takeDamage(2), swim, null),
//                        new StageOption("B", "Build a raft", p -> {}, raft, null)
//                )
//        );
//
//        return new EncounterCard("Underground River Grotto", root);
//    }
//}
//
//public class MerchantBargainEncounter implements EncounterProvider {
//    @Override
//    public EncounterCard create() {
//        // Unique endings
//        EncounterStage endDepart = new EncounterStage(
//                "You slip back into the corridor, map or pockets lighter.",
//                List.of(new StageOption("C", "Continue", p -> {}, null, null))
//        );
//        EncounterStage endTrusted = new EncounterStage(
//                "The merchant nods in respect; you exit with trust earned as payment.",
//                List.of(new StageOption("C", "Continue", p -> p.addStatus("Trusted Trader"), null, null))
//        );
//
//        // Branch stages
//        EncounterStage offer = new EncounterStage(
//                "You counteroffer with fewer coins. His smile wavers before he nods grudgingly.",
//                List.of(
//                        new StageOption("A", "Agree", p -> p.spendGold(35).addItem("Vault Map"), endDepart, null),
//                        new StageOption("W", "Walk away graciously", p -> p.addStatus("Respectful"), endTrusted, null)
//                )
//        );
//
//        EncounterStage haggle = new EncounterStage(
//                "The merchant’s booth is stacked with curiosities. He names a price for a hand-drawn map to a hidden vault.",
//                List.of(
//                        new StageOption("P", "Pay up", p -> p.spendGold(50).addItem("Vault Map"), endDepart, null),
//                        new StageOption("N", "Negotiate", p -> {}, offer, null)
//                )
//        );
//
//        EncounterStage root = new EncounterStage(
//                "A cavern widens into a bustling bazaar illuminated by glowing crystals overhead. Silk banners flutter despite the still air, and tables groan under stacks of artifacts from distant lands. " +
//                        "The hum of haggling voices and clink of coins echoes like distant bells.",
//                List.of(
//                        new StageOption("B", "Browse wares", p -> {}, haggle, null),
//                        new StageOption("L", "Leave quickly", p -> {}, endDepart, null)
//                )
//        );
//
//        return new EncounterCard("Dwarven Bazaar Chamber", root);
//    }
//}
//
//public class BanditRoadblockEncounter implements EncounterProvider {
//    @Override
//    public EncounterCard create() {
//        // Unique endings
//        EncounterStage endClear = new EncounterStage(
//                "With the path clear, you vanish back into the dungeon’s winding halls.",
//                List.of(new StageOption("C", "Continue", p -> {}, null, null))
//        );
//        EncounterStage endRansack = new EncounterStage(
//                "With extra coins in hand, you press onward—now a marked threat to the bandit lord.",
//                List.of(new StageOption("C", "Continue", p -> p.addStatus("Wanted"), null, null))
//        );
//
//        // Branch stages
//        EncounterStage fight = new EncounterStage(
//                "Steel glints as the bandits spring up. The brazier’s embers cast dancing shadows on their blades.",
//                List.of(
//                        new StageOption("V", "Vanquish foes", p -> p.addGold(15), endClear, null),
//                        new StageOption("I", "Intimidate and demand ransom", p -> p.addGold(30), endRansack, null)
//                )
//        );
//
//        EncounterStage root = new EncounterStage(
//                "Beyond a collapsed column, the corridor widens into a crude checkpoint. Rough-hewn barricades of barrels and crates flank two armed bandits lounging by a flickering brazier, their shadows dancing on slick stone walls.",
//                List.of(
//                        new StageOption("F", "Fight them", p -> {}, fight, new BattleCard("Highwaymen Ambush", List.of(new Bandit(), new BanditLeader()))),
//                        new StageOption("P", "Pay toll", p -> p.spendGold(10), endClear, null),
//                        new StageOption("S", "Sneak past", p -> p.addXP(2), endClear, null)
//                )
//        );
//
//        return new EncounterCard("Bandit Roadblock Chamber", root);
//    }
//}
//
//public class MysticFountainEncounter implements EncounterProvider {
//    @Override
//    public EncounterCard create() {
//        // Unique endings
//        EncounterStage endRefreshed = new EncounterStage(
//                "Your reflection ripples once more before stilling; you return to the corridor feeling refreshed.",
//                List.of(new StageOption("C", "Continue", p -> {}, null, null))
//        );
//        EncounterStage endPonder = new EncounterStage(
//                "You leave contemplative, the fountain’s hum echoing in your mind.",
//                List.of(new StageOption("C", "Continue", p -> p.addStatus("Pensive"), null, null))
//        );
//
//        // Branch stages
//        EncounterStage root = new EncounterStage(
//                "A faint glow beckons as you enter a circular chamber. At its center, a crystal fountain bubbles with luminous water, and the air vibrates with a soft harmonic resonance, like whispered lullabies.",
//                List.of(
//                        new StageOption("D", "Drink water", p -> p.heal(5), endRefreshed, null),
//                        new StageOption("L", "Leave water be", p -> {}, endPonder, null),
//                        new StageOption("E", "Examine fountain basin", p -> p.addXP(1), endPonder, null)
//                )
//        );
//
//        return new EncounterCard("Enchanted Fountain Chamber", root);
//    }
//}
//
//public class LostChildEncounter implements EncounterProvider {
//    @Override
//    public EncounterCard create() {
//        // Unique endings
//        EncounterStage endAlone = new EncounterStage(
//                "You step away, solitary once more, the child’s sobs fading behind you.",
//                List.of(new StageOption("C", "Continue", p -> {}, null, null))
//        );
//        EncounterStage endGuarded = new EncounterStage(
//                "You part ways as trusted guardian, the child safe and grateful.",
//                List.of(new StageOption("C", "Continue", p -> p.addStatus("Guardian"), null, null))
//        );
//        EncounterStage endBonded = new EncounterStage(
//                "You walk on together, a new bond forged in these stone halls.",
//                List.of(new StageOption("C", "Continue", p -> p.addCompanion("Lost Child"), null, null))
//        );
//
//        // Branch stages
//        EncounterStage reunion = new EncounterStage(
//                "Past a mosaic floor, you find the child’s caravan—relief floods their faces as they rush forward.",
//                List.of(
//                        new StageOption("R", "Receive reward", p -> p.addGold(20), endGuarded, null),
//                        new StageOption("S", "Stay with them a while", p -> p.addStatus("Guarded"), endGuarded, null)
//                )
//        );
//
//        EncounterStage calm = new EncounterStage(
//                "The child whispers of getting lost after chasing a spectral butterfly. They grasp your cloak with trembling fingers.",
//                List.of(
//                        new StageOption("S", "Search for caravan", p -> p.addXP(3), reunion, null),
//                        new StageOption("A", "Adopt them", p -> {}, endBonded, null)
//                )
//        );
//
//        EncounterStage root = new EncounterStage(
//                "Sunlight vanishes behind you as you enter a dim hallway where muted sobs echo off stone. A small child in patched clothes huddles beneath a broken pillar, clutching a faded doll and blinking at you with tear-streaked cheeks.",
//                List.of(
//                        new StageOption("C", "Comfort the child", p -> {}, calm, null),
//                        new StageOption("L", "Leave them", p -> p.addXP(-1), endAlone, null),
//                        new StageOption("N", "Name and reassure them", p -> p.addStatus("Trusted"), calm, null)
//                )
//        );
//
//        return new EncounterCard("Lost Child Chamber", root);
//    }
//}
//
//public class WhisperingCavernEncounter implements EncounterProvider {
//    @Override
//    public EncounterCard create() {
//        // Unique endings
//        EncounterStage endSilent = new EncounterStage(
//                "The phantom fades as you retrace your steps, cavern quiet once more.",
//                List.of(new StageOption("C", "Continue", p -> {}, null, null))
//        );
//        EncounterStage endGems = new EncounterStage(
//                "Moonstone gems secured, you leave richer in coin and mystic lore.",
//                List.of(new StageOption("C", "Continue", p -> {}, null, null))
//        );
//        EncounterStage endRespect = new EncounterStage(
//                "You depart reverent, the phantom’s riddle echoing in your mind.",
//                List.of(new StageOption("C", "Continue", p -> p.addStatus("Reverent"), null, null))
//        );
//
//        // Branch stages
//        EncounterStage treasure = new EncounterStage(
//                "A fissure splits open, revealing a trove of moonstone gems glittering atop jagged rock.",
//                List.of(
//                        new StageOption("T", "Take gems", p -> p.addItem("Moonstone Gems"), endGems, null),
//                        new StageOption("L", "Leave them undisturbed", p -> p.addXP(1), endRespect, null)
//                )
//        );
//
//        EncounterStage echo = new EncounterStage(
//                "Passing beneath drips of water, you encounter a shimmering phantom whose voice weaves a riddle of shadows.",
//                List.of(
//                        new StageOption("R", "Answer riddle", p -> p.addXP(5), treasure, null),
//                        new StageOption("I", "Ignore and leave", p -> {}, endSilent, null)
//                )
//        );
//
//        EncounterStage root = new EncounterStage(
//                "Descending a spiral staircase, you emerge into an echoing cavern. Dripping stalactites form a rhythmic chorus, and disembodied whispers brush your ears, urging you deeper into shadow.",
//                List.of(
//                        new StageOption("F", "Follow whispers", p -> {}, echo, null),
//                        new StageOption("E", "Escape cave", p -> p.addXP(1), endSilent, null)
//                )
//        );
//
//        return new EncounterCard("Whispering Cavern Chamber", root);
//    }
//}
//
//public class DragonLairExplorationEncounter implements EncounterProvider {
//    @Override
//    public EncounterCard create() {
//        // Unique endings
//        EncounterStage endScarred = new EncounterStage(
//                "Treasure in hand or scars to tell, you climb back into the darkness of the dungeon beyond.",
//                List.of(new StageOption("C", "Continue", p -> {}, null, null))
//        );
//        EncounterStage endSpared = new EncounterStage(
//                "The dragon nods in respect and lets you pass; you depart unscathed but watchful.",
//                List.of(new StageOption("C", "Continue", p -> p.addStatus("Spared by Dragon"), null, null))
//        );
//        EncounterStage endWanted = new EncounterStage(
//                "A few stolen coins weigh heavy as the dragon’s roar pursues you through the halls.",
//                List.of(new StageOption("C", "Continue", p -> p.addStatus("Wanted By Dragon"), null, null))
//        );
//
//        // Branch stages
//        EncounterStage battle = new EncounterStage(
//                "The dragon’s roar shakes the rafters. Claws tear stone as flames lick the cavern walls.",
//                List.of(
//                        new StageOption("V", "Victory", p -> p.addGold(100), endScarred, null),
//                        new StageOption("F", "Feign surrender", p -> p.addStatus("Spared"), endSpared, null)
//                )
//        );
//
//        EncounterStage hoard = new EncounterStage(
//                "Through vaulted pillars you spy a glittering treasure hoard piled high around a jeweled crown.",
//                List.of(
//                        new StageOption("T", "Take crown", p -> p.addItem("Jeweled Crown").addXP(2), endScarred, null),
//                        new StageOption("L", "Leave it", p -> {}, endScarred, null),
//                        new StageOption("S", "Sneak a few coins", p -> p.addGold(30).addStatus("Wanted"), endWanted, null)
//                )
//        );
//
//        EncounterStage root = new EncounterStage(
//                "A grand vestibule opens into a vast chamber where cavern walls glow red with unseen fires. Scorched bones litter the floor, and the ground trembles with each distant roar. A pungent heat wraps around you like a living thing.",
//                List.of(
//                        new StageOption("S", "Sneak deeper", p -> {}, hoard, null),
//                        new StageOption("A", "Attack the dragon", p -> {}, battle, new BattleCard("Young Dragon", List.of(new YoungDragon())))
//                )
//        );
//
//        return new EncounterCard("Dragon’s Lair Chamber", root);
//    }
//}
