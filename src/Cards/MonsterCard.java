package Cards;

import GameWorld.CombatActions.MonsterBasicAttackAction;
import GameWorld.Enums.Team;
import GameWorld.Interfaces.ICombatAction;
import GameWorld.Interfaces.ICombatEntity;
import java.util.List;

public abstract class MonsterCard extends BaseCard implements ICombatEntity {
    private String  name;
    private int     hp, maxHP;
    private int     attack, defense, speed;
    private int     accuracy, evasion, penetration, attackCount;
    private int     critChance;      // percent
    private float   critDamage;      // multiplier
    private int     gold;

    protected List<ICombatAction> actions = List.of(new GameWorld.CombatActions.Slash());

    protected MonsterCard(String name,
                          int hp, int attack, int defense, int speed,
                          int accuracy, int evasion,
                          int penetration, int attackCount,
                          int critChance, float critDamage,
                          int gold) {
        this.name         = name;
        this.hp           = hp;
        this.maxHP        = hp;
        this.attack       = attack;
        this.defense      = defense;
        this.speed        = speed;
        this.accuracy     = accuracy;
        this.evasion      = evasion;
        this.penetration  = penetration;
        this.attackCount  = attackCount;
        this.critChance   = critChance;
        this.critDamage   = critDamage;
        this.gold         = gold;
    }

    // BaseCard
    @Override public String getTitle() { return name; }
    @Override
    public abstract String getText();
    // ICombatEntity
    @Override public String getName()              { return name; }
    @Override public int    getHP()                { return hp; }
    @Override public int    getMaxHP()             { return maxHP; }
    @Override public void   setHP(int v)           { hp   = Math.min(v, maxHP); }
    @Override public void   setMaxHP(int v)        { maxHP = v; }
    @Override public int    getAttack()            { return attack; }
    @Override public void   setAttack(int v)       { attack = Math.max(1, v); }
    @Override public int    getDefense()           { return defense; }
    @Override public void   setDefense(int v)      { defense = v; }
    @Override public int    getSpeed()             { return speed; }
    @Override public void   setSpeed(int v)        { speed = v; }
    @Override public int    getAccuracy()          { return accuracy; }
    @Override public void   setAccuracy(int v)     { accuracy = v; }
    @Override public int    getEvasion()           { return evasion; }
    @Override public void   setEvasion(int v)      { evasion = v; }
    @Override public int    getAttackCount()       { return attackCount; }
    @Override public void   setAttackCount(int v)  { attackCount = v; }
    @Override public int    getPenetration()       { return penetration; }
    @Override public void   setPenetration(int v)  { penetration = v; }
    @Override public int    getCritChance()        { return critChance; }
    @Override public void   setCritChance(int v)   { critChance = v; }
    @Override public float  getCritDamage()        { return critDamage; }
    @Override public void   setCritDamage(float v) { critDamage = v; }
    @Override public void   takeDamage(int amt)    { hp -= amt; }
    public List<ICombatAction> getActions() {
        // return a single “Bite” or “Claw” action
        return List.of(new MonsterBasicAttackAction());
    }
    @Override public void   setActions(List<ICombatAction> a) { actions = a; }
    @Override public Team   getTeam()              { return Team.ENEMY; }


    public int getGold() { return gold; }
}