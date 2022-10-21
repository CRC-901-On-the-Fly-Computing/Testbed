import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// packages
import { MaterialModule } from './material.module';
import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
import { LoadingBarModule } from '@ngx-loading-bar/core';
import { SimpleNotificationsModule } from 'angular2-notifications';

// Services
import { ApiModule as ExecutorSpawnerServiceApiModule } from './generated';

// Layout
import { FooterComponent } from './layout/footer/footer.component';
import { NavComponent } from './layout/nav/nav.component';

// Pages
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { BasicServicesComponent } from './pages/basic-services/basic-services.component';
import { ExecutorsComponent } from './pages/executors/executors.component';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    NavComponent,
    DashboardComponent,
    BasicServicesComponent,
    ExecutorsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    LoadingBarModule,
    LoadingBarRouterModule,
    LoadingBarHttpClientModule,
    SimpleNotificationsModule.forRoot(),
    ExecutorSpawnerServiceApiModule
  ],
  exports: [
  ],
  providers: [
    ExecutorSpawnerServiceApiModule
  ],
  bootstrap: [AppComponent],
  entryComponents: [
  ]
})
export class AppModule { }
