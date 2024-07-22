import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HomeComponent} from './components/home/home.component';
import {MatListModule} from "@angular/material/list";
import {MatDividerModule} from "@angular/material/divider";
import {MatCardModule} from "@angular/material/card";
import {NgOptimizedImage} from "@angular/common";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {DashboardComponent} from './components/dashboard-components/dashboard/dashboard.component';
import {SidenavComponent} from './components/dashboard-components/sidenav/sidenav/sidenav.component';
import {SidenavUtilComponent} from './components/dashboard-components/sidenav/sidenav-util/sidenav-util.component';
import {SidenavBodyComponent} from './components/dashboard-components/sidenav/sidenav-body/sidenav-body.component';
import {SettingsComponent} from './components/dashboard-components/settings/settings.component';
import {DashboardHomeComponent} from './components/dashboard-components/dashboard-home/dashboard-home.component';
import {ActiveParkComponent} from './components/dashboard-components/active-park/active-park.component';
import {PaymentHistoryComponent} from './components/dashboard-components/payment-history/payment-history.component';
import {ActiveUsersComponent} from './components/dashboard-components/active-users/active-users.component';
import {GenericHistoryComponent} from './components/dashboard-components/generic-history/generic-history.component';
import {GoToDirective} from './directives/go-to.directive';
import {
  DashboardGenericComponent
} from './components/dashboard-components/dashboard-generic/dashboard-generic.component';
import {
  DashboardGenericUsrComponent
} from './components/dashboard-components/dashboard-generic-usr/dashboard-generic-usr.component';
import {ClockComponent} from './components/util/clock/clock.component';
import {HttpClientModule} from "@angular/common/http";
import {SignupComponent} from './components/users/signup/signup.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatChipsModule} from "@angular/material/chips";
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    DashboardComponent,
    SidenavComponent,
    SidenavUtilComponent,
    SidenavBodyComponent,
    SettingsComponent,
    DashboardHomeComponent,
    ActiveParkComponent,
    PaymentHistoryComponent,
    ActiveUsersComponent,
    GenericHistoryComponent,
    GoToDirective,
    DashboardGenericComponent,
    DashboardGenericUsrComponent,
    ClockComponent,
    SignupComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatListModule,
    MatDividerModule,
    NgOptimizedImage,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatInputModule,
    HttpClientModule,
    MatDialogModule,
    MatChipsModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
