import { RouterModule, Routes } from '@angular/router';

import { StartComponent } from './components/start/start.component';
import { HeroComponent } from './components/hero/hero.component';
import { GameComponent } from './components/game/game.component';
import { EndComponent } from './components/end/end.component';

const AppRoutes: Routes = [
    { path: 'start', component: StartComponent },
    { path: 'hero', component: HeroComponent },
    { path: 'game', component: GameComponent },
    { path: 'end', component: EndComponent },
    { path: '', redirectTo: '/start', pathMatch: 'full'},
    { path: '**', redirectTo: '/start', pathMatch: 'full'}
];

export const AppRoutingModule = RouterModule.forRoot(AppRoutes, {useHash: true});
