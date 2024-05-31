import { Component } from '@angular/core';
import { User } from '../userDto';``
import { Router } from '@angular/router';
import { BackendserviceService } from '../backendservice.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent {
user:User ={} as User;
permissions:string[]=[];


permissionMap = new Map<string, boolean>();

constructor( private backendService: BackendserviceService, private router:Router) { 
  this.permissionMap.set("can_read_users", false);
  this.permissionMap.set("can_update_users", false);
  this.permissionMap.set("can_delete_users", false);
  this.permissionMap.set("can_create_users", false);
}
validateEmail():boolean{
  const regexp = new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
  return regexp.test(this.user.email);
 
}
onSumbit(){
  if(this.user.firstName==null || this.user.lastName==null || this.user.email==null || this.user.password==null
    || this.user.firstName=="" || this.user.lastName=="" || this.user.email=="" || this.user.password==""){
    alert("Please fill all the fields");
    return;}
    const regexp = new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/); 
   if(regexp.test(this.user.email)==false){
      alert("Please enter a valid email");
      return;
    }
  this.permissionMap.forEach((value: boolean, key: string) => {
    console.log(key, value);
    if(value && !this.permissions.includes(key)){
      this.permissions.push(key);
    }else if(!value && this.permissions.includes(key))
    {
      console.log(this.permissions.indexOf(key));
     this.permissions.splice(this.permissions.indexOf(key),1);
    }
  }); 
  this.user.permissions=this.permissions;
  this.backendService.addUser(this.user).subscribe((response:any) => {
    alert("User added successfully");
    this.router.navigate(['/users']);
  });
}

togglePermission(permission:string){
  console.log("togglePermission:"+permission);
  let permissionValue = this.permissionMap.get(permission);
  this.permissionMap.set(permission,!permissionValue);
  console.log(this.permissionMap);
}

}
