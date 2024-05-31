import { Component } from '@angular/core';
import { BackendserviceService } from '../backendservice.service';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanDeactivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent {
formGroup: any;
loginForm: any;
email:string="";
password:string=""; // Do password hashing and salting and form validation


constructor(private backendService:BackendserviceService,private router:Router) { }
onSubmit() {
console.log(this.email);
this.backendService.login(this.email,this.password).subscribe((response:any) => {
console.log("asdasdasdas"+response);  
if(response.body.token!=null){
  console.log(response); 
  console.log("YPPPPP"); 
  this.backendService.token=response.body.token;
  localStorage.setItem("token",response.body.token);
  
  this.router.navigate(["/"]);
 
  this.backendService.changedTokenEvent.emit();
}  });
}

validateEmail():boolean{
  const regexp = new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
  return regexp.test(this.email);
 
}
}
