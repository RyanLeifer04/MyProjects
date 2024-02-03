import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home';
import { LoginComponent, RegisterComponent } from './account';
import { AuthGuard } from './helpers';

const usersModule = () => import('./users/users.module').then(x => x.UsersModule);
const needsModule = () => import('./needs/needs.module').then(x => x.NeedsModule);
const basketModule = () => import('./basket/basket.module').then(x => x.BasketModule);
const managementModule = () => import('./management/management.module').then(x => x.ManagementModule);
const notificationsModule = () => import('./notifications/notifications.module').then(x => x.NotificationsModule);

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'users', loadChildren: usersModule, canActivate: [AuthGuard] },
  { path: 'needs', loadChildren: needsModule, canActivate: [AuthGuard] },
  { path: 'basket', loadChildren: basketModule, canActivate: [AuthGuard] },
  { path: 'notifications', loadChildren: notificationsModule, canActivate: [AuthGuard] },
  { path: 'adminView', loadChildren: managementModule, canActivate: [AuthGuard] },
  { path: 'account/login', component: LoginComponent },
  { path: 'account/register', component: RegisterComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
