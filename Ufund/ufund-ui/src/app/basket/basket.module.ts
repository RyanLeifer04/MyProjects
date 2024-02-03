import { NgModule } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { BasketRoutingModule } from './basket-routing.module';
import { ListComponent } from '.';

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule,
        BasketRoutingModule
    ],
    declarations: [
        ListComponent
    ]
})
export class BasketModule { }
