import { RouterModule, Routes } from '@angular/router';

import { StartComponent } from './components/start/start.component';
import { HeroComponent } from './components/hero/hero.component';
import { GameComponent } from './components/game/game.component';

const AppRoutes: Routes = [
    { path: 'start', component: StartComponent },
    { path: 'hero', component: HeroComponent },
    { path: 'game', component: GameComponent },
    { path: '', redirectTo: '/start', pathMatch: 'full'},
    { path: '**', redirectTo: '/start', pathMatch: 'full'}
];

export const AppRoutingModule = RouterModule.forRoot(AppRoutes, {useHash: true});