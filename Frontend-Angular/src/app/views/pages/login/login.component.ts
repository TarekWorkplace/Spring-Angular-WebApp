import {Component, Input, OnInit } from '@angular/core';
import { AuthService } from 'src/app/Services/authservice/auth.service';
import { LoginReq } from 'src/app/Modules/LoginReq';
import { Router } from '@angular/router';
import {ToastrModule, ToastrService} from 'ngx-toastr';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent  implements OnInit {
  hasError: boolean = false;
  constructor(private serviceuser :AuthService,private router: Router,private toastr: ToastrService)  { }
  user:LoginReq = new LoginReq();
  sessionid: String ='0';
  invalidLogin = false

  ngOnInit(): void {
  }

  @Input() error!: string | null;
  checkLogin() {

    (this.serviceuser.authenticate(this.user).subscribe(

        data => {
          this.invalidLogin = false
          if (data.token!="PASSWD") {
            localStorage.setItem("id", data.id);
            this.sessionid=data.id;
            this.toastr.success('Welcome Back !', 'Hello');
            this.router.navigateByUrl('/dashboard');
          }

        },
        error => {
          this.invalidLogin = true
          this.error = error.message;
          this.toastr.error('Check your Login', 'Error');

        }
      )
    );

  }
  logOut(){
    this.serviceuser.logOut();

  }

}
