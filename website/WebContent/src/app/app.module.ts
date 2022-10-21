import APP_URL from './app.url.json';

import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FileUploadModule } from 'ng2-file-upload';

import { AppRoutingModule } from './app-routing.module';
import { MaterialImporterModule } from './material-importer.module';

import { BASE_PATH as BASE_PATH_RB, ApiModule as ReviewBoardApiModule } from './generated/api/review-board';
import { BASE_PATH as BASE_PATH_SR, ApiModule as ServiceRequesterApiModule } from './generated/api/service-requester';
import { BASE_PATH as BASE_PATH_UR, ApiModule as UserRegisterApiModule } from './generated/api/user-register';
import { BASE_PATH as BASE_PATH_CP, ApiModule as CodeProviderApiModule } from './generated/api/code-provider';
import { Globals } from './globals';

import { AppComponent } from './app.component';

// Layout
import { FooterComponent } from './layout/footer/footer.component';
import { HeaderComponent } from './layout/header/header.component';
import { SidebarComponent } from './layout/sidebar/sidebar.component';

// Services
import { MessageService } from './services/messages/messages.service';
import { LoadingService } from './services/loading/loading.service';
import { D3Service, D3_DIRECTIVES } from './services/d3';
import { ExecutorsService } from './services/executors/executors.service';
import { DialogService } from './services/dialog/dialog.service';
import { IntroService } from './services/intro/intro.service';

// Pages
// tslint:disable-next-line:max-line-length
import { AggregatedReputationsListComponent } from './pages/ratings/reputation/aggregated-reputations-list/aggregated-reputations-list.component';
import { ReputationsListComponent } from './pages/ratings/reputation/reputations-list/reputations-list.component';
import { RequesterComponent } from './pages/requester/requester.component';
import { ConfigureMarketVariantComponent } from './pages/configure-market-variant/configure-market-variant.component';
import { RateServiceComponent } from './pages/rate-service/rate-service.component';
import { OffersComponent } from './pages/offers/offers.component';
import { OfferComponent } from './pages/offers/offer/offer.component';
import { UserCreatorComponent } from './pages/user-creator/user-creator.component';
import { RatingsComponent } from './pages/ratings/ratings.component';
import { RatingItemComponent } from './pages/ratings/rating-item/rating-item.component';
import { ViewVerificationResultsComponent } from './pages/view-verification-results/view-verification-results.component';
import { MyServicesComponent } from './pages/my-services/my-services.component';
import { UploadServiceComponent } from './pages/services/upload-service/upload-service.component';
import { NewCodeProviderComponent } from './pages/services/new-code-provider/new-code-provider.component';
import { RequestsInProcessComponent } from './pages/requests-in-process/requests-in-process.component';
import { ServicesComponent } from './pages/services/services.component';
import { ServicesListComponent } from './pages/services/services-list/services-list.component';
import { VerificationInfoComponent } from './pages/view-verification-results/verification-info/verification-info.component';
import { ExecutorsComponent } from './pages/executors/executors.component';
import { OtfProviderNetworkComponent } from './pages/otf-provider-network/otf-provider-network.component';

// Pipes
import { MainPipe } from './pipes/pipes.module';

// Components
import { StarComponent } from './components/star/star.component';
import { ChatbotMessageComponent } from './components/chatbot-message/chatbot-message.component';
import { MessagesComponent } from './components/messages/messages.component';
import { LoadingComponent } from './components/loading/loading.component';
import { GraphComponent } from './components/visuals/graph/graph.component';
import { SHARED_VISUALS } from './components/visuals/shared';
import { RolesDropdownComponent } from './components/roles-dropdown/roles-dropdown.component';
import { UploadButtonComponent } from './components/upload-button/upload-button.component';
import { RatingBoxComponent } from './components/rating-box/rating-box.component';
import { FlagIconComponent } from './components/flag-icon/flag-icon.component';
import { DialogComponent } from './components/dialog/dialog.component';
import { IntroComponent } from './components/intro/intro.component';

@NgModule({
  declarations: [
    AggregatedReputationsListComponent,
    AppComponent,
    RequesterComponent,
    ConfigureMarketVariantComponent,
    FooterComponent,
    HeaderComponent,
    RateServiceComponent,
    ReputationsListComponent,
    OffersComponent,
    SidebarComponent,
    UploadServiceComponent,
    UserCreatorComponent,
    RatingsComponent,
    ViewVerificationResultsComponent,
    StarComponent,
    RatingItemComponent,
    ChatbotMessageComponent,
    OfferComponent,
    MyServicesComponent,
    MessagesComponent,
    LoadingComponent,
    ServicesComponent,
    ServicesListComponent,
    NewCodeProviderComponent,
    RequestsInProcessComponent,
    VerificationInfoComponent,
    UploadButtonComponent,
    GraphComponent,
    ...SHARED_VISUALS,
    ...D3_DIRECTIVES,
    RolesDropdownComponent,
    ExecutorsComponent,
    RatingBoxComponent,
    OtfProviderNetworkComponent,
    FlagIconComponent,
    DialogComponent,
    IntroComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialImporterModule,
    ReviewBoardApiModule,
    ServiceRequesterApiModule,
    UserRegisterApiModule,
    CodeProviderApiModule,
    FileUploadModule,
    MainPipe,
  ],
  providers: [
    Globals,
    DialogService,
    ExecutorsService,
    D3Service,
    LoadingService,
    MessageService,
    IntroService,
    ReviewBoardApiModule, { provide: BASE_PATH_RB, useValue: APP_URL.BASE_PATH_RB },
    ServiceRequesterApiModule, { provide: BASE_PATH_SR, useValue: APP_URL.BASE_PATH_SR },
    UserRegisterApiModule, { provide: BASE_PATH_UR, useValue: APP_URL.BASE_PATH_UR },
    CodeProviderApiModule, { provide: BASE_PATH_CP, useValue: APP_URL.BASE_PATH_CP }
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    MessagesComponent,
    DialogComponent,
  ]
})
export class AppModule { }
