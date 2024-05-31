import { Component } from '@angular/core';
import { User } from '../userDto';
import { BackendserviceService } from '../backendservice.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent {
users: User[] = [];
displayedColumns: string[] = ['id', 'name', 'email',"permissions","actions"];
decodedPermissions:[]= this.backendService.decoded.permissions;
updatePermission:boolean=this.decodedPermissions.find((permission:string)=>permission=="can_update_users")!=undefined;
deletePermission:boolean=this.decodedPermissions.find((permission:string)=>permission=="can_delete_users")!=undefined;

constructor(private backendService:BackendserviceService,private router:Router) {
  backendService.getUsers().subscribe((response:any) => {
    console.log(response);
    this.users=response.body;
  });
  console.log("xdd")
  console.log(this.decodedPermissions.find((permission:string)=>permission=="can_deledste_users"));
}
updateUser(user:User){  
this.router.navigate(['/updateUser',user.id]);
}
deleteUser(user:User){
  this.backendService.deleteUser(user.id).subscribe((response:any) => {
    console.log(response);
    this.backendService.getUsers().subscribe((response:any) => {
      console.log(response);
      this.users=response.body;
      if(response.status==200){
        alert("User deleted successfully");
      }
     
    });
  });
}
hasUpdatePermission(){
  let styleUrls;
  if(this.updatePermission){
   styleUrls ={
    'cursor': 'pointer', 'color':'#cccc', 'clickable': 'true'
  }

}else{
   styleUrls ={
    'cursor': 'default', 'color':'#ffff', 'clickable': 'false'
  }
}

  return styleUrls; 
}

}
