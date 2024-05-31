import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanDeactivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { Observable } from 'rxjs';
import { BackendserviceService } from './backendservice.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanDeactivate<unknown> {

  constructor(private router: Router,private api: BackendserviceService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    console.log('canActivate')
    if(this.api.token == ""){
    this.router.navigate(['/login'])
    alert("You must login with valid creditentails to access this page")
    return true;
    }
    console.log(route)
    const permissions:[]=this.api.decoded.permissions;
    if(route.routeConfig?.path=="createuser" && permissions.find((permission:string)=>permission=="can_create_users")==undefined){
      console.log(route);
      alert("You don't have permission to create users");
      this.router.navigate(['/']);
    return true;
  }
  if(route.routeConfig?.path=="updateUser/:id" && permissions.find((permission:string)=>permission=="can_update_users")==undefined){
    console.log(route);
    alert("You don't have permission to update users");
    this.router.navigate(['/']);
    return true;
  } 
  if(route.routeConfig?.path=="deleteUser/:id" && permissions.find((permission:string)=>permission=="can_delete_users")==undefined){
    console.log(route);
    alert("You don't have permission to delete users");
    this.router.navigate(['/']);
    return true;
  } 



  return true;
}



  canDeactivate(
    component: unknown,
    currentRoute: ActivatedRouteSnapshot,
    currentState: RouterStateSnapshot,
    nextState?: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return true;
  }

}
