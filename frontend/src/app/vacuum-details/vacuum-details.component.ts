import { Component } from '@angular/core';
import { BackendserviceService } from '../backendservice.service';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { SseService } from '../sse.service';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-vacuum-details',
  templateUrl: './vacuum-details.component.html',
  styleUrls: ['./vacuum-details.component.css']
})
export class VacuumDetailsComponent {
vacuum:any = {};
startVacuumPermission:boolean=false;
stopVacuumPermission:boolean=false;
dischargeVacuumPermission:boolean=false;
startTime:number=0;
date:number;
constructor(private backendService:BackendserviceService,private route:ActivatedRoute,private sseService:SseService,private cd: ChangeDetectorRef) { 
  this.startVacuumPermission=this.backendService.decoded.permissions.find((permission:string)=>permission=="can_start_vacuum")!=undefined;
  this.stopVacuumPermission=this.backendService.decoded.permissions.find((permission:string)=>permission=="can_stop_vacuum")!=undefined;
  this.dischargeVacuumPermission=this.backendService.decoded.permissions.find((permission:string)=>permission=="can_discharge_vacuum")!=undefined;
  this.route.params.subscribe(params => {
   this.vacuum.id= (params['id'])
   console.log(this.vacuum.id)
  })
  this.date=Date.now();
  this.backendService.getVacuumDetails(this.vacuum.id).subscribe((data)=>{
    this.vacuum=data.body;
  });
  
}

startVacuum(){
  this.sseService.subscribeToFortuneTeller().subscribe((data)=>{{
    this.vacuum.status=data;
    this.cd.detectChanges();
    this.sseService.unsubscribeFromFortuneTeller();
    }});
    
  this.backendService.startVacuum(this.vacuum.id).subscribe((data)=>{
    console.log(data)
   if(data.status==200){
    this.vacuum.status="Updating...";
    alert("Command to start sent succesfully");
   
  }
  
  },(error)=>{
   alert(error.error)
  });
  
}
stopVacuum(){
  this.sseService.subscribeToFortuneTeller().subscribe((data)=>{{
    this.vacuum.status=data;
    this.cd.detectChanges();
    this.sseService.unsubscribeFromFortuneTeller();
    }});
  this.backendService.stopVacuum(this.vacuum.id).subscribe((data)=>{
    console.log(data)
   if(data.status==200){
    this.vacuum.status="Updating...";
    alert("Command to stop sent succesfully");

   }
  },(error)=>{
   alert(error.error)
  });
  
}
  dischargeVacuum(){
    let i=0;
    this.sseService.subscribeToFortuneTeller().subscribe((data)=>{{
      i++;
      this.vacuum.status=data;
      this.cd.detectChanges();
      this.sseService.unsubscribeFromFortuneTeller();
      console.log(data)
        if(i==1)
        this.sseService.subscribeToFortuneTeller().subscribe((data)=>{{
         
          this.vacuum.status=data;
          this.cd.detectChanges();
         
          this.sseService.unsubscribeFromFortuneTeller();
          }});
      }});
    this.backendService.dischargeVacuum(this.vacuum.id).subscribe((data)=>{
      console.log(data)
     if(data.status==200){
      this.vacuum.status="Updating...";
      alert("Command to discharge sent succesfully");
     
    }
    },(error)=>{
     alert(error.error)
    });
    
  }
  startVacuumAsync(){
    console.log(this.formatTimeToDateTimeString(this.startTime+":00"))
    this.backendService.startVacuumAsync(this.vacuum.id,this.formatTimeToDateTimeString(this.startTime+":00")).subscribe((data)=>{
      console.log(data)
     if(data.status==200)
     alert("Command to schedule start sent succesfully");
    },(error)=>{
     alert(error.error)
    });
    
  }
  stopVacuumAsync(){
    this.backendService.stopVacuumAsync(this.vacuum.id,this.formatTimeToDateTimeString(this.startTime+":00")).subscribe((data)=>{
      console.log(data)
     if(data.status==200)
     alert("Command to schedule stop sent succesfully");
    },(error)=>{
     alert(error.error)
    });
    
  }
    dischargeVacuumAsync(){
      this.backendService.dischargeVacuumAsync(this.vacuum.id,this.formatTimeToDateTimeString(this.startTime+":00")).subscribe((data)=>{
        console.log(data)
       if(data.status==200)
       alert("Command to schedule discharge sent succesfully");
      },(error)=>{
       alert(error.error)
      });
      
    }


     formatTimeToDateTimeString(timeString: string): string {
      // Get today's date
      const today = new Date();
  
      // Parse the time string (assuming it's in HH:mm:ss format)
      let [hours, minutes, seconds] = timeString.split(':');
      console.log(hours,minutes,seconds);
      hours=(Number.parseInt(hours)+1).toString(); //add 1 hour to the time jer iz nekog razloga je uvek manji za jedan sat
      const timeDate = new Date(today.getFullYear(), today.getMonth(), today.getDate(), +hours, +minutes, +seconds);
  
      // Format the date and time
      const formattedDateTime = timeDate.toISOString().slice(0, 19).replace('T', ' ');
      console.log(timeDate);
      return formattedDateTime;
  }

}




