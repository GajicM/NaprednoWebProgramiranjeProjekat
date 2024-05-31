import { Injectable,EventEmitter } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { User } from './userDto';
import { jwtDecode } from "jwt-decode";
@Injectable({
  providedIn: 'root'
})
export class BackendserviceService {
  public token: string = localStorage.getItem("token") || "";
  public decoded: any;

  public changedTokenEvent: EventEmitter<void> = new EventEmitter<void>();
  constructor(private http:HttpClient) { 
    try{
      this.decoded = jwtDecode(this.token);
    }catch(Error){
      this.decoded = null;
    }
   
    console.log(this.decoded);

    this.changedTokenEvent.subscribe(()=>{
      // this.token=localStorage.getItem("token") || "";
      console.log("bds ngonint sub")
      console.log(this.token)
      this.refresh()
      
      if(this.decoded!=null && this.decoded.permissions!=null){
        const decodedPermissions : []= this.decoded.permissions || [];
          if(decodedPermissions.length==0)
            alert("You don't have any permissions");
        }
    });
}

  refresh():void{
  //  this.token=localStorage.getItem("token") || "";
    try{
      this.decoded = jwtDecode(this.token);
    }catch(Error){
      this.decoded = null;
    }
    console.log(this.decoded);
  }

   login(email: string, password: string) {
    let url="http://localhost:8080/auth/login";
    const userdto:User = {id:0,firstName:"",lastName:"",email:email,password:password,permissions:["can_read_users"]};
    return this.http.post(url,userdto,{observe: 'response'});
  }
  getUsers() {
    let url="http://localhost:8080/users/";
    this.token=localStorage.getItem("token") || "";
    return this.http.get(url,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  addUser(user:User) { 
    let url="http://localhost:8080/users/addUser";
    this.token=localStorage.getItem("token") || "";
   
    return this.http.post(url,user,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  getUser(id:string) {
    let url1="http://localhost:8080/users/getUser/"+id;
    this.token=localStorage.getItem("token") || "";
    return this.http.get(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  deleteUser(id:number) {
    let url1="http://localhost:8080/users/deleteUsers/"+id;
    this.token=localStorage.getItem("token") || "";
    return this.http.delete(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  updateUser(user:User) {
  let url1="http://localhost:8080/users/updateUser";
  this.token=localStorage.getItem("token") || "";
  return this.http.put(url1,user,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  searchVacuum(name:string,status:[],dateFrom:string, dateTo:string) {
    let url1=`http://localhost:8080/vacuums/searchVacuum?name=${name}&status=${status}&dateFrom=${dateFrom}&dateTo=${dateTo}`;
    this.token=localStorage.getItem("token") || "";
    return this.http.get(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  addVacuum(name:string) {
    let url1="http://localhost:8080/vacuums/addVacuum";
    this.token=localStorage.getItem("token") || "";
    return this.http.post(url1,name,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  deleteVacuum(id:string) {
    let url1="http://localhost:8080/vacuums/deleteVacuum/"+id;
    this.token=localStorage.getItem("token") || "";
    return this.http.delete(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  getErrorMessages(){
    let url1="http://localhost:8080/errors/all";
    this.token=localStorage.getItem("token") || "";
    return this.http.get(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  getVacuumDetails(id:string) {
    let url1="http://localhost:8080/vacuums/"+id;
    this.token=localStorage.getItem("token") || "";
    return this.http.get(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  startVacuum(id:string) {
    let url1="http://localhost:8080/vacuums/startVacuum/"+id;
    this.token=localStorage.getItem("token") || "";
    return this.http.get(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  stopVacuum(id:string) {
    let url1="http://localhost:8080/vacuums/stopVacuum/"+id;
    this.token=localStorage.getItem("token") || "";
    return this.http.get(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  dischargeVacuum(id:string) {
    let url1="http://localhost:8080/vacuums/dischargeVacuum/"+id;
    this.token=localStorage.getItem("token") || "";
    return this.http.get(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  startVacuumAsync(id:string,startTime:string) {
    let url1=`http://localhost:8080/vacuums/scheduleStart?id=${id}&date=${startTime}`;
    this.token=localStorage.getItem("token") || "";
    return this.http.get(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }
  stopVacuumAsync(id:string,startTime:string) {
    let url1=`http://localhost:8080/vacuums/scheduleStop?id=${id}&date=${startTime}`;
    this.token=localStorage.getItem("token") || "";
    return this.http.get(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }

  dischargeVacuumAsync(id:string,startTime:string) {
    let url1=`http://localhost:8080/vacuums/scheduleDischarge?id=${id}&date=${startTime}`;
    this.token=localStorage.getItem("token") || "";
    return this.http.get(url1,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.token }});
  }




}
