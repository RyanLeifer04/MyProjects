import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { NeedsRoutingModule } from './needs-routing.module';
import { ListComponent } from '.';

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        NeedsRoutingModule
    ],
    declarations: [
        ListComponent
    ]
})
export class NeedsModule { }
