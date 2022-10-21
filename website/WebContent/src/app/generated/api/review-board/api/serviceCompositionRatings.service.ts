/**
 * Review-Board
 * A simple REST interface to communicate with the reputation-system.
 *
 * OpenAPI spec version: 2.1
 * Contact: mirkoj@mail.upb.de
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
/* tslint:disable:no-unused-variable member-ordering */

import { Inject, Injectable, Optional }                      from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams,
         HttpResponse, HttpEvent }                           from '@angular/common/http';
import { CustomHttpUrlEncodingCodec }                        from '../encoder';

import { Observable }                                        from 'rxjs/Observable';

import { ExtendedServiceReputation } from '../model/extendedServiceReputation';
import { ReactRepresentableReview } from '../model/reactRepresentableReview';
import { ServiceReputationAndSignature } from '../model/serviceReputationAndSignature';
import { SimpleJSONMessage } from '../model/simpleJSONMessage';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { Configuration }                                     from '../configuration';


@Injectable()
export class ServiceCompositionRatingsService {

    protected basePath = 'https://sfb-k8node-1.cs.uni-paderborn.de:32413/api';
    public defaultHeaders = new HttpHeaders();
    public configuration = new Configuration();

    constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }

    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    private canConsumeForm(consumes: string[]): boolean {
        const form = 'multipart/form-data';
        for (const consume of consumes) {
            if (form === consume) {
                return true;
            }
        }
        return false;
    }


    /**
     * Counts all reviews
     * 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public countAllServiceCompositionReviewsUsingGET(observe?: 'body', reportProgress?: boolean): Observable<SimpleJSONMessage>;
    public countAllServiceCompositionReviewsUsingGET(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<SimpleJSONMessage>>;
    public countAllServiceCompositionReviewsUsingGET(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<SimpleJSONMessage>>;
    public countAllServiceCompositionReviewsUsingGET(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.get<SimpleJSONMessage>(`${this.basePath}/service_composition_ratings/count`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Counts all reviews of this service composition
     * 
     * @param serviceCompositionID serviceCompositionID
     * @param maxOther maxOther
     * @param maxOverall maxOverall
     * @param maxPerformance maxPerformance
     * @param maxSecurity maxSecurity
     * @param maxUsability maxUsability
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public countServiceCompositionReviewsUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe?: 'body', reportProgress?: boolean): Observable<SimpleJSONMessage>;
    public countServiceCompositionReviewsUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<SimpleJSONMessage>>;
    public countServiceCompositionReviewsUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<SimpleJSONMessage>>;
    public countServiceCompositionReviewsUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (serviceCompositionID === null || serviceCompositionID === undefined) {
            throw new Error('Required parameter serviceCompositionID was null or undefined when calling countServiceCompositionReviewsUsingGET.');
        }






        let queryParameters = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
        if (maxOther !== undefined && maxOther !== null) {
            queryParameters = queryParameters.set('maxOther', <any>maxOther);
        }
        if (maxOverall !== undefined && maxOverall !== null) {
            queryParameters = queryParameters.set('maxOverall', <any>maxOverall);
        }
        if (maxPerformance !== undefined && maxPerformance !== null) {
            queryParameters = queryParameters.set('maxPerformance', <any>maxPerformance);
        }
        if (maxSecurity !== undefined && maxSecurity !== null) {
            queryParameters = queryParameters.set('maxSecurity', <any>maxSecurity);
        }
        if (maxUsability !== undefined && maxUsability !== null) {
            queryParameters = queryParameters.set('maxUsability', <any>maxUsability);
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.get<SimpleJSONMessage>(`${this.basePath}/service_composition_ratings/${encodeURIComponent(String(serviceCompositionID))}/count`,
            {
                params: queryParameters,
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Requests the aggregated review for a service composition.
     * 
     * @param serviceCompositionID serviceCompositionID
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getAggregatedServiceCompositionReputationUsingGET(serviceCompositionID: string, observe?: 'body', reportProgress?: boolean): Observable<ExtendedServiceReputation>;
    public getAggregatedServiceCompositionReputationUsingGET(serviceCompositionID: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<ExtendedServiceReputation>>;
    public getAggregatedServiceCompositionReputationUsingGET(serviceCompositionID: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<ExtendedServiceReputation>>;
    public getAggregatedServiceCompositionReputationUsingGET(serviceCompositionID: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (serviceCompositionID === null || serviceCompositionID === undefined) {
            throw new Error('Required parameter serviceCompositionID was null or undefined when calling getAggregatedServiceCompositionReputationUsingGET.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.get<ExtendedServiceReputation>(`${this.basePath}/service_composition_ratings/${encodeURIComponent(String(serviceCompositionID))}`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Requests a list of aggregated reviews for each service composition
     * 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getAggregatedServiceCompositionReviewListUsingGET(observe?: 'body', reportProgress?: boolean): Observable<Array<ExtendedServiceReputation>>;
    public getAggregatedServiceCompositionReviewListUsingGET(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<ExtendedServiceReputation>>>;
    public getAggregatedServiceCompositionReviewListUsingGET(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<ExtendedServiceReputation>>>;
    public getAggregatedServiceCompositionReviewListUsingGET(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.get<Array<ExtendedServiceReputation>>(`${this.basePath}/service_composition_ratings`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Requests all reviews of this service composition
     * 
     * @param serviceCompositionID serviceCompositionID
     * @param maxOther maxOther
     * @param maxOverall maxOverall
     * @param maxPerformance maxPerformance
     * @param maxSecurity maxSecurity
     * @param maxUsability maxUsability
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getServiceCompositionRawReviewListUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe?: 'body', reportProgress?: boolean): Observable<Array<ReactRepresentableReview>>;
    public getServiceCompositionRawReviewListUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<ReactRepresentableReview>>>;
    public getServiceCompositionRawReviewListUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<ReactRepresentableReview>>>;
    public getServiceCompositionRawReviewListUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (serviceCompositionID === null || serviceCompositionID === undefined) {
            throw new Error('Required parameter serviceCompositionID was null or undefined when calling getServiceCompositionRawReviewListUsingGET.');
        }






        let queryParameters = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
        if (maxOther !== undefined && maxOther !== null) {
            queryParameters = queryParameters.set('maxOther', <any>maxOther);
        }
        if (maxOverall !== undefined && maxOverall !== null) {
            queryParameters = queryParameters.set('maxOverall', <any>maxOverall);
        }
        if (maxPerformance !== undefined && maxPerformance !== null) {
            queryParameters = queryParameters.set('maxPerformance', <any>maxPerformance);
        }
        if (maxSecurity !== undefined && maxSecurity !== null) {
            queryParameters = queryParameters.set('maxSecurity', <any>maxSecurity);
        }
        if (maxUsability !== undefined && maxUsability !== null) {
            queryParameters = queryParameters.set('maxUsability', <any>maxUsability);
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.get<Array<ReactRepresentableReview>>(`${this.basePath}/service_composition_ratings/${encodeURIComponent(String(serviceCompositionID))}/rawlist`,
            {
                params: queryParameters,
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Requests all reviews of this service composition
     * 
     * @param serviceCompositionID serviceCompositionID
     * @param maxOther maxOther
     * @param maxOverall maxOverall
     * @param maxPerformance maxPerformance
     * @param maxSecurity maxSecurity
     * @param maxUsability maxUsability
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getServiceCompositionReviewListUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe?: 'body', reportProgress?: boolean): Observable<Array<ServiceReputationAndSignature>>;
    public getServiceCompositionReviewListUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<ServiceReputationAndSignature>>>;
    public getServiceCompositionReviewListUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<ServiceReputationAndSignature>>>;
    public getServiceCompositionReviewListUsingGET(serviceCompositionID: string, maxOther?: number, maxOverall?: number, maxPerformance?: number, maxSecurity?: number, maxUsability?: number, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (serviceCompositionID === null || serviceCompositionID === undefined) {
            throw new Error('Required parameter serviceCompositionID was null or undefined when calling getServiceCompositionReviewListUsingGET.');
        }






        let queryParameters = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
        if (maxOther !== undefined && maxOther !== null) {
            queryParameters = queryParameters.set('maxOther', <any>maxOther);
        }
        if (maxOverall !== undefined && maxOverall !== null) {
            queryParameters = queryParameters.set('maxOverall', <any>maxOverall);
        }
        if (maxPerformance !== undefined && maxPerformance !== null) {
            queryParameters = queryParameters.set('maxPerformance', <any>maxPerformance);
        }
        if (maxSecurity !== undefined && maxSecurity !== null) {
            queryParameters = queryParameters.set('maxSecurity', <any>maxSecurity);
        }
        if (maxUsability !== undefined && maxUsability !== null) {
            queryParameters = queryParameters.set('maxUsability', <any>maxUsability);
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.get<Array<ServiceReputationAndSignature>>(`${this.basePath}/service_composition_ratings/${encodeURIComponent(String(serviceCompositionID))}/list`,
            {
                params: queryParameters,
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Requests the storage of a service composition review.
     * 
     * @param body body
     * @param serviceCompositionID serviceCompositionID
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public rateServiceCompositionUsingPUT(body: ReactRepresentableReview, serviceCompositionID: string, observe?: 'body', reportProgress?: boolean): Observable<SimpleJSONMessage>;
    public rateServiceCompositionUsingPUT(body: ReactRepresentableReview, serviceCompositionID: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<SimpleJSONMessage>>;
    public rateServiceCompositionUsingPUT(body: ReactRepresentableReview, serviceCompositionID: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<SimpleJSONMessage>>;
    public rateServiceCompositionUsingPUT(body: ReactRepresentableReview, serviceCompositionID: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        if (body === null || body === undefined) {
            throw new Error('Required parameter body was null or undefined when calling rateServiceCompositionUsingPUT.');
        }

        if (serviceCompositionID === null || serviceCompositionID === undefined) {
            throw new Error('Required parameter serviceCompositionID was null or undefined when calling rateServiceCompositionUsingPUT.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set('Content-Type', httpContentTypeSelected);
        }

        return this.httpClient.put<SimpleJSONMessage>(`${this.basePath}/service_composition_ratings/${encodeURIComponent(String(serviceCompositionID))}`,
            body,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}
