import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { ManagementRoutingModule } from './management-routing.module';
import { ListComponent, AddEditComponent, PushComponent } from '.';

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        ManagementRoutingModule
    ],
    declarations:[
        ListComponent,
        AddEditComponent,
        PushComponent
    ]
})
export class ManagementModule { }
