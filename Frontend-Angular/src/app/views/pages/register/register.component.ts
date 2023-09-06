import { Component } from '@angular/core';
import { User } from 'src/app/Services/userService/user';
import {Router} from "@angular/router";
import {AuthService} from "src/app/Services/authservice/auth.service";

import {ToastrModule, ToastrService} from 'ngx-toastr';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  SignupRequest = {
    username: '',
    email: '',
    role: '',
    password: '',



  };
  constructor(private serviceuser :AuthService,private router: Router,private toastr: ToastrService) { }




  Register(): void {




    if(this.SignupRequest.role=="1")
    {
      this.SignupRequest.role="Rôle_Fournisseur";

    }else if(this.SignupRequest.role=="2"){

      this.SignupRequest.role="Rôle_Acheteur";
    }else
      this.SignupRequest.role="Rôle_Opérateur";
    console.log(this.SignupRequest);

    this.serviceuser.register(this.SignupRequest).subscribe(
      (response) => {

        this.toastr.success('User Added successfully!', 'Welcome');
        this.router.navigateByUrl('/404');



      },
      (error) => {
        // Handle error here
        this.toastr.error('Verify Your Information !', 'Error');
        console.error('Error updating user', error);
      }
    );



  }


}
