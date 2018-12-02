import { NgModule } from '@angular/core';
import { MatButtonModule, MatListModule, MatFormFieldModule, MatInputModule, MatGridListModule, MatRadioModule } from '@angular/material';

@NgModule({
  imports: [
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    MatRadioModule,
    MatListModule
  ],
  exports: [
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    MatRadioModule,
    MatListModule
  ]
})
export class MaterialAppModule { }