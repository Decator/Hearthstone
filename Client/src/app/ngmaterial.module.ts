import { NgModule } from '@angular/core';
import { MatButtonModule, MatFormFieldModule, MatInputModule, MatGridListModule, MatRadioModule } from '@angular/material';

@NgModule({
  imports: [
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    MatRadioModule
  ],
  exports: [
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    MatRadioModule
  ]
})
export class MaterialAppModule { }