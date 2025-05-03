package com.wimbledor.combat;

public class Stats {
    public int strength, agility, endurance;
    public int willpower, knowledge, cunning;

    public Stats(int str, int agi, int end,
                 int wil, int kno, int cun) {
        strength  = str;
        agility   = agi;
        endurance = end;
        willpower = wil;
        knowledge = kno;
        cunning   = cun;
    }
}
