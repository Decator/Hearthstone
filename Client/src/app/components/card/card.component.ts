import { Component, Input, Output } from '@angular/core';
import { Minion } from '../../app.models';
import { Player } from '../../app.models';
import { EventEmitter } from '@angular/core';

import * as $ from 'jquery';

@Component({
	selector: 'card-component',
	templateUrl: './card.component.html',
	styleUrls: ['./card.component.css']
})
export class CardComponent {

    @Input() card: Minion;
    @Input() disabled: boolean;
    @Input() type: string;
    @Input() player: Player;

    @Output() clicked = new EventEmitter();

    private showFull = false;
    private mouseIn = false;

    constructor(){}

    onClick() {
        // if(!this.disabled){
            this.clicked.emit("clicked");
        // }
    }

    mouseEnter() {
        this.mouseIn = true;

        setTimeout(() => {
            if(this.mouseIn){
                this.showFull = true;
            }
        }, 500);
    }

    mouseLeave() {
        this.mouseIn = false;
        this.showFull = false;
    }
}