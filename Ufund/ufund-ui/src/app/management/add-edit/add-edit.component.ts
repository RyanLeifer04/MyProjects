import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { NeedsService, AlertService } from '../../services';

@Component({
    selector: 'app-add-edit',
    templateUrl: './add-edit.component.html',
    styleUrls: ['./add-edit.component.css']
})
export class AddEditComponent {
    form!: FormGroup;
    id?: number;
    title!: string;
    submitting = false;
    submitted = false;
    isChecked = false;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private needsService: NeedsService,
        private alertService: AlertService
    ) { }

    ngOnInit() {
        this.id = this.route.snapshot.params['id'];

        // form with validation rules
        this.form = this.formBuilder.group({
            id: [this.id],
            name: [''],
            amount_needed: [''],
            amount_in_stock: [''],
            amount_unit: [''],
            tags: ['']
        });

        this.title = 'Add Need';
        if (this.id) {
            // edit mode
            this.title = 'Edit Need';
            this.needsService.getById(((this.id) as unknown) as string)
                .pipe(first())
                .subscribe(x => {
                    this.form.patchValue(x);
                });
        }
        else {
            // add mode
            this.id = 0;
        }
    }

    onSubmit() {
        this.submitted = true;
        // reset alerts on submit
        this.alertService.clear();
        // stop here if form is invalid
        if (this.form.invalid) {
            return;
        }

        this.submitting = true;
        this.saveNeed()
            .pipe(first())
            .subscribe({
                next: () => {
                    this.alertService.success('Need saved', true);
                    this.router.navigateByUrl('/adminView');
                },
                error: error => {
                    this.alertService.error(error);
                    this.submitting = false;
                }
            })
    }

    private saveNeed() {
        // create or update user based on id param
        var tempVal = this.form.value;

        // ensure tags are in array format
        if (tempVal.tags.constructor === Array) {
            tempVal.tags = tempVal.tags.join(',');
        }
        tempVal.tags = tempVal.tags.split(',');

        // update/insert the need
        return this.id
            ? this.needsService.update(this.id!, tempVal)
            : this.needsService.register(tempVal);
    }

    // validate form fields
    validateFields() {
        let amount_needed = 0;
        let amount_in_stock = 0;
        try {
            amount_needed = Number(this.form.get('amount_needed')?.value);
            amount_in_stock = Number(this.form.get('amount_in_stock')?.value);
        } catch (error) { }

        amount_needed = amount_needed > 0 ? amount_needed : 0;
        amount_in_stock = amount_in_stock > 0 ? amount_in_stock : 0;
        amount_in_stock = amount_in_stock > amount_needed ? amount_needed : amount_in_stock;

        this.form.patchValue({ amount_in_stock: amount_in_stock });
        this.form.patchValue({ amount_needed: amount_needed });
        console.log(this.form.value);
    }
}
