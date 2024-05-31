import { Component } from '@angular/core';
import { BackendserviceService } from '../backendservice.service';
import { FormGroup,FormControl } from '@angular/forms';

@Component({
  selector: 'app-search-vacuum',
  templateUrl: './search-vacuum.component.html',
  styleUrls: ['./search-vacuum.component.css']
})
export class SearchVacuumComponent {
  vacuums:any = [];
  displayedColumns: string[] = ['id', 'name','status' ,'active',"createdAt","actions"];
  searchObj:any = {name:"",status:[],dateFrom:"",dateTo:""};
  statusMap:Map<string,boolean>=new Map<string,boolean>();
  deleteVacuumPermission:boolean=false;
  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });
  constructor(private backendService:BackendserviceService) { 
    this.deleteVacuumPermission=this.backendService.decoded.permissions.find((permission:string)=>permission=="can_remove_vacuum")!=undefined;
    this.statusMap.set("ON",true);
    this.statusMap.set("OFF",true);
    this.statusMap.set("DISCHARGING",true);
    this.range.valueChanges.subscribe((data)=>{
  
      this.searchObj.dateFrom=data.start?.toLocaleDateString('en-US', {year: 'numeric', month: '2-digit', day: '2-digit' }) ;
      this.searchObj.dateTo=data.end?.toLocaleDateString('en-US', {year: 'numeric', month: '2-digit', day: '2-digit' });
    })
    this.onSubmit()
  }
  search(name:string,status:[],dateFrom:string,dateTo:string):void{
    this.backendService.searchVacuum(name,status,dateFrom,dateTo).subscribe((data)=>{
      this.vacuums=data.body;
    });
  }
  onSubmit(){
    this.statusMap.forEach((value,key)=>{
      if(value)
        this.searchObj.status.push(key);
    })
    
    this.search(this.searchObj.name,this.searchObj.status,this.searchObj.dateFrom,this.searchObj.dateTo!=undefined?this.searchObj.dateTo:null);
    this.searchObj.status=[];
  }

  toggleStatus(status:string){
    let statusVal = this.statusMap.get(status);
    this.statusMap.set(status,!statusVal);
  }

deleteVacuum(vacuum:string){
  this.backendService.deleteVacuum(vacuum).subscribe((data)=>{
    if (data.status == 200) {
      alert("Vacuum soft-deleted successfully");
    }
    this.onSubmit();
   //refresh page
  });
}

}
