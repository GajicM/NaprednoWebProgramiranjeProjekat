import { Component } from '@angular/core';
import { BackendserviceService } from '../backendservice.service';

@Component({
  selector: 'app-error-messages-component',
  templateUrl: './error-messages-component.component.html',
  styleUrls: ['./error-messages-component.component.css']
})
export class ErrorMessagesComponentComponent {
  errors:any=[];
  displayedColumns: string[] = ['id', 'message','operation',"dateTime",'vacuumId' ,"userId"];
  constructor(private backendService:BackendserviceService) { 
    this.getErrors();

  }

  getErrors(){
    this.backendService.getErrorMessages().subscribe((data)=>{
      this.errors=data.body;
    });
  }

  

}
