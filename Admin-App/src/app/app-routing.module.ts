import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./components/home/home.component";
import { DashboardComponent } from "./components/dashboard/dashboard.component";
import { SettingsComponent } from "./components/settings/settings.component";
import {DashboardHomeComponent} from "./components/dashboard-home/dashboard-home.component";
import {ActiveParkComponent} from "./components/active-park/active-park.component";
import {PaymentHistoryComponent} from "./components/payment-history/payment-history.component";
import {ActiveUsersComponent} from "./components/active-users/active-users.component";
import {GenericHistoryComponent} from "./components/generic-history/generic-history.component";

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    children: [
      { path: 'settings', component: SettingsComponent },
      { path: 'dashboard-home', component: DashboardHomeComponent },
      { path: 'active-park', component: ActiveParkComponent },
      { path: 'payment-history', component: PaymentHistoryComponent },
      { path: 'active-users', component: ActiveUsersComponent },
      { path: 'history', component: GenericHistoryComponent },
      { path: '',redirectTo:'dashboard-home', pathMatch:'full'}
    ]
  },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home' }  // Ruta comodín para redirigir a la ruta por defecto
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
