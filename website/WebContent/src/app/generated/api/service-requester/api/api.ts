export * from './fileUploadController.service';
import { FileUploadControllerService } from './fileUploadController.service';
export * from './requesterApi.service';
import { RequesterApiService } from './requesterApi.service';
export * from './serviceRequester.service';
import { ServiceRequesterService } from './serviceRequester.service';
export * from './userApi.service';
import { UserApiService } from './userApi.service';
export const APIS = [FileUploadControllerService, RequesterApiService, ServiceRequesterService, UserApiService];
