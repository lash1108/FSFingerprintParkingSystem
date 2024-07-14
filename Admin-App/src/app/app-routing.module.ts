import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./components/home/home.component";
import { DashboardComponent } from "./components/dashboard/dashboard.component";
import { SettingsComponent } from "./components/settings/settings.component";
import {DashboardHomeComponent} from "./components/dashboard-home/dashboard-home.component";

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    children: [
      { path: 'settings', component: SettingsComponent },
      { path: 'dashboard-home', component: DashboardHomeComponent },
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
