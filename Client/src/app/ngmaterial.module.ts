import { NgModule } from '@angular/core';
import { MatButtonModule, MatListModule, MatFormFieldModule, MatInputModule, MatGridListModule, MatRadioModule, MatSnackBarModule } from '@angular/material';

@NgModule({
  imports: [
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    MatRadioModule,
    MatListModule,
    MatSnackBarModule
  ],
  exports: [
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    MatRadioModule,
    MatListModule,
    MatSnackBarModule
  ]
})
export class MaterialAppModule { }