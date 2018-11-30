export interface IPlayer {
    uuid: string;
    userName: string;
    hero: Hero;
    manaPool: number;
    manaMaxTurn: number;
    deck: Array<ICard>;
    hand: Array<ICard>;
    board: Array<Minion>;
}

export class Player implements IPlayer {
    uuid: string;
    userName: string;
    hero: Hero;
    manaPool: number;
    manaMaxTurn: number;
    deck: Array<ICard>;
    hand: Array<ICard>;
    board: Array<Minion>;

    constructor(obj?: IPlayer){
        if(obj){
            this.uuid = obj.uuid;
            this.userName = obj.userName;
            this.hero = new Hero(obj.hero);
            this.manaPool = obj.manaPool;
            this.manaMaxTurn = obj.manaMaxTurn;
            this.deck = Array<ICard>();
            for(let card of obj.deck){
                if((card as Minion).taunt != undefined){
                    this.deck.push(new Minion(card as Minion));
                } else {
                    this.deck.push(new Spell(card as Spell));
                }
            }
            this.hand = Array<ICard>();
            for(let card of obj.hand){
                if((card as Minion).taunt != undefined){
                    this.hand.push(new Minion(card as Minion));
                } else {
                    this.hand.push(new Spell(card as Spell));
                }
            }
            this.board = Array<Minion>();
            for(let minion of obj.board){
                this.board.push(new Minion(minion));
            }
        }
    }
}

export interface ICard {
    id: number;
    type: string;
    name: string;
    manaCost: number;
    damage: number;
    description: string;
}

export interface IMinion extends ICard {
    id: number;
    type: string;
    name: string;
    manaCost: number;
    damage: number;
    description: string;
    healthPoints: number;
    taunt: boolean;
    lifeSteal: boolean;
    charge: boolean;
    attackBuffAura: number;
    attacked: boolean;
}

export interface IHero extends ICard {
    id: number;
    type: string;
    name: string;
    manaCost: number;
    damage: number;
    description: string;
    healthPoints: number;
    armorPoints: number;
    nbSummon: number;
    idInvocation: number;
    armorBuff: number;
    target: string;
    heroPowerUsed: boolean;
}

export class Hero implements IHero {
    id: number;
    type: string;
    name: string;
    manaCost: number;
    damage: number;
    description: string;
    healthPoints: number;
    armorPoints: number;
    nbSummon: number;
    idInvocation: number;
    armorBuff: number;
    target: string;
    heroPowerUsed: boolean;

    constructor(obj?: IHero){
        if(obj){
            this.id = obj.id;
            this.type = obj.type;
            this.name = obj.name;
            this.manaCost = obj.manaCost;
            this.damage = obj.damage;
            this.description = obj.description;
            this.healthPoints = obj.healthPoints;
            this.armorPoints = obj.armorPoints;
            this.nbSummon = obj.nbSummon;
            this.idInvocation = obj.idInvocation;
            this.armorBuff = obj.armorBuff;
            this.target = obj.target;
            this.heroPowerUsed = obj.heroPowerUsed;
        }
    }
}

export class Minion implements IMinion {
    id: number;
    type: string;
    name: string;
    manaCost: number;
    damage: number;
    description: string;
    healthPoints: number;
    taunt: boolean;
    lifeSteal: boolean;
    charge: boolean;
    attackBuffAura: number;
    attacked: boolean;

    constructor(obj?: IMinion){
        if(obj){
            this.id = obj.id;
            this.type = obj.type;
            this.name = obj.name;
            this.manaCost = obj.manaCost;
            this.damage = obj.damage;
            this.description = obj.description;
            this.healthPoints = obj.healthPoints;
            this.taunt = obj.taunt;
            this.lifeSteal = obj.lifeSteal;
            this.charge = obj.charge;
            this.attackBuffAura = obj.attackBuffAura;
            this.attacked = obj.attacked;
        }
    }
}

export interface ISpell extends ICard {
    id: number;
    type: string;
    name: string;
    manaCost: number;
    damage: number;
    description: string;
    nbSummon: number;
    idInvocation: number;
    attackBuff: number;
    armorBuff: number;
    nbDraw: number;
    polymorph: boolean;
    target: string;
}

export class Spell implements ISpell {
    id: number;
    type: string;
    name: string;
    manaCost: number;
    damage: number;
    description: string;
    nbSummon: number;
    idInvocation: number;
    attackBuff: number;
    armorBuff: number;
    nbDraw: number;
    polymorph: boolean;
    target: string;

    constructor(obj?: ISpell){
        if(obj){
            this.id = obj.id;
            this.type = obj.type;
            this.name = obj.name;
            this.manaCost = obj.manaCost;
            this.damage = obj.damage;
            this.description = obj.description;
            this.nbSummon = obj.nbSummon;
            this.idInvocation = obj.idInvocation;
            this.attackBuff = obj.attackBuff;
            this.armorBuff = obj.armorBuff;
            this.nbDraw = obj.nbDraw;
            this.polymorph = obj.polymorph;
            this.target = obj.target;
        }
    }
}