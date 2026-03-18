package com.narxoz.rpg;

import com.narxoz.rpg.battle.RaidEngine;
import com.narxoz.rpg.battle.RaidResult;
import com.narxoz.rpg.bridge.AreaSkill;
import com.narxoz.rpg.bridge.FireEffect;
import com.narxoz.rpg.bridge.IceEffect;
import com.narxoz.rpg.bridge.PhysicalEffect;
import com.narxoz.rpg.bridge.ShadowEffect;
import com.narxoz.rpg.bridge.SingleTargetSkill;
import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.EnemyUnit;
import com.narxoz.rpg.composite.HeroUnit;
import com.narxoz.rpg.composite.PartyComposite;
import com.narxoz.rpg.composite.RaidGroup;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Homework 4 Demo: Bridge + Composite ===\n");

        // Leaf units
        HeroUnit warrior = new HeroUnit("Arthas", 140, 30);
        HeroUnit mage = new HeroUnit("Jaina", 90, 40);

        EnemyUnit goblin = new EnemyUnit("Goblin", 70, 20);
        EnemyUnit orc = new EnemyUnit("Orc", 120, 25);
        EnemyUnit warlock = new EnemyUnit("Warlock", 80, 35);

        // Composite hierarchy
        PartyComposite heroes = new PartyComposite("Heroes");
        heroes.add(warrior);
        heroes.add(mage);

        PartyComposite frontline = new PartyComposite("Frontline");
        frontline.add(goblin);
        frontline.add(orc);

        PartyComposite backline = new PartyComposite("Backline");
        backline.add(warlock);

        RaidGroup enemies = new RaidGroup("Enemy Raid");
        enemies.add(frontline);
        enemies.add(backline);

        System.out.println("--- Team Structures ---");
        heroes.printTree("");
        enemies.printTree("");

        // Bridge combinations
        Skill slashFire = new SingleTargetSkill("Slash", 20, new FireEffect());
        Skill slashIce = new SingleTargetSkill("Slash", 20, new IceEffect());
        Skill stormFire = new AreaSkill("Storm", 15, new FireEffect());
        Skill stormShadow = new AreaSkill("Storm", 15, new ShadowEffect());
        Skill strikePhysical = new SingleTargetSkill("Strike", 18, new PhysicalEffect());

        System.out.println("\n--- Bridge Preview ---");
        System.out.println("Same skill, different effects:");
        System.out.println(" - " + slashFire.getSkillName() + " using " + slashFire.getEffectName());
        System.out.println(" - " + slashIce.getSkillName() + " using " + slashIce.getEffectName());

        System.out.println("Same effect, different skills:");
        System.out.println(" - " + stormFire.getSkillName() + " using " + stormFire.getEffectName());
        System.out.println(" - " + slashFire.getSkillName() + " using " + slashFire.getEffectName());

        System.out.println("Extra combinations:");
        System.out.println(" - " + stormShadow.getSkillName() + " using " + stormShadow.getEffectName());
        System.out.println(" - " + strikePhysical.getSkillName() + " using " + strikePhysical.getEffectName());

        // Raid simulation
        RaidEngine engine = new RaidEngine().setRandomSeed(42L);
        RaidResult result = engine.runRaid(heroes, enemies, slashFire, stormShadow);

        System.out.println("\n--- Raid Result ---");
        System.out.println("Winner: " + result.getWinner());
        System.out.println("Rounds: " + result.getRounds());
        System.out.println("\n--- Battle Log ---");
        for (String line : result.getLog()) {
            System.out.println(line);
        }

        System.out.println("\n=== Demo Complete ===");
    }
}