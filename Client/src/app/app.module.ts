import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';

import { WebsocketService } from './services/websocket.service';
import { GameService } from './services/game.service';

@NgModule({
	declarations: [
		AppComponent
	],
	imports: [
		BrowserModule
	],
	providers: [
		WebsocketService,
		GameService
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
