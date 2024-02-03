import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { NotificationsService, AlertService } from '../../services';

@Component({
    selector: 'app-push',
    templateUrl: './push.component.html',
    styleUrls: ['./push.component.css']
})
export class PushComponent 
{
    form!: FormGroup;
    needID!: string;
    message!: string;
    title!: string;
    submitting = false;
    submitted = false;
    isChecked = false;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private notificationService: NotificationsService,
        private alertService: AlertService
    ) { }

    ngOnInit() 
    {
        this.needID = this.route.snapshot.params['needID'];

        // form with validation rules
        this.form = this.formBuilder.group({
            needID: [this.needID],
            message: ['']
        });

        this.title = 'Push Notification';
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
        this.pushNotification()!.pipe(first()).subscribe(
            {
                next: () => 
                {
                    this.alertService.success('Notification Pushed', true);
                    this.router.navigateByUrl('/adminView');
                },
                error: (error: string) => 
                {
                    this.alertService.error(error);
                    this.submitting = false;
                }
            })
    }

    private pushNotification() 
    {
        var tempVal = this.form.value;
        return this.needID
            ? this.notificationService.pushNotification(tempVal.needID, tempVal.message)
            : null;
    }
}


