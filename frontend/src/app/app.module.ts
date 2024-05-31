import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; 
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import {MatSliderModule} from '@angular/material/slider';
import {MatInputModule} from '@angular/material/input';
import { HttpClientModule } from '@angular/common/http';
import {LoginPageComponent} from './login-page/login-page.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatToolbarModule} from '@angular/material/toolbar';
import { UserListComponent } from './user-list/user-list.component';
import {MatListModule} from '@angular/material/list';
import {MatTableModule} from '@angular/material/table';
import { AddUserComponent } from './add-user/add-user.component';
import {MatSelectModule} from '@angular/material/select';
import { UpdateUserComponent } from './update-user/update-user.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ResponseInterceptorInterceptor } from './response-interceptor.interceptor';
import { SearchVacuumComponent } from './search-vacuum/search-vacuum.component';
import {MatDatepickerModule} from '@angular/material/datepicker'
import {FormGroup, FormControl} from '@angular/forms';
import {JsonPipe} from '@angular/common';
import {MatNativeDateModule} from '@angular/material/core';
import { AddVacuumComponent } from './add-vacuum/add-vacuum.component';
import { ErrorMessagesComponentComponent } from './error-messages-component/error-messages-component.component';
import { VacuumDetailsComponent } from './vacuum-details/vacuum-details.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    UserListComponent,
    AddUserComponent,
    UpdateUserComponent,
    SearchVacuumComponent,
    AddVacuumComponent,
    ErrorMessagesComponentComponent,
    VacuumDetailsComponent,
    
  ],
  imports: [ 
    MatFormFieldModule,
    MatDatepickerModule,
    FormsModule,
    ReactiveFormsModule,
    JsonPipe,
    MatNativeDateModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatCheckboxModule,
    MatButtonModule,
    MatSliderModule,
    MatInputModule,
    FormsModule,
    HttpClientModule,
    MatFormFieldModule,
    MatToolbarModule,
    MatListModule,
    MatTableModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatDatepickerModule
    
  ],
  providers: [
   {provide: HTTP_INTERCEPTORS, useClass: ResponseInterceptorInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
