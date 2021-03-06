import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialAppModule } from './ngmaterial.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { StartComponent } from './components/start/start.component';
import { HeroComponent } from './components/hero/hero.component';
import { GameComponent } from './components/game/game.component';
import { EndComponent } from './components/end/end.component';
import { CardComponent } from './components/card/card.component';
import { HeroCardComponent } from './components/hero-card/hero-card.component';

import { AppRoutingModule } from './app.routing.module';

import { SocketService } from './service/socket.service';

@NgModule({
	declarations: [
		AppComponent,
		StartComponent,
		HeroComponent,
		EndComponent,
		GameComponent,
		CardComponent,
		HeroCardComponent
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		BrowserAnimationsModule,
		MaterialAppModule,
		FormsModule,
		ReactiveFormsModule
	],
	providers: [
		SocketService
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
