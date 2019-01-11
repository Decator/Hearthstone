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
    @Input() disabled: boolean;

    @Output() clicked = new EventEmitter();

    constructor() {

    }

    onClick() {
        if (!this.disabled) {
            this.clicked.emit('clicked');
        }
    }
}
