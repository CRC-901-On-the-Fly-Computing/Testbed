import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { BasicServicesComponent } from './pages/basic-services/basic-services.component';
import { ExecutorsComponent } from './pages/executors/executors.component';

const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent },
  { path: 'services', component: BasicServicesComponent },
  { path: 'executors', component: ExecutorsComponent },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
