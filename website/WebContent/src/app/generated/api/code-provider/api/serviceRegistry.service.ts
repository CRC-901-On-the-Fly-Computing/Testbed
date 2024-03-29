/**
 * PostgREST API
 * standard public schema
 *
 * OpenAPI spec version: 0.5.0.0 (903a8d5)
 * 
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

import { ServiceRegistry } from '../model/serviceRegistry';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { Configuration }                                     from '../configuration';


@Injectable()
export class ServiceRegistryService {

    protected basePath = 'http://sfb-k8node-1.cs.uni-paderborn.de:30301';
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
        for (let consume of consumes) {
            if (form === consume) {
                return true;
            }
        }
        return false;
    }


    /**
     * 
     * 
     * @param serviceId 
     * @param codeProviderId 
     * @param serviceSpecification 
     * @param servicePolicy 
     * @param hardware 
     * @param prefer Preference
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public serviceRegistryDelete(serviceId?: string, codeProviderId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, prefer?: string, observe?: 'body', reportProgress?: boolean): Observable<any>;
    public serviceRegistryDelete(serviceId?: string, codeProviderId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, prefer?: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public serviceRegistryDelete(serviceId?: string, codeProviderId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, prefer?: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
    public serviceRegistryDelete(serviceId?: string, codeProviderId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, prefer?: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let queryParameters = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
        if (serviceId !== undefined) {
            queryParameters = queryParameters.set('service_id', <any>serviceId);
        }
        if (codeProviderId !== undefined) {
            queryParameters = queryParameters.set('code_provider_id', <any>codeProviderId);
        }
        if (serviceSpecification !== undefined) {
            queryParameters = queryParameters.set('service_specification', <any>serviceSpecification);
        }
        if (servicePolicy !== undefined) {
            queryParameters = queryParameters.set('service_policy', <any>servicePolicy);
        }
        if (hardware !== undefined) {
            queryParameters = queryParameters.set('hardware', <any>hardware);
        }

        let headers = this.defaultHeaders;
        if (prefer !== undefined && prefer !== null) {
            headers = headers.set('Prefer', String(prefer));
        }

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json',
            'application/vnd.pgrst.object+json',
            'text/csv'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json',
            'application/vnd.pgrst.object+json',
            'text/csv'
        ];

        return this.httpClient.delete<any>(`${this.basePath}/service_registry`,
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
     * 
     * 
     * @param serviceId 
     * @param codeProviderId 
     * @param serviceSpecification 
     * @param servicePolicy 
     * @param hardware 
     * @param select Filtering Columns
     * @param order Ordering
     * @param range Limiting and Pagination
     * @param rangeUnit Limiting and Pagination
     * @param offset Limiting and Pagination
     * @param limit Limiting and Pagination
     * @param prefer Preference
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public serviceRegistryGet(codeProviderId?: string, serviceId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, select?: string, order?: string, range?: string, rangeUnit?: string, offset?: string, limit?: string, prefer?: string, observe?: 'body', reportProgress?: boolean): Observable<ServiceRegistry>;
    public serviceRegistryGet(codeProviderId?: string, serviceId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, select?: string, order?: string, range?: string, rangeUnit?: string, offset?: string, limit?: string, prefer?: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<ServiceRegistry>>;
    public serviceRegistryGet(codeProviderId?: string, serviceId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, select?: string, order?: string, range?: string, rangeUnit?: string, offset?: string, limit?: string, prefer?: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<ServiceRegistry>>;
    public serviceRegistryGet(codeProviderId?: string, serviceId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, select?: string, order?: string, range?: string, rangeUnit?: string, offset?: string, limit?: string, prefer?: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let queryParameters = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
        if (serviceId !== undefined) {
            queryParameters = queryParameters.set('service_id', <any>serviceId);
        }
        if (codeProviderId !== undefined) {
            queryParameters = queryParameters.set('code_provider_id', <any>codeProviderId);
        }
        if (serviceSpecification !== undefined) {
            queryParameters = queryParameters.set('service_specification', <any>serviceSpecification);
        }
        if (servicePolicy !== undefined) {
            queryParameters = queryParameters.set('service_policy', <any>servicePolicy);
        }
        if (hardware !== undefined) {
            queryParameters = queryParameters.set('hardware', <any>hardware);
        }
        if (select !== undefined) {
            queryParameters = queryParameters.set('select', <any>select);
        }
        if (order !== undefined) {
            queryParameters = queryParameters.set('order', <any>order);
        }
        if (offset !== undefined) {
            queryParameters = queryParameters.set('offset', <any>offset);
        }
        if (limit !== undefined) {
            queryParameters = queryParameters.set('limit', <any>limit);
        }

        let headers = this.defaultHeaders;
        if (range !== undefined && range !== null) {
            headers = headers.set('Range', String(range));
        }
        if (rangeUnit !== undefined && rangeUnit !== null) {
            headers = headers.set('Range-Unit', String(rangeUnit));
        }
        if (prefer !== undefined && prefer !== null) {
            headers = headers.set('Prefer', String(prefer));
        }

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json',
            'application/vnd.pgrst.object+json',
            'text/csv'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json',
            'application/vnd.pgrst.object+json',
            'text/csv'
        ];

        return this.httpClient.get<ServiceRegistry>(`${this.basePath}/service_registry`,
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
     * 
     * 
     * @param serviceId 
     * @param codeProviderId 
     * @param serviceSpecification 
     * @param servicePolicy 
     * @param hardware 
     * @param serviceRegistry service_registry
     * @param prefer Preference
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public serviceRegistryPatch(serviceId?: string, codeProviderId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, serviceRegistry?: ServiceRegistry, prefer?: string, observe?: 'body', reportProgress?: boolean): Observable<any>;
    public serviceRegistryPatch(serviceId?: string, codeProviderId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, serviceRegistry?: ServiceRegistry, prefer?: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public serviceRegistryPatch(serviceId?: string, codeProviderId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, serviceRegistry?: ServiceRegistry, prefer?: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
    public serviceRegistryPatch(serviceId?: string, codeProviderId?: string, serviceSpecification?: string, servicePolicy?: string, hardware?: string, serviceRegistry?: ServiceRegistry, prefer?: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let queryParameters = new HttpParams({encoder: new CustomHttpUrlEncodingCodec()});
        if (serviceId !== undefined) {
            queryParameters = queryParameters.set('service_id', <any>serviceId);
        }
        if (codeProviderId !== undefined) {
            queryParameters = queryParameters.set('code_provider_id', <any>codeProviderId);
        }
        if (serviceSpecification !== undefined) {
            queryParameters = queryParameters.set('service_specification', <any>serviceSpecification);
        }
        if (servicePolicy !== undefined) {
            queryParameters = queryParameters.set('service_policy', <any>servicePolicy);
        }
        if (hardware !== undefined) {
            queryParameters = queryParameters.set('hardware', <any>hardware);
        }

        let headers = this.defaultHeaders;
        if (prefer !== undefined && prefer !== null) {
            headers = headers.set('Prefer', String(prefer));
        }

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json',
            'application/vnd.pgrst.object+json',
            'text/csv'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json',
            'application/vnd.pgrst.object+json',
            'text/csv'
        ];
        let httpContentTypeSelected:string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }

        return this.httpClient.patch<any>(`${this.basePath}/service_registry`,
            serviceRegistry,
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
     * 
     * 
     * @param serviceRegistry service_registry
     * @param prefer Preference
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public serviceRegistryPost(serviceRegistry?: ServiceRegistry, prefer?: string, observe?: 'body', reportProgress?: boolean): Observable<any>;
    public serviceRegistryPost(serviceRegistry?: ServiceRegistry, prefer?: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public serviceRegistryPost(serviceRegistry?: ServiceRegistry, prefer?: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
    public serviceRegistryPost(serviceRegistry?: ServiceRegistry, prefer?: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;
        if (prefer !== undefined && prefer !== null) {
            headers = headers.set('Prefer', String(prefer));
        }

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json',
            'application/vnd.pgrst.object+json',
            'text/csv'
        ];
        let httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        let consumes: string[] = [
            'application/json',
            'application/vnd.pgrst.object+json',
            'text/csv'
        ];
        let httpContentTypeSelected:string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }

        return this.httpClient.post<any>(`${this.basePath}/service_registry`,
            serviceRegistry,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}
