import { Component, Input, Output } from '@angular/core';
import { Minion } from '../../app.models';
import { EventEmitter } from '@angular/core';

@Component({
	selector: 'card-component',
	templateUrl: './card.component.html',
	styleUrls: ['./card.component.css']
})
export class CardComponent {

    @Input() card: Minion;
    @Input() disabled: boolean;

    @Output() clicked = new EventEmitter();

    constructor(){

    }

    onClick() {
        if(!this.disabled){
            this.clicked.emit("clicked");
        }
    }
}