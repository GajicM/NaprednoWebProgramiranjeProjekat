import { Component } from '@angular/core';
import { BackendserviceService } from './backendservice.service';
import { jwtDecode } from "jwt-decode";
import { Router } from '@angular/router';
import { SseService } from './sse.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'FEnwpD3';
  token = this.backendService.token;
  decodedPermissions:[]=[]
  readPermission:boolean=false;
  writePermission:boolean=false;
  writeVacuumPermission:boolean=false;
  searchVacuumPermission:boolean=false;
 constructor(private backendService: BackendserviceService,private router:Router,private sseService:SseService) {


  }
ngOnDestroy(){}
 async ngOnInit() {

  //  const decoded = jwtDecode(this.token);
    //console.log(decoded);
    if(this.token!=''){
    this.decodedPermissions= this.backendService.decoded.permissions || [];
    this.readPermission=this.decodedPermissions.find((permission:string)=>permission=="can_read_users")!=undefined;
    this.writePermission=this.decodedPermissions.find((permission:string)=>permission=="can_create_users")!=undefined;
    this.writeVacuumPermission=this.decodedPermissions.find((permission:string)=>permission=="can_add_vacuum")!=undefined;
    this.searchVacuumPermission=this.decodedPermissions.find((permission:string)=>permission=="can_search_vacuum")!=undefined;
    }
    this.backendService.changedTokenEvent.subscribe(()=>{

      this.token=this.backendService.token;
      if(this.token!=''){
        console.log(this.backendService.token)
      this.decodedPermissions= this.backendService.decoded.permissions || [];
      this.readPermission=this.decodedPermissions.find((permission:string)=>permission=="can_read_users")!=undefined;
      this.writeVacuumPermission=this.decodedPermissions.find((permission:string)=>permission=="can_add_vacuum")!=undefined;
      this.writePermission=this.decodedPermissions.find((permission:string)=>permission=="can_create_users")!=undefined;
      this.searchVacuumPermission=this.decodedPermissions.find((permission:string)=>permission=="can_search_vacuum")!=undefined;
      }else{
        this.decodedPermissions=[];
        this.readPermission=false;
        this.writePermission=false;
        this.writeVacuumPermission=false;
        this.searchVacuumPermission=false;
      }
    });
  }
  logout(){
    this.backendService.token='';
    this.token='';
    localStorage.removeItem("token");
    this.backendService.changedTokenEvent.emit();
  }
 //  parseJwt (token:string) {
   // return JSON.parse(Buffer.from(token.split('.')[1], 'base64').toString());
///}
  gotoUsers(){
    if(this.readPermission){
      this.router.navigate(['/users']);
    }else if(this.token!='' && this.token!=null && this.token!=undefined){
      alert("You don't have permission to read users");
    }else{
      alert("You need to login first");
    }
  }


}
