package GameWorld.Interfaces;

import GameWorld.Enums.Team;

import java.util.List;

public interface ICombatEntity {
    // === Getters ===
    int getHP();
    int getMaxHP();
    int getAttack();
    int getAttackCount();
    int getPenetration();
    int getDefense();
    int getSpeed();
    int getEvasion();
    int getAccuracy();
    int getCritChance();
    float getCritDamage();
    String getName();
    Team getTeam();
    List<ICombatAction> getActions();

    // === Setters ===
    void setHP(int hp);
    void setMaxHP(int maxHP);
    void setAttack(int value);
    void setAttackCount(int count);
    void setPenetration(int value);
    void setDefense(int value);
    void setSpeed(int value);
    void setEvasion(int value);
    void setAccuracy(int value);
    void setCritChance(int value);
    void setCritDamage(float value);
    void setName(String value);
    void setTeam(Team value);
    void setActions(List<ICombatAction> value);

    // === Combat interaction ===
    void takeDamage(int amount);
}