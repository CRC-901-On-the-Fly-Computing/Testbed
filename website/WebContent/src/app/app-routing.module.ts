import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RequesterComponent } from './pages/requester/requester.component';
import { ConfigureMarketVariantComponent } from './pages/configure-market-variant/configure-market-variant.component';
import { RateServiceComponent } from './pages/rate-service/rate-service.component';
import { OffersComponent } from './pages/offers/offers.component';
import { OfferComponent } from './pages/offers/offer/offer.component';
import { UploadServiceComponent } from './pages/services/upload-service/upload-service.component';
import { UserCreatorComponent } from './pages/user-creator/user-creator.component';
import { RatingsComponent } from './pages/ratings/ratings.component';
import { ReputationsListComponent } from './pages/ratings/reputation/reputations-list/reputations-list.component';
import { ViewVerificationResultsComponent } from './pages/view-verification-results/view-verification-results.component';
import { VerificationInfoComponent } from './pages/view-verification-results/verification-info/verification-info.component';
import { MyServicesComponent } from './pages/my-services/my-services.component';
import { ServicesComponent } from './pages/services/services.component';
import { ServicesListComponent } from './pages/services/services-list/services-list.component';
import { NewCodeProviderComponent } from './pages/services/new-code-provider/new-code-provider.component';
import { RequestsInProcessComponent } from './pages/requests-in-process/requests-in-process.component';
import { ExecutorsComponent } from './pages/executors/executors.component';
import { OtfProviderNetworkComponent } from './pages/otf-provider-network/otf-provider-network.component';

const appRoutes: Routes = [
  // Service Requester
  { path: 'user-creator', component: UserCreatorComponent },
  { path: 'requester', component: RequesterComponent },
  { path: 'requests-in-process', component: RequestsInProcessComponent },
  { path: 'requests-in-process/:id', component: RequestsInProcessComponent },
  { path: 'offers', component: OffersComponent },
  { path: 'offers/:id/:oid', component: OfferComponent },
  { path: 'my-services', component: MyServicesComponent },
  { path: 'rate', component: RateServiceComponent },
  { path: 'rate/:id', component: RateServiceComponent },
  { path: 'ratings', component: RatingsComponent },
  { path: 'ratings/:id', component: ReputationsListComponent },
  // OTF Provider
  { path: 'verification-results', component: ViewVerificationResultsComponent },
  { path: 'verification-results/:id', component: VerificationInfoComponent },
  // Service Provider
  { path: 'services', component: ServicesComponent },
  { path: 'services/new', component: NewCodeProviderComponent },
  { path: 'services/:id', component: ServicesListComponent },
  { path: 'services/:id/:sid/overlay-report', component: ServicesListComponent },
  { path: 'services/:id/detail', component: NewCodeProviderComponent },
  { path: 'services/:id/edit', component: NewCodeProviderComponent },
  { path: 'services/:id/new', component: UploadServiceComponent },
  { path: 'services/:id/:oid/detail', component: UploadServiceComponent },
  { path: 'services/:id/:oid/edit', component: UploadServiceComponent },
  // Computer Center
  { path: 'executors', component: ExecutorsComponent },
  { path: 'executors/:id', component: ExecutorsComponent },
  // Market Provider
  { path: 'otf-provider-network', component: OtfProviderNetworkComponent },
  { path: 'configure-market', component: ConfigureMarketVariantComponent },
  // Default
  { path: '', redirectTo: '/requester', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
