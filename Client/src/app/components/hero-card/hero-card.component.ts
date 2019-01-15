import { Component, Input, Output } from '@angular/core';
import { Hero } from '../../app.models';
import { EventEmitter } from '@angular/core';

@Component({
	selector: 'hero-card-component',
	templateUrl: './hero-card.component.html',
	styleUrls: ['./hero-card.component.css']
})
export class HeroCardComponent {

    @Input() card: Hero;
    @Input() target: boolean;

    @Output() clicked = new EventEmitter();

    constructor() {}

    /**
     * When clicking on the hero, emit the click event to the game component. 
     */
    onClick() {
        this.clicked.emit('clicked');
    }
}
