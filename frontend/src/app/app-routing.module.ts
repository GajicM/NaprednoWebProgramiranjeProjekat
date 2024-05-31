import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { AuthGuard } from './auth.guard';
import { UserListComponent } from './user-list/user-list.component';
import { AddUserComponent } from './add-user/add-user.component';
import { UpdateUserComponent } from './update-user/update-user.component';
import { SearchVacuumComponent } from './search-vacuum/search-vacuum.component';
import { AddVacuumComponent } from './add-vacuum/add-vacuum.component';
import { VacuumDetailsComponent } from './vacuum-details/vacuum-details.component';
import { ErrorMessagesComponentComponent } from './error-messages-component/error-messages-component.component';

const routes: Routes = [{path: 'login', component:LoginPageComponent },
{path: 'users', component:UserListComponent, canActivate: [AuthGuard],canDeactivate: [AuthGuard] },
{path: 'createuser',component:AddUserComponent, canActivate: [AuthGuard],canDeactivate: [AuthGuard] }  ,
{path: 'updateUser/:id',component:UpdateUserComponent, canActivate: [AuthGuard],canDeactivate: [AuthGuard] } ,
{path:'vacuum/search',component:SearchVacuumComponent, canActivate: [AuthGuard],canDeactivate: [AuthGuard] } ,
{path:'vacuum/add',component:AddVacuumComponent, canActivate: [AuthGuard],canDeactivate: [AuthGuard] } ,
{path:'vacuum/:id',component:VacuumDetailsComponent, canActivate: [AuthGuard],canDeactivate: [AuthGuard] } ,
{path:"errors",component:ErrorMessagesComponentComponent, canActivate: [AuthGuard],canDeactivate: [AuthGuard] } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
