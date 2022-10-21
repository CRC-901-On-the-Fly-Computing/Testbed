export * from './otfProviderRatings.service';
import { OtfProviderRatingsService } from './otfProviderRatings.service';
export * from './serviceCompositionRatings.service';
import { ServiceCompositionRatingsService } from './serviceCompositionRatings.service';
export * from './serviceRatings.service';
import { ServiceRatingsService } from './serviceRatings.service';
export const APIS = [OtfProviderRatingsService, ServiceCompositionRatingsService, ServiceRatingsService];
