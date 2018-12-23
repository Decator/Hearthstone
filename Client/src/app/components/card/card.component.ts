import { Component, Input } from '@angular/core';
import { Minion } from '../../app.models';

@Component({
	selector: 'card-component',
	templateUrl: './card.component.html',
	styleUrls: ['./card.component.css']
})
export class CardComponent {

    @Input() card: Minion;
    @Input() disabled: boolean;

    constructor(){

    }
}