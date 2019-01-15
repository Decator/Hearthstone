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
    @Input() selected: boolean;
    @Input() target: boolean;
    @Input() type: string;

    @Output() clicked = new EventEmitter();

    // When true, show the full size card
    private showFull = false;
    // When true, the mouse is on the card
    private mouseIn = false;

    constructor(){}

    /**
     * When clicking on the card, emit the click event to the game component. 
     */
    onClick() {
        this.clicked.emit("clicked");
    }

    /**
     * If the mouse stays on the card for more than 1 second, show the full size card.
     */
    mouseEnter() {
        this.mouseIn = true;

        setTimeout(() => {
            if(this.mouseIn){
                this.showFull = true;
            }
        }, 1000);
    }

    /**
     * When the mouse leaves the card, stop showing the full size card.
     */
    mouseLeave() {
        this.mouseIn = false;
        this.showFull = false;
    }
}