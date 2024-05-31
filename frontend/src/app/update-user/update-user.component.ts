import { Component } from '@angular/core';
import { User } from '../userDto';
import { BackendserviceService } from '../backendservice.service';
import { ActivatedRoute, Router } from '@angular/router'; 
@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent {

user:User ={} as User;
permissions:string[]=[];
permissionMap = new Map<string, boolean>();
constructor(private backendService:BackendserviceService,private route:ActivatedRoute, private router:Router){

  this.permissionMap.set("can_read_users", false);
  this.permissionMap.set("can_create_users", false);
  this.permissionMap.set("can_update_users", false);
  this.permissionMap.set("can_delete_users", false);
}

ngOnInit(): void {
  this.route.params.subscribe(params => {
    console.log(params);
   console.log( params["id"]);
    this.backendService.getUser(params["id"]).subscribe((response:any) => {
      console.log(response);
      this.user=response.body;
      this.permissions=this.user.permissions;
      this.permissions.forEach((permission:string)=>{
        this.permissionMap.set(permission,true);
      });
    });
  });
}

onSubmit(){
  if(this.user.firstName==null || this.user.lastName==null || this.user.email==null 
    || this.user.firstName=="" || this.user.lastName=="" || this.user.email=="" ){
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
  console.log(this.permissions);
  this.user.permissions=this.permissions;
  console.log(this.user)

  this.backendService.updateUser(this.user).subscribe((response:any) => {
    console.log(response);
    alert("User updated successfully");
    this.router.navigate(['/users']);
    
  });

}
togglePermission(permission:string){
  console.log("togglePermission:"+permission);
  let permissionValue = this.permissionMap.get(permission);
  this.permissionMap.set(permission,!permissionValue);
  console.log(this.permissionMap);
}


validateEmail():boolean{
  const regexp = new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);

  return regexp.test(this.user.email);
 
}

}

