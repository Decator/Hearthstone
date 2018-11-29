import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { StartComponent } from './components/start/start.component';
import { HeroComponent } from './components/hero/hero.component';
import { GameComponent } from './components/game/game.component';

import { AppRoutingModule } from './app.routing.module';

@NgModule({
	declarations: [
		AppComponent,
		StartComponent,
		HeroComponent,
		GameComponent
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		BrowserAnimationsModule
	],
	providers: [],
	bootstrap: [AppComponent]
})
export class AppModule { }
