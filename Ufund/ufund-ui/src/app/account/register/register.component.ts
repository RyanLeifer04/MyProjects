import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { AccountService, AlertService } from 'src/app/services';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  form!: FormGroup;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountService,
    private alertService: AlertService
  ){
    if(this.accountService.userValue){
      this.router.navigate(['/']);
    }
  }

  ngOnInit(){
    this.form = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  onSubmit(){
    this.submitted = true;

    this.alertService.clear();

    if (this.form.invalid){ return; }

    this.accountService.register(this.form.value).pipe(first())
      .subscribe({
        next: () => {
          this.router.navigate(['/account/login'],{queryParams:{registered: true}});
        },
        error: error => { this.alertService.error(error); }
      });
  }

}
