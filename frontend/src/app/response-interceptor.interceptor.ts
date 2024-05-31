import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { Observable,throwError } from 'rxjs';
import { Route, Router } from '@angular/router';
import { BackendserviceService } from './backendservice.service';

@Injectable()
export class ResponseInterceptorInterceptor implements HttpInterceptor {

  constructor(private router:Router,private backendService:BackendserviceService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Handle 401 Unauthorized response here
          console.log(error)
          if(error.error=="Token has expired"){
            alert("Token has expired. Redirecting to login page.");
            this.backendService.token='';
            localStorage.removeItem("token");
            this.backendService.changedTokenEvent.emit();
            this.router.navigate(["/login"]);
          }
          else{
          alert("Unauthorized request. Redirecting to login page.");
          this.router.navigate(["/login"]);
          }
        }
        if(error.status==403){
          alert("You don't have permission to perform this action");
          this.router.navigate(["/"]);
        }
        if(error.status==404){
          alert("Resource not found");
        }
        if(error.status==500){
          alert("Internal server error");
        }

        return throwError(error);
      })
    );

  }
}
