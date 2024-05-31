import { Component } from '@angular/core';
import { BackendserviceService } from '../backendservice.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-add-vacuum',
  templateUrl: './add-vacuum.component.html',
  styleUrls: ['./add-vacuum.component.css']
})
export class AddVacuumComponent {
name:string="";

constructor(private backendService:BackendserviceService,private router:Router) {
  
}

onSubmit(){ 
  console.log(this.name);
  this.backendService.addVacuum(this.name).subscribe((data)=>{
    console.log(data);
    if(data.status==200){
      alert("Vacuum added successfully"); 
      this.router.navigate(['/vacuum/search']);
    }
  });
}



}
