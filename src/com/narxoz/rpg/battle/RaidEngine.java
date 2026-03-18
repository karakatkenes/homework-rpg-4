package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.Random;

public class RaidEngine {
    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {
        RaidResult result = new RaidResult();

        if (teamA == null || teamB == null || teamASkill == null || teamBSkill == null) {
            result.setRounds(0);
            result.setWinner("Invalid");
            result.addLine("Raid cannot start: null input detected.");
            return result;
        }

        if (!teamA.isAlive() || !teamB.isAlive()) {
            result.setRounds(0);
            result.setWinner("Invalid");
            result.addLine("Raid cannot start: one or both teams are already defeated.");
            return result;
        }

        int rounds = 0;
        int maxRounds = 100;

        result.addLine("Raid started: " + teamA.getName() + " vs " + teamB.getName());
        result.addLine(teamA.getName() + " uses " + teamASkill.getSkillName() + " (" + teamASkill.getEffectName() + ")");
        result.addLine(teamB.getName() + " uses " + teamBSkill.getSkillName() + " (" + teamBSkill.getEffectName() + ")");

        while (teamA.isAlive() && teamB.isAlive() && rounds < maxRounds) {
            rounds++;
            result.addLine("---- Round " + rounds + " ----");

            if (teamA.isAlive()) {
                int before = teamB.getHealth();
                teamASkill.cast(teamB);
                int after = teamB.getHealth();
                int dealt = Math.max(0, before - after);

                result.addLine(teamA.getName() + " casts " + teamASkill.getSkillName()
                        + " [" + teamASkill.getEffectName() + "] on " + teamB.getName()
                        + " for " + dealt + " damage.");
                result.addLine(teamB.getName() + " HP after attack: " + teamB.getHealth());
            }

            if (!teamB.isAlive()) {
                break;
            }

            if (teamB.isAlive()) {
                int before = teamA.getHealth();
                teamBSkill.cast(teamA);
                int after = teamA.getHealth();
                int dealt = Math.max(0, before - after);

                result.addLine(teamB.getName() + " casts " + teamBSkill.getSkillName()
                        + " [" + teamBSkill.getEffectName() + "] on " + teamA.getName()
                        + " for " + dealt + " damage.");
                result.addLine(teamA.getName() + " HP after attack: " + teamA.getHealth());
            }
        }

        result.setRounds(rounds);

        if (teamA.isAlive() && !teamB.isAlive()) {
            result.setWinner(teamA.getName());
            result.addLine("Winner: " + teamA.getName());
        } else if (teamB.isAlive() && !teamA.isAlive()) {
            result.setWinner(teamB.getName());
            result.addLine("Winner: " + teamB.getName());
        } else if (!teamA.isAlive() && !teamB.isAlive()) {
            result.setWinner("Draw");
            result.addLine("Result: Draw");
        } else {
            result.setWinner("No winner");
            result.addLine("Raid stopped due to max rounds limit.");
        }

        return result;
    }
}